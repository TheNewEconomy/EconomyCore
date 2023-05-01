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

import net.tnemc.menu.bukkit.BukkitPlayer;
import net.tnemc.menu.core.MenuManager;
import net.tnemc.menu.core.compatibility.InventoryClickHandler;
import net.tnemc.menu.core.icon.ActionType;
import net.tnemc.menu.core.viewer.ViewerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * PlayerClickListener
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerClickListener implements Listener {

  private final JavaPlugin plugin;

  public PlayerClickListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onClick(final InventoryClickEvent event) {
    final BukkitPlayer player = new BukkitPlayer((OfflinePlayer)event.getWhoClicked(), plugin);

    final Optional<ViewerData> data = MenuManager.instance().getViewer(player.identifier());

    if(player.inventory().inMenu() && data.isPresent()) {

      final boolean cancel = new InventoryClickHandler().handle(convertClick(event.getClick()),
                                                                player, event.getSlot()
      );

      if(cancel) {
        event.setCancelled(true);
      }
    }
  }

  private ActionType convertClick(ClickType click) {
    return switch(click) {
      case SHIFT_LEFT -> ActionType.LEFT_SHIFT;
      case RIGHT -> ActionType.RIGHT_CLICK;
      case SHIFT_RIGHT -> ActionType.RIGHT_SHIFT;
      case MIDDLE -> ActionType.SCROLL_CLICK;
      case DOUBLE_CLICK -> ActionType.DOUBLE_CLICK;
      case DROP -> ActionType.DROP;
      case CONTROL_DROP -> ActionType.DROP_CTRL;
      //case ClickType. -> ActionType.OFFHAND_SWAP;
      case NUMBER_KEY -> ActionType.NUMBER;
      default -> ActionType.LEFT_CLICK;
    };
  }
}