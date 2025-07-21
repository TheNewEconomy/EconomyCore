package net.tnemc.core.account.holdings;

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
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.utils.Identifier;

import java.math.BigDecimal;

/**
 * HoldingsHandler represents a handler that could provide additional holding types.
 *
 * @author creatorfromhell
 * @see Account
 * @see CurrencyType
 * @see Identifier
 * @since 0.1.2.0
 */
public interface HoldingsHandler {

  /**
   * The identifier for this handler.
   *
   * @return The identifier that represents this handler.
   */
  Identifier identifier();

  /**
   * Used to determine if this handler may be used for the specified{@link Currency} and
   * {@link CurrencyType}.
   *
   * @param currency The currency.
   * @param type     The currency type.
   *
   * @return True if it supports the currency type, otherwise false.
   */
  boolean supports(Currency currency, CurrencyType type);

  /**
   * Used to determine if this handler may be used for the specified {@link Account}. This defaults
   * to true for all accounts.
   *
   * @param currency The currency.
   * @param account  The account.
   *
   * @return True if it supports the account, otherwise false.
   */
  default boolean appliesTo(final Account account, final Currency currency) {

    return true;
  }

  /**
   * @return If balances of this handler should be saved to the database. Return false to do your
   * own data handling.
   */
  default boolean database() {

    return true;
  }

  /**
   * Used to set the holdings for a specific account.
   *
   * @param account  The account.
   * @param region   The name of the region involved. This is usually a world, but could be
   *                 something else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param type     The currency type.
   * @param amount   The amount to set the player's holdings to.
   *
   * @return True if the holdings have been set, otherwise false.
   */
  boolean setHoldings(Account account, String region, Currency currency, CurrencyType type, BigDecimal amount);

  /**
   * Used to get the holdings for a specific account from this handler.
   *
   * @param account  The Account.
   * @param region   The name of the region involved. This is usually a world, but could be
   *                 something else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param type     The currency type.
   *
   * @return The holdings for the specific account.
   */
  HoldingsEntry getHoldings(Account account, String region, Currency currency, CurrencyType type);
}