package net.tnemc.bukkit.listeners.inventory;
/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.handlers.inventory.ItemCraftHandler;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.bukkit.BukkitItemStack;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import net.tnemc.plugincore.core.utils.HandlerResponse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * ItemCraftEvent
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class ItemCraftListener implements Listener {

  @EventHandler
  public void onEvent(final CraftItemEvent event) {

    final PlayerProvider provider = PluginCore.server().initializePlayer(event.getWhoClicked());

    for(final ItemStack item : event.getInventory().getMatrix()) {

      if(item == null) {
        continue;
      }

      final AbstractItemStack<?> stack = new BukkitItemStack().of(item);
      final HandlerResponse handler = new ItemCraftHandler().handle(provider, stack);

      if(handler.isCancelled()) {
        event.setCancelled(true);
        break;
      }
    }
  }
}