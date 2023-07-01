package net.tnemc.bungee.message;
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

import com.google.common.io.ByteArrayDataOutput;
import net.tnemc.bungee.BungeeCore;

import java.io.DataInputStream;
import java.util.UUID;

/**
 * MessageHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public abstract class MessageHandler {

  private String tag;

  public MessageHandler(String tag) {
    this.tag = tag;
  }

  public static void sendToAll(final String channel, ByteArrayDataOutput out) {
    BungeeCore.instance().getProxy().getServers().values().forEach(server->{
      if(server.getPlayers().size() > 0) {
        server.sendData(channel, out.toByteArray(), false);
      }
    });
  }

  public abstract void handle(UUID player, UUID server, DataInputStream stream);
}