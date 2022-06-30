package net.tnemc.core.transaction;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

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
  TransactionResult process(Transaction transaction);
}