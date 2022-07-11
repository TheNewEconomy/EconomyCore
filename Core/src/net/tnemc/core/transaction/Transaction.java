package net.tnemc.core.transaction;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.ActionSource;
import net.tnemc.core.utils.AccountHelper;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;

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
  private ActionSource source;

  private TransactionParticipant from;
  private TransactionParticipant to;
  private HoldingsModifier modifierTo;
  private HoldingsModifier modifierFrom;

  private Consumer<TransactionResult> resultConsumer;

  public Transaction(String type) {
    this.type = type;
  }

  /**
   * Used to create a new Transaction object from the specified transaction type.
   *
   * @param type The identifier of the transaction type.
   * @return An instance of the Transaction object created with the specified type.
   */
  public static Transaction of(final String type) {
    return new Transaction(type);
  }

  /**
   * Used to set the {@link TransactionParticipant from} participant.
   *
   * //TODO: Link with transaction system logic.
   *
   * @param id The identifier of this participant.
   * @param modifier The {@link HoldingsModifier modifier} associated with this participant.
   * @return An instance of the Transaction object with the new
   * participant.
   */
  public Transaction from(final String id, final HoldingsModifier modifier) {
    final String currency = modifier.getCurrency();
    final String region = modifier.getRegion();
    final HoldingsEntry entry = new HoldingsEntry(currency,
                                            region,
                                            AccountHelper.getHoldings(id,region, currency));

    this.from = new TransactionParticipant(id, entry);
    this.from.setEndingBalance(entry.modifyGrab(modifier));

    this.modifierFrom = modifier;
    return this;
  }
  /**
   * Used to set the {@link TransactionParticipant to} participant.
   *
   * //TODO: Link with transaction system logic.
   *
   * @param id The identifier of this participant.
   * @param modifier The {@link HoldingsModifier modifier} associated with this participant.
   * @return An instance of the Transaction object with the new
   * participant.
   */
  public Transaction to(final String id, final HoldingsModifier modifier) {
    final String currency = modifier.getCurrency();
    final String region = modifier.getRegion();
    final HoldingsEntry entry = new HoldingsEntry(currency,
                                                  region,
                                                  AccountHelper.getHoldings(id,region, currency));

    this.to = new TransactionParticipant(id, entry);
    this.to.setEndingBalance(entry.modifyGrab(modifier));

    this.modifierTo = modifier;
    return this;
  }

  /**
   * Used to set {@link ActionSource source} of this transaction.
   * @param source The {@link ActionSource source} of this transaction.
   * @return An instance of the Transaction object with the new {@link ActionSource source}.
   */
  public Transaction source(final ActionSource source) {
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

  public ActionSource getSource() {
    return source;
  }

  public TransactionParticipant getFrom() {
    return from;
  }

  public TransactionParticipant getTo() {
    return to;
  }

  public HoldingsModifier getModifierTo() {
    return modifierTo;
  }

  public HoldingsModifier getModifierFrom() {
    return modifierFrom;
  }

  public Consumer<TransactionResult> getResultConsumer() {
    return resultConsumer;
  }
}