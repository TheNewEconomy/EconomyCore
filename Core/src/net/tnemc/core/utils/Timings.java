package net.tnemc.core.utils;

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