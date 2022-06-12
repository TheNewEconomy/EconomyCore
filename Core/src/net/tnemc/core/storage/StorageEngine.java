package net.tnemc.core.storage;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.StorageSettings;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * StorageEngine
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface StorageEngine {

  /**
   * The identifier for this {@link StorageEngine engine}.
   *
   * @return The identifier for this engine, which is used for search-related purposes.
   */
  String identifier();

  /**
   * Outlines a list of drivers that may be found for this storage engine. This allows us to support
   * multiple driver classes such as maria, mysql, etc. for each storage engine.
   *
   * @return The list of drivers that may be possible to find.
   */
  LinkedHashSet<String> getDrivers();

  /**
   * Generates the connection URL String based on the provided details.
   *
   * @param settings The storage settings to use for this url.
   * @return The generated connection URL using the details provided.
   */
  String getURL(StorageSettings settings);

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