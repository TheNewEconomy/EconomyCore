package net.tnemc.core.api;
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

import net.tnemc.core.api.callback.TNECallback;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * CallbackEntry represents a class that serves as an entry for a specific callback.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CallbackEntry {

  private final List<Function<TNECallback, Boolean>> consumers = new ArrayList<>();

  private final Class<? extends TNECallback> clazz;

  public CallbackEntry(Class<? extends TNECallback> clazz) {
    this.clazz = clazz;
  }

  /**
   * Used to add a consumer for this callback.
   * @param consumer The consumer to add for this callback. This is an implementation of the
   *                 {@link Function} interface, which accepts an {@link TNECallback} parameter and
   *                 returns a boolean value, which indicates if the "event" should be cancelled or
   *                 not.
   */
  public void addConsumer(final Function<TNECallback, Boolean> consumer) {

    consumers.add(consumer);
  }

  /**
   * Used to call all the consumers for this callback.
   * @param callback The callback class for this consumer.
   *
   * @return This returns a boolean value, which equates to whether this event is cancelled or not.
   */
  public boolean call(final TNECallback callback) {
    if(!clazz.isInstance(callback)) {
      return false;
    }

    boolean cancelled = false;
    for(Function<TNECallback, Boolean> consumer : consumers) {
      if(consumer.apply(callback)) {
        cancelled = true;
      }
    }
    return cancelled;
  }
}