package net.tnemc.core.command;

/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.account.holdings.modify.HoldingsOperation;
import net.tnemc.core.account.shared.MemberPermissions;
import net.tnemc.core.actions.source.PlayerSource;
import net.tnemc.core.channel.MessageHandler;
import net.tnemc.core.command.parameters.PercentBigDecimal;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Note;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.currency.parser.ParseMoney;
import net.tnemc.core.currency.type.MixedType;
import net.tnemc.core.manager.TopManager;
import net.tnemc.core.manager.top.TopPage;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.utils.MISCUtils;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.CmdSource;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import net.tnemc.plugincore.core.io.message.MessageData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.tnemc.core.EconomyManager.TOP_PER_PAGE;

/**
 * MoneyCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MoneyCommand extends BaseCommand {

  //ArgumentsParser: [currency]
  public static void onMyBal(final CmdSource<?> sender) {

    if(sender.player().isPresent()) {
      sender.player().get().inventory().openMenu(sender.player().get(), "my_bal");
    }
  }

  //ArgumentsParser: [currency] [world]
  public static void onBalance(final CmdSource<?> sender, final Currency currency, final String region) {

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.balance." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "balance check");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    if(sender.player().isPresent() && MainConfig.yaml().getBoolean("Core.Commands.GUIAlternatives", true)) {
      sender.player().get().inventory().openMenu(sender.player().get(), "my_bal");
      return;
    }

    final Optional<Account> account = BaseCommand.account(sender, "balance");
    if(account.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }
    onOther(sender, account.get(), region, currency);
  }

  //ArgumentsParser: <amount> <to currency> [from currency]
  public static void onConvert(final CmdSource<?> sender, final PercentBigDecimal amount, final Currency currency, final Currency fromCurrency) {

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.convert.to." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "convert to");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }

      if(!player.get().hasPermission("tne.money.convert.from." + fromCurrency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "convert from");
        data.addReplacement("$currency", fromCurrency.getDisplay());
        sender.message(data);
        return;
      }
    }

    if(amount.value().compareTo(BigDecimal.ZERO) < 0) {
      sender.message(new MessageData("Messages.Money.Negative"));
      return;
    }

    if(currency.getUid().equals(fromCurrency.getUid())) {
      sender.message(new MessageData("Messages.Money.ConvertSame"));
      return;
    }

    final Optional<Account> account = BaseCommand.account(sender, "convert");
    if(account.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }

    final Optional<BigDecimal> converted = fromCurrency.convertValue(currency.getIdentifier(), amount.value());
    if(converted.isEmpty()) {
      final MessageData data = new MessageData("Messages.Money.NoConversion");
      data.addReplacement("$converted", currency.getIdentifier());
      sender.message(data);
      return;
    }

    final HoldingsModifier modifier = new HoldingsModifier(BaseCommand.region(sender),
                                                           currency.getUid(),
                                                           converted.get().setScale(currency.getDecimalPlaces(), RoundingMode.DOWN)
    );

    final HoldingsModifier modifierFrom = new HoldingsModifier(BaseCommand.region(sender),
                                                               fromCurrency.getUid(),
                                                               amount.value().setScale(currency.getDecimalPlaces(), RoundingMode.DOWN).negate()
    );

    final UUID sourceID = (sender.identifier().isPresent())? sender.identifier().get() : TNECore.instance().getServerAccount();
    final Transaction transaction = new Transaction("convert")
            .from(account.get(), modifierFrom)
            .to(account.get(), modifier)
            .processor(EconomyManager.baseProcessor())
            .source(new PlayerSource(sourceID));

    final Optional<Receipt> receipt = processTransaction(sender, transaction, account.get().getName(), amount.value());
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Converted");
      data.addReplacement("$from_amount", amount.value().toPlainString());
      data.addReplacement("$amount", CurrencyFormatter.format(account.get(),
                                                              modifierFrom.asEntry()));
      sender.message(data);
    }
  }

  //ArgumentsParser: <amount> [currency]
  public static void onDeposit(final CmdSource<?> sender, final ParseMoney parseMoney, final Currency currencyParam, final String regionParam) {

    String region = parseMoney.region();
    final Currency currency = parseMoney.currency();

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.deposit." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "deposit");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    if(player.isPresent() && region.equalsIgnoreCase("world-113")) {
      region = player.get().world();
    }

    if(parseMoney.amount().compareTo(BigDecimal.ZERO) < 0) {
      sender.message(new MessageData("Messages.Money.Negative"));
      return;
    }

    final Optional<Account> senderAccount = BaseCommand.account(sender, "deposit");

    if(senderAccount.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }

    region = TNECore.eco().region().resolve(region);

    if(!(currency.type() instanceof MixedType)) {
      sender.message(new MessageData("Messages.Money.NotMixed"));
      return;
    }

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           parseMoney.amount(),
                                                           EconomyManager.VIRTUAL
    );

    final UUID sourceID = (sender.identifier().isPresent())? sender.identifier().get() : TNECore.instance().getServerAccount();
    final Transaction transaction = new Transaction("deposit")
            .to(senderAccount.get(), modifier)
            .from(senderAccount.get(), modifier.counter(EconomyManager.ITEM_ONLY))
            .processor(EconomyManager.baseProcessor())
            .source(new PlayerSource(sourceID));

    final Optional<Receipt> receipt = processTransaction(sender, transaction, senderAccount.get().getName(), parseMoney.amount());
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Deposit");
      data.addReplacement("$amount", CurrencyFormatter.format(senderAccount.get(),
                                                              modifier.asEntry()));
      sender.message(data);
    }
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onGive(final CmdSource<?> sender, final Account account, final ParseMoney parseMoney, final String region, final Currency currency) {


    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent() && !player.get().hasPermission("tne.money.give." + parseMoney.currency().getIdentifier())) {

      final MessageData data = new MessageData("Messages.Account.BlockedAction");
      data.addReplacement("$action", "give funds");
      data.addReplacement("$currency", parseMoney.currency().getDisplay());
      sender.message(data);
      return;
    }


    final HoldingsModifier modifier = new HoldingsModifier(parseMoney.region(),
                                                           parseMoney.currency().getUid(),
                                                           parseMoney.amount());

    final UUID sourceID = (sender.identifier().isPresent())? sender.identifier().get() : TNECore.instance().getServerAccount();
    final Transaction transaction = new Transaction("give")
            .to(account, modifier)
            .source(new PlayerSource(sourceID));

    final Optional<Receipt> receipt = processTransaction(sender, transaction, account.getName(), parseMoney.amount());
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Gave");
      data.addReplacement("$player", account.getName());
      data.addReplacement("$currency", parseMoney.currency().getIdentifier());
      data.addReplacement("$amount", CurrencyFormatter.format(account,
                                                              modifier.asEntry()));
      sender.message(data);

      final MessageData msgData = new MessageData("Messages.Money.Given");
      msgData.addReplacement("$currency", parseMoney.currency().getIdentifier());
      msgData.addReplacement("$player", (sender.name() == null)? MainConfig.yaml().getString("Core.Server.Account.Name") : sender.name());
      msgData.addReplacement("$amount", CurrencyFormatter.format(account, modifier.asEntry()));

      MessageHandler.send(account.getIdentifier(), msgData.grab(account.getIdentifier()));
      if(account.isPlayer() && ((PlayerAccount)account).isOnline()) {

        final Optional<PlayerProvider> provider = ((PlayerAccount)account).getPlayer();

        provider.ifPresent(playerProvider->playerProvider.message(msgData));
      }
    }
  }

  //ArgumentsParser: <amount> [world] [currency]
  public static void onGiveAll(final CmdSource<?> sender, final ParseMoney parseMoney, final String regionParam, final Currency currencyParam) {

    final String region = TNECore.eco().region().resolve(parseMoney.region());
    final Currency currency = parseMoney.currency();

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.give." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "give funds");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           parseMoney.amount());

    final List<String> accounts = new ArrayList<>();

    final UUID sourceID = (sender.identifier().isPresent())? sender.identifier().get() : TNECore.instance().getServerAccount();
    for(final Account account : TNECore.eco().account().getAccounts().values()) {

      final Transaction transaction = new Transaction("give")
              .to(account, modifier)
              .source(new PlayerSource(sourceID));

      final Optional<Receipt> receipt = processTransaction(sender, transaction, account.getName(), parseMoney.amount());
      if(receipt.isPresent()) {

        accounts.add(account.getName());

        final MessageData msgData = new MessageData("Messages.Money.Given");
        msgData.addReplacement("$currency", currency.getIdentifier());
        msgData.addReplacement("$player", (sender.name() == null)? MainConfig.yaml().getString("Core.Server.Account.Name") : sender.name());
        msgData.addReplacement("$amount", CurrencyFormatter.format(account, modifier.asEntry()));

        MessageHandler.send(account.getIdentifier(), msgData.grab(account.getIdentifier()));
        if(account.isPlayer() && ((PlayerAccount)account).isOnline()) {

          final Optional<PlayerProvider> provider = ((PlayerAccount)account).getPlayer();

          provider.ifPresent(playerProvider->playerProvider.message(msgData));
        }
      }
    }

    final MessageData data = new MessageData("Messages.Money.Gave");
    data.addReplacement("$player", String.join(", ", accounts));
    data.addReplacement("$currency", currency.getIdentifier());
    data.addReplacement("$amount", CurrencyFormatter.format(null, modifier.asEntry()));
    sender.message(data);
  }

  public static void onGiveNote(final CmdSource<?> sender, final Account acc, final ParseMoney parseMoney, final Currency currency) {

    final Optional<Note> note = parseMoney.currency().getNote();

    final Optional<Account> accountOpt = BaseCommand.account(acc.getIdentifier(), "note");
    final Account account = accountOpt.orElse(acc);
    if(note.isPresent() && account instanceof final PlayerAccount player) {

      final Optional<PlayerProvider> provider = player.getPlayer();

      if(provider.isEmpty() || !player.isOnline()) {
        sender.message(new MessageData("Messages.Note.CreateOffline"));
        return;
      }

      if(parseMoney.amount().compareTo(note.get().getMinimum()) < 0) {
        final MessageData min = new MessageData("Messages.Note.Minimum");
        min.addReplacement("$amount", note.get().getMinimum().toPlainString());
        sender.message(min);
        return;
      }

      final BigDecimal rounded = parseMoney.amount().setScale(parseMoney.currency().getDecimalPlaces(), RoundingMode.DOWN);

      final Collection<AbstractItemStack<Object>> left = PluginCore.server().calculations().giveItems(Collections.singletonList(note.get().stack(parseMoney.currency().getIdentifier(), BaseCommand.region(sender), rounded)), provider.get().inventory().getInventory(false));

      final MessageData entryMSG = new MessageData("Messages.Note.Given");
      entryMSG.addReplacement("$currency", parseMoney.currency().getIdentifier());
      entryMSG.addReplacement("$amount", CurrencyFormatter.format(account, rounded));
      provider.get().message(entryMSG);

      final MessageData senderMSG = new MessageData("Messages.Note.Created");
      senderMSG.addReplacement("$player", provider.get().getName());
      senderMSG.addReplacement("$currency", parseMoney.currency().getIdentifier());
      senderMSG.addReplacement("$amount", CurrencyFormatter.format(account, rounded));
      sender.message(senderMSG);

      if(!left.isEmpty()) {
        PluginCore.server().calculations().drop(left, player.getUUID(), true);
        provider.get().message(new MessageData("Messages.Note.Dropped"));
      }
      return;
    }
    sender.message(new MessageData("Messages.Note.CreateOffline"));
  }

  //ArgumentsParser: <amount> [currency]
  public static void onNote(final CmdSource<?> sender, final ParseMoney parseMoney, final Currency currency) {

    final Optional<Account> account = BaseCommand.account(sender, "note");
    final Optional<Note> note = parseMoney.currency().getNote();
    if(account.isPresent() && note.isPresent() && account.get() instanceof final PlayerAccount player) {

      final Optional<PlayerProvider> provider = player.getPlayer();
      if(provider.isEmpty()) {
        return;
      }

      if(EconomyManager.limitCurrency() && !provider.get().hasPermission("tne.money.note." + parseMoney.currency().getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "note");
        data.addReplacement("$currency", parseMoney.currency().getDisplay());
        sender.message(data);
        return;
      }

      if(parseMoney.amount().compareTo(note.get().getMinimum()) < 0) {
        final MessageData min = new MessageData("Messages.Note.Minimum");
        min.addReplacement("$amount", note.get().getMinimum().toPlainString());
        sender.message(min);
        return;
      }

      final BigDecimal rounded = parseMoney.amount().setScale(parseMoney.currency().getDecimalPlaces(), RoundingMode.DOWN);

      final BigDecimal amt = rounded.add(note.get().getFee().calculateTax(rounded)).setScale(parseMoney.currency().getDecimalPlaces(), RoundingMode.DOWN);

      final HoldingsModifier modifier = new HoldingsModifier(BaseCommand.region(sender),
                                                             parseMoney.currency().getUid(),
                                                             amt
      );

      final UUID sourceID = (sender.identifier().isPresent())? sender.identifier().get() : TNECore.instance().getServerAccount();
      final Transaction transaction = new Transaction("note")
              .from(account.get(), modifier.counter())
              .processor(EconomyManager.baseProcessor())
              .source(new PlayerSource(sourceID));


      final Optional<Receipt> receipt = processTransaction(sender, transaction, account.get().getName(), parseMoney.amount());
      if(receipt.isPresent()) {
        final Collection<AbstractItemStack<Object>> left = PluginCore.server().calculations().giveItems(Collections.singletonList(note.get().stack(parseMoney.currency().getIdentifier(), BaseCommand.region(sender), rounded)), provider.get().inventory().getInventory(false));

        final MessageData entryMSG = new MessageData("Messages.Note.Given");
        entryMSG.addReplacement("$currency", parseMoney.currency().getIdentifier());
        entryMSG.addReplacement("$amount", CurrencyFormatter.format(account.get(), modifier.asEntry()));
        sender.message(entryMSG);

        if(!left.isEmpty()) {
          PluginCore.server().calculations().drop(left, ((PlayerAccount)account.get()).getUUID(), true);
          sender.message(new MessageData("Messages.Note.Dropped"));
        }
      }
    }
  }

  //ArgumentsParser: <player> [world] [currency]
  public static void onOther(final CmdSource<?> sender, final Account account, String region, final Currency currency) {

    final Optional<PlayerProvider> player = sender.player();
    final Optional<UUID> senderID = sender.identifier();

    final boolean other = (senderID.isPresent() && !senderID.get().equals(account.getIdentifier()));

    if(EconomyManager.limitCurrency() && player.isPresent() && other) {
      if(!player.get().hasPermission("tne.money.other." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "balance check other");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    if(player.isPresent() && region.equalsIgnoreCase("world-113")) {
      region = player.get().world();
    }

    region = TNECore.eco().region().resolve(region);

    final String resolve = region;

    if(TNECore.eco().region().getDisabledRegions().contains(resolve)) {

      final MessageData regionMSG = new MessageData("Messages.General.Disabled");
      sender.message(regionMSG);
      return;
    }

    final String header = (other)? "Messages.Money.HoldingsMultiOther" : "Messages.Money.HoldingsMulti";

    final MessageData msg = new MessageData(header);
    msg.addReplacement("$world", MISCUtils.worldFormatted(region));
    msg.addReplacement("$player", account.getName());
    sender.message(msg);

    TNECore.eco().currency().currencies().forEach((cur)->{
      if(cur.isBalanceShow()) {
        printBalance(sender, account, resolve, cur);
      }
    });
  }

  public static void printBalance(final CmdSource<?> sender, final Account account, final String region, final Currency currency) {

    final MessageData entryMSG = new MessageData("Messages.Money.HoldingsMultiSingle");
    entryMSG.addReplacement("$currency", currency.getIdentifier());

    BigDecimal amount = BigDecimal.ZERO;
    for(final HoldingsEntry entry : currency.type().getHoldings(account, region, currency, EconomyManager.NORMAL)) {
      amount = amount.add(entry.getAmount());

      if(entry.getHandler().asID().equalsIgnoreCase(EconomyManager.INVENTORY_ONLY.asID())) {
        if(currency.type().supportsItems()) {
          entryMSG.addReplacement("$inventory", CurrencyFormatter.format(account, entry));
        } else {
          entryMSG.addReplacement("$inventory", "0");
        }
      }

      if(entry.getHandler().asID().equalsIgnoreCase(EconomyManager.E_CHEST.asID())) {
        if(currency.type().supportsItems()) {
          entryMSG.addReplacement("$ender", CurrencyFormatter.format(account, entry));
        } else {
          entryMSG.addReplacement("$ender", "0");
        }
      }

      if(entry.getHandler().asID().equalsIgnoreCase(EconomyManager.VIRTUAL.asID())) {
        entryMSG.addReplacement("$virtual", CurrencyFormatter.format(account, entry));
      }
    }
    entryMSG.addReplacement("$amount", CurrencyFormatter.format(account, new HoldingsEntry(region, currency.getUid(), amount, EconomyManager.NORMAL)));
    sender.message(entryMSG);
  }

  //ArgumentsParser: <player> <amount> [currency] [from:account]
  public static void onPay(final CmdSource<?> sender, final Account acc, final ParseMoney parseMoney, final Currency currency) {

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.pay." + parseMoney.currency().getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "pay");
        data.addReplacement("$currency", parseMoney.currency().getDisplay());
        sender.message(data);
        return;
      }
    }

    if(parseMoney.amount().compareTo(BigDecimal.ZERO) < 0) {
      sender.message(new MessageData("Messages.Money.Negative"));
      return;
    }

    final Optional<Account> senderAccount = BaseCommand.account(sender, "pay");

    final Optional<Account> accountOpt = BaseCommand.account(acc.getIdentifier(), "payreceive");
    final Account account = accountOpt.orElse(acc);

    if(senderAccount.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }

    if(senderAccount.get().getIdentifier().equals(account.getIdentifier())) {
      final MessageData data = new MessageData("Messages.Money.SelfPay");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }

    if(!MainConfig.yaml().getBoolean("Core.Commands.Pay.Offline", true)) {
      if(!(account instanceof PlayerAccount) || !((PlayerAccount)account).isOnline()) {

        sender.message(new MessageData("Messages.Money.PayFailedOnline"));
        return;
      }
    }

    if(MainConfig.yaml().getInt("Core.Commands.Pay.Radius", 0) > 0) {
      final MessageData data = new MessageData("Messages.Money.PayFailedDistance");
      data.addReplacement("$distance", String.valueOf(MainConfig.yaml().getInt("Core.Commands.Pay.Radius")));

      if(!(senderAccount.get() instanceof PlayerAccount) || !((PlayerAccount)senderAccount.get()).isOnline()
         || !(account instanceof PlayerAccount) || !((PlayerAccount)account).isOnline()) {
        sender.message(data);
        return;
      }

      final Optional<PlayerProvider> senderPlayer = ((PlayerAccount)senderAccount.get()).getPlayer();
      final Optional<PlayerProvider> playerPlayer = ((PlayerAccount)account).getPlayer();
      if(senderPlayer.isEmpty() || playerPlayer.isEmpty()
         || senderPlayer.get().getLocation().isEmpty() || playerPlayer.get().getLocation().isEmpty()) {
        sender.message(data);
        return;
      }

      if(senderPlayer.get().getLocation().get().distance(playerPlayer.get().getLocation().get()) > MainConfig.yaml().getInt("Core.Commands.Pay.Radius")) {
        sender.message(data);
        return;
      }
    }

    final HoldingsModifier modifier = new HoldingsModifier(BaseCommand.region(sender),
                                                           parseMoney.currency().getUid(),
                                                           parseMoney.amount()
    );

    final UUID sourceID = (sender.identifier().isPresent())? sender.identifier().get() : TNECore.instance().getServerAccount();
    final Transaction transaction = new Transaction("pay")
            .to(account, modifier)
            .from(senderAccount.get(), modifier.counter())
            .processor(EconomyManager.baseProcessor())
            .source(new PlayerSource(sourceID));

    final Optional<Receipt> receipt = processTransaction(sender, transaction, account.getName(), parseMoney.amount());
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Paid");
      data.addReplacement("$player", account.getName());
      data.addReplacement("$currency", parseMoney.currency().getIdentifier());
      data.addReplacement("$amount", CurrencyFormatter.format(account,
                                                              modifier.asEntry()));
      sender.message(data);

      final MessageData msgData = new MessageData("Messages.Money.Received");
      msgData.addReplacement("$player", (sender.name() == null)? MainConfig.yaml().getString("Core.Server.Account.Name") : sender.name());
      msgData.addReplacement("$amount", CurrencyFormatter.format(account, modifier.asEntry()));
      MessageHandler.send(account.getIdentifier(), msgData.grab(account.getIdentifier()));

      if(account.isPlayer() && ((PlayerAccount)account).isOnline()) {

        final Optional<PlayerProvider> provider = PluginCore.server().findPlayer(((PlayerAccount)account).getUUID());
        provider.ifPresent(playerProvider->playerProvider.message(msgData));
      }
    }
  }

  //ArgumentsParser: <player> <amount> [currency]
  public static void onRequest(final CmdSource<?> sender, final Account account, final ParseMoney parseMoney, final Currency currency) {

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.request." + parseMoney.currency().getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "request funds");
        data.addReplacement("$currency", parseMoney.currency().getDisplay());
        sender.message(data);
        return;
      }
    }

    if(parseMoney.amount().compareTo(BigDecimal.ZERO) < 0) {
      sender.message(new MessageData("Messages.Money.Negative"));
      return;
    }

    final Optional<PlayerProvider> provider = PluginCore.server().findPlayer(((PlayerAccount)account).getUUID());
    if(provider.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", account.getName());
      sender.message(data);
      return;
    }

    final MessageData msg = new MessageData("Messages.Money.RequestSender");
    msg.addReplacement("$player", account.getName());
    msg.addReplacement("$amount", parseMoney.amount().toPlainString());
    sender.message(msg);

    final MessageData request = new MessageData("Messages.Money.Request");
    request.addReplacement("$player", sender.name());
    request.addReplacement("$amount", parseMoney.amount().toPlainString());
    request.addReplacement("$currency", parseMoney.currency().getIdentifier());
    provider.get().message(request);
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onSet(final CmdSource<?> sender, final Account account, final ParseMoney parseMoney, final String regionParam, final Currency currencyParam) {

    String region = parseMoney.region();
    final Currency currency = parseMoney.currency();

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.set." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "set funds");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    if(player.isPresent() && region.equalsIgnoreCase("world-113")) {
      region = player.get().world();
    }

    region = TNECore.eco().region().resolve(region);

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           parseMoney.amount().setScale(currency.getDecimalPlaces(), RoundingMode.DOWN),
                                                           HoldingsOperation.SET);

    final UUID sourceID = (sender.identifier().isPresent())? sender.identifier().get() : TNECore.instance().getServerAccount();
    final Transaction transaction = new Transaction("set")
            .to(account, modifier)
            .processor(EconomyManager.baseProcessor())
            .source(new PlayerSource(sourceID));

    final Optional<Receipt> receipt = processTransaction(sender, transaction, account.getName(), parseMoney.amount());

    if(receipt.isPresent()) {
      final MessageData msg = new MessageData("Messages.Money.Set");
      msg.addReplacement("$player", account.getName());
      msg.addReplacement("$currency", currency.getIdentifier());
      msg.addReplacement("$amount", CurrencyFormatter.format(account,
                                                             modifier.asEntry())
                        );
      sender.message(msg);
    }
  }

  //ArgumentsParser: <amount> [world] [currency]
  public static void onSetAll(final CmdSource<?> sender, final ParseMoney parseMoney, final String regionParam, final Currency currencyParam) {

    String region = parseMoney.region();
    final Currency currency = parseMoney.currency();

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.setall." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "set all funds");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    if(player.isPresent() && region.equalsIgnoreCase("world-113")) {
      region = player.get().world();
    }

    region = TNECore.eco().region().resolve(region);

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           parseMoney.amount().setScale(currency.getDecimalPlaces(), RoundingMode.DOWN),
                                                           HoldingsOperation.SET);

    final UUID sourceID = (sender.identifier().isPresent())? sender.identifier().get() : TNECore.instance().getServerAccount();
    for(final Account account : TNECore.eco().account().getAccounts().values()) {
      final Transaction transaction = new Transaction("set")
              .to(account, modifier)
              .processor(EconomyManager.baseProcessor())
              .source(new PlayerSource(sourceID));

      final Optional<Receipt> receipt = processTransaction(sender, transaction, account.getName(), parseMoney.amount());

      if(receipt.isPresent()) {
        final MessageData msg = new MessageData("Messages.Money.Set");
        msg.addReplacement("$player", account.getName());
        msg.addReplacement("$currency", currency.getIdentifier());
        msg.addReplacement("$amount", CurrencyFormatter.format(account,
                                                               modifier.asEntry())
                          );

        msg.addReplacements(new String[]{
                ""
        }, new String[]{

        });
        sender.message(msg);
        return;
      }
    }
  }

  public static void onSwitch(final CmdSource<?> sender, final Account account) {

    if(account instanceof final SharedAccount shared) {

      if(sender.identifier().isEmpty()) {

        final MessageData data = new MessageData("Messages.Account.SwitchedFailed");
        data.addReplacement("$account", account.getName());
        sender.message(data);
        return;
      }

      if(!shared.hasPermission(sender.identifier().get(), MemberPermissions.WITHDRAW)) {

        final MessageData data = new MessageData("Messages.Account.SwitchedFailed");
        data.addReplacement("$account", account.getName());
        sender.message(data);
        return;
      }

      doSwitch(sender.identifier().get(), account.getIdentifier());
      final MessageData data = new MessageData("Messages.Account.Switched");
      data.addReplacement("$account", account.getName());
      sender.message(data);
      return;
    } else {
      if(sender.identifier().isPresent() && sender.player().isPresent()) {

        if(account.getIdentifier().equals(sender.identifier().get()) || sender.player().get().hasPermission("tne.money.switch.override")) {

          doSwitch(sender.identifier().get(), account.getIdentifier());
          final MessageData data = new MessageData("Messages.Account.Switched");
          data.addReplacement("$account", account.getName());
          sender.message(data);
          return;
        }
      }
    }
    final MessageData data = new MessageData("Messages.Account.SwitchedFailed");
    data.addReplacement("$account", account.getName());
    sender.message(data);
  }

  //helper method for onSwitch
  private static void doSwitch(final UUID account, final UUID swapAccount) {

    if(swapAccount.equals(account)) {
      TNECore.eco().account().removeSwap("balance", account);
      TNECore.eco().account().removeSwap("convert", account);
      TNECore.eco().account().removeSwap("deposit", account);
      TNECore.eco().account().removeSwap("note", account);
      TNECore.eco().account().removeSwap("pay", account);
      TNECore.eco().account().removeSwap("payreceive", account);
      TNECore.eco().account().removeSwap("withdraw", account);
      return;
    }
    TNECore.eco().account().addSwap("balance", account, swapAccount);
    TNECore.eco().account().addSwap("convert", account, swapAccount);
    TNECore.eco().account().addSwap("deposit", account, swapAccount);
    TNECore.eco().account().addSwap("note", account, swapAccount);
    TNECore.eco().account().addSwap("pay", account, swapAccount);
    TNECore.eco().account().addSwap("payreceive", account, swapAccount);
    TNECore.eco().account().addSwap("withdraw", account, swapAccount);
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onTake(final CmdSource<?> sender, final Account account, final ParseMoney parseMoney, final String regionParam, final Currency currencyParam) {

    String region = parseMoney.region();
    final Currency currency = parseMoney.currency();

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.take." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "take funds");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    if(player.isPresent() && region.equalsIgnoreCase("world-113")) {
      region = player.get().world();
    }

    region = TNECore.eco().region().resolve(region);


    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           parseMoney.amount()
    );

    final UUID sourceID = (sender.identifier().isPresent())? sender.identifier().get() : TNECore.instance().getServerAccount();
    final Transaction transaction = new Transaction("take")
            .to(account, modifier.counter())
            .processor(EconomyManager.baseProcessor())
            .source(new PlayerSource(sourceID));

    final Optional<Receipt> receipt = processTransaction(sender, transaction, account.getName(), parseMoney.amount());
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Took");
      data.addReplacement("$player", account.getName());
      data.addReplacement("$currency", currency.getIdentifier());
      data.addReplacement("$amount", CurrencyFormatter.format(account,
                                                              modifier.asEntry()));
      sender.message(data);

      final MessageData msgData = new MessageData("Messages.Money.Taken");
      msgData.addReplacement("$player", (sender.name() == null)? MainConfig.yaml().getString("Core.Server.Account.Name") : sender.name());
      msgData.addReplacement("$currency", currency.getIdentifier());
      msgData.addReplacement("$amount", CurrencyFormatter.format(account, modifier.asEntry()));
      MessageHandler.send(account.getIdentifier(), msgData.grab(account.getIdentifier()));

      if(account.isPlayer() && ((PlayerAccount)account).isOnline()) {

        final Optional<PlayerProvider> provider = ((PlayerAccount)account).getPlayer();

        provider.ifPresent(playerProvider->playerProvider.message(msgData));
      }
    }
  }

  //ArgumentsParser: [page] [currency:name] [world:world] [limit:#]
  public static void onTop(final CmdSource<?> sender, Integer page, final Currency currency, final Boolean refresh) {

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.top." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "balance top");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    final Optional<Account> senderAccount = BaseCommand.account(sender, "top");

    if(senderAccount.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }

    if(player.isPresent() && TNECore.eco().region().getDisabledRegions().contains(player.get().world())) {

      final MessageData regionMSG = new MessageData("Messages.General.Disabled");
      sender.message(regionMSG);
      return;
    }

    if(refresh && !senderAccount.get().isPlayer() || refresh && senderAccount.get().isPlayer() && ((PlayerAccount)senderAccount.get()).getPlayer().isPresent()
                                                     && ((PlayerAccount)senderAccount.get()).getPlayer().get().hasPermission("tne.money.top.refresh")) {

      TopManager.instance().load();
    }
    final int max = TNECore.eco().getTopManager().page(currency.getUid());

    if(page < 1) page = 1;
    if(page > max) page = max;

    final TopPage<String> pageEntry = TNECore.eco().getTopManager().page(page, currency.getUid());
    if(pageEntry != null) {
      final MessageData data = new MessageData("Messages.Money.Top");
      data.addReplacement("$page", String.valueOf(page));
      data.addReplacement("$page_top", String.valueOf(max));
      sender.message(data);

      final int adjusted = (page - 1) * TOP_PER_PAGE;
      int i = 1;
      for(final Map.Entry<String, BigDecimal> entry : pageEntry.getValues().entrySet()) {
        final MessageData en = new MessageData("Messages.Money.TopEntry");
        en.addReplacement("$pos", (adjusted + i));
        en.addReplacement("$player", entry.getKey());
        en.addReplacement("$amount", CurrencyFormatter.format(senderAccount.get(), new HoldingsEntry(TNECore.eco().region().defaultRegion(), currency.getUid(), entry.getValue(), EconomyManager.NORMAL)));
        sender.message(en);

        i++;
      }
    }
  }

  //ArgumentsParser: <amount> [currency]
  public static void onWithdraw(final CmdSource<?> sender, final ParseMoney parseMoney, final Currency currencyParam, final String regionParam) {

    String region = parseMoney.region();
    final Currency currency = parseMoney.currency();

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.withdraw." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "withdraw funds");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    if(player.isPresent() && region.equalsIgnoreCase("world-113")) {
      region = player.get().world();
    }

    if(parseMoney.amount().compareTo(BigDecimal.ZERO) < 0) {
      sender.message(new MessageData("Messages.Money.Negative"));
      return;
    }

    final Optional<Account> senderAccount = BaseCommand.account(sender, "withdraw");
    if(senderAccount.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }

    region = TNECore.eco().region().resolve(region);

    if(!(currency.type() instanceof MixedType)) {
      sender.message(new MessageData("Messages.Money.NotMixed"));
      return;
    }

    final HoldingsModifier modifier = new HoldingsModifier(BaseCommand.region(sender),
                                                           currency.getUid(),
                                                           parseMoney.amount(),
                                                           EconomyManager.ITEM_ONLY
    );

    final UUID sourceID = (sender.identifier().isPresent())? sender.identifier().get() : TNECore.instance().getServerAccount();
    final Transaction transaction = new Transaction("withdraw")
            .to(senderAccount.get(), modifier)
            .from(senderAccount.get(), modifier.counter(EconomyManager.VIRTUAL))
            .processor(EconomyManager.baseProcessor())
            .source(new PlayerSource(sourceID));

    final Optional<Receipt> receipt = processTransaction(sender, transaction, senderAccount.get().getName(), parseMoney.amount());
    if(receipt.isPresent()) {

      final MessageData data = new MessageData("Messages.Money.Withdrawn");
      data.addReplacement("$currency", currency.getIdentifier());
      data.addReplacement("$amount", CurrencyFormatter.format(senderAccount.get(),
                                                              modifier.asEntry()));
      sender.message(data);
    }
  }

  public static Optional<Receipt> processTransaction(final CmdSource<?> sender, final Transaction transaction, final String modifiedAccount, final BigDecimal modifier) {

    try {
      final TransactionResult result = transaction.process();

      if(!result.isSuccessful()) {
        final MessageData data = new MessageData(result.getMessage());
        data.addReplacement("$player", modifiedAccount);
        data.addReplacement("$amount", modifier.toPlainString());

        sender.message(data);
        return Optional.empty();
      }

      return result.getReceipt();
    } catch(final InvalidTransactionException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }
}