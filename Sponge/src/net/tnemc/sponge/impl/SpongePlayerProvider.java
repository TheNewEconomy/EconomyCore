package net.tnemc.sponge.impl;

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

import net.tnemc.core.compatibility.InventoryProvider;
import net.tnemc.core.compatibility.Location;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.menu.Menu;
import net.tnemc.item.AbstractItemStack;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

/**
 * The Sponge implementation of the {@link PlayerProvider}.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongePlayerProvider implements PlayerProvider {

  private Player player;

  public SpongePlayerProvider(Player player) {
    this.player = player;
  }

  /**
   * Used to get the {@link UUID} of this player.
   *
   * @return The {@link UUID} of this player.
   */
  @Override
  public UUID getUUID() {
    return player.getUniqueId();
  }

  /**
   * Used to get the name of this player.
   *
   * @return The name of this player.
   */
  @Override
  public String getName() {
    return player.getName();
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

  @Override
  public String getRegion(boolean resolve) {
    return null;
  }

  /**
   * Used to get the amount of experience this player has.
   *
   * @return The amount of levels this player has.
   */
  @Override
  public int getExp() {
    return player.get(Keys.TOTAL_EXPERIENCE).orElse(0);
  }

  /**
   * Used to set the amount of experience this player has.
   *
   * @param exp The amount of experience to set for this player.
   */
  @Override
  public void setExp(int exp) {
    player.offer(Keys.TOTAL_EXPERIENCE, exp);
  }

  /**
   * Used to get the amount of experience levels this player has.
   *
   * @return The amount of experience levels this player has.
   */
  @Override
  public int getExpLevel() {
    return player.get(Keys.EXPERIENCE_LEVEL).orElse(0);
  }

  /**
   * Used to set the amount of experience levels this player has.
   *
   * @param level The amount of experience levels to set for this player.
   */
  @Override
  public void setExpLevel(int level) {
    player.offer(Keys.EXPERIENCE_LEVEL, level);
  }

  @Override
  public Object getInventory(boolean ender) {
    return null;
  }

  @Override
  public InventoryProvider<?> inventory() {
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
    return player.hasPermission(permission);
  }

  @Override
  public void message(MessageData messageData) {

  }
}