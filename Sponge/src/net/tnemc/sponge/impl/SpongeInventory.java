package net.tnemc.sponge.impl;
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
import net.tnemc.core.menu.Menu;
import net.tnemc.item.AbstractItemStack;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.UUID;

/**
 * SpongeInventory
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeInventory implements InventoryProvider<Inventory> {

  private final UUID id;

  public SpongeInventory(UUID id) {
    this.id = id;
  }

  /**
   * The player associated with this inventory provider.
   *
   * @return The {@link UUID} for the player for this {@link InventoryProvider}
   */
  @Override
  public UUID player() {
    return null;
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
    return null;
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
    return null;
  }

  /**
   * Used to open the provided inventory for this player.
   *
   * @param inventory The inventory to open.
   */
  @Override
  public void openInventory(Inventory inventory) {

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
