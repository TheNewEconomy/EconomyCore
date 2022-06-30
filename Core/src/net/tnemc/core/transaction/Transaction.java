package net.tnemc.core.transaction;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.utils.exceptions.InvalidTransactionException;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Represents a financial transaction.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Transaction {

  private TransactionProcessor processor; //TODO: default processor(s).

  private String type;
  private TransactionSource source;

  private UUID from;
  private UUID to;
  private Charge charge;

  //TODO: Transaction to and from. Should we allow conversion here? Probably.

  private Consumer<TransactionResult> resultConsumer;

  public Transaction(String type) {
    this.type = type;
  }

  /**
   * Used to set the transaction type of this transaction.
   * @param type The identifier of the transaction type.
   * @return An instance of the Transaction object with the new
   * transaction type.
   */
  public Transaction of(final String type) {
    this.type = type;
    return this;
  }

  /**
   * Used to set {@link TransactionSource source} of this transaction.
   * @param source The {@link TransactionSource source} of this transaction.
   * @return An instance of the Transaction object with the new {@link TransactionSource source}.
   */
  public Transaction source(final TransactionSource source) {
    this.source = source;
    return this;
  }

  /**
   * Used to set {@link TransactionProcessor processor} of this transaction.
   * @param processor The {@link TransactionProcessor processor} of this transaction.
   * @return An instance of the Transaction object with the new {@link TransactionProcessor processor}.
   */
  public Transaction processor(final TransactionProcessor processor) {
    this.processor = processor;
    return this;
  }

  /**
   * Processes the transaction and sends the result to the {@link Transaction#resultConsumer Result
   * Handler}.
   *
   * @throws InvalidTransactionException If all required aspects of the transaction are not present.
   */
  public void process(Consumer<TransactionResult> resultConsumer) throws InvalidTransactionException {
    this.resultConsumer = resultConsumer;

    process();
  }

  /**
   * Processes the transaction and sends the result to the {@link Transaction#resultConsumer Result
   * Handler}.
   *
   * @throws InvalidTransactionException If all required aspects of the transaction are not present.
   */
  public void process() throws InvalidTransactionException {
    String missing = null;

    if(this.type == null) {
      missing = "Transaction Type";
    }

    if(this.processor == null) {
      missing = "Transaction Processor";
    }

    if(this.to == null && this.from == null) {
      missing = "Both Transaction Parties";
    }

    if(missing != null) {
      throw new InvalidTransactionException(missing);
    }

    if(resultConsumer != null) {
      resultConsumer.accept(processor.process(this));
    }
  }

  public TransactionProcessor getProcessor() {
    return processor;
  }

  public String getType() {
    return type;
  }

  public TransactionSource getSource() {
    return source;
  }
}