package net.tnemc.core.io.maps;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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