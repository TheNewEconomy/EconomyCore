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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * MessageMessageHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MessageMessageHandler extends MessageHandler {

  public MessageMessageHandler() {

    super("message");
  }

  public static void send(final UUID server, final String identifier, final String message) {

    final ByteArrayDataOutput out = ByteStreams.newDataOutput();


    out.writeUTF(server.toString());
    out.writeUTF(identifier);
    out.writeUTF(message);


    sendToAll("tne:message", out, false);
  }

  @Override
  public void handle(final UUID server, final DataInputStream stream) {

    try {
      final String identifier = stream.readUTF();
      final String message = stream.readUTF();

      send(server, identifier, message);
    } catch(final IOException e) {
      e.printStackTrace();
    }
  }
}