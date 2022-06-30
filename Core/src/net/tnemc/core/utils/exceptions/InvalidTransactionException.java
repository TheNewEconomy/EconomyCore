package net.tnemc.core.utils.exceptions;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

/**
 * IncompleteTransactionException
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class InvalidTransactionException extends Exception {

  /**
   * Constructs a new exception with {@code null} as its detail message.
   * The cause is not initialized, and may subsequently be initialized by a
   * call to {@link #initCause}.
   */
  public InvalidTransactionException(String missing) {
    super("The transaction attempted was missing a requirement and could not be processed!" +
              "Missing: " + missing);
  }
}