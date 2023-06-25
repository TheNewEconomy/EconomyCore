package net.tnemc.core.transaction;


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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.ActionSource;
import net.tnemc.core.api.callback.transaction.PostTransactionCallback;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.transaction.processor.BaseTransactionProcessor;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Represents a financial transaction.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Transaction {

  private TransactionProcessor processor;

  private final String type;
  private ActionSource source;

  private TransactionParticipant from;
  private TransactionParticipant to;
  private HoldingsModifier modifierTo;
  private HoldingsModifier modifierFrom;

  private boolean admin;
  private boolean track;

  private Consumer<TransactionResult> resultConsumer;

  public Transaction(String type) {
    this.type = type;

    this.processor = new BaseTransactionProcessor();
  }

  /**
   * Used to determine if the "to" participant is losing funds in this transaction.
   * @return True if the "to" participant is losing funds, otherwise false.
   */
  public boolean isToLosing() {
    return to != null && modifierTo != null && modifierTo.isRemoval();
  }

  /**
   * Used to determine if the "from" participant is losing funds in this transaction.
   * @return True if the "from" participant is losing funds, otherwise false.
   */
  public boolean isFromLosing() {
    return from != null && modifierFrom.isRemoval();
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
   * @param id The identifier of this participant.
   * @param modifier The {@link HoldingsModifier modifier} associated with this participant.
   * @return An instance of the Transaction object with the new
   * participant.
   */
  public Transaction from(final String id, final HoldingsModifier modifier) {

    final Optional<Account> account = TNECore.eco().account().findAccount(id);
    account.ifPresent(value->from(value, modifier));
    return this;
  }

  /**
   * Used to set the {@link TransactionParticipant from} participant.
   *
   * @param account The account reference of this participant.
   * @param modifier The {@link HoldingsModifier modifier} associated with this participant.
   * @return An instance of the Transaction object with the new
   * participant.
   */
  public Transaction from(final Account account, final HoldingsModifier modifier) {

    final List<HoldingsEntry> balances = account.getHoldings(modifier.getRegion(), modifier.getCurrency());

    if(balances.isEmpty()) {
      balances.add(new HoldingsEntry(modifier.getRegion(), modifier.getCurrency(),
                                     BigDecimal.ZERO, EconomyManager.NORMAL
      ));
    }

    this.from = new TransactionParticipant(account.getIdentifier(), balances);

    final Optional<TransactionType> type = TNECore.eco().transaction().findType(this.type);

    if(type.isPresent() && type.get().fromTax().isPresent()) {
      final BigDecimal tax = type.get().fromTax().get()
          .calculateTax(modifier.getModifier());

      for(HoldingsEntry entry : balances) {
        this.from.getEndingBalances().add(entry.modifyGrab(modifier).modifyGrab(tax.negate()));
      }
      this.from.setTax(tax);
    } else {
      for(HoldingsEntry entry : balances) {
        this.from.getEndingBalances().add(entry.modifyGrab(modifier));
      }
    }

    this.modifierFrom = modifier;
    return this;
  }

  /**
   * Used to set the {@link TransactionParticipant to} participant.
   *
   * @param id The identifier of this participant.
   * @param modifier The {@link HoldingsModifier modifier} associated with this participant.
   * @return An instance of the Transaction object with the new
   * participant.
   */
  public Transaction to(final String id, final HoldingsModifier modifier) {

    final Optional<Account> account = TNECore.eco().account().findAccount(id);
    account.ifPresent(value->to(value, modifier));
    return this;
  }

  /**
   * Used to set the {@link TransactionParticipant to} participant.
   *
   *
   * @param account The account reference of this participant.
   * @param modifier The {@link HoldingsModifier modifier} associated with this participant.
   * @return An instance of the Transaction object with the new
   * participant.
   */
  public Transaction to(final Account account, final HoldingsModifier modifier) {

    final List<HoldingsEntry> balances = account.getHoldings(modifier.getRegion(), modifier.getCurrency());

    if(balances.isEmpty()) {
      balances.add(new HoldingsEntry(modifier.getRegion(), modifier.getCurrency(),
                                     BigDecimal.ZERO, EconomyManager.NORMAL));
    }

    this.to = new TransactionParticipant(account.getIdentifier(), balances);

    final Optional<TransactionType> type = TNECore.eco().transaction().findType(this.type);

    final BigDecimal tax = (type.isPresent() && type.get().toTax().isPresent())? type.get().toTax().get()
                           .calculateTax(modifier.getModifier()) : BigDecimal.ZERO;

    BigDecimal working = null;
    final boolean take = (modifier.getModifier().compareTo(BigDecimal.ZERO) < 0);

    if(take) {
      working = modifier.getModifier().multiply(new BigDecimal(-1));
    }

    boolean done = false;

    for(int i = 0; i < balances.size(); i++) {
      final HoldingsEntry entry = balances.get(i);
      HoldingsEntry ending;

      TNECore.log().debug("entry bal: " + entry.getAmount().toPlainString(), DebugLevel.DEVELOPER);
      TNECore.log().debug("entry bal: " + entry.getRegion(), DebugLevel.DEVELOPER);

      if(!done) {
        if(!take) {

          ending = entry.modifyGrab(modifier).modifyGrab(tax.negate());
          this.to.getEndingBalances().add(ending);

          TNECore.log().debug("End: " + ending.getAmount().toPlainString(), DebugLevel.DEVELOPER);
          TNECore.log().debug("End: " + ending.getRegion(), DebugLevel.DEVELOPER);
          done = true;
        } else {

          TNECore.log().debug("Working: " + working.toPlainString(), DebugLevel.DEVELOPER);

          if(entry.getAmount().compareTo(working) >= 0) {
            TNECore.log().debug("Value: " + working.toPlainString(), DebugLevel.DEVELOPER);

            ending = entry.modifyGrab(working.multiply(new BigDecimal(-1))).modifyGrab(tax.negate());
            this.to.getEndingBalances().add(ending);
            TNECore.log().debug("break out since we are good to go with this entry", DebugLevel.DEVELOPER);
            done = true;
          } else {

            if(i == (balances.size() - 1)) {
              ending = entry.modifyGrab(working.multiply(new BigDecimal(-1)));

            } else {
              TNECore.log().debug("Keep Working", DebugLevel.DEVELOPER);
              ending = entry.modifyGrab(entry.getAmount().multiply(new BigDecimal(-1)));
              working = working.subtract(entry.getAmount());
            }
          }
        }
      } else {
        ending = entry;
      }
      this.to.getEndingBalances().add(ending);
    }

    working = null;

    this.to.setTax(tax);

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
   * Used to set this as an administrator performed transaction. This allows it to override certain
   * things such as account statuses.
   *
   * @param admin If this transaction is an administrator transaction or not.
   * @return An instance of the Transaction object with the new {@link ActionSource source}.
   */
  public Transaction admin(final boolean admin) {
    this.admin = admin;
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
  public TransactionResult process() throws InvalidTransactionException {
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

    final TransactionResult result = processor.process(this);

    if(resultConsumer != null) {
      resultConsumer.accept(result);
    }

    final PostTransactionCallback postTransaction = new PostTransactionCallback(result);
    TNECore.callbacks().call(postTransaction);

    return result;
  }

  public Optional<Account> getFromAccount() {
    if(from != null) {
      return TNECore.eco().account().findAccount(from.getId());
    }

    return Optional.empty();
  }

  public Optional<Account> getToAccount() {
    if(to != null) {
      return TNECore.eco().account().findAccount(to.getId());
    }

    return Optional.empty();
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

  public boolean isAdmin() {
    return admin;
  }

  public boolean isTrack() {
    return track;
  }

  public void setTrack(boolean track) {
    this.track = track;
  }

  public Consumer<TransactionResult> getResultConsumer() {
    return resultConsumer;
  }
}