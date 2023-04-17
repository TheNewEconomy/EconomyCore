package net.tnemc.core.command.args;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.io.message.MessageData;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

/**
 * ArgumentsParser
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ArgumentsParser {

  private final CmdSource sender;
  private final String[] arguments;

  public ArgumentsParser(CmdSource sender, String[] arguments) {
    this.sender = sender;
    this.arguments = arguments;
  }

  public ArgumentsParser splitArgsAt(final int index) {
    return new ArgumentsParser(sender,
                               ((arguments.length > index)?
                                   Arrays.copyOfRange(arguments, index, arguments.length) : new String[0]));
  }

  public CmdSource sender() {
    return sender;
  }

  public String[] args() {
    return arguments;
  }

  public String parseRegion(final int index) {
    if(index >= arguments.length) {
      return sender.region();
    }
    return arguments[index];
  }

  public Optional<BigDecimal> parseAmount(final int index) {
    return parseAmount(index, "Messages.Money.InvalidFormat");
  }

  public Optional<BigDecimal> parseAmount(final int index, final String failedMessage) {
    if(index < arguments.length) {

      try {
        return Optional.of(new BigDecimal(arguments[index]));
      } catch(final NumberFormatException ignore) {
        return Optional.empty();
      }
    }
    sender.message(new MessageData(failedMessage));
    return Optional.empty();
  }

  public Optional<Account> parseAccount(final int index) {
    return parseAccount(index, false);
  }

  public Optional<Account> parseAccount(final int index, final boolean online) {
    if(index < arguments.length) {
      final Optional<Account> account = TNECore.eco().account().findAccount(arguments[index]);

      if(account.isEmpty() || (online && !TNECore.server().online(arguments[index]))) {
        final MessageData data = new MessageData("Messages.Admin.NoAccount");
        data.addReplacement("$name", arguments[index]);
        sender.message(data);
        return Optional.empty();
      }
      return account;
    }
    return Optional.empty();
  }

  public Currency parseCurrency(final int index) {
    return parseCurrency(index, TNECore.eco().region().defaultRegion());
  }

  public Currency parseCurrency(final int index, final String region) {
    if(index < arguments.length) {

      final Optional<Currency> cur = TNECore.eco().currency().findCurrency(arguments[index]);
      if(cur.isPresent()) {
        return cur.get();
      }
    }
    return TNECore.eco().currency().getDefaultCurrency(region);
  }
}