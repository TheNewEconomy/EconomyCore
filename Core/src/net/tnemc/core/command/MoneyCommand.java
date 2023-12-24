package net.tnemc.core.command;

/*
 * The New Economy
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
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
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.account.holdings.modify.HoldingsOperation;
import net.tnemc.core.actions.source.PlayerSource;
import net.tnemc.core.command.parameters.PercentBigDecimal;
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Note;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.currency.type.MixedType;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.manager.TopManager;
import net.tnemc.core.manager.top.TopPage;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import net.tnemc.item.AbstractItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * MoneyCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MoneyCommand extends BaseCommand {

  //ArgumentsParser: [currency]
  public static void onMyBal(CmdSource<?> sender, Currency currency) {
    if(sender.player().isPresent()) {
      sender.player().get().inventory().openMenu(sender.player().get(), "my_bal");
    }
  }

  //ArgumentsParser: [currency] [world]
  public static void onBalance(CmdSource<?> sender, Currency currency, String region) {

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

    final Optional<Account> account = sender.account();
    if(account.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }
    onOther(sender, account.get(), region, currency);
  }

  //ArgumentsParser: <amount> <to currency> [from currency]
  public static void onConvert(CmdSource<?> sender, PercentBigDecimal amount, Currency currency, Currency fromCurrency) {

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

    final Optional<Account> account = sender.account();
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

    final HoldingsModifier modifier = new HoldingsModifier(sender.region(),
                                                           currency.getUid(),
                                                           converted.get().setScale(currency.getDecimalPlaces(), RoundingMode.DOWN)
    );

    final HoldingsModifier modifierFrom = new HoldingsModifier(sender.region(),
                                                               fromCurrency.getUid(),
                                                               amount.value().setScale(currency.getDecimalPlaces(), RoundingMode.DOWN).negate()
    );

    final Transaction transaction = new Transaction("convert")
        .from(account.get(), modifierFrom)
        .to(account.get(), modifier)
        .processor(EconomyManager.baseProcessor())
        .source(new PlayerSource(sender.identifier()));

    final Optional<Receipt> receipt = processTransaction(sender, transaction);
    if(receipt.isPresent()){
      final MessageData data = new MessageData("Messages.Money.Converted");
      data.addReplacement("$from_amount", amount.value().toPlainString());
      data.addReplacement("$amount", CurrencyFormatter.format(account.get(),
                                                              modifierFrom.asEntry()));
      sender.message(data);
    }
  }

  //ArgumentsParser: <amount> [currency]
  public static void onDeposit(CmdSource<?> sender, PercentBigDecimal amount, Currency currency, String region) {

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

    if(amount.value().compareTo(BigDecimal.ZERO) < 0) {
      sender.message(new MessageData("Messages.Money.Negative"));
      return;
    }

    final Optional<Account> senderAccount = sender.account();

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

    final HoldingsModifier modifier = new HoldingsModifier(sender.region(),
            currency.getUid(),
            amount,
            EconomyManager.VIRTUAL
    );

    final Transaction transaction = new Transaction("deposit")
            .to(senderAccount.get(), modifier)
            .from(senderAccount.get(), modifier.counter(EconomyManager.ITEM_ONLY))
            .processor(EconomyManager.baseProcessor())
            .source(new PlayerSource(sender.identifier()));

    final Optional<Receipt> receipt = processTransaction(sender, transaction);
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Deposit");
      data.addReplacement("$amount", CurrencyFormatter.format(senderAccount.get(),
              modifier.asEntry()));
      sender.message(data);
    }
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onGive(CmdSource<?> sender, Account account, PercentBigDecimal amount, String region, Currency currency) {

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

    region = TNECore.eco().region().resolve(region);

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           amount);

    final Transaction transaction = new Transaction("give")
        .to(account, modifier)
        .processor(EconomyManager.baseProcessor())
        .source(new PlayerSource(sender.identifier()));

    final Optional<Receipt> receipt = processTransaction(sender, transaction);
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Gave");
      data.addReplacement("$player", account.getName());
      data.addReplacement("$currency", currency.getIdentifier());
      data.addReplacement("$amount", CurrencyFormatter.format(account,
                                                              modifier.asEntry()));
      sender.message(data);

      if(account.isPlayer() && ((PlayerAccount)account).isOnline()) {

        final Optional<PlayerProvider> provider = ((PlayerAccount)account).getPlayer();

        if(provider.isPresent()) {
          final MessageData msgData = new MessageData("Messages.Money.Given");
          msgData.addReplacement("$currency", currency.getIdentifier());
          msgData.addReplacement("$player", (sender.name() == null)? MainConfig.yaml().getString("Core.Server.Account.Name") : sender.name());
          msgData.addReplacement("$amount", CurrencyFormatter.format(account,
                                                                     modifier.asEntry()));
          provider.get().message(msgData);
        }
      }
    }
  }

  //ArgumentsParser: <amount> [currency]
  public static void onNote(CmdSource<?> sender, PercentBigDecimal amount, Currency currency) {

    final Optional<Account> account = sender.account();
    final Optional<Note> note = currency.getNote();
    if(account.isPresent() && note.isPresent() && account.get() instanceof PlayerAccount) {

      final Optional<PlayerProvider> provider = ((PlayerAccount)account.get()).getPlayer();
      if(provider.isEmpty()) {
        return;
      }

      if(EconomyManager.limitCurrency() && !provider.get().hasPermission("tne.money.note." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "note");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }

      if(amount.value().compareTo(note.get().getMinimum()) < 0) {
        final MessageData min = new MessageData("Messages.Note.Minimum");
        min.addReplacement("$amount", note.get().getMinimum().toPlainString());
        sender.message(min);
        return;
      }

      final BigDecimal rounded = amount.value().setScale(currency.getDecimalPlaces(), RoundingMode.DOWN);

      final BigDecimal amt = rounded.add(note.get().getFee().calculateTax(rounded)).setScale(currency.getDecimalPlaces(), RoundingMode.DOWN);

      final HoldingsModifier modifier = new HoldingsModifier(sender.region(),
                                                             currency.getUid(),
              amt
      );

      final Transaction transaction = new Transaction("note")
          .from(account.get(), modifier.counter())
          .processor(EconomyManager.baseProcessor())
          .source(new PlayerSource(sender.identifier()));


      final Optional<Receipt> receipt = processTransaction(sender, transaction);
      if(receipt.isPresent()) {
        final Collection<AbstractItemStack<Object>> left = TNECore.server().calculations().giveItems(Collections.singletonList(note.get().stack(currency.getIdentifier(), sender.region(), rounded)), provider.get().inventory().getInventory(false));

        if(left.size() > 0) {
          TNECore.server().calculations().drop(left, ((PlayerAccount)account.get()).getUUID());
        }

        final MessageData entryMSG = new MessageData("Messages.Note.Given");
        entryMSG.addReplacement("$currency",currency.getIdentifier());
        entryMSG.addReplacement("$amount", CurrencyFormatter.format(account.get(), modifier.asEntry()));
        sender.message(entryMSG);
      }
    }
  }

  //ArgumentsParser: <player> [world] [currency]
  public static void onOther(CmdSource<?> sender, Account account, String region, Currency currency) {

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.other." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "balance check other");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    region = TNECore.eco().region().resolve(region);

    final MessageData msg = new MessageData("Messages.Money.HoldingsMulti");
    msg.addReplacement("$world", region);
    sender.message(msg);

    final String resolve = region;

    TNECore.eco().currency().currencies().forEach((cur)->{

      final MessageData entryMSG = new MessageData("Messages.Money.HoldingsMultiSingle");
      entryMSG.addReplacement("$currency", cur.getIdentifier());

      BigDecimal amount = BigDecimal.ZERO;
      for(HoldingsEntry entry : cur.type().getHoldings(account, resolve, cur, EconomyManager.NORMAL)) {
        amount = amount.add(entry.getAmount());

        if(entry.getHandler().asID().equalsIgnoreCase(EconomyManager.INVENTORY_ONLY.asID())) {
          if(cur.type().supportsItems()) {
            entryMSG.addReplacement("$inventory", CurrencyFormatter.format(account, entry));
          } else {
            entryMSG.addReplacement("$inventory", "0");
          }
        }

        if(entry.getHandler().asID().equalsIgnoreCase(EconomyManager.E_CHEST.asID())) {
          if(cur.type().supportsItems()) {
            entryMSG.addReplacement("$ender", CurrencyFormatter.format(account, entry));
          } else {
            entryMSG.addReplacement("$ender", "0");
          }
        }

        if(entry.getHandler().asID().equalsIgnoreCase(EconomyManager.VIRTUAL.asID())) {
          entryMSG.addReplacement("$virtual", CurrencyFormatter.format(account, entry));
        }
      }

      entryMSG.addReplacement("$amount", CurrencyFormatter.format(account, new HoldingsEntry(resolve, cur.getUid(), amount, EconomyManager.NORMAL)));
      sender.message(entryMSG);
    });
  }

  //ArgumentsParser: <player> <amount> [currency] [from:account]
  public static void onPay(CmdSource<?> sender, Account account, PercentBigDecimal amount, Currency currency, String from) {

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.pay." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "pay");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }

    if(amount.value().compareTo(BigDecimal.ZERO) < 0) {
      sender.message(new MessageData("Messages.Money.Negative"));
      return;
    }

    final Optional<Account> senderAccount = sender.account();

    if(senderAccount.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }

    if(senderAccount.get().getIdentifier().equalsIgnoreCase(account.getIdentifier())) {
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

    final HoldingsModifier modifier = new HoldingsModifier(sender.region(),
                                                           currency.getUid(),
            amount
    );

    final Transaction transaction = new Transaction("pay")
        .to(account, modifier)
        .from(senderAccount.get(), modifier.counter())
        .processor(EconomyManager.baseProcessor())
        .source(new PlayerSource(sender.identifier()));

    final Optional<Receipt> receipt = processTransaction(sender, transaction);
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Paid");
      data.addReplacement("$player", account.getName());
      data.addReplacement("$currency", currency.getIdentifier());
      data.addReplacement("$amount", CurrencyFormatter.format(account,
                                                              modifier.asEntry()));
      sender.message(data);

      if(account.isPlayer() && ((PlayerAccount)account).isOnline()) {

        final Optional<PlayerProvider> provider = TNECore.server().findPlayer(((PlayerAccount)account).getUUID());
        if(provider.isPresent()) {

          final MessageData msgData = new MessageData("Messages.Money.Received");
          msgData.addReplacement("$player", (sender.name() == null)? MainConfig.yaml().getString("Core.Server.Account.Name") : sender.name());
          msgData.addReplacement("$amount", CurrencyFormatter.format(account,
                                                                     modifier.asEntry()));
          provider.get().message(msgData);
        }
      }
    }
  }

  //ArgumentsParser: <player> <amount> [currency]
  public static void onRequest(CmdSource<?> sender, Account account, BigDecimal amount, Currency currency) {

    final Optional<PlayerProvider> player = sender.player();
    if(EconomyManager.limitCurrency() && player.isPresent()) {
      if(!player.get().hasPermission("tne.money.request." + currency.getIdentifier())) {
        final MessageData data = new MessageData("Messages.Account.BlockedAction");
        data.addReplacement("$action", "request funds");
        data.addReplacement("$currency", currency.getDisplay());
        sender.message(data);
        return;
      }
    }
    if(amount.compareTo(BigDecimal.ZERO) < 0) {
      sender.message(new MessageData("Messages.Money.Negative"));
      return;
    }

    final Optional<PlayerProvider> provider = TNECore.server().findPlayer(((PlayerAccount)account).getUUID());
    if(provider.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", account.getName());
      sender.message(data);
      return;
    }

    final MessageData msg = new MessageData("Messages.Money.RequestSender");
    msg.addReplacement("$player", account.getName());
    msg.addReplacement("$amount", amount.toPlainString());
    sender.message(msg);

    final MessageData request = new MessageData("Messages.Money.Request");
    request.addReplacement("$player", sender.name());
    request.addReplacement("$amount", amount.toPlainString());
    request.addReplacement("$currency", currency.getIdentifier());
    provider.get().message(request);
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onSet(CmdSource<?> sender, Account account, BigDecimal amount, String region, Currency currency) {

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

    region = TNECore.eco().region().resolve(region);

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
            amount.setScale(currency.getDecimalPlaces(), RoundingMode.DOWN),
                                                           HoldingsOperation.SET);

    final Transaction transaction = new Transaction("set")
        .to(account, modifier)
        .processor(EconomyManager.baseProcessor())
        .source(new PlayerSource(sender.identifier()));

    final Optional<Receipt> receipt = processTransaction(sender, transaction);

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
  public static void onSetAll(CmdSource<?> sender, BigDecimal amount, String region, Currency currency) {

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

    region = TNECore.eco().region().resolve(region);

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
            amount.setScale(currency.getDecimalPlaces(), RoundingMode.DOWN),
                                                           HoldingsOperation.SET);

    for(Account account : TNECore.eco().account().getAccounts().values()) {
      final Transaction transaction = new Transaction("set")
          .to(account, modifier)
          .processor(EconomyManager.baseProcessor())
          .source(new PlayerSource(sender.identifier()));

      final Optional<Receipt> receipt = processTransaction(sender, transaction);

      if(receipt.isPresent()) {
        final MessageData msg = new MessageData("Messages.Money.Set");
        msg.addReplacement("$player", account.getName());
        msg.addReplacement("$currency", currency.getIdentifier());
        msg.addReplacement("$amount", CurrencyFormatter.format(account,
                                                               modifier.asEntry())
        );

        msg.addReplacements(new String[] {
                ""
        }, new String[] {

        });
        sender.message(msg);
        return;
      }
    }
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onTake(CmdSource<?> sender, Account account, PercentBigDecimal amount, String region, Currency currency) {

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

    region = TNECore.eco().region().resolve(region);


    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           amount
    );

    final Transaction transaction = new Transaction("take")
        .to(account, modifier.counter())
        .processor(EconomyManager.baseProcessor())
        .source(new PlayerSource(sender.identifier()));

    final Optional<Receipt> receipt = processTransaction(sender, transaction);
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Took");
      data.addReplacement("$player", account.getName());
      data.addReplacement("$currency", currency.getIdentifier());
      data.addReplacement("$amount", CurrencyFormatter.format(account,
                                                              modifier.asEntry()));
      sender.message(data);

      if(account.isPlayer() && ((PlayerAccount)account).isOnline()) {

        final Optional<PlayerProvider> provider = ((PlayerAccount)account).getPlayer();

        if(provider.isPresent()) {
          final MessageData msgData = new MessageData("Messages.Money.Taken");
          msgData.addReplacement("$player", (sender.name() == null)? MainConfig.yaml().getString("Core.Server.Account.Name") : sender.name());
          msgData.addReplacement("$currency", currency.getIdentifier());
          msgData.addReplacement("$amount", CurrencyFormatter.format(account,
                                                                     modifier.asEntry()));
          provider.get().message(msgData);
        }
      }
    }
  }

  //ArgumentsParser: [page] [currency:name] [world:world] [limit:#]
  public static void onTop(CmdSource<?> sender, Integer page, Currency currency, Boolean refresh) {

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

    final Optional<Account> senderAccount = sender.account();

    if(senderAccount.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }

    if(refresh && !senderAccount.get().isPlayer() || refresh && senderAccount.get().isPlayer() && ((PlayerAccount)senderAccount.get()).getPlayer().isPresent()
            && ((PlayerAccount)senderAccount.get()).getPlayer().get().hasPermission("tne.money.top.refresh")) {

      TopManager.instance().load();
    }

    TopPage<String> pageEntry = TNECore.eco().getTopManager().page(page, currency.getUid());
    if(pageEntry != null) {
      final MessageData data = new MessageData("Messages.Money.Top");
      data.addReplacement("$page", String.valueOf(page));
      data.addReplacement("$page_top", String.valueOf(TNECore.eco().getTopManager().page(currency.getUid())));
      sender.message(data);

      for(Map.Entry<String, BigDecimal> entry : pageEntry.getValues().entrySet()) {
        final MessageData en = new MessageData("Messages.Money.TopEntry");
        en.addReplacement("$player", entry.getKey());
        en.addReplacement("$amount", CurrencyFormatter.format(senderAccount.get(), new HoldingsEntry(TNECore.eco().region().defaultRegion(), currency.getUid(), entry.getValue(), EconomyManager.NORMAL)));
        sender.message(en);
      }
    }
  }

  //ArgumentsParser: <amount> [currency]
  public static void onWithdraw(CmdSource<?> sender, PercentBigDecimal amount, Currency currency, String region) {

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

    if(amount.value().compareTo(BigDecimal.ZERO) < 0) {
      sender.message(new MessageData("Messages.Money.Negative"));
      return;
    }

    final Optional<Account> senderAccount = sender.account();
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

    final HoldingsModifier modifier = new HoldingsModifier(sender.region(),
            currency.getUid(),
            amount,
            EconomyManager.ITEM_ONLY
    );

    final Transaction transaction = new Transaction("withdraw")
            .to(senderAccount.get(), modifier)
            .from(senderAccount.get(), modifier.counter(EconomyManager.VIRTUAL))
            .processor(EconomyManager.baseProcessor())
            .source(new PlayerSource(sender.identifier()));

    final Optional<Receipt> receipt = processTransaction(sender, transaction);
    if(receipt.isPresent()) {

      final MessageData data = new MessageData("Messages.Money.Withdrawn");
      data.addReplacement("$currency", currency.getIdentifier());
      data.addReplacement("$amount", CurrencyFormatter.format(senderAccount.get(),
              modifier.asEntry()));
      sender.message(data);
    }
  }

  private static Optional<Receipt> processTransaction(CmdSource<?> sender, Transaction transaction) {
    try {
      final TransactionResult result = transaction.process();

      if(!result.isSuccessful()) {
        sender.message(new MessageData(result.getMessage()));
        return Optional.empty();
      }

      if(result.getReceipt().isPresent()) {
        if(transaction.getTo() != null) {
          transaction.getTo().asAccount().ifPresent((account->account.log(result.getReceipt().get())));
        }

        if(transaction.getFrom() != null) {
          transaction.getFrom().asAccount().ifPresent((account->account.log(result.getReceipt().get())));
        }
      }

      return result.getReceipt();
    } catch(InvalidTransactionException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }
}