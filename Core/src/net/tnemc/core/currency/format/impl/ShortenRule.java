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

public class ShortenRule implements FormatRule {

  @Override
  public String name() {

    return "<shorten>";
  }

  @Override
  public String format(@Nullable Account account, HoldingsEntry entry, String format) {

    final Monetary monetary = entry.asMonetary();

    if(format.contains("<shorten>")) {
      format = "<symbol><short.amount>";
    }

    final Optional<Currency> currency = entry.currency();

    if(currency.isEmpty() || monetary.major().compareTo(new BigInteger("1009")) <= 0) {
      return format.replace("<short.amount>", monetary.major().toString());
    }

    final String whole = monetary.major().toString();
    final int pos = ((whole.length() - 1) / 3) - 1;
    final int posInclude = ((whole.length() % 3) == 0)? 3 : whole.length() % 3;
    String wholeSub = whole.substring(0, posInclude);

    if(whole.length() > 3) {
      String extra = whole.substring(posInclude, posInclude + 2);
      if(Integer.valueOf(extra) > 0) {
        if(extra.endsWith("0")) {
          extra = extra.substring(0, extra.length() - 1);
        }
        wholeSub = wholeSub + "." + extra;
      }
    }

    char pre;
    if(currency.get().getPrefixes().length() < (pos + 1)) {
      pre = '^';
    } else {
      pre = currency.get().getPrefixes().charAt(pos);
    }

    return format.replace("<short.amount>", wholeSub + pre);
  }
}