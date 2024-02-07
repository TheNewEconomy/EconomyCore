package net.tnemc.core.transaction;

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

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents the result of a {@link Transaction}.
 *
 * @see Transaction
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TransactionResult {

  private @Nullable Receipt receipt;
  private boolean successful;
  private String message;

  public TransactionResult(boolean successful, String message) {
    this.successful = successful;
    this.message = message;
  }

  /**
   * The receipt for the {@link Transaction transaction} if it was successful, otherwise an empty
   * Optional.
   *
   * @return The receipt for the {@link Transaction transaction} if it was successful, otherwise an empty
   * Optional.
   */
  public Optional<Receipt> getReceipt() {
    return Optional.ofNullable(receipt);
  }

  public void setReceipt(@Nullable Receipt receipt) {
    this.receipt = receipt;
  }

  public boolean isSuccessful() {
    return successful;
  }

  public void setSuccessful(boolean successful) {
    this.successful = successful;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}