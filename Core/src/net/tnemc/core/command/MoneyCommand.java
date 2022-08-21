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
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.compatibility.CmdSource;

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

      //Try to create and retry search
      TNECore.eco().account().createAccount(sender.identifier().toString(), sender.name());

      account = TNECore.eco().account().findAccount(sender.identifier());

      if(account.isEmpty()) {
        sender.message("Messages.Account.NoSuch");
        return;
      }
    }

    final Optional<HoldingsEntry> entry = account.get().getHoldings(region, currency);
    if(entry.isEmpty()) {

      //Shouldn't happen, but if it does handle it.
      sender.message("Messages.Account.NoHoldings");
      //TODO: message variables.
      return;
    }

    sender.message("Messages.Money.Holdings");
    //TODO: message variables.
  }

  //Arguments: <amount> <to currency[:world]> [from currency[:world]]
  public static void onConvert(CmdSource sender, String[] args) {

  }

  //Arguments: <player> <amount> [world] [currency]
  public static void onGive(CmdSource sender, String[] args) {

  }

  //Arguments: <amount> [currency]
  public static void onNote(CmdSource sender, String[] args) {

  }

  //Arguments: <player> <amount> [from:account] [currency]
  public static void onPay(CmdSource sender, String[] args) {

  }

  //Arguments: <player> <amount> [currency]
  public static void onRequest(CmdSource sender, String[] args) {

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