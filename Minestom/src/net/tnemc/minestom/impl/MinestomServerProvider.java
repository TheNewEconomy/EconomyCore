package net.tnemc.minestom.impl;
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

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.providers.CalculationsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * MinestomServerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MinestomServerProvider implements ServerConnector {

  private final MinestomItemCalculations calc = new MinestomItemCalculations();
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
    return MinestomPlayerProvider.find(identifier.toString());
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
    return MinecraftServer.getConnectionManager().getPlayer(uuid) != null;
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
    return MinecraftServer.getConnectionManager().getPlayer(name) != null;
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

    final Player player = MinecraftServer.getConnectionManager().getPlayer(name);

    if(player != null) {
      return player.isOnline();
    }
    return false;
  }

  /**
   * Returns the name of the default world.
   *
   * @return The name of the default world.
   */
  @Override
  public String defaultWorld() {
    //TODO: Default World
    return null;
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
  public AbstractItemStack<?> stackBuilder() {
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

  @Override
  public <S, T extends AbstractItemStack<S>, INV> CalculationsProvider<T, S, INV> calculations() {
    return null;
  }

  @Override
  public <S> AbstractItemStack<S> denominationToStack(ItemDenomination denomination) {
    return null;
  }

  @Override
  public MinestomItemCalculations itemCalculations() {
    return calc;
  }
}
