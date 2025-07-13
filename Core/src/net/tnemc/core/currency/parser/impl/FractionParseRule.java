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

import net.tnemc.core.currency.parser.ParseMoney;
import net.tnemc.core.currency.parser.ParseRule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FractionRule
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class FractionParseRule implements ParseRule {

  /**
   * Retrieves an identifier.
   *
   * @return The identifier as a String.
   */
  @Override
  public String identifier() {

    return "fraction";
  }

  /**
   * Applies the given parse money object to the input string according to the specified rules.
   *
   * @param parseMoney the ParseMoney object to apply the rules to
   * @param input      the input string to apply the rules on
   */
  @Override
  public String apply(final ParseMoney parseMoney, final String input) {

    final Matcher matcher = Pattern.compile("([0-9]+)/([0-9]+)").matcher(input);
    if(matcher.find()) {

      final BigDecimal numerator = new BigDecimal(matcher.group(1));
      final BigDecimal denominator = new BigDecimal(matcher.group(2));

      if(denominator.compareTo(BigDecimal.ZERO) == 0) {
        throw new IllegalArgumentException("Division by zero in fraction: " + input);
      }

      final BigDecimal fractionValue = numerator.divide(denominator, 10, RoundingMode.HALF_UP);
      parseMoney.amount(fractionValue);

      return input.replace(matcher.group(), fractionValue.toPlainString()).trim();
    }
    return input;
  }
}