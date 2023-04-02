package net.tnemc.minestom.impl;
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

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.AbstractInventory;
import net.tnemc.core.compatibility.InventoryProvider;
import net.tnemc.menu.minestom.MinestomInventory;

import java.util.UUID;

/**
 * MinestomInventoryProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MinestomInventoryProvider extends MinestomInventory implements InventoryProvider<AbstractInventory> {
  public MinestomInventoryProvider(UUID id) {
    super(id);
  }

  /**
   * Used to get an inventory object.
   *
   * @param ender True if the ender chest object should be returned, otherwise false.
   *
   * @return The inventory object.
   */
  @Override
  public AbstractInventory getInventory(boolean ender) {
    final Player player = MinecraftServer.getConnectionManager().getPlayer(player());

    if(player == null) return null;

    if(ender) {
      //TODO: Ender chest?
    }
    return player.getInventory();
  }
}