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
import org.bukkit.inventory.Inventory;

/**
 * BukkitInventoryProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BukkitInventoryProvider implements InventoryProvider<Inventory> {


  @Override
  public PlayerProvider player() {
    return null;
  }

  @Override
  public Inventory build(Menu menu) {
    return null;
  }

  @Override
  public Inventory getInventory(boolean ender) {
    return null;
  }
}
