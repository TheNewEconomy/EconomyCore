package net.tnemc.core.account.history;

import net.tnemc.core.io.cache.RefreshConcurrentMap;
import net.tnemc.core.io.cache.listeners.TransactionMapListener;
import net.tnemc.core.transaction.Transaction;

import java.util.UUID;

/**
 * Represents a log for transactions related to an account.
 *
 * @see Transaction
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class TransactionLog {

  RefreshConcurrentMap<UUID, Transaction> history;

  private UUID id;

  /**
   * Initializes a new Transaction Log class for a specific account.
   *
   * @param id The identifier of the account.
   */
  public TransactionLog(UUID id) {
    this.id = id;

    history = new RefreshConcurrentMap<>(false, 0, new TransactionMapListener());
  }

  public RefreshConcurrentMap<UUID, Transaction> getHistory() {
    return history;
  }

  public void setHistory(RefreshConcurrentMap<UUID, Transaction> history) {
    this.history = history;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}