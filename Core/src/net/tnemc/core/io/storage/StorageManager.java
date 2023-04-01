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
import net.tnemc.core.config.DataConfig;

/**
 * The manager, which manages everything related to storage.
 * Manages:
 * - Loading
 * - Saving
 * - Caching
 * - Connections
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class StorageManager {

  private static StorageManager instance;

  final HikariConfig hikariConfig = new HikariConfig();

  private final String pool = "TNE-Pool-1";


  public StorageManager(DataConfig config) {
    instance = this;
  }

  public static StorageManager instance() {
    return instance;
  }

  /**
   * Used to save all data in TNE.
   */
  public void saveAll() {
    //TODO: Storage Manager. Will this be in TNDL?
  }

  /**
   * Used to reset all data in TNE.
   */
  public void reset() {
    //TODO: Storage Manager. Will this be in TNDL?
  }

  /**
   * Used to back up data that is currently in the database.
   * @return True if the backup was successful, otherwise false.
   */
  public boolean backup() {
    //TODO: Storage Manager. Will this be in TNDL?
    return false;
  }
}