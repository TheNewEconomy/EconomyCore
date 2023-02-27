package net.tnemc.core.currency.format;
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

import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;

/**
 * Represents a single formatting rule, which is used to add to a currency's formatting.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface FormatRule {

  /**
   * The identifier for this rule.
   * @return The human-friendly identifier for this rule.
   */
  String name();

  /**
   * Used to format a TNE format string based on the provided holdings and account information.
   * @param account The account to use for this formatting.
   * @param entry The holdings entry to use for formatting.
   * @param format The format string that these should be provided for.
   * @return The formatted string.
   */
  String format(Account account, HoldingsEntry entry, String format);
}