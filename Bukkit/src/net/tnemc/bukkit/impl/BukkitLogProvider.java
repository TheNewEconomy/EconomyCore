package net.tnemc.bukkit.impl;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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