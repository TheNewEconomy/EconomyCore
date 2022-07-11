package net.tnemc.core.actions.source;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.actions.ActionSource;

import java.util.UUID;

/**
 * PlayerSource
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public record PlayerSource(UUID id) implements ActionSource {

  /**
   * Used to get the name of the source of the action.
   * <p>
   * Please note: There is no guarantee of uniqueness.
   *
   * @return The name of the source for a specific action. This could be user-friendly
   * or not. This should be the name of the source that has caused this action to occur. For
   * instance, a plugin name.
   * @since 0.1.2.0
   */
  @Override
  public String name() {
    return id.toString();
  }

  /**
   * Used to get a description of the reason for why the action was performed.
   *
   * @return The reason for the action that was performed.
   * @since 0.1.2.0
   */
  @Override
  public String reason() {
    return "command";
  }
}