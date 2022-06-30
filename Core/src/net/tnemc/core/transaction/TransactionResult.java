package net.tnemc.core.transaction;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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