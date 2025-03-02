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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RomanParseRule
 *
 * @author creatorfromhell
 * @since 0.1.3.6
 */
public class RomanParseRule implements ParseRule {

  private static final Map<Character, Integer> ROMAN_VALUES = new HashMap<>();

  static {
    ROMAN_VALUES.put('I', 1);
    ROMAN_VALUES.put('V', 5);
    ROMAN_VALUES.put('X', 10);
    ROMAN_VALUES.put('L', 50);
    ROMAN_VALUES.put('C', 100);
    ROMAN_VALUES.put('D', 500);
    ROMAN_VALUES.put('M', 1000);
  }

  /**
   * Retrieves an identifier.
   *
   * @return The identifier as a String.
   */
  @Override
  public String identifier() {

    return "roman";
  }

  /**
   * Applies the given parse money object to the input string according to the specified rules.
   *
   * @param parseMoney the ParseMoney object to apply the rules to
   * @param input      the input string to apply the rules on
   */
  @Override
  public String apply(final ParseMoney parseMoney, final String input) {

    final Matcher matcher = Pattern.compile("^([^IVXLCDM]*)([IVXLCDM]+)").matcher(input);
    if(matcher.find()) {

      final String roman = matcher.group(2);
      final int numericValue = romanToInteger(roman);
      final BigDecimal value = new BigDecimal(numericValue);
      parseMoney.amount(value);
      return input.replaceFirst(roman, value.toPlainString()).trim();
    }
    return input;
  }

  private int romanToInteger(final String roman) {
    int sum = 0;
    int prevValue = 0;

    for(int i = roman.length() - 1; i >= 0; i--) {

      final int value = ROMAN_VALUES.get(roman.charAt(i));

      if(value < prevValue) {
        sum -= value; // Subtraction case (e.g., IV = 4)
      } else {
        sum += value;
      }

      prevValue = value;
    }
    return sum;
  }
}