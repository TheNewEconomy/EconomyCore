package net.tnemc.core.compatibility;

import net.tnemc.core.io.message.TranslationProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Acts like a bridge between various server softwares and classes specific to those
 * server softwares such as opening menus, and sending messages.
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

  TranslationProvider translation();
}