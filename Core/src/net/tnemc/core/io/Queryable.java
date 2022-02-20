package net.tnemc.core.io;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents an object that can be used to directly interact with a database.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public interface Queryable<K, V> {

  /**
   * Used to load the respective object.
   *
   * @param key The key that corresponds to the object that should be loaded.
   * @return The loaded object.
   */
  V load(K key);

  /**
   * Used to refresh the provided object from the database.
   *
   * @param key The key associated with the object.
   * @param object The object to refresh.
   * @param complete A {@link BiConsumer} that returns an instance of the object, and a boolean that
   *                 returns true if the object was successfully refreshed, or false if the same
   *                 object was returned instead of a refreshed one.
   */
  default void refresh(K key, V object, BiConsumer<V, Boolean> complete) {
    save(object, (success)->{
      if(success) {
        complete.accept(load(key), true);
      } else {
        complete.accept(object, false);
      }
    });
  }

  /**
   * Used to save the object associated with this {@link Queryable}.
   *
   * @param object The object to save.
   * @param onCompletion Consumer that triggers on success or failure. Returns true if saving was
   *                     successful, otherwise false.
   */
  void save(V object, Consumer<Boolean> onCompletion);
}