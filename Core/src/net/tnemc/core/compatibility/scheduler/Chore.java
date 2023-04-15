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

/**
 * Chore represents a task for the implementation's {@link SchedulerProvider}
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 *
 * @param <T> Represents the implementation's Task object.
 */
public abstract class Chore<T> {

  protected int id;
  protected final T task;
  protected final ChoreExecution execution;

  public Chore(T task, ChoreExecution execution) {
    this.task = task;
    this.execution = execution;
  }

  public Chore(int id, T task, ChoreExecution execution) {
    this.id = id;
    this.task = task;
    this.execution = execution;
  }

  /**
   * Used to get the id for this Task.
   * @return The id for this Task.
   */
  public int id() {
    return id;
  }

  /**
   * Used to get the locale Task object.
   * @return The locale Task object.
   */
  public T locale() {
    return task;
  }

  /**
   * Returns this Chore's execution environment.
   * @return The {@link ChoreExecution environment} for this Chore.
   */
  public ChoreExecution execution() {
    return execution;
  }

  /**
   * Cancels this task.
   */
  public abstract void cancel();
}