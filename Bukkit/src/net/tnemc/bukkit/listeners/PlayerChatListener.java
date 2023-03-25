package net.tnemc.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * The New Menu Library
 *
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
public class PlayerChatListener implements Listener {

  private final JavaPlugin plugin;

  public PlayerChatListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChat(final AsyncPlayerChatEvent event) {


    /*final BukkitPlayerProvider player = new BukkitPlayerProvider(event.getPlayer());
    if(MenuManager.instance().inMenu(player.identifier())) {
      event.setCancelled(true);

      Optional<ViewerData> viewer = MenuManager.instance().getViewer(player.identifier());
      if(viewer.isPresent() && viewer.get().awaitingChat()
          && viewer.get().getChatCallback().test(new PlayerChatCallback(player, event.getMessage()))) {
        MenuManager.instance().resumeViewer(player.identifier());
        player.inventory().openMenu(player, viewer.get().getMenu(), viewer.get().getPage());
        /*Bukkit.getScheduler().runTask(plugin, ()->{
          player.inventory().openMenu(player, viewer.get().getMenu(), 4);
        });
      }
    }*/
  }
}