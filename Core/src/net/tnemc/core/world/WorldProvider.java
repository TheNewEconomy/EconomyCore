package net.tnemc.core.world;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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
    return resolveWorld(player.getWorld());
  }
}