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

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.tnemc.core.compatibility.InventoryProvider;
import net.tnemc.core.compatibility.Location;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.io.message.MessageData;

import java.util.Optional;
import java.util.UUID;

/**
 * FabricPlayerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class FabricPlayerProvider implements PlayerProvider {

  private ServerPlayerEntity player;

  public FabricPlayerProvider(ServerPlayerEntity player) {
    this.player = player;
  }

  /**
   * Used to get the name of this player.
   *
   * @return The name of this player.
   */
  @Override
  public String getName() {
    return player.getName().toString();
  }

  /**
   * Used to get the location of this player.
   *
   * @return The location of this player.
   */
  @Override
  public Optional<Location> getLocation() {
    final BlockPos pos = player.getBlockPos();

    return Optional.of(new Location("", pos.getX(), pos.getY(), pos.getZ()));
  }

  /**
   * Used to get the name of the world this player is currently in.
   *
   * @return The name of the world.
   */
  @Override
  public String world() {
    return player.world.getServer();
  }

  /**
   * Used to get the name of the biome this player is currently in.
   *
   * @return The name of the biome.
   */
  @Override
  public String biome() {
    return player.world.getBiome(player.getBlockPos()).toString();
  }

  /**
   * Used to get the amount of experience this player has.
   *
   * @return The amount of levels this player has.
   */
  @Override
  public int getExp() {
    return 0;
  }

  /**
   * Used to set the amount of experience this player has.
   *
   * @param exp The amount of experience to set for this player.
   */
  @Override
  public void setExp(int exp) {

  }

  /**
   * Used to get the amount of experience levels this player has.
   *
   * @return The amount of experience levels this player has.
   */
  @Override
  public int getExpLevel() {
    return 0;
  }

  /**
   * Used to set the amount of experience levels this player has.
   *
   * @param level The amount of experience levels to set for this player.
   */
  @Override
  public void setExpLevel(int level) {

  }

  @Override
  public UUID identifier() {
    return null;
  }

  @Override
  public InventoryProvider<?> inventory() {
    return null;
  }

  /**
   * Used to determine if this player has the specified permission node.
   *
   * @param permission The node to check for.
   *
   * @return True if the player has the permission, otherwise false.
   */
  @Override
  public boolean hasPermission(String permission) {
    return false;
  }

  @Override
  public void message(String message) {

  }

  /**
   * Used to send a message to this command source.
   *
   * @param messageData The message data to utilize for this translation.
   */
  @Override
  public void message(MessageData messageData) {

  }
}