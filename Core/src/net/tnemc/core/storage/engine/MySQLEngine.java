package net.tnemc.core.storage.engine;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.StorageSettings;
import net.tnemc.core.storage.StorageEngine;
import net.tnemc.core.storage.StorageManager;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * MySQLEngine
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MySQLEngine implements StorageEngine {

  @Override
  public String identifier() {
    return "mysql";
  }

  /**
   * Outlines a list of drivers that may be found for this storage engine. This allows us to support
   * multiple driver classes such as maria, mysql, etc. for each storage engine.
   *
   * @return The list of drivers that may be possible to find.
   */
  @Override
  public LinkedHashSet<String> getDrivers() {
    LinkedHashSet<String> set = new LinkedHashSet<>();
    set.add("org.mariadb.jdbc.Driver");
    set.add("com.mysql.cj.jdbc.Driver");
    set.add("com.mysql.jdbc.Driver");

    return set;
  }

  /**
   * Generates the connection URL String based on the provided details.
   *
   * @param settings The storage settings to use for this url.
   *
   * @return The generated connection URL using the details provided.
   */
  @Override
  public String getURL(StorageSettings settings) {
    return "jdbc:mysql://" + settings.host() + ":" + settings.port() + "/" + settings.database() +
        "?useSSL=" + settings.ssl() + "&allowPublicKeyRetrieval=" + settings.publicKey();
  }

  /**
   * Initializes the storage engine.
   *
   * @param manager
   */
  @Override
  public void initialize(StorageManager manager) {

  }

  @Override
  public Map<String, Object> hikariProperties() {
    return StorageEngine.super.hikariProperties();
  }
}