package net.tnemc.core.compatibility;

import net.tnemc.core.uuid.UUIDProvider;
import net.tnemc.core.world.WorldConnectionProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Acts like a bridge between various server softwares and classes specific to those
 * server softwares such as opening menus, and sending messages.
 *
 * @since 0.1.1.17
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

  WorldConnectionProvider worldConnectionProvider();

  /**
   * @return The {@link UUIDProvider}
   */
  UUIDProvider uuidProvider();

  String defaultWorld();


}