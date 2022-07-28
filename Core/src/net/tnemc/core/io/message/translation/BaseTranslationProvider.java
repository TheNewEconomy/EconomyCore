package net.tnemc.core.io.message.translation;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.io.message.TranslationProvider;

import java.util.UUID;

/**
 * BaseTranslationProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BaseTranslationProvider implements TranslationProvider {
  /**
   * Used to get the language of a player with the associated identifier.
   *
   * @param identifier The identifier of the player.
   *
   * @return The language that should be used for this player.
   */
  @Override
  public String getLang(UUID identifier) {
    return "default";
  }

  /**
   * Used to translate a node for the given language. This should resort to the default if the
   * specified language doesn't exist.
   *
   * @param node     The node to translate.
   * @param language The language to translate the node to.
   *
   * @return The translated message represented by the node, or the default for if the node doesn't
   * exist.
   */
  @Override
  public String translateNode(String node, String language) {
    return TNECore.instance().getMessage().getString(node, language);
  }
}
