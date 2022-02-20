package net.tnemc.core.io.cache;

/**
 * An object that represents a check in an {@link RefreshConcurrentMap}.
 *
 * @see RefreshConcurrentMap
 * @since 1.0.0
 * @author creatorfromhell
 */
public enum CheckLevel {

  /**
   * Check the map first, and if nothing exists, check the database.
   */
  MAP_FIRST,

  /**
   * Check the database only.
   */
  DB_ONLY,

  /**
   * Check the map only.
   */
  MAP_ONLY;
}