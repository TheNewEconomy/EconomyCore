package net.tnemc.core.io.storage.connect;
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
import com.zaxxer.hikari.HikariDataSource;
import net.tnemc.core.io.storage.Dialect;
import net.tnemc.core.io.storage.SQLEngine;
import net.tnemc.core.io.storage.StorageConnector;
import net.tnemc.core.io.storage.StorageEngine;
import net.tnemc.core.io.storage.StorageManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQLConnector
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SQLConnector implements StorageConnector<Connection> {

  private DataSource source;
  private HikariConfig config;

  /**
   * Used to initialize a connection to the specified {@link StorageEngine}
   */
  @Override
  public void initialize() {
    this.config = new HikariConfig();


    this.source = new HikariDataSource(((SQLEngine)StorageManager.instance().getEngine()).config());
  }

  /**
   * Used to get the connection from the
   *
   * @return The connection.
   */
  @Override
  public Connection connection() throws SQLException {
    if(source == null) initialize();
    return source.getConnection();
  }

  /**
   * Used to execute a prepared query.
   * @param query The query string.
   * @param variables An array of variables for the prepared statement.
   * @return The {@link ResultSet}.
   */
  public ResultSet executeQuery(final String query, Object[] variables) {
    try(Connection connection = connection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      for(int i = 0; i < variables.length; i++) {
        statement.setObject((i + 1), variables[i]);
      }
      return statement.executeQuery();

    } catch(SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Used to execute a prepared update.
   * @param query The query string.
   * @param variables An array of variables for the prepared statement.
   */
  public void executeUpdate(final String query, Object[] variables) {
    try(Connection connection = connection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      for(int i = 0; i < variables.length; i++) {
        statement.setObject((i + 1), variables[i]);
      }
      statement.executeUpdate();

    } catch(SQLException e) {
      e.printStackTrace();
    }
  }

  public Dialect dialect() {
    return ((SQLEngine)StorageManager.instance().getEngine()).dialect();
  }
}