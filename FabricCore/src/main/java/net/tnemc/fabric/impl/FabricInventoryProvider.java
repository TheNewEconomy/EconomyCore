package net.tnemc.fabric.impl;
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

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.tnemc.core.compatibility.InventoryProvider;
import net.tnemc.fabric.FabricCore;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.menu.core.Menu;
import net.tnemc.menu.core.compatibility.MenuPlayer;

import java.util.UUID;

/**
 * FabricInventoryProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class FabricInventoryProvider implements InventoryProvider<Inventory> {

  private final UUID player;

  public FabricInventoryProvider(UUID player) {
    this.player = player;
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

    final ServerPlayerEntity player = FabricCore.mcSERVER().getPlayerManager().getPlayer(player());
    if(player != null) {
      if(ender) {
        return player.getEnderChestInventory();
      }
      return player.getInventory();
    }
    return null;
  }

  @Override
  public UUID player() {
    return player;
  }

  @Override
  public Inventory build(MenuPlayer menuPlayer, Menu menu, int i) {
    return null;
  }

  @Override
  public void openInventory(Inventory inventory) {

    final ServerPlayerEntity player = FabricCore.mcSERVER().getPlayerManager().getPlayer(player());
    if(player != null) {
      //open inventory
    }
  }

  @Override
  public void updateInventory(int i, AbstractItemStack<?> abstractItemStack) {

    final ServerPlayerEntity player = FabricCore.mcSERVER().getPlayerManager().getPlayer(player());
    if(player != null) {
      player.getInventory().setStack(i, (ItemStack)abstractItemStack.locale());
    }
  }

  @Override
  public void close() {

  }
}