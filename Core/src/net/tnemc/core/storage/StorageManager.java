package net.tnemc.core.storage;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.StorageSettings;
import net.tnemc.core.TNECore;

import java.util.HashMap;
import java.util.Map;

/**
 * The manager, which manages everything related to storage.
 * Manages:
 * - Loading
 * - Saving
 * - Caching
 * - Connections
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class StorageManager {

  private Map<String, StorageEngine> engines = new HashMap<>();

  private StorageSettings settings;

  private static StorageManager instance;

  public StorageManager(StorageSettings settings) {
    instance = this;

    this.settings = settings;
  }

  public StorageEngine engine() {
    return engines.getOrDefault(settings.type(), engines.get("h2"));
  }

  public static final StorageSettings settings() {
    return instance.settings;
  }

  public static StorageManager instance() {
    return instance;
  }
}