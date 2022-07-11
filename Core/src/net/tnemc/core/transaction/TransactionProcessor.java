package net.tnemc.core.transaction;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.EconomyResponse;

import java.util.LinkedList;
import java.util.Optional;

/**
 * This is a class that handles the actual processing of a transaction.
 *
 * @see Transaction
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface TransactionProcessor {

  /**
   * Processes a transaction.
   * @param transaction The {@link Transaction transaction} to handle.
   * @return The {@link TransactionResult result} from performing the transaction.
   */
  default TransactionResult process(Transaction transaction) {
    Optional<EconomyResponse> response = processChecks(transaction);

    if(response.isPresent() && !response.get().success()) {
      return new TransactionResult(false, response.get().response());
    }

    if(transaction.getFrom() != null) {
      Optional<Account> from = TNECore.eco().account().findAccount(transaction.getFrom().getId());
      from.ifPresent(account->account.setHoldings(transaction.getFrom().getEndingBalance()));
      //TODO: This is where we do the currency type stuff
    }

    if(transaction.getTo() != null) {
      Optional<Account> to = TNECore.eco().account().findAccount(transaction.getTo().getId());
      to.ifPresent(account->account.setHoldings(transaction.getTo().getEndingBalance()));
      //TODO: This is where we do the currency type stuff
    }
    return new TransactionResult(true, "");
  }

  default Optional<EconomyResponse> processChecks(Transaction transaction) {
    EconomyResponse response = null;
    for(final String str : getChecks()) {
      Optional<TransactionCheck> check = TNECore.eco().transaction().findCheck(str);
      if(check.isPresent()) {
        response = check.get().process(transaction);

        if(!response.success())
          break;
      }
    }

    return Optional.ofNullable(response);
  }

  /**
   * Used to get the checks for this processor.
   * @return The checks for this processor.
   */
  LinkedList<String> getChecks();

  /**
   * Used to add {@link TransactionCheck check} to this processor.
   * @param check The check to add.
   */
  void addCheck(final TransactionCheck check);

  /**
   * Used to add {@link TransactionCheck checks} from a group to this processor.
   * @param group The group to add.
   */
  void addGroup(final TransactionCheckGroup group);
}