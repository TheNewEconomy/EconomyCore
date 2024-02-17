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

import com.destroystokyo.paper.profile.PlayerProfile;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import net.tnemc.bukkit.BukkitCore;
import net.tnemc.core.TNECore;
import net.tnemc.folia.impl.scheduler.FoliaScheduler;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.bukkit.BukkitCalculationsProvider;
import net.tnemc.item.bukkit.BukkitItemStack;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.bukkit.hook.PAPIParser;
import net.tnemc.plugincore.bukkit.impl.BukkitPlayerProvider;
import net.tnemc.plugincore.bukkit.impl.BukkitProxyProvider;
import net.tnemc.plugincore.core.compatibility.CmdSource;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import net.tnemc.plugincore.core.compatibility.ProxyProvider;
import net.tnemc.plugincore.core.compatibility.ServerConnector;
import net.tnemc.plugincore.core.compatibility.helper.CraftingRecipe;
import net.tnemc.plugincore.core.compatibility.scheduler.SchedulerProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * FoliaServerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class FoliaServerProvider implements ServerConnector {

  private final BukkitCalculationsProvider calc = new BukkitCalculationsProvider();
  private final BukkitProxyProvider proxy = new BukkitProxyProvider();

  private final FoliaScheduler scheduler;

  protected String world = null;

  public FoliaServerProvider() {
    this.scheduler = new FoliaScheduler();
  }

  @Override
  public String name() {
    return "folia";
  }

  /**
   * Used to replace placeholders from a string.
   *
   * @param player The player to use for the placeholder replacement.
   * @param message The message to replace placeholders in.
   *
   * @return The string after placeholders have been replaced.
   */
  @Override
  public String replacePlaceholder(UUID player, String message) {
    final Optional<PlayerProvider> playerOpt = PluginCore.server().findPlayer(player);
    if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") && playerOpt.isPresent()
            && playerOpt.get() instanceof BukkitPlayerProvider bukkitPlayer) {
      return PAPIParser.parse(bukkitPlayer, message);
    }
    return message;
  }

  /**
   * The proxy provider to use for this implementation.
   *
   * @return The proxy provider to use for this implementation.
   */
  @Override
  public ProxyProvider proxy() {
    return proxy;
  }

  /**
   * Used to convert an {@link CommandActor} to a {@link CmdSource}.
   *
   * @param actor The command actor.
   *
   * @return The {@link CmdSource} for this actor.
   */
  @Override
  public CmdSource<?> source(@NotNull CommandActor actor) {
    return null;
  }

  /**
   * Used to get the amount of online players.
   *
   * @return The amount of online players.
   */
  @Override
  public int onlinePlayers() {
    return Bukkit.getOnlinePlayers().size();
  }

  /**
   * Attempts to find a {@link PlayerProvider player} based on an {@link UUID identifier}.
   *
   * @param identifier The identifier
   *
   * @return An Optional containing the located {@link PlayerProvider player}, or an empty
   * Optional if no player is located.
   */
  @Override
  public Optional<PlayerProvider> findPlayer(@NotNull UUID identifier) {

    return Optional.of(FoliaPlayerProvider.find(identifier.toString()));
  }

  /**
   * This is used to return an instance of an {@link PlayerProvider player} based on the provided
   * instance's player object.
   *
   * @param player The instance of the player.
   *
   * @return The initialized {@link PlayerProvider player object}.
   */
  @Override
  public PlayerProvider initializePlayer(@NotNull Object player) {
    if(player instanceof Player playerObj) {
      return new FoliaPlayerProvider(playerObj);
    }
    return null;
  }

  /**
   * Used to determine if this player has played on this server before.
   *
   * @param uuid The {@link UUID} that is associated with the player.
   *
   * @return True if the player has played on the server before, otherwise false.
   */
  @Override
  public boolean playedBefore(UUID uuid) {
    return false;
  }

  /**
   * Used to determine if a player with the specified username has played
   * before.
   *
   * @param name The username to search for.
   *
   * @return True if someone with the specified username has played before,
   * otherwise false.
   */
  @Override
  public boolean playedBefore(String name) {
    return false;
  }

  /**
   * Used to determine if a player with the specified username is online.
   *
   * @param name The username to search for.
   *
   * @return True if someone with the specified username is online.
   */
  @Override
  public boolean online(String name) {
    try {

      final UUID id = UUID.fromString(name);
      return Bukkit.getPlayer(id) != null;
    } catch(Exception ignore) {
      return Bukkit.getPlayer(name) != null;
    }
  }

  @Override
  public Optional<UUID> fromName(String name) {

    final PlayerProfile profile = Bukkit.createProfile(name);
    final boolean complete = profile.completeFromCache();

    if(complete) {
      return Optional.ofNullable(profile.getId());
    }
    return Optional.empty();
  }

  /**
   * Used to locate a username for a specific name. This could be called from either a primary or
   * secondary thread, and should not call back to the Mojang API ever.
   *
   * @param id The {@link UUID} to use for the search.
   *
   * @return An optional containing the name if exists, otherwise false.
   */
  @Override
  public Optional<String> fromID(UUID id) {
    for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
      if(player.getUniqueId().equals(id)) {
        return Optional.ofNullable(player.getName());
      }
    }
    return Optional.empty();
  }

  @Override
  public String defaultWorld() {
    if(world == null) {
      world = Bukkit.getServer().getWorlds().get(0).getName();
    }
    return world;
  }

  /**
   * Determines if a plugin with the correlating name is currently installed.
   *
   * @param name The name to use for our check.
   *
   * @return True if a plugin with that name exists, otherwise false.
   */
  @Override
  public boolean pluginAvailable(String name) {
    return Bukkit.getPluginManager().isPluginEnabled(name);
  }

  @Override
  public String replaceColours(String string, boolean strip) {
    if(strip) {
      return ChatColor.stripColor(string);
    }
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  @Override
  public AbstractItemStack<?> stackBuilder() {
    return new BukkitItemStack();
  }

  @Override
  public void saveResource(String path, boolean replace) {
    BukkitCore.instance().getPlugin().saveResource(path, replace);
  }

  /**
   * Provides this implementation's {@link SchedulerProvider scheduler}.
   *
   * @return The scheduler for this implementation.
   */
  @Override
  public FoliaScheduler scheduler() {
    return scheduler;
  }

  /**
   * Used to register a crafting recipe to the server.
   *
   * @param recipe The crafting recipe to register.
   *
   * @see CraftingRecipe
   */
  @Override
  public void registerCrafting(@NotNull CraftingRecipe recipe) {
    if(recipe.isShaped()) {
      final ShapedRecipe shaped = new ShapedRecipe((ItemStack)recipe.getResult().locale());
      shaped.shape(recipe.getRows());

      for(Map.Entry<Character, String> ingredient : recipe.getIngredients().entrySet()) {
        shaped.setIngredient(ingredient.getKey(), Material.valueOf(ingredient.getValue()));
      }
      Bukkit.getServer().addRecipe(shaped);
    } else {
      final ShapelessRecipe shapeless = new ShapelessRecipe((ItemStack)recipe.getResult().locale());
      for(Map.Entry<Character, String> ingredient : recipe.getIngredients().entrySet()) {
        shapeless.addIngredient(1, Material.valueOf(ingredient.getValue()));
      }
      Bukkit.getServer().addRecipe(shapeless);
    }
  }

  @Override
  public BukkitCalculationsProvider calculations() {
    return calc;
  }
}