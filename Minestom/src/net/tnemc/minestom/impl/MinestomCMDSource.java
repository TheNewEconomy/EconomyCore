package net.tnemc.minestom.impl;
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

import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.io.message.MessageData;

import java.util.Optional;
import java.util.UUID;

/**
 * MinestomCMDSource
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MinestomCMDSource implements CmdSource {

  private final MinestomPlayerProvider provider;
  private final CommandSender sender;
  private final UUID identifier;

  public MinestomCMDSource(CommandSender sender) {
    this.sender = sender;

    if(sender instanceof Player) {
      provider = new MinestomPlayerProvider((Player)sender);
      identifier = provider.identifier();
    } else {
      provider = null;
      //TODO: Server account.
      identifier = UUID.randomUUID();
    }
  }

  /**
   * The UUID of this command source.
   *
   * @return The UUID of this command source.
   */
  public UUID identifier() {
    return identifier;
  }

  /**
   * The name of this command source.
   *
   * @return The name of this command source.
   */
  public String name() {
    return "";
  }

  /**
   * Used to get the related {@link PlayerProvider} for this command source.
   *
   * @return An optional containing the related {@link PlayerProvider} if this command source is a
   * player, otherwise an empty {@link Optional}.
   */
  public Optional<PlayerProvider> player() {
    return Optional.of(provider);
  }

  /**
   * Used to send a message to this command source.
   *
   * @param messageData The message data to utilize for this translation.
   */
  public void message(MessageData messageData) {

  }
}