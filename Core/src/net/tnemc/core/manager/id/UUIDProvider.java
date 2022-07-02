package net.tnemc.core.manager.id;

/*
 * This class provides UUID-related functions, and has a concept of
 * "live" UUIDs and "stored" UUIDs.
 *
 * live UUIDs are any UUID that is directly associated with a Player account.
 *
 * stored UUIDs are any UUID that is generated for use inside the plugin
 * only. This could be a fake account related to an NPC account, or related
 * to a name that has never been registered on Minecraft.net.
 */

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static net.tnemc.core.utils.PlayerHelper.playerMatcher;

/**
 * Represents an provider that is used to resolve and store UUID<->Username Pairs.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface UUIDProvider {

  /**
   * Returns the associated {@link UUIDAPI uuid api} associated with this provider.
   *
   * @see UUIDAPI
   * @return The associated {@link UUIDAPI uuid api} associated with this provider.
   */
  UUIDAPI api();

  /**
   * Used to retrieve a {@link UUIDPair}.
   *
   * @param name The username of the pair.
   * @return An optional containing the pair if found, otherwise an empty
   * optional.
   */
  Optional<UUIDPair> retrieve(final String name);

  /**
   * Used to retrieve a name from its associated {@link UUID}.
   * @param id The {@link UUID} to use in the search.
   * @return An optional containing the name if found, otherwise an empty optional.
   */
  Optional<String> retrieveName(final UUID id);

  /**
   * Used to store a Username & UUID pair. This could be to a map, or to
   * a database for persistent usage or to both.
   * @param pair The {@link UUIDPair}
   */
  void store(final UUIDPair pair);

  default UUID generateOffline(final String name) {
    return UUID.nameUUIDFromBytes(("Offline:" + name).getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Used to determine if a string is a valid minecraft username or not.
   * @param name The name to check.
   * @return True if the name is a valid minecraft username, otherwise false.
   */
  static boolean validate(final String name) {
    if(name.length() >= 3 && name.length() <= 16) {
      return playerMatcher().matcher(name).matches();
    }
    return false;
  }

  /**
   * Used to determine if a string is a valid UUID.
   * @param identifier The string to check.
   * @return True if the name is a valid UUID, otherwise false.
   */
  static boolean isUUID(final String identifier) {
    try {
      final UUID uuid = UUID.fromString(identifier);
      return true;
    } catch(Exception ignore) {
      return false;
    }
  }
}