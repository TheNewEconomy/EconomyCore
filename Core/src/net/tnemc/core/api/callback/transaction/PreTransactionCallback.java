package net.tnemc.core.api.callback.transaction;
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

import net.tnemc.core.api.callback.TNECallbacks;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.plugincore.core.api.callback.Callback;

/**
 * PreTransactionCallback represents a callback that is called before a transaction is performed. This
 * makes it cancellable.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PreTransactionCallback implements Callback {

  protected Transaction transaction;

  public PreTransactionCallback(Transaction transaction) {
    this.transaction = transaction;
  }

  /**
   * The name of this callback.
   *
   * @return The name of this callback.
   */
  @Override
  public String name() {
    return TNECallbacks.TRANSACTION_PRE.id();
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }
}