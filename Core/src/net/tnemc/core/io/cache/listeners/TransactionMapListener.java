package net.tnemc.core.io.cache.listeners;

import net.tnemc.core.io.cache.RefreshMapListener;
import net.tnemc.core.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionMapListener implements RefreshMapListener<UUID, Transaction> {

  /**
   * Used to save a value to the database.
   *
   * @param key   The key added.
   * @param value The value added.
   */
  @Override
  public void put(@NotNull UUID key, @NotNull Transaction value) {

  }

  /**
   * Used to get a value from the database using the specified key.
   *
   * @param key The key used in the get request.
   *
   * @return An optional value associated with the specified key.
   */
  @Override
  public @Nullable Transaction get(@NotNull Object key) {
    return null;
  }

  /**
   * Tests if the specified object is a valid key in the database.
   *
   * @param key possible key
   *
   * @return True if the database contains the key, otherwise false.
   */
  @Override
  public boolean containsKey(@NotNull Object key) {
    return false;
  }

  /**
   * Tests if the specified object is a valid object in the database.
   *
   * @param key value we are searching for in the database
   *
   * @return True if the database contains the value, otherwise false.
   */
  @Override
  public boolean containsValue(@NotNull Object key) {
    return false;
  }

  /**
   * Called when a key expires in the map, before it is refreshed.
   *
   * @param key   The key that expired.
   * @param value The value associated with the key that expired.
   */
  @Override
  public void preRefresh(@NotNull UUID key, @NotNull Transaction value) {

  }

  /**
   * Used to remove an object from the database.
   *
   * @param key The key removed.
   */
  @Override
  public boolean remove(@NotNull Object key) {
    return false;
  }

  /**
   * Used to delete all values from the database.
   */
  @Override
  public void clear() {

  }

  /**
   * Generates a {@link ConcurrentHashMap} from all values in the database.
   *
   * @return The generated map.
   */
  @Override
  public ConcurrentHashMap<UUID, Transaction> map() {
    return null;
  }
}
