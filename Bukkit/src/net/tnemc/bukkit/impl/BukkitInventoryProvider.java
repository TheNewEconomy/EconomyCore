package net.tnemc.bukkit.impl;
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

import net.tnemc.core.compatibility.InventoryProvider;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icon.Icon;
import net.tnemc.item.AbstractItemStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * BukkitInventoryProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BukkitInventoryProvider implements InventoryProvider<Inventory> {

  final UUID id;

  public BukkitInventoryProvider(UUID id) {
    this.id = id;
  }

  /**
   * The player associated with this inventory provider.
   *
   * @return The {@link UUID} for the player for this {@link InventoryProvider}
   */
  @Override
  public UUID player() {
    return id;
  }

  /**
   * Builds an inventory object from a menu.
   *
   * @param menu
   * @param page
   *
   * @return The built inventory.
   */
  @Override
  public Inventory build(Menu menu, int page) {
    Inventory inventory = Bukkit.createInventory(null, menu.getSize(), menu.getTitle());

    for(Map.Entry<Integer, Icon> entry : menu.getPages().get(page).getIcons().entrySet()) {

      inventory.setItem(entry.getKey(), (ItemStack)entry.getValue().getItem().locale());
    }

    return inventory;
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

  /**
   * Used to open the provided inventory for this player.
   *
   * @param inventory The inventory to open.
   */
  @Override
  public void openInventory(Inventory inventory) {
    final OfflinePlayer player = Bukkit.getOfflinePlayer(player());
    if(player.getPlayer() != null) {
      player.getPlayer().openInventory(inventory);
    }
  }

  /**
   * Used to update the menu the player is in with a new item for a specific slot.
   *
   * @param slot The slot to update.
   * @param item The item to update the specified slot with.
   */
  @Override
  public void updateMenu(int slot, AbstractItemStack<?> item) {

  }
}
