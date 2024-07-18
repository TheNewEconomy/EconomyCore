package net.tnemc.core.currency.type;

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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
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

  @Override
  public String description() {
    return "A simple currency type that is strictly virtual, which is imaginary money used in commands.";
  }

  protected HoldingsEntry virtual(Account account, String region, Currency currency) {
    final Optional<HoldingsEntry> holdings = account.getWallet().getHoldings(region,
                                                                             currency.getUid(),
                                                                             EconomyManager.VIRTUAL
    );


    if(holdings.isPresent()) {
      return holdings.get();
    }
    return new HoldingsEntry(region,
                             currency.getUid(),
                             BigDecimal.ZERO,
                             EconomyManager.VIRTUAL);
  }
}