package net.tnemc.core.manager.id.impl.provider;

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

import net.tnemc.core.manager.id.UUIDAPI;
import net.tnemc.core.manager.id.UUIDPair;
import net.tnemc.core.manager.id.UUIDProvider;
import net.tnemc.core.manager.id.impl.AshconAPI;

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