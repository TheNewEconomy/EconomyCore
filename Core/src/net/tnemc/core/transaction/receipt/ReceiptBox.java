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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.history.AwayHistory;
import net.tnemc.core.transaction.history.SortedHistory;

import java.util.Date;
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

  private final ConcurrentHashMap<UUID, Receipt> receipts = new ConcurrentHashMap<>();

  private AwayHistory away = null;
  private SortedHistory sorted = null;
  private boolean checked = false;

  /**
   * Used to calculate transactions that happened while an account owner was away.
   * @param account The identifier of the account.
   * @return An Optional containing the account history if applicable, otherwise an empty Optional.
   */
  public Optional<AwayHistory> away(final UUID account) {

    if(checked) {
      return Optional.ofNullable(away);
    }
    checked = true;

    if(away != null) {
      return Optional.of(away);
    }

    final Optional<Account> acc = TNECore.eco().account().findAccount(account);
    if(acc.isEmpty() || !(acc.get() instanceof PlayerAccount)) {
      return Optional.empty();
    }

    final AwayHistory history = new AwayHistory(account);
    final long time = new Date().getTime();

    int i = 0;

    for(Map.Entry<Long, Receipt> entry : range(((PlayerAccount)acc.get()).getLastOnline(), time).entrySet()) {

      final Receipt receipt = entry.getValue();

      if(receipt.getFrom() != null && receipt.getFrom().getId().equalsIgnoreCase(account.toString())
          || receipt.getTo() != null && receipt.getTo().getId().equalsIgnoreCase(account.toString())) {
        history.getReceipts().put(receipt.getTime(), receipt.getId());
        i++;
      }
    }

    if(i <= 0) {
      return Optional.empty();
    }

    away = history;
    return Optional.of(history);
  }

  public SortedHistory getSorted(final String identifier) {
    if(sorted == null) {
      sorted = new SortedHistory(identifier);
    }
    return sorted;
  }

  /**
   * Used to clear the AwayHistory instance.
   */
  public void clearAwayReceipts() {
    away = null;
    checked = false;
  }

  /**
   * Logs a receipt to this receipt box.
   * @param receipt The {@link Receipt} to log.
   */
  public void log(final Receipt receipt) {
    receipts.put(receipt.getId(), receipt);
  }

  /**
   * Used to destroy a receipt based on the time it occurred.
   * @param time The time to use for the destruction.
   */
  public void destroy(final long time) {
    receipts.entrySet().removeIf(entry->entry.getValue().getTime() == time);
  }

  /**
   * Used to destroy a receipt based on the {@link UUID} it occurred.
   * @param identifier The identifier to use for the destruction.
   */
  public void destroy(final UUID identifier) {
    receipts.remove(identifier);
  }

  /**
   * Used to find a receipt based on the time it occurred.
   * @param time The time to use for the search.
   * @return An optional with the {@link Receipt} if it exists, otherwise an empty Optional.
   */
  public Optional<Receipt> findReceipt(final long time) {

    for(Receipt receipt : receipts.values()) {
      if(receipt.getTime() == time) {
        return Optional.of(receipt);
      }
    }
    return Optional.empty();
  }

  /**
   * Used to find a receipt based on the {@link UUID} it occurred.
   * @param identifier The {@link UUID} to use for the search.
   * @return An optional with the {@link Receipt} if it exists, otherwise an empty Optional.
   */
  public Optional<Receipt> findReceipt(final UUID identifier) {
    return Optional.ofNullable(receipts.get(identifier));
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

  public ConcurrentHashMap<UUID, Receipt> getReceipts() {
    return receipts;
  }
}