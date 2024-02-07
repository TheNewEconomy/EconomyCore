package net.tnemc.folia.impl;

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

import net.tnemc.menu.folia.FoliaInventory;
import net.tnemc.plugincore.core.compatibility.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * FoliaInventoryProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class FoliaInventoryProvider extends FoliaInventory implements InventoryProvider<Inventory> {

  public FoliaInventoryProvider(UUID id, JavaPlugin plugin) {
    super(id, plugin);
  }

  /**
   * Used to get an inventory object.
   *
   * @param ender True if the ender chest object should be returned, otherwise false.
   *
   * @return The inventory object.
   */
  @Override
  public Inventory getInventory(boolean ender) {
    final OfflinePlayer player = Bukkit.getOfflinePlayer(player());
    if(player.getPlayer() == null) return null;

    if(ender) {
      return player.getPlayer().getEnderChest();
    }
    return player.getPlayer().getInventory();
  }
}
