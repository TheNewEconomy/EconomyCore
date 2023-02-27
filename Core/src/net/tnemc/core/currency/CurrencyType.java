package net.tnemc.core.currency;

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
import net.tnemc.core.account.holdings.HoldingsType;

import java.math.BigDecimal;

/**
 * Represents a type of currency.
 *
 * @see Currency
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface CurrencyType {

  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  String name();

  /**
   * @return True if this currency type supports offline players, otherwise false.
   */
  default boolean offline() {
    return true;
  }

  /**
   * @return True if this currency requires setting the player's balance when they log in. This
   * should be true for currency types like item currency.
   */
  default boolean loginCalculation() {
    return false;
  }

  /**
   * @return If balances of this currency type should be saved to the database. Return false to do
   * your own data handling.
   */
  default boolean database() {
    return true;
  }

  /**
   * @return True if this currency type is based on physical items.
   */
  default boolean supportsItems() {
    return false;
  }

  /**
   * Used to get the holdings for a specific account from this currency type.
   *
   * @param account The uuid of the account.
   * @param region The name of the region involved. This is usually a world, but could be something
   *               else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @return The holdings for the specific account.
   */
  BigDecimal getHoldings(Account account, String region, Currency currency, HoldingsType type);

  /**
   * Used to set the holdings for a specific account.
   *
   * @param region The name of the region involved. This is usually a world, but could be something
   *               else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param amount The amount to set the player's holdings to.
   *
   * @return True if the holdings have been set, otherwise false.
   */
  boolean setHoldings(Account account, String region, Currency currency, HoldingsType type, BigDecimal amount);
}
