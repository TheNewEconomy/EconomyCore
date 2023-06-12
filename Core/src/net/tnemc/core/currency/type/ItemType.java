package net.tnemc.core.currency.type;

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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.account.Account;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.utils.Identifier;

import java.math.BigDecimal;

import static net.tnemc.core.EconomyManager.INVENTORY_ONLY;

/**
 * Represents our currency type that is based on items.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ItemType extends VirtualType {
  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  @Override
  public String name() {
    return "item";
  }

  /**
   * @return True if this currency type is based on physical items.
   */
  @Override
  public boolean supportsItems() {
    return true;
  }

  @Override
  public boolean supportsVirtual() {
    return false;
  }

  /**
   * Used to set the holdings for a specific account.
   *
   * @param account  The Account to set the holdings for.
   * @param region   The name of the region involved. This is usually a world, but could be something
   *                 else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param type The {@link Identifier} of the holdings handler to use.
   * @param amount   The amount to set the player's holdings to.
   *
   * @return True if the holdings have been set, otherwise false.
   */
  @Override
  public boolean setHoldings(Account account, String region, Currency currency, Identifier type, BigDecimal amount) {
    if(type.equals(EconomyManager.NORMAL)) type = INVENTORY_ONLY;

    return super.setHoldings(account, region, currency, type, amount);
  }
}