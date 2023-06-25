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
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Note;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.transaction.processor.BaseTransactionProcessor;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import net.tnemc.item.AbstractItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    for(String str : TNECore.eco().region().getRegions().keySet()) {
      TNECore.log().debug("Region: " + str, DebugLevel.DEVELOPER);
    }

    TNECore.log().debug("Regions: " + TNECore.eco().region().getRegions().keySet().size(), DebugLevel.DEVELOPER);

    TNECore.log().debug("Resolver check", DebugLevel.DEVELOPER);
    TNECore.log().debug("Resolved Nether:" + TNECore.eco().region().resolve("world_the_nether"), DebugLevel.DEVELOPER);
    TNECore.log().debug("Resolved invalid:" + TNECore.eco().region().resolve("world113"), DebugLevel.DEVELOPER);

    //If our currency doesn't exist this is probably a username, so check for their balance instead.
    final Optional<Account> account = sender.account();
    account.ifPresent(value->onOther(sender, value, region, currency));
  }

  //ArgumentsParser: <amount> <to currency> [from currency]
  public static void onConvert(CmdSource<?> sender, BigDecimal amount, Currency currency, Currency fromCurrency) {
    if(currency.getUid().equals(fromCurrency.getUid())) {
      sender.message(new MessageData("Messages.Money.ConvertSame"));
      return;
    }

    Optional<Account> account = sender.account();
    if(account.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }

    final Optional<BigDecimal> converted = fromCurrency.convertValue(currency.getIdentifier(), amount);
    if(converted.isEmpty()) {
      final MessageData data = new MessageData("Messages.Money.NoConversion");
      data.addReplacement("$converted", currency.getIdentifier());
      sender.message(data);
      return;
    }

    final HoldingsModifier modifier = new HoldingsModifier(sender.region(),
                                                           currency.getUid(),
                                                           converted.get()
    );

    final HoldingsModifier modifierFrom = new HoldingsModifier(sender.region(),
                                                               fromCurrency.getUid(),
                                                               amount.negate()
    );

    final Transaction transaction = new Transaction("convert")
        .from(account.get(), modifierFrom)
        .to(account.get(), modifier)
        .processor(new BaseTransactionProcessor())
        .source(new PlayerSource(sender.identifier()));

    Optional<Receipt> receipt = processTransaction(sender, transaction);
    if(receipt.isPresent()){
      final MessageData data = new MessageData("Messages.Money.Converted");
      data.addReplacement("$from_amount", amount.toPlainString());
      data.addReplacement("$amount", CurrencyFormatter.format(account.get(),
                                                              modifierFrom.asEntry()));
      sender.message(data);
    }
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onGive(CmdSource<?> sender, Account player, BigDecimal amount, String region, Currency currency) {

    region = TNECore.eco().region().resolve(region);

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           amount);

    final Transaction transaction = new Transaction("give")
        .to(player, modifier)
        .processor(new BaseTransactionProcessor())
        .source(new PlayerSource(sender.identifier()));

    Optional<Receipt> receipt = processTransaction(sender, transaction);
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Gave");
      data.addReplacement("$player", player.getName());
      data.addReplacement("$amount", CurrencyFormatter.format(player,
                                                              modifier.asEntry()));
      sender.message(data);

      if(player.isPlayer() && ((PlayerAccount)player).isOnline()) {

        final Optional<PlayerProvider> provider = ((PlayerAccount)player).getPlayer();

        if(provider.isPresent()) {
          final MessageData msgData = new MessageData("Messages.Money.Given");
          msgData.addReplacement("$amount", CurrencyFormatter.format(player,
                                                                     modifier.asEntry()));
          provider.get().message(msgData);
        }
      }
    }
  }

  //ArgumentsParser: <amount> [currency]
  public static void onNote(CmdSource<?> sender, BigDecimal amount, Currency currency) {

    final Optional<Account> account = sender.account();
    final Optional<Note> note = currency.getNote();
    if(account.isPresent() && note.isPresent() && account.get() instanceof PlayerAccount) {

      final Optional<PlayerProvider> provider = ((PlayerAccount)account.get()).getPlayer();
      if(provider.isEmpty()) {
        return;
      }

      if(amount.compareTo(note.get().getMinimum()) < 0) {
        sender.message(new MessageData("Messages.Note.Minimum"));
        return;
      }

      final BigDecimal amt = amount.add(note.get().getFee());

      final HoldingsModifier modifier = new HoldingsModifier(sender.region(),
                                                             currency.getUid(),
                                                             amt
      );

      final Transaction transaction = new Transaction("note")
          .from(account.get(), modifier.counter())
          .processor(new BaseTransactionProcessor())
          .source(new PlayerSource(sender.identifier()));


      Optional<Receipt> receipt = processTransaction(sender, transaction);
      if(receipt.isPresent()) {
        Collection<AbstractItemStack<Object>> left = TNECore.server().calculations().giveItems(Collections.singletonList(note.get().stack(currency.getIdentifier(), sender.region(), amt)), provider.get().inventory().getInventory(false));

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
  public static void onOther(CmdSource<?> sender, Account player, String region, Currency currency) {
    final List<HoldingsEntry> holdings = new ArrayList<>();

    region = TNECore.eco().region().resolve(region);

    if(!currency.isGlobalDefault()) {
      BigDecimal amount = BigDecimal.ZERO;
      for(HoldingsEntry entry : player.getHoldings(region, currency.getUid())) {
        amount = amount.add(entry.getAmount());
      }
      holdings.add(new HoldingsEntry(region, currency.getUid(), amount, EconomyManager.NORMAL));
    } else {
      holdings.addAll(player.getAllHoldings(region, EconomyManager.NORMAL));
    }

    if(holdings.isEmpty()) {

      //Shouldn't happen, but if it does handle it.
      final MessageData msg = new MessageData("Messages.Account.NoHoldings");
      msg.addReplacement("$currency", currency.getIdentifier());
      sender.message(msg);
      return;
    }

    final MessageData msg = new MessageData("Messages.Money.HoldingsMulti");
    msg.addReplacement("$world", region);
    sender.message(msg);

    for(HoldingsEntry entry : holdings) {

      Optional<Currency> entryCur = TNECore.eco().currency().findCurrency(entry.getCurrency());

      if(entryCur.isPresent()) {

        final MessageData entryMSG = new MessageData("Messages.Money.HoldingsMultiSingle");
        entryMSG.addReplacement("$currency", entryCur.get().getIdentifier());
        entryMSG.addReplacement("$amount", CurrencyFormatter.format(player, entry));
        sender.message(entryMSG);
      }
    }
  }

  //ArgumentsParser: <player> <amount> [currency] [from:account]
  public static void onPay(CmdSource<?> sender, Account player, BigDecimal amount, Currency currency, String from) {

    Optional<Account> senderAccount = sender.account();

    if(senderAccount.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", sender.name());
      sender.message(data);
      return;
    }

    final HoldingsModifier modifier = new HoldingsModifier(sender.region(),
                                                           currency.getUid(),
                                                           amount
    );

    final Transaction transaction = new Transaction("pay")
        .to(player, modifier)
        .from(senderAccount.get(), modifier.counter())
        .processor(new BaseTransactionProcessor())
        .source(new PlayerSource(sender.identifier()));

    final Optional<Receipt> receipt = processTransaction(sender, transaction);
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Paid");
      data.addReplacement("$player", player.getName());
      data.addReplacement("$amount", CurrencyFormatter.format(player,
                                                              modifier.asEntry()));
      sender.message(data);

      if(player.isPlayer() && ((PlayerAccount)player).isOnline()) {

        final Optional<PlayerProvider> provider = TNECore.server().findPlayer(((PlayerAccount)player).getUUID());
        if(provider.isPresent()) {
          final MessageData msgData = new MessageData("Messages.Money.Received");
          data.addReplacement("$player", sender.name());
          msgData.addReplacement("$amount", CurrencyFormatter.format(player,
                                                                     modifier.asEntry()));
          provider.get().message(msgData);
        }
      }
    }
  }

  //ArgumentsParser: <player> <amount> [currency]
  public static void onRequest(CmdSource<?> sender, Account player, BigDecimal amount, Currency currency) {
    Optional<PlayerProvider> provider = TNECore.server().findPlayer(((PlayerAccount)player).getUUID());
    if(provider.isEmpty()) {
      final MessageData data = new MessageData("Messages.General.NoPlayer");
      data.addReplacement("$player", player.getName());
      sender.message(data);
      return;
    }

    final MessageData msg = new MessageData("Messages.Money.RequestSender");
    msg.addReplacement("$player", player.getName());
    msg.addReplacement("$amount", amount.toPlainString());
    sender.message(msg);

    final MessageData request = new MessageData("Messages.Money.Request");
    request.addReplacement("$player", sender.name());
    msg.addReplacement("$amount", amount.toPlainString());
    request.addReplacement("$currency", currency.getIdentifier());
    provider.get().message(request);
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onSet(CmdSource<?> sender, Account player, BigDecimal amount, String region, Currency currency) {

    region = TNECore.eco().region().resolve(region);

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           amount,
                                                           HoldingsOperation.SET);

    final Transaction transaction = new Transaction("set")
        .to(player, modifier)
        .processor(new BaseTransactionProcessor())
        .source(new PlayerSource(sender.identifier()));

    Optional<Receipt> receipt = processTransaction(sender, transaction);

    if(receipt.isPresent()) {
      final MessageData msg = new MessageData("Messages.Money.Set");
      msg.addReplacement("$player", player.getName());
      msg.addReplacement("$amount", CurrencyFormatter.format(player,
                                                             modifier.asEntry())
      );
      sender.message(msg);
    }
  }

  //ArgumentsParser: <amount> [world] [currency]
  public static void onSetAll(CmdSource<?> sender, BigDecimal amount, String region, Currency currency) {

    region = TNECore.eco().region().resolve(region);

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           amount,
                                                           HoldingsOperation.SET);

    for(Account account : TNECore.eco().account().getAccounts().values()) {
      final Transaction transaction = new Transaction("set")
          .to(account, modifier)
          .processor(new BaseTransactionProcessor())
          .source(new PlayerSource(sender.identifier()));

      Optional<Receipt> receipt = processTransaction(sender, transaction);

      if(receipt.isPresent()) {
        final MessageData msg = new MessageData("Messages.Money.Set");
        msg.addReplacement("$player", account.getName());
        msg.addReplacement("$amount", CurrencyFormatter.format(account,
                                                               modifier.asEntry())
        );
        sender.message(msg);
        return;
      }
    }
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onTake(CmdSource<?> sender, Account player, BigDecimal amount, String region, Currency currency) {

    region = TNECore.eco().region().resolve(region);

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency.getUid(),
                                                           amount
    );

    final Transaction transaction = new Transaction("take")
        .to(player, modifier.counter())
        .processor(new BaseTransactionProcessor())
        .source(new PlayerSource(sender.identifier()));

    Optional<Receipt> receipt = processTransaction(sender, transaction);
    if(receipt.isPresent()) {
      final MessageData data = new MessageData("Messages.Money.Took");
      data.addReplacement("$player", player.getName());
      data.addReplacement("$amount", CurrencyFormatter.format(player,
                                                              modifier.asEntry()));
      sender.message(data);

      if(player.isPlayer() && ((PlayerAccount)player).isOnline()) {

        final Optional<PlayerProvider> provider = ((PlayerAccount)player).getPlayer();

        if(provider.isPresent()) {
          final MessageData msgData = new MessageData("Messages.Money.Taken");
          data.addReplacement("$player", sender.name());
          msgData.addReplacement("$amount", CurrencyFormatter.format(player,
                                                                     modifier.asEntry()));
          provider.get().message(msgData);
        }
      }
    }
  }

  //ArgumentsParser: [page] [currency:name] [world:world] [limit:#]
  public static void onTop() {
    //TODO: This
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