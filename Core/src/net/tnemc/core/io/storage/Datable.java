package net.tnemc.core.io.storage;
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

import java.util.Collection;
import java.util.Optional;

/**
 * Datable
 *
 * @param <O> The object that is going to be stored/loaded from the database.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 *
 */
public interface Datable<O> {

  /**
   * The class that is represented by the O parameter.
   * @return The class that represents the parameter.
   */
  Class<? extends O> clazz();

  /**
   * USed to purge the objects of this datable.
   * @param connector The storage connector to use for this transaction.
   */
  void purge(StorageConnector<?> connector);

  /**
   * Used to store this object.
   * @param connector The storage connector to use for this transaction.
   * @param object The object to be stored.
   */
  void store(StorageConnector<?> connector, O object);

  /**
   * Used to store all objects of this type.
   * @param connector The storage connector to use for this transaction.
   */
  void storeAll(StorageConnector<?> connector);

  /**
   * Used to load this object.
   * @param connector The storage connector to use for this transaction.
   * @param identifier The identifier used to identify the object to load.
   * @return The object to load.
   */
  Optional<O> load(StorageConnector<?> connector, final String identifier);

  /**
   * Used to load all objects of this type.
   * @param connector The storage connector to use for this transaction.
   * @return A collection containing the objects loaded.
   */
  Collection<O> loadAll(StorageConnector<?> connector);
}