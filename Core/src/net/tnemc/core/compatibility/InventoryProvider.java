package net.tnemc.core.compatibility;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.menu.Menu;
import net.tnemc.item.AbstractItemStack;

import java.util.UUID;

/**
 * A class that acts as a bridge between various inventory objects on different server software providers.
 *
 * @param <INV> Represents the platform's Inventory object.
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface InventoryProvider<INV> {

  /**
   * The player associated with this inventory provider.
   * @return The {@link UUID} for the player for this {@link InventoryProvider}
   */
  UUID player();

  /**
   * Builds an inventory object from a menu.
   * @return The built inventory.
   */
  default INV build(final Menu menu) {
    return build(menu, 1);
  }

  /**
   * Builds an inventory object from a menu.
   * @return The built inventory.
   */
  INV build(final Menu menu, int page);

  /**
   * Used to get an inventory object.
   *
   * @param ender True if the ender chest object should be returned, otherwise false.
   * @return The inventory object.
   */
  INV getInventory(boolean ender);

  /**
   * Used to open the provided inventory for this player.
   *
   * @param inventory The inventory to open.
   */
  void openInventory(final INV inventory);

  /**
   * Used to determine if this player is inside of a {@link Menu}
   *
   * @return True if this player is inside the specified menu, otherwise false.
   */
  default boolean inMenu() {
    return TNECore.menu().inMenu(player());
  }

  /**
   * Used to open the provided menu for this player.
   * @param menu The menu to open.
   */
  default void openMenu(final Menu menu) {
    openMenu(menu, 1);
  }

  /**
   * Used to open the provided menu for this player on the specified page.
   * @param menu The menu to open.
   * @param page The page to open.
   */
  default void openMenu(final Menu menu, final int page) {

    openInventory(build(menu, page));
    TNECore.menu().updateViewer(player(), menu.getName(), page);
  }

  /**
   * Used to open the provided menu for this player.
   * @param menu The menu to open.
   */
  default void openMenu(final String menu) {
    openMenu(menu, 1);
  }

  /**
   * Used to open the provided menu for this player on the specified page.
   * @param menu The menu to open.
   * @param page The page to open.
   */
  default void openMenu(final String menu, final int page) {

    if(TNECore.menu().menus.containsKey(menu)) {

      openInventory(build(TNECore.menu().menus.get(menu), page));
      TNECore.menu().updateViewer(player(), menu, page);
    }
  }

  /**
   * Used to update the menu the player is in with a new item for a specific slot.
   *
   * @param slot The slot to update.
   * @param item The item to update the specified slot with.
   */
  void updateMenu(int slot, AbstractItemStack<?> item);
}