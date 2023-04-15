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

import net.tnemc.core.config.DataConfig;
import net.tnemc.core.io.storage.SQLEngine;

import java.util.HashMap;
import java.util.Map;

public class MySQL extends StandardSQL {

  /**
   * The name of this engine.
   *
   * @return The engine name.
   */
  @Override
  public String name() {
    return "mysql";
  }

  @Override
  public String[] driver() {
    return new String[] {
        "org.mariadb.jdbc.Driver",
        "com.mysql.cj.jdbc.Driver",
        "com.mysql.jdbc.Driver"
    };
  }

  @Override
  public String[] dataSource() {
    return new String[] {
        "org.mariadb.jdbc.MariaDbDataSource",
        "com.mysql.jdbc.jdbc2.optional.MysqlDataSource",
        "com.mysql.cj.jdbc.MysqlDataSource"
    };
  }

  @Override
  public String url(String file, String host, int port, String database) {
    return "jdbc:mysql://" + host + ":" + port + "/" + database;
  }

  /**
   * Used to get addition hikari properties for this {@link SQLEngine}.
   * @return A map containing the additional properties.
   */
  @Override
  public Map<String, Object> properties() {
    Map<String, Object> properties = new HashMap<>();

    properties.put("autoReconnect", true);
    properties.put("cachePrepStmts", true);
    properties.put("prepStmtCacheSize", 250);
    properties.put("prepStmtCacheSqlLimit", 2048);
    properties.put("rewriteBatchedStatements", true);
    properties.put("useServerPrepStmts", true);
    properties.put("cacheCallableStmts", true);
    properties.put("cacheResultSetMetadata", true);
    properties.put("cacheServerConfiguration", true);
    properties.put("useLocalSessionState", true);
    properties.put("elideSetAutoCommits", true);
    properties.put("alwaysSendSetIsolation", false);
    properties.put("useSSL", DataConfig.yaml().getBoolean("Data.Database.SQL.SSL"));

    return properties;
  }
}