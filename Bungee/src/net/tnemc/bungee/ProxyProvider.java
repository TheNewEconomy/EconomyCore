package net.tnemc.bungee;

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

import net.tnemc.bungee.message.backlog.MessageData;
import org.jetbrains.annotations.NotNull;

/**
 * ProxyProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface ProxyProvider {

  /**
   * Used to send data to every server that is not this server.
   * @param channel The channel to use for sending the data.
   * @param out The data to send.
   */
  void sendToAll(final String channel, byte[] out, boolean backlog);

  /**
   * Used to send data to a specific server.
   * @param serverName The server name.
   * @param channel The channel to use for sending the data.
   * @param out The data to send.
   */
  void sendTo(final String serverName, final String channel, byte[] out);

  /**
   * Used to send any backlog data to a server.
   * @param data The {@link MessageData} to use for determining the server, and backlog to send.
   */
  void sendBacklog(@NotNull final MessageData data);
}