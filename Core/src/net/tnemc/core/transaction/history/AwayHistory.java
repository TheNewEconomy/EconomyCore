package net.tnemc.core.transaction.history;

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

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * AwayHistory
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AwayHistory {

  protected final NavigableMap<Long, UUID> receipts = new TreeMap<>();

  private final UUID account;

  public AwayHistory(final UUID account) {

    this.account = account;
  }

  public NavigableMap<Long, UUID> getReceipts() {

    return receipts;
  }

  public UUID getAccount() {

    return account;
  }

  public NavigableMap<Long, UUID> getPage(int page) {

    if(page > maxPages()) page = 1;

    final NavigableMap<Long, UUID> values = new TreeMap<>();
    final int start = ((page * 5) - 5) + 1;
    final int end = start + 5;

    final int i = 1;
    for(final Map.Entry<Long, UUID> entry : receipts.entrySet()) {
      if(i < start) continue;

      values.put(entry.getKey(), entry.getValue());

      if(i >= end) break;
    }
    return values;
  }

  public int maxPages() {

    int max = receipts.size() / 5;

    if((receipts.size() % 5) > 0) {
      max += 1;
    }
    return max;
  }
}