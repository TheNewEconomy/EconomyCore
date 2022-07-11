package net.tnemc.core.transaction.processor;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionCheck;
import net.tnemc.core.transaction.TransactionCheckGroup;
import net.tnemc.core.transaction.TransactionParticipant;
import net.tnemc.core.transaction.TransactionProcessor;
import net.tnemc.core.transaction.TransactionResult;

import java.util.LinkedList;

/**
 * BaseTransactionProcessor
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BaseTransactionProcessor implements TransactionProcessor {

  private final LinkedList<String> checks = new LinkedList<>();

  /**
   * Processes a transaction.
   *
   * @param transaction The {@link Transaction transaction} to handle.
   *
   * @return The {@link TransactionResult result} from performing the transaction.
   */
  @Override
  public TransactionResult process(Transaction transaction) {


    return null;
  }

  /**
   * Used to get the checks for this processor.
   *
   * @return The checks for this processor.
   */
  @Override
  public LinkedList<String> getChecks() {
    return checks;
  }

  /**
   * Used to add {@link TransactionCheck check} to this processor.
   *
   * @param check The check to add.
   */
  @Override
  public void addCheck(TransactionCheck check) {
    checks.add(check.identifier());
  }

  /**
   * Used to add {@link TransactionCheck checks} from a group to this processor.
   *
   * @param group The group to add.
   */
  @Override
  public void addGroup(TransactionCheckGroup group) {
    checks.addAll(group.getChecks());
  }
}