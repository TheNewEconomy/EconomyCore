package net.tnemc.core.actions.source;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.actions.ActionSource;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an {@link ActionSource source} that represents a plugin performing actions.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PluginSource implements ActionSource {

  /**
   * Represents the name of the plugin that is being used for actions.
   */
  private String plugin;

  /**
   * Represents the reason for actions.
   */
  private String reason = "Default action.";

  /**
   * Used to create an {@link ActionSource source} object that represents a plugin.
   *
   * @param plugin The name of the plugin causing the action.
   *
   * @since 0.1.2.0
   */
  public PluginSource(String plugin) {
    this.plugin = plugin;
  }

  /**
   * Used to create an {@link ActionSource source} object that represents a plugin with a specific
   * reason attached.
   *
   * @param plugin The name of the plugin causing the action.
   * @param reason The reason the action was performed.
   *
   * @since 0.1.2.0
   */
  public PluginSource(@NotNull String plugin, @NotNull String reason) {
    this.plugin = plugin;
    this.reason = reason;
  }

  /**
   * Used to change the reason of this {@link PluginSource source}. This could be used to utilize a
   * singular {@link PluginSource source} object while further modifying the reason as needed.
   *
   * @param reason The reason the action was performed.
   *
   * @return The modified {@link PluginSource source} with the specified reason.
   *
   * @since 0.1.2.0
   */
  public PluginSource withReason(@NotNull String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Used to get the name of the source of the action.
   *
   * @return The name of the source for a specific action. This could be user-friendly
   * or not. This should be the name of the implementation that has caused this action to occur. For
   * instance, a plugin name.
   * <p>
   * Please note: There is no guarantee of uniqueness.
   *
   * @since 0.1.2.0
   */
  @Override
  public String name() {
    return plugin;
  }

  /**
   * Used to get a description of the reason for why the action was performed.
   *
   * @return The reason for the action that was performed.
   *
   * @since 0.1.2.0
   */
  @Override
  public String reason() {
    return reason;
  }
}
