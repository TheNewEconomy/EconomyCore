package net.tnemc.sponge.listeners.player;
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

import net.tnemc.core.handlers.player.PlayerJoinHandler;
import net.tnemc.core.utils.HandlerResponse;
import net.tnemc.sponge.impl.SpongePlayerProvider;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * PlayerCloseInventoryListener
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerCloseInventoryListener {

  private final Plugin plugin;

  public PlayerCloseInventoryListener(Plugin plugin) {
    this.plugin = plugin;
  }

  @Listener
  public void onClose(InteractInventoryEvent.Close event, @First Player player) {

    if(isEnderChest(event.getTargetInventory())) {
      final HandlerResponse handle = new PlayerJoinHandler().handle(new SpongePlayerProvider(player, plugin));
    }
  }

  public boolean isEnderChest(Container container) {
    if (container instanceof TileEntityCarrier) {
      Location<World> location = ((TileEntityCarrier)container).getLocation();
      if (location.getBlockType() == BlockTypes.ENDER_CHEST) {
        return true;
      }
    }
    return false;
  }
}
