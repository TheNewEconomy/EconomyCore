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