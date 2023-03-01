package net.tnemc.bukkit.impl;

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

import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.tnemc.bukkit.BukkitTNECore;
import net.tnemc.bukkit.TNE;
import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.InventoryProvider;
import net.tnemc.core.compatibility.Location;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.io.message.MessageHandler;
import net.tnemc.core.menu.Menu;
import net.tnemc.item.AbstractItemStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * BukkitPlayerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BukkitPlayerProvider implements PlayerProvider {

  private final OfflinePlayer player;

  public BukkitPlayerProvider(OfflinePlayer player) {
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

  /**
   * Used to get the name of the region this player is in. This could be the world itself, or maybe
   * a third-party related region such as world guard.
   *
   * @param resolve Whether the returned region should be resolved to using the {@link net.tnemc.core.region.RegionProvider}.
   *
   * @return The name of the region.
   */
  @Override
  public String getRegion(final boolean resolve) {
    String world = TNECore.server().defaultWorld();

    if(player.getPlayer() != null) {
      world = player.getPlayer().getWorld().getName();
    }

    if(resolve) {
      return TNECore.eco().region().resolveRegion(world);
    }

    return world;
  }

  /**
   * Used to get the amount of experience this player has.
   *
   * @return The amount of levels this player has.
   */
  @Override
  public int getExp() {
    if(player.getPlayer() == null) {
      return 0;
    }
    return (int)player.getPlayer().getExp();
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
    if(player.getPlayer() == null) {
      return false;
    }
    return player.getPlayer().hasPermission(permission);
  }

  /**
   * Used to send a message to this command source.
   *
   * @param messageData The message data to utilize for this translation.
   */
  @Override
  public void message(final MessageData messageData) {
    if(player.getPlayer() == null) {
      return;
    }

    try(BukkitAudiences provider = BukkitAudiences.create(TNE.instance())) {
      MessageHandler.translate(messageData, player.getUniqueId(), provider.sender(player.getPlayer()));
    }
  }

  public static BukkitPlayerProvider find(final String identifier) {
    try {
      return new BukkitPlayerProvider(Bukkit.getOfflinePlayer(UUID.fromString(identifier)));
    } catch (Exception ignore) {
      return new BukkitPlayerProvider(Bukkit.getOfflinePlayer(identifier));
    }
  }
}
