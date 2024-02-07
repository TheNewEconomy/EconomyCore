package net.tnemc.bungee.message.handlers;
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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.tnemc.bungee.message.MessageHandler;
import net.tnemc.bungee.message.MessageManager;
import net.tnemc.bungee.message.backlog.ConfigEntry;

import java.io.DataInputStream;
import java.util.UUID;

/**
 * ConfigMessageHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ConfigMessageHandler extends MessageHandler {
  public ConfigMessageHandler() {
    super("config");
  }

  @Override
  public void handle(UUID server, DataInputStream stream) {

    final ConfigEntry entry = ConfigEntry.fromBytes(stream);
    if(entry != null) {
      MessageManager.instance().getHubs().put(entry.getPin(), entry);
      //send(server, entry.getBytes());
    }

  }

  public static void send(UUID server, byte[] left) {
    final ByteArrayDataOutput out = ByteStreams.newDataOutput();


    out.writeUTF(server.toString());
    out.write(left);


    sendToAll("tne:config", out);
  }
}