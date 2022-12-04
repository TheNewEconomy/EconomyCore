package net.tnemc.core.command;
/*
 * The New Economy
 * Copyright (CmdSource) 2022 Daniel "creatorfromhell" Vidmar
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
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.source.PlayerSource;
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.transaction.processor.BaseTransactionProcessor;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * MoneyCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MoneyCommand extends BaseCommand {

  //Arguments: [world] [currency]
  public static void onBalance(CmdSource sender, String[] args) {

    final String region = (args.length >= 1)? args[0] : sender.region();
    final String currency = (args.length >= 2)? args[1] : "USD";
    //TODO: Default currency.

    Optional<Account> account = TNECore.eco().account().findAccount(sender.identifier());

    if(account.isEmpty()) {
      sender.message(new MessageData("Messages.Account.NoSuch"));
      return;
    }

    final Optional<HoldingsEntry> entry = account.get().getHoldings(region, currency);
    if(entry.isEmpty()) {

      //Shouldn't happen, but if it does handle it.
      final MessageData msg = new MessageData("Messages.Account.NoHoldings");
      msg.addReplacement("$currency", currency);
      sender.message(msg);
      return;
    }

    final MessageData msg = new MessageData("Messages.Money.Holdings");
    msg.addReplacement("$amount", entry.get().getAmount().toPlainString());
    sender.message(msg);
    //TODO: currency formatting.
  }

  //Arguments: <amount> <to currency[:world]> [from currency[:world]]
  public static void onConvert(CmdSource sender, String[] args) {

  }

  //Arguments: <player> <amount> [world] [currency]
  public static void onGive(CmdSource sender, String[] args) {
    long startTime = System.nanoTime();
    if(args.length < 2) {
      //TODO: Help
      return;
    }

    final String region = (args.length >= 3)? args[2] : sender.region();
    final String currency = (args.length >= 4)? args[3] : "USD";
    //TODO: Default currency.

    Optional<Account> account = TNECore.eco().account().findAccount(args[0]);

    if(account.isEmpty()) {
      sender.message(new MessageData("Messages.General.NoPlayer"));
      return;
    }

    final HoldingsModifier modifier = new HoldingsModifier(region,
                                                           currency,
                                                           new BigDecimal(args[1]));
    //TODO: Value args check

    final Transaction transaction = new Transaction("give")
        .to(account.get(), modifier)
        .processor(new BaseTransactionProcessor())
        .source(new PlayerSource(sender.identifier()));

    Optional<Receipt> receipt = Optional.empty();
    try {
      final TransactionResult result = transaction.process();
      System.out.println(result.getMessage());
      receipt = result.getReceipt();
    } catch(InvalidTransactionException e) {
      e.printStackTrace();
    }

    //TODO: Receipt logging and success checking
    long endTime = System.nanoTime();

    long duration = (endTime - startTime);

    sender.message(new MessageData("<red>Transaction took " + duration + "to execute!"));

    //TODO: Success message
  }

  //Arguments: <amount> [currency]
  public static void onNote(CmdSource sender, String[] args) {

  }

  //Arguments: <player> <amount> [currency] [from:account]
  public static void onPay(CmdSource sender, String[] args) {
    long startTime = System.nanoTime();
    if(args.length < 2) {
      //TODO: Help
      return;
    }

    final String currency = (args.length >= 3)? args[2] : "USD";
    //TODO: Default currency.

    Optional<Account> account = TNECore.eco().account().findAccount(args[0]);
    Optional<Account> senderAccount = TNECore.eco().account().findAccount(sender.identifier());

    if(account.isEmpty() || senderAccount.isEmpty()) {
      sender.message(new MessageData("Messages.General.NoPlayer"));
      return;
    }

    Optional<PlayerProvider> provider = TNECore.server().findPlayer(((PlayerAccount)account.get()).getUUID());
    if(provider.isEmpty()) {
      sender.message(new MessageData("Messages.General.NoPlayer"));
      return;
    }

    final HoldingsModifier modifier = new HoldingsModifier(sender.region(),
                                                           currency,
                                                           new BigDecimal(args[1]));
    //TODO: Value args check

    final Transaction transaction = new Transaction("pay")
        .to(account.get(), modifier)
        .from(senderAccount.get(), modifier.counter())
        .processor(new BaseTransactionProcessor())
        .source(new PlayerSource(sender.identifier()));

    Optional<Receipt> receipt = Optional.empty();
    try {
      final TransactionResult result = transaction.process();
      System.out.println(result.getMessage());
      receipt = result.getReceipt();
    } catch(InvalidTransactionException e) {
      e.printStackTrace();
    }

    //TODO: Receipt logging and success checking
    long endTime = System.nanoTime();

    long duration = (endTime - startTime);

    sender.message(new MessageData("<red>Transaction took " + duration + "to execute!"));

    //TODO: Success message
  }

  //Arguments: <player> <amount> [currency]
  public static void onRequest(CmdSource sender, String[] args) {
    long startTime = System.nanoTime();
    if(args.length < 2) {
      //TODO: Help
      return;
    }

    final String currency = (args.length >= 3)? args[2] : "USD";
    //TODO: Default currency.

    Optional<Account> account = TNECore.eco().account().findAccount(args[0]);

    if(account.isEmpty() || !TNECore.server().online(args[0])) {
      sender.message(new MessageData("Messages.General.NoPlayer"));
      return;
    }

    Optional<PlayerProvider> provider = TNECore.server().findPlayer(((PlayerAccount)account.get()).getUUID());
    if(provider.isEmpty()) {
      sender.message(new MessageData("Messages.General.NoPlayer"));
      return;
    }

    final MessageData msg = new MessageData("Messages.Money.RequestSender");
    msg.addReplacement("$player", args[0]);
    msg.addReplacement("$amount", args[1]);
    sender.message(msg);

    final MessageData request = new MessageData("Messages.Money.Request");
    request.addReplacement("$player", sender.name());
    request.addReplacement("$amount", args[1]);
    request.addReplacement("$currency", currency);
    provider.get().message(request);

    long endTime = System.nanoTime();

    long duration = (endTime - startTime);

    sender.message(new MessageData("<red>Transaction took " + duration + "to execute!"));
  }

  //Arguments: <player> <amount> [world] [currency]
  public static void onSet(CmdSource sender, String[] args) {

  }

  //Arguments: <amount> [world]
  public static void onSetAll(CmdSource sender, String[] args) {

  }

  //Arguments: <player> <amount> [world] [currency]
  public static void onTake(CmdSource sender, String[] args) {

  }

  //Arguments: [page] [currency:name] [world:world] [limit:#]
  public static void onTop(CmdSource sender, String[] args) {

  }
}