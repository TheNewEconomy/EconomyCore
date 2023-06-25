package net.tnemc.bukkit.listeners.player;
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

import net.tnemc.bukkit.impl.BukkitPlayerProvider;
import net.tnemc.core.TNECore;
import net.tnemc.core.handlers.player.PlayerInteractHandler;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.SerialItem;
import net.tnemc.item.bukkit.BukkitItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * PlayerInteractListener
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerInteractListener implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInteract(final PlayerInteractEvent event) {
    final BukkitPlayerProvider provider = new BukkitPlayerProvider(event.getPlayer());

    final ItemStack stack = event.getItem();

    if(event.getAction().equals(Action.RIGHT_CLICK_AIR) ||
    event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

      new PlayerInteractHandler().handle(provider, BukkitItemStack.locale(stack));
    }
  }
}