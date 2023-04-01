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
   * The {@link StorageConnector} for this {@link StorageEngine}.
   * @return The storage connector for this engine.
   */
  StorageConnector<?, ?> connector();
}