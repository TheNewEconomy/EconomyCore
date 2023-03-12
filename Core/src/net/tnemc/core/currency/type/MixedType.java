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

import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.HoldingsType;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static net.tnemc.core.account.holdings.HoldingsType.E_CHEST;
import static net.tnemc.core.account.holdings.HoldingsType.INVENTORY_ONLY;
import static net.tnemc.core.account.holdings.HoldingsType.VIRTUAL_HOLDINGS;

/**
 * Represents a currency type that is a mixture of items and virtual.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MixedType extends ItemType {
  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  @Override
  public String name() {
    return "mixed";
  }

  /**
   * @return True if this currency type is based on physical items.
   */
  @Override
  public boolean supportsItems() {
    return true;
  }

  /**
   * Used to get the holdings for a specific account from this currency type.
   *
   * @param account  The uuid of the account.
   * @param region   The name of the region involved. This is usually a world, but could be something
   *                 else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param type     The holdings type
   *
   * @return The holdings for the specific account.
   */
  @Override
  public List<HoldingsEntry> getHoldings(Account account, String region, Currency currency, HoldingsType type) {
    return switch(type) {
      case E_CHEST -> Collections.singletonList(getEChest(account, region, currency));
      case INVENTORY_ONLY -> Collections.singletonList(getInventory(account, region, currency));
      case VIRTUAL_HOLDINGS -> Collections.singletonList(virtual(account, region, currency));

      default -> Arrays.asList(getEChest(account, region, currency),
                               getInventory(account, region, currency),
                               virtual(account, region, currency));
    };
  }

  /**
   * Used to set the holdings for a specific account.
   *
   * @param account  The account
   * @param region   The name of the region involved. This is usually a world, but could be something
   *                 else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param type     The holdings type
   * @param amount   The amount to set the player's holdings to.
   *
   * @return True if the holdings have been set, otherwise false.
   */
  @Override
  public boolean setHoldings(Account account, String region, Currency currency, HoldingsType type, BigDecimal amount) {

    if(type.equals(E_CHEST) || type.equals(INVENTORY_ONLY)) {
      return super.setHoldings(account, region, currency, type, amount);
    }

    account.getWallet().setHoldings(new HoldingsEntry(region, currency.getIdentifier(), amount, VIRTUAL_HOLDINGS));
    return true;
  }
}