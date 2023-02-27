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

import net.tnemc.core.currency.calculations.ItemCalculations;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.io.message.TranslationProvider;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.SerialItem;
import net.tnemc.item.providers.CalculationsProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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
   * Attempts to find a {@link PlayerProvider player} based on an {@link UUID identifier}.
   * @param identifier The identifier
   * @return An Optional containing the located {@link PlayerProvider player}, or an empty
   * Optional if no player is located.
   */
  Optional<PlayerProvider> findPlayer(@NotNull UUID identifier);

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
   * Returns the name of the default world.
   * @return The name of the default world.
   */
  String defaultWorld();

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
   * Used to replace colour codes in a string.
   * @param string The string to format.
   * @return The formatted string.
   */
  String replaceColours(final String string);

  /**
   * Provides an instance of the {@link TranslationProvider} for this server.
   * @return The instance of the {@link TranslationProvider}.
   */
  TranslationProvider translation();

  /**
   * Used to save a resource from the plugin's jar file to the local system's storage.
   * @param path The path to the resource inside the jar.
   * @param replace If the file exists in the local system's storage, should it be replaced?
   */
  void saveResource(final String path, final boolean replace);

  <S, T extends AbstractItemStack<S>, INV> CalculationsProvider<T, S, INV> calculations();

  default SerialItem<?> denominationToSerial(final ItemDenomination denomination) {
    return denomination.toSerial();
  }

  <S> AbstractItemStack<S> denominationToStack(final ItemDenomination denomination);

  <INV> ItemCalculations<INV> itemCalculations();
}