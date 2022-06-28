package net.tnemc.core.compatibility;


/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 2/20/2022.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

import net.tnemc.core.compatibility.log.DebugLevel;

/**
 * Provides a compatibility layer for logging purposes.
 *
 * @since 0.1.2.0
 * @author creatorfromhell
 */
public interface LogProvider {

  /**
   * Sends an informative message, which doesn't contain an error or debug message.
   * @param message The message to send.
   */
  default void inform(String message) {
    inform(message, DebugLevel.STANDARD);
  }

  /**
   * Sends an informative message, which doesn't contain an error or debug message.
   * @param message The message to send.
   * @param level The {@link DebugLevel} to log this message at.
   */
  void inform(String message, DebugLevel level);

  /**
   * Sends a message related to debug purposes.
   * @param message The message to send.
   */
  default void debug(String message) {
    debug(message, DebugLevel.STANDARD);
  }

  /**
   * Sends a message related to debug purposes.
   * @param message The message to send.
   * @param level The {@link DebugLevel} to log this message at.
   */
  void debug(String message, DebugLevel level);

  /**
   * Sends a warning message.
   * @param message The message to send.
   */
  default void warning(String message) {
    warning(message, DebugLevel.STANDARD);
  }

  /**
   * Sends a warning message.
   * @param message The message to send.
   * @param level The {@link DebugLevel} to log this message at.
   */
  void warning(String message, DebugLevel level);

  /**
   * Sends an error-related message.
   * @param message The message to send.
   */
  default void error(String message) {
    error(message, DebugLevel.STANDARD);
  }

  /**
   * Sends an error-related message.
   * @param message The message to send.
   * @param level The {@link DebugLevel} to log this message at.
   */
  void error(String message, DebugLevel level);

  /**
   * Sends an error-related message.
   * @param message The message to send.
   * @param exception The error's {@link Exception}.
   * @param level The {@link DebugLevel} to log this message at.
   */
  void error(String message, Exception exception, DebugLevel level);
}