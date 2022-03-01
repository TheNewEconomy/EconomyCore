package net.tnemc.core.utils;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.log.DebugLevel;

/**
 * Timings
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Timings implements AutoCloseable {

  private String statement = "Timings: ";
  private long start;
  private long end;

  /**
   * Starts our timings in order to measure duration of methods or actions.
   * @return The timings object.
   */
  public static Timings time() {
    return new Timings().start();
  }


  /**
   * Used to start our timings with a statement for logging purposes.
   * @param statement The statement to use.
   * @return The timings instance.
   */
  public static Timings time(String statement) {
    return new Timings().start().withStatement(statement);
  }

  /**
   * Starts our timings in order to measure duration of methods or actions.
   * @return The timings object.
   */
  public Timings start() {
    start = System.nanoTime();
    return this;
  }

  /**
   * Used to build our timings with a statement for logging purposes.
   * @param statement The statement to use.
   * @return The timings instance.
   */
  public Timings withStatement(String statement) {
    this.statement = statement;
    return this;
  }

  /**
   * Stops the timings and returns the duration.
   * @return Return the duration this timings lasted.
   */
  public long stop() {
    this.end = System.nanoTime();
    return (end - start);
  }

  /**
   * Stops the timings and logs it to the console and server log.
   * @param level The DebugLevel to use for this.
   */
  public void stopLog(DebugLevel level) {
    this.end = System.nanoTime();
    TNECore.log().debug(statement + (end - start), level);
  }

  @Override
  public void close() throws Exception {
    stop();
  }
}