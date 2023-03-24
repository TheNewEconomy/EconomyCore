package net.tnemc.bukkit.listeners;
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

import net.tnemc.menu.bukkit.BukkitPlayer;
import net.tnemc.menu.core.MenuManager;
import net.tnemc.menu.core.utils.CloseType;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * PlayerCloseInventoryEvent
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerCloseInventoryEvent implements Listener {

  private final JavaPlugin plugin;

  public PlayerCloseInventoryEvent(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onClose(final InventoryCloseEvent event) {

    final BukkitPlayer player = new BukkitPlayer((OfflinePlayer)event.getPlayer(), plugin);

    if(MenuManager.instance().inMenu(player.identifier())) {

      final CloseType type = ((MenuManager.instance().getViewer(player.identifier()).get().isPaused() ||
          MenuManager.instance().getViewer(player.identifier()).get().isSwitching())? CloseType.TEMPORARY : CloseType.CLOSE);

      MenuManager.instance().onClose(player, type);

      MenuManager.instance().switchViewer(player.identifier(), false);

    }
  }
}
