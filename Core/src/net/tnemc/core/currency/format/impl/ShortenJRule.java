package net.tnemc.core.currency.format.impl;
/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.format.FormatRule;
import net.tnemc.core.utils.Monetary;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Optional;

/**
 * ShortenJRule
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ShortenJRule implements FormatRule {

  @Override
  public String name() {
    return "<shortenj>";
  }

  /**
   * Used to format a TNE format string based on the provided holdings and account information.
   *
   * @param account The account to use for this formatting.
   * @param entry   The holdings entry to use for formatting.
   * @param format  The format string that these should be provided for.
   *
   * @return The formatted string.
   */
  @Override
  public String format(@Nullable Account account, HoldingsEntry entry, String format) {
    final Monetary monetary = entry.asMonetary();

    if(format.contains("<shortenj>")) {
      format = "<symbol><short.amount>";
    }

    final Optional<Currency> currency = entry.currency();

    if(currency.isEmpty() || monetary.major().compareTo(new BigInteger("9999")) <= 0) {
      return format.replace("<short.amount>", monetary.major().toString());
    }

    final int zeros = countZeros(monetary.major().toString());
    final int multiple = closestMultiple(zeros);
    final int symbol = multiple / 4;
    final String replacement = generateZeros(multiple);


    char pre;
    if(currency.get().getPrefixesj().length() < (symbol)) {
      pre = '^';
    } else {
      pre = currency.get().getPrefixesj().charAt(symbol - 1);
    }

    return format.replace("<short.amount>", monetary.major().toString().replace(replacement, "") + pre);
  }

  private int countZeros(final String numStr) {
    int zeroCount = 0;

    for(char c : numStr.toCharArray()) {
      if(c == '0') {
        zeroCount++;
      }
    }
    return zeroCount;
  }

  private int closestMultiple(final int zeroCount) {
    return (zeroCount / 4) * 4;
  }

  private String generateZeros(final int multiple) {
    final StringBuilder zeros = new StringBuilder();

    for(int i = 0; i < multiple; i++) {
      zeros.append("0");
    }
    return zeros.toString();
  }
}