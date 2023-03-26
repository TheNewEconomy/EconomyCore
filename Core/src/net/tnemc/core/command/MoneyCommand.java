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

import co.aikar.commands.BaseCommand;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.HoldingsType;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.account.holdings.modify.HoldingsOperation;
import net.tnemc.core.actions.source.PlayerSource;
import net.tnemc.core.command.args.ArgumentsParser;
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.transaction.processor.BaseTransactionProcessor;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;

import java.math.BigDecimal;
import java.util.ArrayList;
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
  public static void onMyBal(ArgumentsParser parser) {
    if(parser.sender().player().isPresent()) {
      parser.sender().player().get().inventory().openMenu(parser.sender().player().get(), "my_bal");
    }
  }

  //ArgumentsParser: [currency] [world]
  public static void onBalance(ArgumentsParser parser) {
    final String currency = (parser.args().length >= 1)? parser.args()[0] : "USD";

    //If our currency doesn't exist this is probably a username, so check for their balance instead.
    Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency(currency);
    if(currencyOptional.isEmpty()) {
      final String identifier = (parser.args().length > 0)? parser.args()[0] : parser.sender().identifier().toString();
      onOther(parser.splitArgsAt(1), identifier);
    }
  }

  //ArgumentsParser: <amount> <to currency> [from currency]
  public static void onConvert(ArgumentsParser parser) {

    long startTime = System.nanoTime();
    if(parser.args().length < 2) {
      //TODO: Help
      return;
    }

    final Currency currency = parser.parseCurrency(1);
    final Currency fromCurrency = parser.parseCurrency(2);

    final Optional<BigDecimal> amount = parser.parseAmount(0);
    if(amount.isPresent()) {

      if(currency.getUid().equals(fromCurrency.getUid())) {
        parser.sender().message(new MessageData("Messages.Money.ConvertSame"));
        return;
      }

      Optional<Account> account = parser.sender().account();
      if(account.isEmpty()) {
        final MessageData data = new MessageData("Messages.General.NoPlayer");
        data.addReplacement("$player", parser.sender().name());
        parser.sender().message(data);
        return;
      }

      final Optional<BigDecimal> converted = fromCurrency.convertValue(currency.getIdentifier(), amount.get());
      if(converted.isEmpty()) {
        final MessageData data = new MessageData("Messages.Money.NoConversion");
        data.addReplacement("$converted", currency.getIdentifier());
        parser.sender().message(data);
        return;
      }

      final HoldingsModifier modifier = new HoldingsModifier(parser.sender().region(),
                                                             currency.getUid(),
                                                             converted.get()
      );

      final HoldingsModifier modifierFrom = new HoldingsModifier(parser.sender().region(),
                                                                 fromCurrency.getUid(),
                                                                 amount.get().negate()
      );

      final Transaction transaction = new Transaction("convert")
          .from(account.get(), modifierFrom)
          .to(account.get(), modifier)
          .processor(new BaseTransactionProcessor())
          .source(new PlayerSource(parser.sender().identifier()));

      Optional<Receipt> receipt = processTransaction(parser.sender(), transaction);
      if(receipt.isPresent()){
        final MessageData data = new MessageData("Messages.Money.Converted");
        data.addReplacement("$from_amount", amount.get().toPlainString());
        data.addReplacement("$amount", converted.get().toPlainString());
        parser.sender().message(data);
      }
    }

    //TODO: Receipt logging and success checking
    long endTime = System.nanoTime();

    long duration = (endTime - startTime);

    parser.sender().message(new MessageData("<red>Transaction took " + duration + "to execute!"));

    //TODO: Success message
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onGive(ArgumentsParser parser) {
    long startTime = System.nanoTime();
    if(parser.args().length < 2) {
      //TODO: Help
      return;
    }

    final String region = parser.parseRegion(2);
    final Currency currency = parser.parseCurrency(3, region);

    final Optional<Account> account = parser.parseAccount(0);
    final Optional<BigDecimal> amount = parser.parseAmount(1);
    if(account.isPresent() && amount.isPresent()) {

      final HoldingsModifier modifier = new HoldingsModifier(region,
                                                             currency.getUid(),
                                                             amount.get()
      );

      final Transaction transaction = new Transaction("give")
          .to(account.get(), modifier)
          .processor(new BaseTransactionProcessor())
          .source(new PlayerSource(parser.sender().identifier()));

      Optional<Receipt> receipt = processTransaction(parser.sender(), transaction);
      if(receipt.isPresent()) {
        //TODO: Success handler
      }
    }

    long endTime = System.nanoTime();

    long duration = (endTime - startTime);

    parser.sender().message(new MessageData("<red>Transaction took " + duration + "to execute!"));

    //TODO: Success message
  }

  //ArgumentsParser: <amount> [currency]
  public static void onNote(ArgumentsParser parser) {
    long startTime = System.nanoTime();
    if(parser.args().length < 1) {
      //TODO: Help
      return;
    }

    final Currency currency = parser.parseCurrency(3);

    final Optional<Account> account = parser.parseAccount(0);
    final Optional<BigDecimal> amount = parser.parseAmount(1);
    if(account.isPresent() && amount.isPresent()) {


      final HoldingsModifier modifier = new HoldingsModifier(parser.sender().region(),
                                                             currency.getUid(),
                                                             amount.get()
      );

      final Transaction transaction = new Transaction("note")
          .from(account.get(), modifier.counter())
          .processor(new BaseTransactionProcessor())
          .source(new PlayerSource(parser.sender().identifier()));


      Optional<Receipt> receipt = processTransaction(parser.sender(), transaction);
      if(receipt.isPresent()) {
        //TODO: Success checking.
      }
    }

    //TODO: Receipt logging and success checking
    long endTime = System.nanoTime();

    long duration = (endTime - startTime);

    parser.sender().message(new MessageData("<red>Transaction took " + duration + "to execute!"));
  }

  public static void onOther(ArgumentsParser parser) {
    if(parser.args().length < 1) {
      //TODO: help
      return;
    }

    onOther(parser.splitArgsAt(1), parser.args()[0]);
  }

  //ArgumentsParser: <player> [world] [currency]
  public static void onOther(ArgumentsParser parser, String identifier) {

    final String region = parser.parseRegion(1);
    final Currency currency = parser.parseCurrency(0, region);

    final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
    if(account.isPresent()) {

      final List<HoldingsEntry> holdings = new ArrayList<>();

      if(parser.args().length >= 2) {
        BigDecimal amount = BigDecimal.ZERO;
        for(HoldingsEntry entry : account.get().getHoldings(region, currency.getUid())) {
          amount = amount.add(entry.getAmount());
        }
        holdings.add(new HoldingsEntry(region, currency.getUid(), amount, HoldingsType.NORMAL_HOLDINGS));
      } else {
        holdings.addAll(account.get().getAllHoldings(region, HoldingsType.NORMAL_HOLDINGS));
      }

      if(holdings.isEmpty()) {

        //Shouldn't happen, but if it does handle it.
        final MessageData msg = new MessageData("Messages.Account.NoHoldings");
        msg.addReplacement("$currency", currency.getIdentifier());
        parser.sender().message(msg);
        return;
      }

      final MessageData msg = new MessageData("Messages.Money.HoldingsMulti");
      msg.addReplacement("$world", region);
      parser.sender().message(msg);

      for(HoldingsEntry entry : holdings) {

        Optional<Currency> entryCur = TNECore.eco().currency().findCurrency(entry.getCurrency());

        if(entryCur.isPresent()) {
          //TODO: currency formatting.

          final MessageData entryMSG = new MessageData("Messages.Money.HoldingsMultiSingle");
          entryMSG.addReplacement("$currency", entryCur.get().getIdentifier());
          entryMSG.addReplacement("$amount", entry.getAmount().toPlainString());
          parser.sender().message(entryMSG);
        }
      }
    }
  }

  //ArgumentsParser: <player> <amount> [currency] [from:account]
  public static void onPay(ArgumentsParser parser) {
    long startTime = System.nanoTime();
    if(parser.args().length < 2) {
      //TODO: Help
      return;
    }

    Optional<Account> senderAccount = parser.sender().account();

    final Currency currency = parser.parseCurrency(2);

    final Optional<Account> account = parser.parseAccount(0, true);
    final Optional<BigDecimal> amount = parser.parseAmount(1);
    if(account.isPresent() && amount.isPresent()) {

      if(senderAccount.isEmpty()) {
        final MessageData data = new MessageData("Messages.General.NoPlayer");
        data.addReplacement("$player", parser.args()[0]);
        parser.sender().message(data);
        return;
      }

      final Optional<PlayerProvider> provider = TNECore.server().findPlayer(((PlayerAccount)account.get()).getUUID());
      if(provider.isEmpty()) {
        final MessageData data = new MessageData("Messages.General.NoPlayer");
        data.addReplacement("$player", parser.args()[0]);
        parser.sender().message(data);
        return;
      }

      final HoldingsModifier modifier = new HoldingsModifier(parser.sender().region(),
                                                             currency.getUid(),
                                                             amount.get()
      );

      final Transaction transaction = new Transaction("pay")
          .to(account.get(), modifier)
          .from(senderAccount.get(), modifier.counter())
          .processor(new BaseTransactionProcessor())
          .source(new PlayerSource(parser.sender().identifier()));

      final Optional<Receipt> receipt = processTransaction(parser.sender(), transaction);
      if(receipt.isPresent()) {
        //TODO: Success handler.
      }
    }

    long endTime = System.nanoTime();

    long duration = (endTime - startTime);

    parser.sender().message(new MessageData("<red>Transaction took " + duration + "to execute!"));
  }

  //ArgumentsParser: <player> <amount> [currency]
  public static void onRequest(ArgumentsParser parser) {
    long startTime = System.nanoTime();
    if(parser.args().length < 2) {
      //TODO: Help
      return;
    }

    final Currency currency = parser.parseCurrency(2);

    final Optional<Account> account = parser.parseAccount(0, true);
    final Optional<BigDecimal> amount = parser.parseAmount(1);
    if(account.isPresent() && amount.isPresent()) {

      Optional<PlayerProvider> provider = TNECore.server().findPlayer(((PlayerAccount)account.get()).getUUID());
      if(provider.isEmpty()) {
        final MessageData data = new MessageData("Messages.General.NoPlayer");
        data.addReplacement("$player", parser.args()[0]);
        parser.sender().message(data);
        return;
      }

      final MessageData msg = new MessageData("Messages.Money.RequestSender");
      msg.addReplacement("$player", parser.args()[0]);
      msg.addReplacement("$amount", parser.args()[1]);
      parser.sender().message(msg);

      final MessageData request = new MessageData("Messages.Money.Request");
      request.addReplacement("$player", parser.sender().name());
      request.addReplacement("$amount", parser.args()[1]);
      request.addReplacement("$currency", currency.getIdentifier());
      provider.get().message(request);
    }

    long endTime = System.nanoTime();

    long duration = (endTime - startTime);

    parser.sender().message(new MessageData("<red>Transaction took " + duration + "to execute!"));
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onSet(ArgumentsParser parser) {
    long startTime = System.nanoTime();

    if(parser.args().length < 2) {
      //TODO: Help
      return;
    }

    final String region = parser.parseRegion(2);
    final Currency currency = parser.parseCurrency(3, region);

    final Optional<Account> account = parser.parseAccount(0);
    final Optional<BigDecimal> amount = parser.parseAmount(1);
    if(account.isPresent() && amount.isPresent()) {

      final HoldingsModifier modifier = new HoldingsModifier(region,
                                                             currency.getUid(),
                                                             amount.get(),
                                                             HoldingsOperation.SET);

      final Transaction transaction = new Transaction("set")
          .to(account.get(), modifier)
          .processor(new BaseTransactionProcessor())
          .source(new PlayerSource(parser.sender().identifier()));

      Optional<Receipt> receipt = processTransaction(parser.sender(), transaction);

      long endTime = System.nanoTime();

      long duration = (endTime - startTime);

      parser.sender().message(new MessageData("<red>Transaction took " + duration + "to execute!"));

      if(receipt.isPresent()) {
        final MessageData msg = new MessageData("Messages.Money.Set");
        msg.addReplacement("$player", parser.args()[0]);
        msg.addReplacement("$amount", parser.args()[1]);
        parser.sender().message(msg);
        return;
      }
      //TODO: Failure handling
    }
  }

  //ArgumentsParser: <amount> [world]
  public static void onSetAll(ArgumentsParser parser) {
    //TODO: This
  }

  //ArgumentsParser: <player> <amount> [world] [currency]
  public static void onTake(ArgumentsParser parser) {

    long startTime = System.nanoTime();
    if(parser.args().length < 2) {
      //TODO: Help
      return;
    }

    final String region = parser.parseRegion(2);
    final Currency currency = parser.parseCurrency(3, region);

    final Optional<Account> account = parser.parseAccount(0);
    final Optional<BigDecimal> amount = parser.parseAmount(1);
    if(account.isPresent() && amount.isPresent()) {

      final HoldingsModifier modifier = new HoldingsModifier(region,
                                                             currency.getUid(),
                                                             amount.get()
      );

      final Transaction transaction = new Transaction("take")
          .to(account.get(), modifier.counter())
          .processor(new BaseTransactionProcessor())
          .source(new PlayerSource(parser.sender().identifier()));

      Optional<Receipt> receipt = processTransaction(parser.sender(), transaction);
      if(receipt.isPresent()) {
        //TODO: Success message.
      }

      long endTime = System.nanoTime();
      //TODO: Remove timing code.
      long duration = (endTime - startTime);

      parser.sender().message(new MessageData("<red>Transaction took " + duration + "to execute!"));
    }
  }

  //ArgumentsParser: [page] [currency:name] [world:world] [limit:#]
  public static void onTop(ArgumentsParser parser) {
    //TODO: This
  }

  private static Optional<Receipt> processTransaction(CmdSource sender, Transaction transaction) {
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

      System.out.println(result.getMessage());
      return result.getReceipt();
    } catch(InvalidTransactionException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }
}