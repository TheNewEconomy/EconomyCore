package net.tnemc.bukkit.impl;

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

import net.tnemc.bukkit.TNE;
import net.tnemc.core.compatibility.InventoryProvider;
import net.tnemc.menu.bukkit.BukkitInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * BukkitInventoryProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BukkitInventoryProvider extends BukkitInventory implements InventoryProvider<Inventory> {

  public BukkitInventoryProvider(UUID id, JavaPlugin plugin) {
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
