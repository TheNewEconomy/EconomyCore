package net.tnemc.minestom.impl.scheduler;
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

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import net.tnemc.core.compatibility.scheduler.Chore;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.core.compatibility.scheduler.SchedulerProvider;

/**
 * MinestomScheduler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MinestomScheduler extends SchedulerProvider<MinestomChore> {
  /**
   * Used to create a task, which will execute after the specified delay.
   *
   * @param task  The task to run.
   * @param delay The delay, in ticks.
   * @param environment The execution environment for the task.
   */
  @Override
  public void createDelayedTask(Runnable task, long delay, ChoreExecution environment) {
    final ExecutionType type = (environment.equals(ChoreExecution.MAIN_THREAD))? ExecutionType.SYNC
                               : ExecutionType.ASYNC;
    MinecraftServer.getSchedulerManager().scheduleTask(task, TaskSchedule.tick((int)delay),
                                                       TaskSchedule.immediate(), type);
  }

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
  @Override
  public MinestomChore createRepeatingTask(Runnable task, long delay, long period, ChoreExecution environment) {
    final ExecutionType type = (environment.equals(ChoreExecution.MAIN_THREAD))? ExecutionType.SYNC
                                                                               : ExecutionType.ASYNC;

    return new MinestomChore(MinecraftServer.getSchedulerManager()
                                 .scheduleTask(task, TaskSchedule.tick((int)delay),
                                               TaskSchedule.tick((int)period), type), environment);
  }
}
