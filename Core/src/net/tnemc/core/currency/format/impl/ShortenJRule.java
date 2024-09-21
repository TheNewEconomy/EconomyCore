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

  private final BigInteger threshold = new BigInteger("9999");

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

    final String monetaryMajor = monetary.major().toString();

    if(currency.isEmpty() || monetary.major().compareTo(new BigInteger("9999")) <= 0) {
      return format.replace("<short.amount>", monetaryMajor);
    }

    String working = monetaryMajor;
    final StringBuilder builder = new StringBuilder();
    while(!working.isBlank() && new BigInteger(working).compareTo(new BigInteger("9999")) > 0) {

      working = build(currency.get(), working, builder);
    }
    builder.append(working);

    return format.replace("<short.amount>", builder.toString());
  }

  private String build(final Currency currency, String working, final StringBuilder builder) {
    final int strLength = working.length() - 1;
    final int multiple = (strLength / 4) * 4;
    final int majorKeep = strLength % 4;
    final int symbol = multiple / 4;
    final StringBuilder workingBuilder = new StringBuilder();

    char pre;
    if(currency.getPrefixesj().length() < (symbol)) {
      pre = '^';
    } else {
      pre = currency.getPrefixesj().charAt(symbol - 1);
    }

    for(int i = 0; i < working.length(); i++) {

      if(i > majorKeep) {

        if(i == majorKeep + 1) {
          builder.append(pre);
        }

        if(working.charAt(i) != '0') {

          workingBuilder.append(working.substring(i));
          break;
        }
        continue;
      }

      builder.append(working.charAt(i));
    }
    return workingBuilder.toString();

    /*for(int i = 0; i < working.length(); i++) {

      if(i > majorKeep) {

        if(i == majorKeep + 1) {
          builder.append(pre);
        }

        if(working.charAt(i) != '0') {
          builder.append(working.substring(i));
          break;
        }
        continue;
      }
      builder.append(working.charAt(i));
    }*/
  }
}