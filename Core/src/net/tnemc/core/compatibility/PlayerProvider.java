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

import java.util.Optional;
import java.util.UUID;

/**
 * A class that acts as a bridge between various player objects on different server software providers.
 *
 * @since 0.1.2.0
 * @author creatorfromhell
 */
public interface PlayerProvider {

  /**
   * Used to get the {@link UUID} of this player.
   * @return The {@link UUID} of this player.
   */
  UUID getUUID();

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
   * Used to get the name of the region this player is in. This could be the world itself, or maybe
   * a third-party related region such as world guard.
   *
   * @param resolve Whether the returned region should be resolved to using the {@link net.tnemc.core.region.RegionProvider}.
   *
   * @return The name of the region.
   */
  String getRegion(final boolean resolve);

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