package net.tnemc.core.storage;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.StorageSettings;

/**
 * Represents a data storage provider for us to utilize to perform database operations.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface StorageProvider {

  /**
   * The name of this provider.
   * @return The name of the provider.
   */
  String identifier();

  /**
   * Does this provider support direct save?
   * For instance, SQL-based providers do, but flatfile does not.
   *
   * @return True if this provider supports direct saving operations.
   */
  boolean directSave();

  /**
   * Initialize our provider.
   */
  void initialize();
}