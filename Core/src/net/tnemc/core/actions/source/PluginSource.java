package net.tnemc.core.actions.source;

/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import net.tnemc.core.actions.ActionSource;

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
  private final String plugin;

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
   * Used to get the name of the source of the action.
   *
   * @return The name of the source for a specific action. This could be user-friendly or not. This
   * should be the name of the implementation that has caused this action to occur. For instance, a
   * plugin name.
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
   * Used to get the type of action source.
   *
   * @return The name of the type of action source.
   */
  @Override
  public String type() {

    return "plugin";
  }
}
