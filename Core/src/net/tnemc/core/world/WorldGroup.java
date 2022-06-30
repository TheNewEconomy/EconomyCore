package net.tnemc.core.world;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * WorldGroup
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class WorldGroup {

  private final List<String> worlds = new ArrayList<>();
  private final List<String> currencies = new ArrayList<>();

  private final String name;

  public WorldGroup(final String name) {
    this.name = name;
  }

  /**
   * @return The unique name tied to this WorldConnectionProvider.
   */
  @NotNull
  public String name() {
    return name;
  }

  /**
   * Used to add a world to this group.
   * @param world The name of the world to add.
   */
  public void addWorld(final String world) {
    worlds.add(world);
  }

  public boolean has(final String world) {
    return worlds.contains(world);
  }

  /**
   * Used to remove a world from this group.
   * @param world The name of the world to remove.
   */
  public void removeWorld(final String world) {
    worlds.remove(world);
  }
}