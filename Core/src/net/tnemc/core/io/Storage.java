package net.tnemc.core.io;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Storage Engine. This is used to use multiple types of storage systems with ease.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public interface Storage {

  /**
   * The driver String for this storage engine.
   *
   * @return The driver String for this storage engine.
   */
  String getDriver();

  /**
   * Whether to use the dataSourceURL or not. If false, we use the driver string instead.
   *
   * @return Whether to use the dataSourceURL or not. If false, we use the driver string instead.
   */
  Boolean dataSource();

  /**
   * The data source String for this storage engine.
   *
   * @return The data source String for this storage engine.
   */
  String dataSourceURL();

  /**
   * Generates the connection URL String based on the provided details.
   *
   * @param file The file name, if applicable.
   * @param host The host to connect to.
   * @param port The port to connect to.
   * @param database The database name to connect to.
   * @return The generated connection URL using the details provided.
   */
  String getURL(String file, String host, int port, String database);

  /**
   * Initializes the storage engine.
   *
   */
  void initialize(StorageManager manager);

  /**
   * Map containing properties to control Hikari CP.
   * @return Map containing properties to control Hikari CP.
   */
  default Map<String, Object> hikariProperties() {
    return new HashMap<>();
  }
}