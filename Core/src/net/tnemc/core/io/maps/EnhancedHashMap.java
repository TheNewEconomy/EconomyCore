package net.tnemc.core.io.maps;

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EnhancedHashMap
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class EnhancedHashMap<K, V> extends ConcurrentHashMap<K, V> {

  /**
   * Used to add a value with the requirement of specifying the key by utilizing the {@link MapKey}
   * annotation on a method.
   *
   * @param value The value to add to this map.
   * @return The value that is being replaced in the map if exists, otherwise null.
   * @throws UnsupportedOperationException If a value does not have a valid method with the
   * {@link MapKey} annotation, which matches the value type of the key for this map.
   */
  @SuppressWarnings("unchecked")
  public V put(V value) throws UnsupportedOperationException {

    for(Method method : value.getClass().getDeclaredMethods()) {

      //TODO: How to validate correct return type?
      if(method.isAnnotationPresent(MapKey.class)) {
        try {
          return put((K)method.invoke(value), value);
        } catch(IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }

    throw new UnsupportedOperationException("No valid key method found using the MapKey annotation.");
  }
}