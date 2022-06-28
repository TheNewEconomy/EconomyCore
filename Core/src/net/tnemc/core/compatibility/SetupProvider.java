package net.tnemc.core.compatibility;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

/**
 * A class that acts like a bridge for the initial TNE setup process. This is utilized
 * to set up basic features, and read offline player data to have the plugin install
 * seemlessly into the server without missing a beat.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface SetupProvider {

  default void setup() {

  }

  /**
   * This method is used to load existing UUIDs and map them to players in the UUID System.
   */
  void loadExistingIDS();
}