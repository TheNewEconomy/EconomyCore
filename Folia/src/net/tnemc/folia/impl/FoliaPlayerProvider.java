package net.tnemc.folia.impl;

/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
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

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.tnemc.core.TNECore;
import net.tnemc.menu.folia.FoliaPlayer;
import net.tnemc.paper.PaperCore;
import net.tnemc.plugincore.core.compatibility.Location;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;

/**
 * FoliaPlayerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class FoliaPlayerProvider extends FoliaPlayer implements PlayerProvider {

  private final OfflinePlayer player;

  public FoliaPlayerProvider(final OfflinePlayer player) {

    super(player, PaperCore.instance().getPlugin());
    this.player = player;
  }

  /**
   * Used to get the {@link UUID} of this player.
   *
   * @return The {@link UUID} of this player.
   */
  @Override
  public UUID identifier() {

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
  public Optional<Location> getLocation() {

    if(player.getPlayer() == null) {
      return Optional.empty();
    }

    final org.bukkit.Location locale = player.getPlayer().getLocation();

    return Optional.of(new Location(locale.getWorld().getName(),
                                    locale.getX(), locale.getY(),
                                    locale.getZ()
    ));
  }

  /**
   * Used to get the name of the world this player is currently in.
   *
   * @return The name of the world.
   */
  @Override
  public String world() {

    String world = TNECore.eco().region().defaultRegion();

    if(player.getPlayer() != null) {
      world = player.getPlayer().getWorld().getName();
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

    String biome = TNECore.eco().region().defaultRegion();

    if(player.getPlayer() != null) {
      biome = player.getPlayer().getLocation().getBlock().getBiome().getKey().getKey();
    }
    return biome;
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
  public void setExp(final int exp) {

    if(player.getPlayer() != null) {
      player.getPlayer().setTotalExperience(exp);
    }
  }

  /**
   * Used to get the amount of experience levels this player has.
   *
   * @return The amount of experience levels this player has.
   */
  @Override
  public int getExpLevel() {

    if(player.getPlayer() == null) {
      return 0;
    }
    return player.getPlayer().getLevel();
  }

  /**
   * Used to set the amount of experience levels this player has.
   *
   * @param level The amount of experience levels to set for this player.
   */
  @Override
  public void setExpLevel(final int level) {

    if(player.getPlayer() != null) {
      player.getPlayer().setLevel(level);
    }
  }

  @Override
  public FoliaInventoryProvider inventory() {

    return new FoliaInventoryProvider(identifier(), PaperCore.instance().getPlugin());
  }

  /**
   * Used to determine if this player has the specified permission node.
   *
   * @param permission The node to check for.
   *
   * @return True if the player has the permission, otherwise false.
   */
  @Override
  public boolean hasPermission(final String permission) {

    if(player.getPlayer() == null) {
      return false;
    }
    return player.getPlayer().hasPermission(permission);
  }

  @Override
  public void message(final String message) {

    if(player.getPlayer() != null) {
      message(new MessageData(message));
    }
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

    try(BukkitAudiences provider = BukkitAudiences.create(PaperCore.instance().getPlugin())) {
      MessageHandler.translate(messageData, player.getUniqueId(), provider.sender(player.getPlayer()));
    }
  }

  public OfflinePlayer getPlayer() {

    return player;
  }

  public static FoliaPlayerProvider find(final String identifier) {

    try {
      return new FoliaPlayerProvider(Bukkit.getOfflinePlayer(UUID.fromString(identifier)));
    } catch(Exception ignore) {
      return new FoliaPlayerProvider(Bukkit.getOfflinePlayer(identifier));
    }
  }
}
