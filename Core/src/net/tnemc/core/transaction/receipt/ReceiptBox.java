package net.tnemc.core.transaction.receipt;
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

import net.tnemc.core.transaction.Receipt;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ReceiptBox represents an object that can hold receipts.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 * @see Receipt
 */
public class ReceiptBox {

  private final ConcurrentHashMap<Long, Receipt> receipts = new ConcurrentHashMap<>();

  /**
   * Logs a receipt to this receipt box.
   * @param receipt The {@link Receipt} to log.
   */
  public void log(final Receipt receipt) {
    receipts.put(receipt.getTime(), receipt);
  }

  /**
   * Used to destroy a receipt based on the time it occurred.
   * @param time The time to use for the destruction.
   */
  public void destroy(final long time) {
    receipts.remove(time);
  }

  /**
   * Used to destroy a receipt based on the {@link UUID} it occurred.
   * @param identifier The identifier to use for the destruction.
   */
  public void destroy(final UUID identifier) {
    receipts.entrySet().removeIf(entry->entry.getValue().getId().equals(identifier));
  }

  /**
   * Used to find a receipt based on the time it occurred.
   * @param time The time to use for the search.
   * @return An optional with the {@link Receipt} if it exists, otherwise an empty Optional.
   */
  public Optional<Receipt> find(final long time) {
    return Optional.ofNullable(receipts.get(time));
  }

  /**
   * Used to find a receipt based on the {@link UUID} it occurred.
   * @param identifier The {@link UUID} to use for the search.
   * @return An optional with the {@link Receipt} if it exists, otherwise an empty Optional.
   */
  public Optional<Receipt> find(final UUID identifier) {

    for(Receipt receipt : receipts.values()) {
      if(receipt.getId().equals(identifier)) {
        return Optional.of(receipt);
      }
    }
    return Optional.empty();
  }

  /**
   * This is used to return a range of {@link Receipt receipts}, based on a time range.
   * @param start The start of the time to use for the range.
   * @param end The end of the time to use for the range.
   * @return A {@link TreeMap} of the receipts that occurred during the time range.
   */
  public TreeMap<Long, Receipt> range(final long start, final long end) {
    final TreeMap<Long, Receipt> range = new TreeMap<>();

    for(Receipt receipt : receipts.values()) {
      final long time = receipt.getTime();
      if(start <= time && end >= time) {
        range.put(time, receipt);
      }
    }
    return range;
  }
}