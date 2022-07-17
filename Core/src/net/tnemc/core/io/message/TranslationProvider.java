package net.tnemc.core.io.message;


/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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


import java.util.UUID;

/**
 * Represents a class that provides translation services.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface TranslationProvider {

  /**
   * Used to get the language of a player with the associated identifier.
   * @param identifier The identifier of the player.
   * @return The language that should be used for this player.
   */
  String getLang(final UUID identifier);

  /**
   * Used to translate a node for the given language. This should resort to the default if the
   * specified language doesn't exist.
   *
   * @param node The node to translate.
   * @param language The language to translate the node to.
   * @return The translated message represented by the node, or the default for if the node doesn't
   * exist.
   */
  String translateNode(final String node, final String language);

  /**
   * Used to translate a node for the given language for the given player. This should resort to the
   * default if the specified language doesn't exist.
   * @param identifier The identifier of the given player.
   * @param node The node to translate.
   * @return The translated message represented by the node, or the default for if the node doesn't
   * exist.
   */
  default String translate(final UUID identifier, final String node) {
    return translateNode(node, getLang(identifier));
  }
}