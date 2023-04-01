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

/**
 * StorageConnector represents a connection helper class for a storage method.
 *
 * @param <C> Represents the connection object for this connector.
 * @param <E> Represents the {@link StorageEngine} type for this connector.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 * @see StorageEngine
 */
public interface StorageConnector<C,E> {

  /**
   * Used to initialize a connection to the specified {@link StorageEngine}
   * @param engine The storage engine.
   */
  void initialize(E engine);


  /**
   * Used to get the connection from the
   * @param engine The storage engine.
   *
   * @return The connection.
   */
  C connection(E engine) throws Exception;
}