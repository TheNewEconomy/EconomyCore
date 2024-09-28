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

import net.tnemc.core.utils.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to keep track of regional holdings for an account.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyHoldings {

  private final Map<String, HoldingsEntry> holdings = new ConcurrentHashMap<>();

  /**
   * Used to add {@link HoldingsEntry holdings} for a specific {@link Identifier}.
   *
   * @param type  The type to add the holdings to.
   * @param entry The holdings to add to the type.
   */
  public void setHoldingsEntry(final @NotNull HoldingsEntry entry, final @NotNull Identifier type) {

    holdings.put(type.asID(), entry);
  }

  /**
   * Used to get the {@link HoldingsEntry holdings} for a specific {@link Identifier}.
   *
   * @param type The type to add the holdings to.
   *
   * @return The {@link HoldingsEntry} if it belongs, or an empty optional if it doesn't.
   */
  public Optional<HoldingsEntry> getHoldingsEntry(final @NotNull Identifier type) {

    return Optional.ofNullable(holdings.get(type.asID()));
  }

  public Map<String, HoldingsEntry> getHoldings() {

    return holdings;
  }
}