package net.tnemc.bukkit.impl;

/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.LogProvider;
import net.tnemc.core.compatibility.log.DebugLevel;

import java.util.logging.Logger;

/**
 * BukkitLogProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public record BukkitLogProvider(Logger logger) implements LogProvider {

  /**
   * Sends an informative message, which doesn't contain an error or debug message.
   *
   * @param message The message to send.
   * @param level   The {@link DebugLevel} to log this message at.
   */
  @Override
  public void inform(String message, DebugLevel level) {
    if(level.compare(TNECore.instance().getLevel())) {
      logger.info(message);
    }
  }

  /**
   * Sends a message related to debug purposes.
   *
   * @param message The message to send.
   * @param level   The {@link DebugLevel} to log this message at.
   */
  @Override
  public void debug(String message, DebugLevel level) {
    if(level.compare(TNECore.instance().getLevel())) {
      logger.warning("[DEBUG]: " + message);
    }
  }

  /**
   * Sends a warning message.
   *
   * @param message The message to send.
   * @param level   The {@link DebugLevel} to log this message at.
   */
  @Override
  public void warning(String message, DebugLevel level) {
    if(level.compare(TNECore.instance().getLevel())) {
      logger.warning(message);
    }
  }

  /**
   * Sends an error-related message.
   *
   * @param message The message to send.
   * @param level   The {@link DebugLevel} to log this message at.
   */
  @Override
  public void error(String message, DebugLevel level) {
    if(level.compare(TNECore.instance().getLevel())) {
      logger.warning(message);
    }
  }

  /**
   * Sends an error-related message.
   *
   * @param message   The message to send.
   * @param exception The error's {@link Exception}.
   * @param level     The {@link DebugLevel} to log this message at.
   */
  @Override
  public void error(String message, Exception exception, DebugLevel level) {
    if(level.compare(TNECore.instance().getLevel())) {
      logger.warning("====== Exception Occurred ======");
      for(StackTraceElement trace : exception.getStackTrace()) {
        logger.warning(trace.toString());
      }
      logger.warning("====== Please report this to someone ======");
    }
  }
}