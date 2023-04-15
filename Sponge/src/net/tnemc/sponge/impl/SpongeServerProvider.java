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

import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.core.compatibility.scheduler.SchedulerProvider;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.providers.CalculationsProvider;
import net.tnemc.sponge.SpongeCore;
import net.tnemc.sponge.impl.scheduler.SpongeScheduler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

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

  private final SpongeScheduler scheduler;

  public SpongeServerProvider() {
    this.scheduler = new SpongeScheduler();
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

  /**
   * Returns the name of the default world.
   *
   * @return The name of the default world.
   */
  @Override
  public String defaultWorld() {
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

  @Override
  public <S, T extends AbstractItemStack<S>, INV> CalculationsProvider<T, S, INV> calculations() {
    return null;
  }

  @Override
  public <S> AbstractItemStack<S> denominationToStack(ItemDenomination denomination) {
    return null;
  }

  @Override
  public SpongeItemCalculations itemCalculations() {
    return calc;
  }
}
