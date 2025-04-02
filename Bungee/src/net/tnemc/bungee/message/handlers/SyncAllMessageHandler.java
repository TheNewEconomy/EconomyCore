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

import net.tnemc.bungee.BungeeCore;
import net.tnemc.bungee.message.MessageManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * SyncAllMessageHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SyncAllMessageHandler extends AccountHandler {

  public SyncAllMessageHandler() {

    super("sync");
  }

  @Override
  public void handle(final String player, final String accountName, final UUID server, final DataInputStream in) {

      try {
          final String serverAddress = in.readUTF();
          final int serverPort = in.readInt();

        MessageManager.instance().backlog(serverAddress);
        MessageManager.instance().backlog(String.valueOf(serverPort));
        MessageManager.instance().backlog(serverAddress + ":" + serverPort);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}