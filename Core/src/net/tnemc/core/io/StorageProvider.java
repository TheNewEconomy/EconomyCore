package net.tnemc.core.io;

import java.sql.SQLException;

/**
 * Represents a class that provides the SQL logic for each {@link Storage engine}.
 *
 * @see Storage
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public abstract class StorageProvider {
  //TODO: implement this for h2, and mysql.

  /**
   * The identifier of this storage engine.
   *
   * @return The identifier of this storage engine.
   */
  public abstract String identifier();

  /**
   * The Storage engine for this data provider.
   *
   * @see Storage
   * @return The Storage engine for this data provider.
   * @throws SQLException if it cannot connect to the database with the engine provided.
   */
  public abstract Storage engine() throws SQLException;
}