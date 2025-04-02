package net.tnemc.velocity;

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

import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.tnemc.bungee.ProxyProvider;
import net.tnemc.bungee.message.MessageManager;
import net.tnemc.bungee.message.backlog.BacklogEntry;
import net.tnemc.bungee.message.backlog.MessageData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * VelocityProxy
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class VelocityProxy implements ProxyProvider {

  /**
   * Used to send data to every server that is not this server.
   *
   * @param channel The channel to use for sending the data.
   * @param out     The data to send.
   */
  @Override
  public void sendToAll(final String channel, final byte[] out, final boolean backlog) {

    VelocityCore.instance().getServer().getAllServers().forEach(server->{
      if(!server.getPlayersConnected().isEmpty()) {
        server.sendPluginMessage(MinecraftChannelIdentifier.from(channel), out);
      } else {

        if(backlog) {
          MessageManager.instance().addData(String.valueOf(server.getServerInfo().getAddress().getPort()), new BacklogEntry(channel, out));
        }
      }
    });
  }

  /**
   * Used to send data to a specific server.
   *
   * @param serverName The server name.
   * @param channel    The channel to use for sending the data.
   * @param out        The data to send.
   */
  @Override
  public void sendTo(final String serverName, final String channel, final byte[] out) {

    final Optional<RegisteredServer> server = VelocityCore.instance().getServer().getServer(serverName);
    server.ifPresent(registeredServer->registeredServer.sendPluginMessage(MinecraftChannelIdentifier.from(channel), out));
  }

  /**
   * Used to send any backlog data to a server.
   *
   * @param data The {@link MessageData} to use for determining the server, and backlog to send.
   */
  @Override
  public void sendBacklog(@NotNull final MessageData data) {

    for(final RegisteredServer server : VelocityCore.instance().getServer().getAllServers()) {
      if(server.getServerInfo().getAddress().getPort() == Integer.valueOf(data.getServerName())) {
        for(final BacklogEntry entry : data.getBacklog()) {
          server.sendPluginMessage(MinecraftChannelIdentifier.from(entry.channel()), entry.out());
        }
      }
    }
  }
}
