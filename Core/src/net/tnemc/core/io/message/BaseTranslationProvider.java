package net.tnemc.core.io.message;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.TranslationProvider;

import java.util.Map;
import java.util.Optional;
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

    Optional<Account> account = TNECore.eco().account().findAccount(identifier);

    if(account.isPresent() && account.get().isPlayer()) {
      return ((PlayerAccount)account.get()).getLanguage();
    }
    return "default";
  }

  /**
   * Used to translate a node for the given language. This should resort to the default if the
   * specified language doesn't exist.
   *
   * @param messageData The message data to utilize for this translation.
   * @param language The language to translate the node to.
   *
   * @return The translated message represented by the node, or the default for if the node doesn't
   * exist.
   */
  @Override
  public String translateNode(final MessageData messageData, String language) {
    String string = TNECore.core().message().getString(messageData.getNode(), language);

    for(Map.Entry<String, String> replacement : messageData.getReplacements().entrySet()) {
      TNECore.log().debug("Replace: " + replacement.getKey() + ":" + replacement.getValue(), DebugLevel.DEVELOPER);
      string = string.replace(replacement.getKey(), replacement.getValue());
    }
    return string;
  }
}