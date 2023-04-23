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

import net.minestom.server.timer.Task;
import net.tnemc.core.compatibility.scheduler.Chore;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;

/**
 * MinestomChore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MinestomChore extends Chore<Task> {

  public MinestomChore(Task task, ChoreExecution execution) {
    super(task.id(), task, execution);
  }

  /**
   * Cancels this task.
   */
  @Override
  public void cancel() {
    task.cancel();
  }
}