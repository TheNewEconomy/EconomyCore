package net.tnemc.core.compatibility.scheduler;
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

import java.util.concurrent.TimeUnit;

/**
 * ChoreTime represents a time for a {@link Chore}.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 *
 * @see Chore
 */
public class ChoreTime {

  private final int time;
  private final TimeUnit unit;

  public ChoreTime(int ticks) {
    this.time = ticks;

    //We set our unit to null for ticks.
    this.unit = null;
  }

  public ChoreTime(int time, TimeUnit unit) {
    this.time = time;
    this.unit = unit;
  }

  public long asSeconds() {
    if(unit == null) {
      //this is ticks since our unit is null, so we have to divide by 20
      return (time / 20);
    }
    return unit.toSeconds(time);
  }

  public long asTicks() {
    if(unit == null) {
      //this is ticks since our unit is null, so we just return it
      return time;
    }
    return (unit.toSeconds(time) * 20);
  }

  public int getTime() {
    return time;
  }
}