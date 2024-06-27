package net.tnemc.core.manager;
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

import net.tnemc.core.transaction.Receipt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Manages receipts for transactions. Provides methods to add, retrieve, and remove receipts
 * by their UUID, time, and within specific time ranges.
 *
 * @see Receipt
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class ReceiptManager {

  private final ConcurrentMap<UUID, Receipt> receipts = new ConcurrentHashMap<>();

  /**
   * Adds a new receipt to the manager.
   *
   * @param receipt The receipt to be added.
   */
  public void log(final Receipt receipt) {
    receipts.put(receipt.getId(), receipt);
  }

  /**
   * Retrieves a receipt by its UUID.
   *
   * @param id The UUID of the receipt.
   * @return The receipt with the specified UUID, or null if not found.
   */
  public Optional<Receipt> getReceiptByUUID(final UUID id) {
    return Optional.ofNullable(receipts.get(id));
  }

  /**
   * Retrieves receipts involving a specific transaction participant.
   *
   * @param participantUUID The UUID of the transaction participant.
   * @return A list of receipts that involve the specified participant.
   */
  public List<Receipt> getReceiptsByParticipant(final UUID participantUUID) {
    return receipts.values().stream()
            .filter(receipt->(receipt.getFrom() != null && receipt.getFrom().getId().equals(participantUUID)) ||
                    (receipt.getTo() != null && receipt.getTo().getId().equals(participantUUID)))
            .collect(Collectors.toList());
  }

  /**
   * Retrieves receipts by their time.
   *
   * @param time The time of the receipts.
   * @return A list of receipts that occurred at the specified time.
   */
  public List<Receipt> getReceiptByTime(final long time) {
    return receipts.values().stream()
            .filter(receipt -> receipt.getTime() == time)
            .collect(Collectors.toList());
  }

  /**
   * Retrieves receipts that occurred between two time periods.
   *
   * @param startTime The start time of the range.
   * @param endTime The end time of the range.
   * @return A TreeMap where the key is the receipt time and the value is the Receipt object.
   */
  public TreeMap<Long, Receipt> getReceiptsBetweenTimes(final long startTime, final long endTime) {
    return receipts.values().stream()
            .filter(receipt -> receipt.getTime() >= startTime && receipt.getTime() <= endTime)
            .collect(Collectors.toMap(Receipt::getTime, receipt -> receipt, (e1, e2) -> e1, TreeMap::new));
  }

  /**
   * Retrieves receipts by their time and a transaction participant UUID.
   *
   * @param time The time of the receipts.
   * @param participantUUID The UUID of the transaction participant.
   * @return A list of receipts that occurred at the specified time and involve the specified participant.
   */
  public List<Receipt> getReceiptsByTimeAndParticipant(final long time, final UUID participantUUID) {
    return receipts.values().stream()
            .filter(receipt -> receipt.getTime() == time &&
                    (receipt.getFrom() != null && receipt.getFrom().getId().equals(participantUUID) ||
                            receipt.getTo() != null && receipt.getTo().getId().equals(participantUUID)))
            .collect(Collectors.toList());
  }

  /**
   * Retrieves receipts that occurred between two time periods and involve a specific transaction participant.
   *
   * @param startTime The start time of the range.
   * @param endTime The end time of the range.
   * @param participantUUID The UUID of the transaction participant.
   * @return A TreeMap where the key is the receipt time and the value is the Receipt object.
   */
  public TreeMap<Long, Receipt> getReceiptsBetweenTimesAndParticipant(final long startTime, final long endTime, final UUID participantUUID) {
    return receipts.values().stream()
            .filter(receipt -> receipt.getTime() >= startTime && receipt.getTime() <= endTime &&
                    (receipt.getFrom() != null && receipt.getFrom().getId().equals(participantUUID) ||
                            receipt.getTo() != null && receipt.getTo().getId().equals(participantUUID)))
            .collect(Collectors.toMap(Receipt::getTime, receipt -> receipt, (e1, e2) -> e1, TreeMap::new));
  }

  /**
   * Removes a receipt by its UUID.
   *
   * @param id The UUID of the receipt to be removed.
   * @return true if the receipt was removed, false otherwise.
   */
  public boolean removeReceiptByUUID(final UUID id) {
    return receipts.remove(id) != null;
  }

  /**
   * Removes receipts by their time.
   *
   * @param time The time of the receipts to be removed.
   * @return A list of receipts that were removed.
   */
  public List<Receipt> removeReceiptsByTime(final long time) {
    final List<Receipt> removedReceipts = getReceiptByTime(time);
    for (final Receipt receipt : removedReceipts) {
      receipts.remove(receipt.getId());
    }
    return removedReceipts;
  }

  /**
   * Removes receipts that occurred between two time periods.
   *
   * @param startTime The start time of the range.
   * @param endTime The end time of the range.
   * @return A list of receipts that were removed.
   */
  public List<Receipt> removeReceiptsBetweenTimes(final long startTime, final long endTime) {
    final List<Receipt> removedReceipts = new ArrayList<>(getReceiptsBetweenTimes(startTime, endTime).values());
    for (final Receipt receipt : removedReceipts) {
      receipts.remove(receipt.getId());
    }
    return removedReceipts;
  }

  /**
   * Removes receipts by their time and a transaction participant UUID.
   *
   * @param time The time of the receipts.
   * @param participantUUID The UUID of the transaction participant.
   * @return A list of receipts that were removed.
   */
  public List<Receipt> removeReceiptsByTimeAndParticipant(final long time, final UUID participantUUID) {
    final List<Receipt> removedReceipts = getReceiptsByTimeAndParticipant(time, participantUUID);
    for (final Receipt receipt : removedReceipts) {
      receipts.remove(receipt.getId());
    }
    return removedReceipts;
  }

  /**
   * Removes receipts that occurred between two time periods and involve a specific transaction participant.
   *
   * @param startTime The start time of the range.
   * @param endTime The end time of the range.
   * @param participantUUID The UUID of the transaction participant.
   * @return A list of receipts that were removed.
   */
  public List<Receipt> removeReceiptsBetweenTimesAndParticipant(final long startTime, final long endTime, final UUID participantUUID) {
    final List<Receipt> removedReceipts = new ArrayList<>(getReceiptsBetweenTimesAndParticipant(startTime, endTime, participantUUID).values());
    for (final Receipt receipt : removedReceipts) {
      receipts.remove(receipt.getId());
    }
    return removedReceipts;
  }
}
