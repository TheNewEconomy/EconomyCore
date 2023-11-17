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

import net.tnemc.core.config.DataConfig;
import net.tnemc.core.io.storage.Dialect;
import net.tnemc.core.io.storage.SQLEngine;
import net.tnemc.core.io.storage.engine.StandardSQL;

import java.util.HashMap;
import java.util.Map;

public class SQLite extends StandardSQL {

  public SQLite() {
    super();
  }

  public SQLite(final String prefix, Dialect dialect) {
    super(prefix, dialect);
  }

  /**
   * The name of this engine.
   *
   * @return The engine name.
   */
  @Override
  public String name() {
    return "sqlite";
  }

  @Override
  public String[] driver() {
    return new String[] {
        "org.sqlite.JDBC"
    };
  }

  @Override
  public String[] dataSource() {
    return new String[0];
  }

  @Override
  public String url(String file, String host, int port, String database) {
    return "jdbc:sqlite:" + file;
  }

  /**
   * Used to get addition hikari properties for this {@link SQLEngine}.
   * @return A map containing the additional properties.
   */
  @Override
  public Map<String, Object> properties() {
    final Map<String, Object> properties = new HashMap<>();

    properties.put("cachePrepStmts", true);
    properties.put("prepStmtCacheSize", 250);
    properties.put("prepStmtCacheSqlLimit", 2048);
    properties.put("rewriteBatchedStatements", true);
    properties.put("useServerPrepStmts", true);
    return properties;
  }
}