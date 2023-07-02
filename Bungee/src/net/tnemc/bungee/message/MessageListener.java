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

import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.tnemc.bungee.message.handlers.BalanceMessageHandler;
import net.tnemc.bungee.message.handlers.SyncAllMessageHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all Plugin Channel messages.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MessageListener implements Listener {

  private final Map<String, MessageHandler> handlers = new HashMap<>();

  public MessageListener() {
    handlers.put("balance", new BalanceMessageHandler());
    handlers.put("sync", new SyncAllMessageHandler());
  }


  @EventHandler
  public void onMessage(PluginMessageEvent event) {
    if(!event.getTag().startsWith("tne:")) {
      return;
    }

    if(!(event.getSender() instanceof Server)) {
      System.out.println("Event sender not server.");
      event.setCancelled(true);
      return;
    }

    final String tag = event.getTag().replace("tne:", "");

    if(handlers.containsKey(tag)) {
      try {
        ByteArrayInputStream stream = new ByteArrayInputStream(event.getData());
        DataInputStream in = new DataInputStream(stream);

        UUID server = UUID.fromString(in.readUTF());
        UUID player = UUID.fromString(in.readUTF());
        handlers.get(tag).handle(player, server, in);

        in.close();
        stream.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }
}