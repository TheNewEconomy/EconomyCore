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

import net.tnemc.bukkit.TNE;
import net.tnemc.bukkit.impl.scheduler.BukkitScheduler;
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.core.compatibility.helper.CraftingRecipe;
import net.tnemc.core.compatibility.scheduler.SchedulerProvider;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.region.RegionMode;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.bukkit.BukkitCalculationsProvider;
import net.tnemc.item.bukkit.BukkitItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.command.CommandActor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * BukkitServerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BukkitServerProvider implements ServerConnector {

  private BukkitCalculationsProvider calc = new BukkitCalculationsProvider();

  private final BukkitScheduler scheduler;

  public BukkitServerProvider() {
    this.scheduler = new BukkitScheduler();
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
    return new BukkitCMDSource((BukkitCommandActor)actor);
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

    return Optional.of(BukkitPlayerProvider.find(identifier.toString()));
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

  /**
   * Returns the name of the default region.
   *
   * @param mode The {@link RegionMode} to use for this.
   *
   * @return The name of the default region. This could be different based on the current
   * {@link RegionMode}.
   */
  @Override
  public String defaultRegion(final RegionMode mode) {

    if(mode.name().equalsIgnoreCase("biome")) {
      final World world = Bukkit.getServer().getWorlds().get(0);

      return world.getSpawnLocation().getBlock().getBiome().getKey().getKey();
    }
    return Bukkit.getServer().getWorlds().get(0).getName();
  }

  /**
   * Used to replace colour codes in a string.
   *
   * @param string The string to format.
   *
   * @return The formatted string.
   */
  @Override
  public String replaceColours(String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  @Override
  public AbstractItemStack<?> stackBuilder() {
    return new BukkitItemStack();
  }

  @Override
  public void saveResource(String path, boolean replace) {
    TNE.instance().saveResource(path, replace);
  }

  /**
   * Provides this implementation's {@link SchedulerProvider scheduler}.
   *
   * @return The scheduler for this implementation.
   */
  @Override
  public BukkitScheduler scheduler() {
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
      final ShapedRecipe shaped = new ShapedRecipe(denominationToStack(recipe.getResult()).locale());
      shaped.shape(recipe.getRows());

      for(Map.Entry<Character, String> ingredient : recipe.getIngredients().entrySet()) {
        shaped.setIngredient(ingredient.getKey(), Material.valueOf(ingredient.getValue()));
      }
      Bukkit.getServer().addRecipe(shaped);
    } else {
      final ShapelessRecipe shapeless = new ShapelessRecipe(denominationToStack(recipe.getResult()).locale());
      for(Map.Entry<Character, String> ingredient : recipe.getIngredients().entrySet()) {
        shapeless.addIngredient(1, Material.valueOf(ingredient.getValue()));
      }
      Bukkit.getServer().addRecipe(shapeless);
    }
  }

  @Override
  public BukkitItemStack denominationToStack(ItemDenomination denomination) {
    return new BukkitItemStack().of(denomination.getMaterial(), 1)
        .enchant(denomination.getEnchantments())
        .lore(denomination.getLore())
        .flags(denomination.getFlags())
        .damage(denomination.getDamage())
        .display(denomination.getName())
        .modelData(denomination.getCustomModel());
  }

  @Override
  public BukkitCalculationsProvider calculations() {
    return calc;
  }

  @Override
  public BukkitItemCalculations itemCalculations() {
    return new BukkitItemCalculations();
  }
}