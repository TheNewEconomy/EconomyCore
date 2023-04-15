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

import java.util.concurrent.ConcurrentHashMap;

/**
 * SchedulerProvider represents a bridge between the core and the implementation's scheduling
 * system.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 *
 * @param <C> Represents the implementation's {@link Chore} object.
 */
public abstract class SchedulerProvider<C extends Chore<?>> {

  protected final ConcurrentHashMap<Integer, C> chores = new ConcurrentHashMap<>();

  /**
   * Used to create a task, which will execute after the specified delay.
   * @param task The task to run.
   * @param delay The delay, in ticks.
   * @param environment The execution environment for the task.
   */
  public abstract void createDelayedTask(Runnable task, long delay, ChoreExecution environment);

  /**
   * Used to create a task, which repeats after a specified period.
   *
   * @param task The task to run.
   * @param delay The delay to run the task, in ticks.
   * @param period The period to run the task.
   * @param environment The execution environment for the task.
   *
   * @return The associated {@link Chore} with this task.
   */
  public abstract C createRepeatingTask(Runnable task, long delay, long period, ChoreExecution environment);

  /**
   * Used to create a task, which repeats after a specified period.
   * @param task The task to run.
   * @param delay The delay to run the task, in ticks.
   * @param period The period to run the task.
   *
   * @return The id, or -1 if no id can be returned.
   */
  public int createRepeatingTaskID(Runnable task, long delay, long period, ChoreExecution environment) {
    return createRepeatingTask(task, delay, period, environment).id();
  }

  /**
   * Used to cancel a task.
   * @param id The id of the task to cancel.
   */
  public void cancelTask(final int id) {
    if(chores.containsKey(id)) {
      chores.get(id).cancel();
      chores.remove(id);
    }
  }
}