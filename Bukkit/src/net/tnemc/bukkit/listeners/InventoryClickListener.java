package net.tnemc.bukkit.listeners;
/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.bukkit.TNE;
import net.tnemc.bukkit.impl.BukkitPlayerProvider;
import net.tnemc.core.TNECore;
import net.tnemc.core.handlers.InventoryClickHandler;
import net.tnemc.core.menu.icon.ActionType;
import net.tnemc.core.menu.viewer.ViewerData;
import net.tnemc.core.utils.HandlerResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

/**
 * InventoryClickListener
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class InventoryClickListener implements Listener {

  private final TNE plugin;

  public InventoryClickListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onClick(final InventoryClickEvent event) {
    BukkitPlayerProvider provider = new BukkitPlayerProvider((OfflinePlayer)event.getWhoClicked());

    final Optional<ViewerData> data = TNECore.menu().getData(provider.getUUID());
    if(provider.inventory().inMenu() && data.isPresent()) {

      final HandlerResponse handle = new InventoryClickHandler().handle(data.get().getMenu(),
                                                                        convertClick(event.getClick()),
                                                                        provider,
                                                                        data.get().getPage(),
                                                                        event.getSlot());



      if(handle.isCancelled()) {
        event.setCancelled(true);
      }
    }
  }

  private ActionType convertClick(ClickType click) {
    switch(click) {
      case SHIFT_LEFT:
        return ActionType.LEFT_SHIFT;
      case RIGHT:
        return ActionType.RIGHT_CLICK;
      case SHIFT_RIGHT:
        return ActionType.RIGHT_SHIFT;
      case MIDDLE:
        return ActionType.SCROLL_CLICK;
      case DOUBLE_CLICK:
        return ActionType.DOUBLE_CLICK;
      case DROP:
        return ActionType.DROP;
      case CONTROL_DROP:
        return ActionType.DROP_CTRL;
      case SWAP_OFFHAND:
        return ActionType.OFFHAND_SWAP;
      case NUMBER_KEY:
        return ActionType.NUMBER;
      default:
        return ActionType.LEFT_CLICK;
    }
  }
}