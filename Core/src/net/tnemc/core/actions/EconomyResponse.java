package net.tnemc.core.actions;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

/**
 * Represents a response by the Economy Plugin.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface EconomyResponse {

  /**
   * @since 0.1.2.0
   * @return True if the associated action was performed correctly.
   */
  boolean success();

  /**
   * @since 0.1.2.0
   * @return The string to return to the performer of the action.
   */
  default String response() {
    return "Default response string";
  }
}