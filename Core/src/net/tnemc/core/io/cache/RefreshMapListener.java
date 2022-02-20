package net.tnemc.core.io.cache;


import net.tnemc.core.io.Datable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A listener that is associated with a {@link RefreshConcurrentMap} which provides methods
 * that are called when various actions occur in the map such as adding and removing entries.
 *
 * @param <K> The Object associated with the map's key object.
 * @param <V> The Object associated with the map's value object.
 * @see RefreshConcurrentMap
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public interface RefreshMapListener<K, V extends Datable<K, V>> {

  /**
   * Used to save a value to the database.
   *
   * @param key The key added.
   * @param value The value added.
   */
  void put(@NotNull K key, @NotNull V value);

  /**
   * Used to get a value from the database using the specified key.
   *
   * @param key The key used in the get request.
   *
   * @return An optional value associated with the specified key.
   */
  @Nullable V get(@NotNull Object key);

  /**
   * Tests if the specified object is a valid key in the database.
   *
   * @param key possible key
   * @return True if the database contains the key, otherwise false.
   */
  boolean containsKey(@NotNull Object key);

  /**
   * Tests if the specified object is a valid object in the database.
   *
   * @param key value we are searching for in the database
   * @return True if the database contains the value, otherwise false.
   */
  boolean containsValue(@NotNull Object key);

  /**
   * Called when a key expires in the map, before it is refreshed.
   *
   * @param key The key that expired.
   * @param value The value associated with the key that expired.
   */
  void preRefresh(@NotNull K key, @NotNull V value);

  /**
   * Used to remove an object from the database.
   *
   * @param key The key removed.
   */
  boolean remove(@NotNull Object key);

  /**
   * Used to delete all values from the database.
   */
  void clear();

  /**
   * Returns an enumeration of the keys in the database.
   *
   * @return
   */
  default Enumeration<K> keys() {
    return map().keys();
  }

  /**
   * Returns an enumeration of the values in the database.
   *
   * @return
   */
  default Enumeration<V> elements() {
    return map().elements();
  }

  /**
   *
   *
   * @return
   */
  default Set<Map.Entry<K, V>> entrySet() {
    return map().entrySet();
  }

  /**
   * Generates a {@link ConcurrentHashMap} from all values in the database.
   *
   * @return The generated map.
   */
  ConcurrentHashMap<K, V> map();
}