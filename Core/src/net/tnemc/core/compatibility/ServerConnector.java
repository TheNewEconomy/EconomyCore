package net.tnemc.core.compatibility;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.helper.CraftingRecipe;
import net.tnemc.core.compatibility.scheduler.SchedulerProvider;
import net.tnemc.core.currency.calculations.ItemCalculations;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.region.RegionMode;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.providers.CalculationsProvider;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Acts like a bridge between various server software and classes specific to each
 * server software such as opening menus, and sending messages.
 *
 * @since 0.1.2.0
 * @author creatorfromhell
 */
public interface ServerConnector {

  /**
   * The name of this implementation.
   * @return The name of the implementation.
   */
  String name();

  /**
   * Used to replace placeholders from a string.
   * @param player The player to use for the placeholder replacement.
   * @param message The message to replace placeholders in.
   * @return The string after placeholders have been replaced.
   */
  String replacePlaceholder(final UUID player, final String message);

  /**
   * The proxy provider to use for this implementation.
   * @return The proxy provider to use for this implementation.
   */
  ProxyProvider proxy();

  /**
   * Used to convert an {@link CommandActor} to a {@link CmdSource}.
   * @return The {@link CmdSource} for this actor.
   */
  CmdSource<?> source(@NotNull CommandActor actor);

  /**
   * Used to get the amount of online players.
   * @return The amount of online players.
   */
  int onlinePlayers();

  /**
   * Attempts to find a {@link PlayerProvider player} based on an {@link UUID identifier}.
   * @param identifier The identifier
   * @return An Optional containing the located {@link PlayerProvider player}, or an empty
   * Optional if no player is located.
   */
  Optional<PlayerProvider> findPlayer(@NotNull UUID identifier);

  /**
   * This is used to return an instance of an {@link PlayerProvider player} based on the provided
   * instance's player object.
   * @param player The instance of the player.
   * @return The initialized {@link PlayerProvider player object}.
   */
  PlayerProvider initializePlayer(@NotNull Object player);

  /**
   * Used to get a cached UUID based on a username.
   * @param name The name to use for the check.
   * @return The UUID from the cached player. If no data with the player name exists, this will not
   * check the MojangAPI, it'll instead generate a random UUID.
   */
  default UUID getCached(final String name) {
    return UUID.randomUUID();
  }

  /**
   * Used to determine if this player has played on this server before.
   *
   * @param uuid The {@link UUID} that is associated with the player.
   * @return True if the player has played on the server before, otherwise false.
   */
  boolean playedBefore(UUID uuid);

  /**
   * Used to determine if a player with the specified username has played
   * before.
   * @param name The username to search for.
   * @return True if someone with the specified username has played before,
   * otherwise false.
   */
  boolean playedBefore(final String name);

  /**
   * Used to determine if a player with the specified username is online.
   * @param name The username to search for.
   * @return True if someone with the specified username is online.
   */
  boolean online(final String name);

  /**
   * Used to locate a UUID for a specific name. This could be called from either a primary or secondary thread, and
   * should not call back to the Mojang API ever.
   * @param name The name to use for the search.
   * @return An optional containing the UUId if exists, otherwise false.
   */
  Optional<UUID> fromName(final String name);

  /**
   * Used to locate a username for a specific name. This could be called from either a primary or secondary thread, and
   * should not call back to the Mojang API ever.
   * @param id The {@link UUID} to use for the search.
   * @return An optional containing the name if exists, otherwise false.
   */
  Optional<String> fromID(final UUID id);

  /**
   * Returns the {@link Pattern pattern} utilized to determine if a string is a valid
   * player username.
   *
   * @return The {@link Pattern pattern} to use for determining if a string is a valid
   * player username.
   *
   * @see Pattern
   */
  default Pattern playerMatcher() {
    return Pattern.compile("^\\w*$");
  }

  /**
   * Returns the name of the default region.
   *
   * @param mode The {@link RegionMode} to use for this.
   *
   * @return The name of the default region. This could be different based on the current
   * {@link RegionMode}.
   */
  String defaultRegion(final RegionMode mode);

  /**
   * Returns the name of the default region.
   *
   * @return The name of the default region. This could be different based on the current
   * {@link RegionMode}.
   */
  default String defaultRegion() {
    return defaultRegion(TNECore.eco().region().getMode());
  }

  /**
   * Determines if a plugin with the correlating name is currently installed.
   *
   * @param name The name to use for our check.
   * @return True if a plugin with that name exists, otherwise false.
   */
  default boolean pluginAvailable(final String name) {
    return false;
  }
  /**
   * Determines if a plugin with the correlating class is currently installed.
   *
   * @param classPath The class to use for our check.
   * @return True if a plugin with that class exists, otherwise false.
   */
  default boolean pluginClassAvailable(final String classPath) {
    try {
      Class.forName("net.milkbowl.vault.economy.Economy");
      return true;
    } catch(Exception ignore) {}
    return false;
  }

  /**
   * Used to replace colour codes in a string.
   * @param string The string to format.
   * @return The formatted string.
   */
  String replaceColours(final String string);

  /**
   * Provides direct access to the implementation's instance of TNIL's AbstractItemStack, which acts
   * as a builder.
   * @return The software's implementation of {@link AbstractItemStack}.
   */
  AbstractItemStack<?> stackBuilder();

  /**
   * Used to save a resource from the plugin's jar file to the local system's storage.
   * @param path The path to the resource inside the jar.
   * @param replace If the file exists in the local system's storage, should it be replaced?
   */
  void saveResource(final String path, final boolean replace);

  /**
   * Provides this implementation's {@link SchedulerProvider scheduler}.
   * @return The scheduler for this implementation.
   */
  SchedulerProvider<?> scheduler();

  /**
   * Used to register a crafting recipe to the server.
   * @param recipe The crafting recipe to register.
   * @see CraftingRecipe
   */
  void registerCrafting(@NotNull final CraftingRecipe recipe);

  <S, T extends AbstractItemStack<S>, INV> CalculationsProvider<T, S, INV> calculations();

  default <S> AbstractItemStack<S> denominationToStack(final ItemDenomination denomination) {
    return denominationToStack(denomination, 1);
  }

  <S> AbstractItemStack<S> denominationToStack(final ItemDenomination denomination, final int amount);

  <INV> ItemCalculations<INV> itemCalculations();


}