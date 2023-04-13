package net.tnemc.core.io.storage.engine;

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
import net.tnemc.core.io.storage.SQLEngine;
import net.tnemc.core.io.storage.connect.SQLConnector;

public class H2 extends StandardSQL {

  /**
   * The name of this engine.
   *
   * @return The engine name.
   */
  @Override
  public String name() {
    return "h2";
  }

  @Override
  public String[] driver() {
    return new String[] {
      "org.h2.Driver"
    };
  }

  @Override
  public String[] dataSource() {

    return new String[]{
      "org.h2.jdbcx.JdbcDataSource"
    };
  }

  @Override
  public String url(String file, String host, int port, String database) {
    return "jdbc:h2:file:" + file + ";mode=MySQL;DB_CLOSE_ON_EXIT=TRUE;FILE_LOCK=NO";
  }
}