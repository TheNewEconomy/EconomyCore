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
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.Location;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.menu.minestom.MinestomPlayer;

import java.util.Optional;
import java.util.UUID;

/**
 * MinestomPlayerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MinestomPlayerProvider extends MinestomPlayer implements PlayerProvider {

  public MinestomPlayerProvider(Player player) {
    super(player);
  }

  /**
   * Used to get the name of this player.
   *
   * @return The name of this player.
   */
  public String getName() {
    return player.getName().toString();
  }

  /**
   * Used to get the location of this player.
   *
   * @return The location of this player.
   */
  public Optional<Location> getLocation() {
    if(player== null) {
      return Optional.empty();
    }

    final Pos locale = player.getPosition();

    return Optional.of(new Location(player.getDimensionType().getName().namespace(),
                                    locale.x(), locale.y(), locale.z()));
  }

  /**
   * Used to get the name of the world this player is currently in.
   *
   * @return The name of the world.
   */
  @Override
  public String world() {
    String world = TNECore.eco().region().defaultRegion();

    if(player!= null) {
      world = player.getDimensionType().getName().namespace();
    }
    return world;
  }

  /**
   * Used to get the name of the biome this player is currently in.
   *
   * @return The name of the biome.
   */
  @Override
  public String biome() {

    //It appears Minestom doesn't provide a way to get biomes.
    return world();
  }

  /**
   * Used to get the amount of experience this player has.
   *
   * @return The amount of levels this player has.
   */
  public int getExp() {
    if(player == null) {
      return 0;
    }
    return (int)player.getExp();
  }

  /**
   * Used to set the amount of experience this player has.
   *
   * @param exp The amount of experience to set for this player.
   */
  public void setExp(int exp) {
    if(player != null) {
      player.setExp(exp);
    }
  }

  /**
   * Used to get the amount of experience levels this player has.
   *
   * @return The amount of experience levels this player has.
   */
  public int getExpLevel() {
    if(player == null) {
      return 0;
    }
    return player.getLevel();
  }

  /**
   * Used to set the amount of experience levels this player has.
   *
   * @param level The amount of experience levels to set for this player.
   */
  public void setExpLevel(int level) {
    if(player != null) {
      player.setLevel(level);
    }
  }

  public UUID identifier() {
    return player.getUuid();
  }

  public MinestomInventoryProvider inventory() {
    return new MinestomInventoryProvider(identifier());
  }

  /**
   * Used to determine if this player has the specified permission node.
   *
   * @param permission The node to check for.
   *
   * @return True if the player has the permission, otherwise false.
   */
  public boolean hasPermission(String permission) {
    return player.hasPermission(permission);
  }

  public void message(String message) {

  }

  /**
   * Used to send a message to this command source.
   *
   * @param data The message data to utilize for this translation.
   */
  public void message(MessageData data) {

  }

  public static Optional<PlayerProvider> find(final String identifier) {
    Player player;
    try {
      player = MinecraftServer.getConnectionManager().getPlayer(UUID.fromString(identifier));
    } catch (Exception ignore) {
      player = MinecraftServer.getConnectionManager().getPlayer(identifier);
    }

    if(player == null) {
      return Optional.empty();
    }
    return Optional.of(new MinestomPlayerProvider(player));
  }
}
