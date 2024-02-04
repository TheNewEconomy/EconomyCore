package net.tnemc.bukkit.impl.scheduler;
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

import net.tnemc.core.compatibility.scheduler.Chore;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;
import org.bukkit.scheduler.BukkitTask;

/**
 * BukkitChore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BukkitChore extends Chore<BukkitTask> {

  public BukkitChore(BukkitTask task, ChoreExecution execution) {
    super(task.getTaskId(), task, execution);
  }

  /**
   * Cancels this task.
   */
  @Override
  public void cancel() {
    this.task.cancel();
  }
}
