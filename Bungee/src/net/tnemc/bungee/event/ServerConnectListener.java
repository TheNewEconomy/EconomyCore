package net.tnemc.bungee.event;
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

import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.tnemc.bungee.message.MessageManager;
import net.tnemc.bungee.message.backlog.ConfigEntry;

/**
 * ServerConnectListener
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ServerConnectListener implements Listener {

  @EventHandler
  public void onMessage(ServerConnectEvent event) {

    if(event.getPlayer().getServer().getInfo().getPlayers().size() >= 1) {

      final String address = event.getPlayer().getServer().getInfo().getSocketAddress().toString();
      MessageManager.instance().backlog(address);

      for(ConfigEntry entry : MessageManager.instance().getHubs().values()) {
        if(!entry.getSynced().contains(address)) {

          event.getTarget().sendData("tne:config", entry.getBytes(), false);
          entry.getSynced().add(address);
        }
      }
    }
  }
}
