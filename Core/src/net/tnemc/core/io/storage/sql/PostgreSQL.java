package net.tnemc.core.io.storage.sql;

import net.tnemc.core.io.storage.SQLStorage;


public class PostgreSQL extends SQLStorage {

  @Override
  public String getDriver() {
    return "org.postgresql.Driver";
  }

  @Override
  public Boolean dataSource() {
    return true;
  }

  @Override
  public String dataSourceURL() {
    return "org.postgresql.ds.PGSimpleDataSource";
  }

  @Override
  public String getURL(String file, String host, int port, String database) {
    return "jdbc:postgresql://" + host + ":" + port + "/" + database;
  }
}