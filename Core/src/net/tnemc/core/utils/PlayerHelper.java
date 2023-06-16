package net.tnemc.core.utils;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.menu.core.compatibility.MenuPlayer;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * PlayerHelper - Utilities relating to all things player-related.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerHelper {

  /**
   * Returns the {@link Pattern pattern} utilized to determine if a string is a valid
   * player username.
   *
   * @return The {@link Pattern pattern} to use for determining if a string is a valid
   * player username.
   *
   * @see Pattern
   */
  public static Pattern playerMatcher() {
    return Pattern.compile("^\\w*$");
  }

  public static void message(MenuPlayer player, final MessageData data) {

    final Optional<PlayerProvider> provider = TNECore.server().findPlayer(player.identifier());
    provider.ifPresent(playerProvider->playerProvider.message(data));
  }
}