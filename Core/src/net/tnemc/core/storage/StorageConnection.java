package net.tnemc.core.storage;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.tnemc.core.StorageSettings;
import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.log.DebugLevel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import static net.tnemc.core.storage.StorageManager.settings;

/**
 * Used to create and interact with Storage Engine connections.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class StorageConnection {

  private Connection connection;
  private HikariDataSource hikariSource;
  private final StorageEngine engine;

  //Various variables.
  private String url;
  private String driver;
  private boolean pool;
  private StorageSettings settings;

  public StorageConnection(StorageSettings settings) {
    this.settings = settings;
    this.engine = StorageManager.instance().engine();
    this.pool = settings().pool();
  }

  protected void connect() {
    url = engine.getURL(settings());
    findDriver();

    try {
      if(pool) {
        hikariSource();
        Connection connection = connection();
        closePool(connection);
      } else {
        connection = DriverManager.getConnection(url);
      }
    } catch(SQLException e) {
      TNECore.log().error("Couldn't connect to the database.", e, DebugLevel.STANDARD);
    }
  }

  public Connection connection() throws SQLException {
    if(settings().pool()) {
      return hikariSource.getConnection();
    } else {
      return connection;
    }
  }

  public void closePool(Connection connection) {
    if(!pool) {
      return;
    }

    try {
      connection.close();
    } catch(SQLException e) {
      TNECore.log().error("Error while closing pool connection.", e, DebugLevel.STANDARD);
    }
  }

  public void close() {
    try {

      if(connection != null) connection.close();

      if(hikariSource != null) hikariSource.close();
    } catch(SQLException e) {
      TNECore.log().error("Error while closing connection.", e, DebugLevel.STANDARD);
    }
  }

  protected void hikariSource() {
    HikariConfig config = new HikariConfig();
    config.setPoolName("TNE");
    config.setJdbcUrl(url);
    config.setUsername(settings().user());
    config.setPassword(settings().pass());
    config.setMaximumPoolSize(settings().poolSize());
    config.setMinimumIdle(settings().poolIdle());
    config.setMaxLifetime(settings().poolLife());
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    config.addDataSourceProperty("userServerPrepStmts", "true");

    for(Map.Entry<String, Object> entry : engine.hikariProperties().entrySet()) {
      config.addDataSourceProperty(entry.getKey(), entry.getValue());
    }
    hikariSource = new HikariDataSource(config);

  }

  protected void findDriver() {
    for(final String driver : engine.getDrivers()) {
      if(tryDriver(driver)) break;
    }
  }

  protected boolean tryDriver(final String driver) {
    try {
      Class.forName(driver);
    } catch(InstantiationError | IllegalAccessError | ClassNotFoundException ignore) {
      return false;
    }
    this.driver = driver;
    return true;
  }
}