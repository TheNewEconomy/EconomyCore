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
import java.util.Optional;

/**
 * Represents our currency type that is based on nothing.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class VirtualType implements CurrencyType {
  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  @Override
  public String name() {
    return "virtual";
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
  public BigDecimal getHoldings(Account account, String region, Currency currency, HoldingsType type) {
    final Optional<HoldingsEntry> holdings = account.getWallet().getHoldings(region, currency.getIdentifier(), type);


    if(holdings.isPresent()) {
      return holdings.get().getAmount();
    }
    return BigDecimal.ZERO;
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
    account.getWallet().setHoldings(new HoldingsEntry(region, currency.getIdentifier(), amount), type);
    return true;
  }
}