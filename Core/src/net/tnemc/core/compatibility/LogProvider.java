package net.tnemc.core.compatibility;

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