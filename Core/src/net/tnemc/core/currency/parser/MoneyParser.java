package net.tnemc.core.currency.parser;
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

import net.tnemc.core.currency.parser.impl.FractionParseRule;
import net.tnemc.core.currency.parser.impl.NumericParseRule;
import net.tnemc.core.currency.parser.impl.RandomParseRule;
import net.tnemc.core.currency.parser.impl.RomanParseRule;
import net.tnemc.core.currency.parser.impl.ShortenParseRule;
import net.tnemc.core.currency.parser.impl.SymbolParseRule;

import java.util.LinkedHashMap;

/**
 * MoneyParser
 *
 * @author creatorfromhell
 * @since 0.1.3.6
 */
public class MoneyParser {

  private final LinkedHashMap<String, ParseRule> rules = new LinkedHashMap<>();

  public MoneyParser() {

    addRule(new SymbolParseRule()); //$5 -> sets currency to USD
    addRule(new ShortenParseRule()); // 5k -> 5000
    addRule(new FractionParseRule()); // 1/2 -> .50
    addRule(new RomanParseRule()); // X, IV
    addRule(new RandomParseRule()); // random(1-10) = 6?
    addRule(new NumericParseRule()); // scientific notation and general numerics
  }

  public void addRule(final ParseRule rule) {
    rules.put(rule.identifier(), rule);
  }

  public ParseMoney parse(final String region, final String input) {
    final ParseMoney parseMoney = new ParseMoney(region);

    for (final ParseRule rule : rules.values()) {
      rule.apply(parseMoney, input);
    }

    return parseMoney;
  }
}