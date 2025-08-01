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

import net.tnemc.core.account.Account;

/**
 * MoneyParseRule
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public interface ParseRule {

  /**
   * Retrieves an identifier.
   *
   * @return The identifier as a String.
   */
  String identifier();

  /**
   * Applies the given ParseMoney object to the input string according to the specified rules.
   *
   * @param parseMoney The ParseMoney object to apply the rules to.
   * @param input      The input string to apply the rules on.
   *
   * @return A String after applying the rules to the input string based on the ParseMoney object.
   */
  String apply(Account account, ParseMoney parseMoney, String input);
}