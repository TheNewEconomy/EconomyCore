package net.tnemc.core.id.impl.provider;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.id.UUIDAPI;
import net.tnemc.core.id.UUIDPair;
import net.tnemc.core.id.UUIDProvider;
import net.tnemc.core.id.impl.AshconAPI;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BaseUUIDProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BaseUUIDProvider implements UUIDProvider {

  //TODO: implement database system
  private final ConcurrentHashMap<UUID, UUIDPair> pairs = new ConcurrentHashMap<>();
  /**
   * Returns the associated {@link UUIDAPI uuid api} associated with this provider.
   *
   * @return The associated {@link UUIDAPI uuid api} associated with this provider.
   * @see UUIDAPI
   */
  @Override
  public UUIDAPI api() {
    return new AshconAPI();
  }

  /**
   * Used to retrieve a {@link UUIDPair}.
   *
   * @param name The username of the pair.
   *
   * @return An optional containing the pair if found, otherwise an empty
   * optional.
   */
  @Override
  public Optional<UUIDPair> retrieve(String name) {
    for(UUIDPair pair : pairs.values()) {
      if(pair.getUsername().equalsIgnoreCase(name)) {
        return Optional.of(pair);
      }
    }
    return Optional.empty();
  }

  /**
   * Used to retrieve a name from its associated {@link UUID}.
   * @param id The {@link UUID} to use in the search.
   * @return An optional containing the name if found, otherwise an empty optional.
   */
  @Override
  public Optional<String> retrieveName(final UUID id) {
    if(pairs.containsKey(id)) {
      return Optional.ofNullable(pairs.get(id).getUsername());
    }
    return Optional.empty();
  }

  /**
   * Used to store a Username & UUID pair. This could be to a map, or to
   * a database for persistent usage or to both.
   *
   * @param pair The {@link UUIDPair}
   */
  @Override
  public void store(UUIDPair pair) {
    pairs.put(pair.getIdentifier(), pair);
  }
}