package net.tnemc.folia.impl.scheduler;
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

import net.tnemc.bukkit.BukkitCore;
import net.tnemc.core.compatibility.scheduler.Chore;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.core.compatibility.scheduler.ChoreTime;
import net.tnemc.core.compatibility.scheduler.SchedulerProvider;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

/**
 * FoliaScheduler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class FoliaScheduler extends SchedulerProvider<FoliaChore> {
  /**
   * Used to create a task, which will execute after the specified delay.
   *
   * @param task  The task to run.
   * @param delay The delay, in ticks.
   * @param environment The execution environment for the task.
   */
  @Override
  public void createDelayedTask(Runnable task, ChoreTime delay, ChoreExecution environment) {
    //we divide the delay by 20 because Folia uses seconds, and the delay is sent in ticks.
    Bukkit.getAsyncScheduler().runDelayed(BukkitCore.instance().getPlugin(), (scheduledTask) -> task.run(), delay.asSeconds(), TimeUnit.SECONDS);
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
  public FoliaChore createRepeatingTask(Runnable task, ChoreTime delay, ChoreTime period, ChoreExecution environment) {
    return new FoliaChore(Bukkit.getAsyncScheduler()
                                .runAtFixedRate(BukkitCore.instance().getPlugin(), (scheduledTask)->task.run(),
                                                delay.asSeconds(), period.asSeconds(), TimeUnit.SECONDS),
                                                environment);
  }
}
