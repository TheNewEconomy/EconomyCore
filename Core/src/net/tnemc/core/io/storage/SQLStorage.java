package net.tnemc.core.io.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.tnemc.core.io.Storage;
import net.tnemc.core.io.StorageManager;
import org.javalite.activejdbc.DB;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public abstract class SQLStorage implements Storage {
  private static DB db;
  private static HikariConfig config;
  private static HikariDataSource dataSource;
  private static boolean initialized = false;

  @Override
  public void initialize(StorageManager manager) {
    db = new DB("TNE");

    initialized = true;
    if(initialized) {
      config = new HikariConfig();

      try {
        config.setDriverClassName(manager.getProviders().get(manager.getType()).engine().getDriver());
        config.setJdbcUrl(manager.getProviders().get(manager.getType()).engine().getURL(manager.getFile(), manager.getHost(), manager.getPort(), manager.getDatabase()));
      } catch(SQLException e) {
        e.printStackTrace();
      }

      config.setUsername(manager.getUser());

      if(!manager.getPassword().equalsIgnoreCase("")) {
        config.setPassword(manager.getPassword());
      }

      for(Map.Entry<String, Object> entry : hikariProperties().entrySet()) {
        config.addDataSourceProperty(entry.getKey(), entry.getValue());
      }

      dataSource = new HikariDataSource(config);
    }
  }

  public static DB getDb() {
    return db;
  }

  public static void open() {
    if(db.hasConnection()) return;
    db.open(dataSource);
  }

  public static void close() {
    if(!db.hasConnection()) return;
    db.close();
  }

  public static void open(DataSource datasource) {
    if(db.hasConnection()) return;
    db.open(datasource);
  }

  public Boolean connected(StorageManager manager) {
    return true;
  }

  public static Connection connection(StorageManager manager) throws SQLException {
    return db.open(dataSource).getConnection();
  }

  public static ResultSet executeQuery(Statement statement, String query) {
    try(ResultSet results = db.getConnection().createStatement().executeQuery(query)) {
      return results;
    } catch(SQLException ignore) {}

    return null;
  }

  public static ResultSet executePreparedQuery(PreparedStatement statement, Object[] variables) {
    try {
      for(int i = 0; i < variables.length; i++) {
        statement.setObject((i + 1), variables[i]);
      }
      return statement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void executeUpdate(String query) {
    db.open(dataSource);
    try {
      db.getConnection().createStatement().executeUpdate(query);
    } catch (SQLException ignore) {}
    db.close();
  }

  public static void executePreparedUpdate(String query, Object[] variables) {
    db.open(dataSource);
    try(PreparedStatement statement = db.getConnection().prepareStatement(query)) {

      for(int i = 0; i < variables.length; i++) {
        statement.setObject((i + 1), variables[i]);
      }
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    db.close();
  }

  public static void close(Connection connection, Statement statement, ResultSet results) {
    if(results != null) {
      try {
        results.close();
      } catch(SQLException e) {
        e.printStackTrace();
      }
    }

    if(statement != null) {
      try {
        statement.close();
      } catch(SQLException e) {
        e.printStackTrace();
      }
    }

    if(connection != null) {
      try {
        connection.close();
      } catch(SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public static HikariDataSource getDataSource() {
    return dataSource;
  }

  public static Integer boolToDB(boolean value) {
    return (value)? 1 : 0;
  }

  public static Boolean boolFromDB(int value) {
    return (value == 1);
  }
}