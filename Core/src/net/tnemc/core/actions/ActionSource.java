package net.tnemc.core.actions;


/*
 * The New Economy
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
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
   * Used to get the type of action source.
   * @return The name of the type of action source.
   */
  String type();

  /**
   * Used to get a description of the reason for why the action was performed.
   *
   * @return The reason for the action that was performed.
   *
   * @since 0.1.2.0
   */
  String reason();
}