package net.tnemc.core.transaction.receipt;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.manager.TransactionManager;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.history.AwayHistory;
import net.tnemc.core.transaction.history.SortedHistory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ReceiptBox represents an object that can hold receipts.
 *
 * @author creatorfromhell
 * @see Receipt
 * @since 0.1.2.0
 */
public class ReceiptBox {

  private final Queue<UUID> receipts = new ConcurrentLinkedQueue<>();
  protected UUID receiptOwner;
  private AwayHistory away = null;
  private SortedHistory sorted = null;
  private boolean checked = false;

  public ReceiptBox(final UUID receiptOwner) {

    this.receiptOwner = receiptOwner;
  }

  /**
   * Used to calculate transactions that happened while an account owner was away.
   *
   * @param account The identifier of the account.
   *
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

    for(final Map.Entry<Long, Receipt> entry : range(((PlayerAccount)acc.get()).getLastOnline(), time).entrySet()) {

      final Receipt receipt = entry.getValue();

      if(receipt.getFrom() != null && receipt.getFrom().getId().equals(account)
         || receipt.getTo() != null && receipt.getTo().getId().equals(account)) {
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

  public SortedHistory getSorted(final UUID identifier) {

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
   * Logs a receipt reference to this receipt box.
   *
   * @param receipt The {@link Receipt} to log.
   */
  public void logReference(final Receipt receipt) {

    receipts.add(receipt.getId());
  }

  /**
   * Used to destroy a receipt based on the time it occurred.
   *
   * @param time The time to use for the destruction.
   */
  public void destroy(final long time) {

    TransactionManager.receipts().removeReceiptsByTimeAndParticipant(time, receiptOwner);
  }

  /**
   * Used to destroy a receipt based on the {@link UUID} it occurred.
   *
   * @param identifier The identifier to use for the destruction.
   */
  public void destroy(final UUID identifier) {

    receipts.remove(identifier);
  }

  /**
   * Used to find a receipt based on the time it occurred.
   *
   * @param time The time to use for the search.
   *
   * @return An optional with the {@link Receipt} if it exists, otherwise an empty Optional.
   */
  public List<Receipt> findReceipts(final long time) {

    return TransactionManager.receipts().getReceiptByTime(time);
  }

  /**
   * Used to find a receipt based on the {@link UUID} it occurred.
   *
   * @param identifier The {@link UUID} to use for the search.
   *
   * @return An optional with the {@link Receipt} if it exists, otherwise an empty Optional.
   */
  public Optional<Receipt> findReceipt(final UUID identifier) {

    return TransactionManager.receipts().getReceiptByUUID(identifier);
  }

  /**
   * This is used to return a range of {@link Receipt receipts}, based on a time range.
   *
   * @param start The start of the time to use for the range.
   * @param end   The end of the time to use for the range.
   *
   * @return A {@link TreeMap} of the receipts that occurred during the time range.
   */
  public TreeMap<Long, Receipt> range(final long start, final long end) {

    return TransactionManager.receipts().getReceiptsBetweenTimesAndParticipant(start, end, receiptOwner);
  }

  public Queue<UUID> getReceipts() {

    return receipts;
  }
}