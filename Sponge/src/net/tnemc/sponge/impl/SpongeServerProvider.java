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

import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.ProxyProvider;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.core.compatibility.helper.CraftingRecipe;
import net.tnemc.core.compatibility.scheduler.SchedulerProvider;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.region.RegionMode;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.providers.CalculationsProvider;
import net.tnemc.sponge.SpongeCore;
import net.tnemc.sponge.impl.scheduler.SpongeScheduler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.world.World;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.sponge.SpongeCommandActor;

import java.util.Optional;
import java.util.UUID;

/**
 * SpongeServerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeServerProvider implements ServerConnector {

  private final SpongeItemCalculations calc = new SpongeItemCalculations();
  private final SpongeProxyProvider proxy = new SpongeProxyProvider();

  private final SpongeScheduler scheduler;

  public SpongeServerProvider() {
    this.scheduler = new SpongeScheduler();
  }

  @Override
  public String name() {
    return "sponge";
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
    return new SpongeCMDSource((SpongeCommandActor)actor);
  }

  /**
   * Used to get the amount of online players.
   *
   * @return The amount of online players.
   */
  @Override
  public int onlinePlayers() {
    return Sponge.getServer().getOnlinePlayers().size();
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
    final Optional<Player> player = Sponge.getServer().getPlayer(identifier);
    return player.map(value->new SpongePlayerProvider(value, SpongeCore.instance().getPlugin()));
  }

  /**
   * Used to determine if this player has played on this server before.
   *
   * @param identifier The {@link UUID} that is associated with the player.
   *
   * @return True if the player has played on the server before, otherwise false.
   */
  @Override
  public boolean playedBefore(UUID identifier) {
    final Optional<Player> player = Sponge.getServer().getPlayer(identifier);
    return player.map(Player::hasPlayedBefore).orElse(false);
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
    final Optional<Player> player = Sponge.getServer().getPlayer(name);
    return player.map(Player::hasPlayedBefore).orElse(false);
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
    final Optional<Player> player = Sponge.getServer().getPlayer(name);
    return player.map(User::isOnline).orElse(false);
  }

  @Override
  public Optional<UUID> fromName(String name) {
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
    return Optional.empty();
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
      final Optional<World> world = Sponge.getServer().getWorld(Sponge.getServer().getDefaultWorldName());
      if(world.isPresent()) {
        return world.get().getSpawnLocation().getBiome().getName();
      }
    }
    return Sponge.getServer().getDefaultWorldName();
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

  @Override
  public AbstractItemStack<?> stackBuilder() {
    return null;
  }

  @Override
  public void saveResource(String path, boolean replace) {
    //TODO: This
  }

  /**
   * Provides this implementation's {@link SchedulerProvider scheduler}.
   *
   * @return The scheduler for this implementation.
   */
  @Override
  public SpongeScheduler scheduler() {
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
    //TODO: Sponge Register crafting
  }

  @Override
  public <S, T extends AbstractItemStack<S>, INV> CalculationsProvider<T, S, INV> calculations() {
    return null;
  }

  @Override
  public <S> AbstractItemStack<S> denominationToStack(ItemDenomination denomination, final int amount) {
    return null;
  }

  @Override
  public SpongeItemCalculations itemCalculations() {
    return calc;
  }
}
