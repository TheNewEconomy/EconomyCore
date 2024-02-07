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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.utils.Identifier;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to keep track of regional holdings for an account.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class RegionHoldings {

  private final Map<UUID, CurrencyHoldings> holdings = new ConcurrentHashMap<>();

  public void setHoldingsEntry(final HoldingsEntry entry, final Identifier type) {
    final CurrencyHoldings currencyHoldings =
        holdings.getOrDefault(entry.getCurrency(), new CurrencyHoldings());

    currencyHoldings.setHoldingsEntry(entry, type);

    holdings.put(entry.getCurrency(), currencyHoldings);
  }

  public Optional<HoldingsEntry> getHoldingsEntry(final UUID currency) {
    return getHoldingsEntry(currency, EconomyManager.NORMAL);
  }

  public Optional<HoldingsEntry> getHoldingsEntry(final UUID currency, final Identifier type) {
    if(holdings.containsKey(currency)) {
      return holdings.get(currency).getHoldingsEntry(type);
    }
    return Optional.empty();
  }

  public Map<UUID, CurrencyHoldings> getHoldings() {
    return holdings;
  }
}