package net.tnemc.core.compatibility;

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

import net.tnemc.core.io.message.MessageData;
import net.tnemc.menu.core.compatibility.MenuPlayer;

import java.util.Optional;

/**
 * A class that acts as a bridge between various player objects on different server software providers.
 *
 * @since 0.1.2.0
 * @author creatorfromhell
 */
public interface PlayerProvider extends MenuPlayer {

  /**
   * Used to get the name of this player.
   * @return The name of this player.
   */
  String getName();

  /**
   * Used to get the location of this player.
   * @return The location of this player.
   */
  Optional<Location> getLocation();

  /**
   * Used to get the name of the world this player is currently in.
   *
   * @return The name of the world.
   */
  String world();

  /**
   * Used to get the name of the biome this player is currently in.
   *
   * @return The name of the biome.
   */
  String biome();

  /**
   * Used to get the amount of experience this player has.
   *
   * @return The amount of levels this player has.
   */
  int getExp();

  /**
   * Used to set the amount of experience this player has.
   *
   * @param exp The amount of experience to set for this player.
   */
  void setExp(int exp);

  /**
   * Used to get the amount of experience levels this player has.
   *
   * @return The amount of experience levels this player has.
   */
  int getExpLevel();

  /**
   * Used to set the amount of experience levels this player has.
   *
   * @param level The amount of experience levels to set for this player.
   */
  void setExpLevel(int level);

  InventoryProvider<?> inventory();

  /**
   * Used to determine if this player has the specified permission node.
   *
   * @param permission The node to check for.
   * @return True if the player has the permission, otherwise false.
   */
  boolean hasPermission(String permission);

  /**
   * Used to send a message to this command source.
   * @param messageData The message data to utilize for this translation.
   */
  void message(final MessageData messageData);
}