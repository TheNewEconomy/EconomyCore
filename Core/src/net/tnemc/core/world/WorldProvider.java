package net.tnemc.core.world;

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

import net.tnemc.core.compatibility.PlayerProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class that provides everything related to worlds.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class WorldProvider {

  private ConcurrentHashMap<String, WorldGroup> groups = new ConcurrentHashMap<>();

  /**
   * Used to add a world group to our existing map of groups.
   * @param name The name of the group
   * @param group The group instance.
   */
  public void addGroup(final String name, final WorldGroup group) {
    groups.put(name, group);
  }

  /**
   * Used to find a world group based on its name.
   * @param name The name of the group to find.
   * @return An optional containing the {@link WorldGroup} if it exists, otherwise an empty optional.
   */
  public Optional<WorldGroup> retrieve(final String name) {
    return Optional.ofNullable(groups.get(name));
  }

  /**
   * Used to resolve the world name to its connection or itself if no connection exists.
   *
   * @param world The world to get the true name, with connection if possible.
   *
   * @return The world name if no connection, otherwise the name of the connection.
   */
  @NotNull
  public String resolveWorld(String world) {
    for(WorldGroup group : groups.values()) {
      if(group.has(world)) {
        return group.name();
      }
    }
    return world;
  }

  /**
   * Used to resolve the world name to its connection or itself if no connection exists based on a
   * player.
   *
   * @param player The player to use for getting the world connection.
   *
   * @return The world name if no connection, otherwise the name of the connection.
   */
  @NotNull
  public String resolveWorld(PlayerProvider player) {
    return resolveWorld(player.getRegion());
  }
}