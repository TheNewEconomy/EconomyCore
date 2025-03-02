package net.tnemc.core.currency.parser.impl;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.parser.ParseMoney;
import net.tnemc.core.currency.parser.ParseRule;

import java.math.RoundingMode;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SymbolParseRule
 *
 * @author creatorfromhell
 * @since 0.1.3.6
 */
public class SymbolParseRule implements ParseRule {


  /**
   * Retrieves an identifier.
   *
   * @return The identifier as a String.
   */
  @Override
  public String identifier() {

    return "symbol";
  }

  @Override
  public String apply(final ParseMoney parseMoney, final String input) {
    final Matcher matcher = Pattern.compile("^([^0-9]+)").matcher(input);
    if(matcher.find()) {

      final String symbol = matcher.group(1).trim();
      final Optional<Currency> currency = TNECore.eco().currency().findCurrencyBySymbol(symbol);
      if(currency.isPresent()) {

        parseMoney.currency(currency.get());
        parseMoney.amount(parseMoney.amount().setScale(currency.get().getDecimalPlaces(), RoundingMode.FLOOR));

        return input.replace(matcher.group(), "").trim();
      }
    }
    return input;
  }
}