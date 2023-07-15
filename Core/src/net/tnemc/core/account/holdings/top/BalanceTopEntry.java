package net.tnemc.core.account.holdings.top;
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

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BalanceTopEntry
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BalanceTopEntry {

  private final ConcurrentHashMap<UUID, BigDecimal> balances = new ConcurrentHashMap<>();
  private final LinkedHashMap<UUID, BigDecimal> sorted = new LinkedHashMap<>();

  private final String region;
  private final UUID currency;

  public BalanceTopEntry(String region, UUID currency) {
    this.region = region;
    this.currency = currency;
  }

  public ConcurrentHashMap<UUID, BigDecimal> getBalances() {
    return balances;
  }

  public String getRegion() {
    return region;
  }

  public UUID getCurrency() {
    return currency;
  }

  public LinkedHashMap<UUID, BigDecimal> sorted() {
    return sorted;
  }
}