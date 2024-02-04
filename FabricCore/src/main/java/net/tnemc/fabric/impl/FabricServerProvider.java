package net.tnemc.fabric.impl;
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

import net.minecraft.server.network.ServerPlayerEntity;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.core.compatibility.helper.CraftingRecipe;
import net.tnemc.core.compatibility.scheduler.SchedulerProvider;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.region.RegionMode;
import net.tnemc.fabric.FabricCore;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.providers.CalculationsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * FabricServerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class FabricServerProvider implements ServerConnector {

  private final FabricItemCalculations calc = new FabricItemCalculations();
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
    final Optional<ServerPlayerEntity> player = Optional.ofNullable(FabricCore.mcSERVER().getPlayerManager().getPlayer(identifier));

    if(player.isPresent()) {
      return Optional.of(new FabricPlayerProvider(player.get()));
    }
    return Optional.empty();
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
    return false;
  }

  /**
   * Returns the {@link Pattern pattern} utilized to determine if a string is a valid
   * player username.
   *
   * @return The {@link Pattern pattern} to use for determining if a string is a valid
   * player username.
   * @see Pattern
   */
  @Override
  public Pattern playerMatcher() {
    return ServerConnector.super.playerMatcher();
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
  public String defaultRegion(RegionMode mode) {
    return null;
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
    return ServerConnector.super.pluginAvailable(name);
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
    return null;
  }

  /**
   * Provides direct access to the implementation's instance of TNIL's AbstractItemStack, which acts
   * as a builder.
   *
   * @return The software's implementation of {@link AbstractItemStack}.
   */
  @Override
  public net.tnemc.item.AbstractItemStack<?> stackBuilder() {
    return null;
  }

  /**
   * Used to save a resource from the plugin's jar file to the local system's storage.
   *
   * @param path    The path to the resource inside the jar.
   * @param replace If the file exists in the local system's storage, should it be replaced?
   */
  @Override
  public void saveResource(String path, boolean replace) {

  }

  /**
   * Provides this implementation's {@link SchedulerProvider scheduler}.
   *
   * @return The scheduler for this implementation.
   */
  @Override
  public SchedulerProvider<?> scheduler() {
    return null;
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

  }

  @Override
  public <S, T extends AbstractItemStack<S>, INV> CalculationsProvider<T, S, INV> calculations() {
    return null;
  }

  @Override
  public <S> AbstractItemStack<S> denominationToStack(ItemDenomination denomination) {
    return null;
  }

  @Override
  public FabricItemCalculations itemCalculations() {
    return calc;
  }
}
