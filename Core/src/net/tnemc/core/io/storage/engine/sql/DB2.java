package net.tnemc.core.io.storage.engine.sql;

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

import net.tnemc.core.io.storage.engine.StandardSQL;

public class DB2 extends StandardSQL {

  /**
   * The name of this engine.
   *
   * @return The engine name.
   */
  @Override
  public String name() {
    return "db2";
  }

  @Override
  public String[] driver() {
    return new String[] {
      "com.ibm.db2.jcc.DB2Driver"
    };
  }

  @Override
  public String[] dataSource() {
    return new String[] {
      "com.ibm.db2.jcc.DB2SimpleDataSource"
    };
  }

  @Override
  public String url(String file, String host, int port, String database) {
    return "jdbc:db2://" + host + ":" + port + "/" + database;
  }
}
