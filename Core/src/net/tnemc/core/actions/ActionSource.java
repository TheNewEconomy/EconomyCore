package net.tnemc.core.actions;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

/**
 * Represents the source of an action that was performed. This could be anything from balance changes
 * in the economy API to other API-related actions.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface ActionSource {

  /**
   *
   * Used to get the name of the source of the action.
   *
   * Please note: There is no guarantee of uniqueness.
   *
   * @return The name of the source for a specific action. This could be user-friendly
   * or not. This should be the name of the source that has caused this action to occur. For
   * instance, a plugin name.
   *
   * @since 0.1.2.0
   */
  String name();

  /**
   * Used to get a description of the reason for why the action was performed.
   *
   * @return The reason for the action that was performed.
   *
   * @since 0.1.2.0
   */
  String reason();
}