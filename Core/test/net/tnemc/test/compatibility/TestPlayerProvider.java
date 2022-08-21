package net.tnemc.test.compatibility;

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

import net.tnemc.core.compatibility.Location;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.menu.Menu;
import net.tnemc.item.AbstractItemStack;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * TestPlayerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TestPlayerProvider implements PlayerProvider {

  final String name;

  public TestPlayerProvider(String name) {
    this.name = name;
  }

  /**
   * Used to get the {@link UUID} of this player.
   *
   * @return The {@link UUID} of this player.
   */
  @Override
  public UUID getUUID() {
    return UUID.nameUUIDFromBytes(getName().getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Used to get the name of this player.
   *
   * @return The name of this player.
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Used to get the location of this player.
   *
   * @return The location of this player.
   */
  @Override
  public Location getLocation() {
    return null;
  }

  /**
   * Used to get the name of the world this player is in.
   *
   * @return The name of the world.
   */
  @Override
  public String getRegion() {
    return "world";
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

  /**
   * Used to get an inventory object.
   *
   * @param ender True if the ender chest object should be returned, otherwise false.
   *
   * @return The inventory object.
   */
  @Override
  public Object getInventory(boolean ender) {
    return null;
  }

  /**
   * Used to determine if this player is inside of the specified {@link Menu}.
   *
   * @param name The name of the menu
   *
   * @return True if this player is inside the specified menu, otherwise false.
   */
  @Override
  public boolean inMenu(String name) {
    return false;
  }

  /**
   * Used to open the provided menu for this player.
   *
   * @param menu The menu to open.
   */
  @Override
  public void openMenu(Menu menu) {

  }

  /**
   * Used to open the provided menu for this player.
   *
   * @param menu The menu to open.
   */
  @Override
  public void openMenu(String menu) {

  }

  /**
   * Used to update the menu the player is in with a new item for a specific slot.
   *
   * @param slot
   * @param item
   */
  @Override
  public void updateMenu(int slot, AbstractItemStack<?> item) {

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
    return true;
  }
}
