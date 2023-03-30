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

import com.zaxxer.hikari.HikariConfig;

/**
 * StorageEngine
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface StorageEngine {

  /**
   * The name of this engine.
   * @return The engine name.
   */
  String name();

  /**
   * The driver Strings for this storage engine.
   *
   * @return The driver Strings for this storage engine.
   */
  String[] driver();

  /**
   * The data source Strings for this storage engine.
   *
   * @return The data source Strings for this storage engine.
   */
  String[] dataSource();

  /**
   * Generates the connection URL String based on the provided details.
   *
   * @param file The file name, if applicable.
   * @param host The host to connect to.
   * @param port The port to connect to.
   * @param database The database name to connect to.
   * @return The generated connection URL using the details provided.
   */
  String url(String file, String host, int port, String database);

  /**
   * Used to get the {@link HikariConfig} for this {@link StorageEngine}.
   * @return The {@link HikariConfig}.
   */
  HikariConfig config();
}