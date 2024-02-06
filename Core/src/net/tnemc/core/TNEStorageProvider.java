package net.tnemc.core;
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

import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.io.storage.dialect.impl.MariaDialect;
import net.tnemc.core.io.storage.dialect.impl.MariaOutdatedDialect;
import net.tnemc.core.io.storage.dialect.impl.MySQLDialect;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreTime;
import net.tnemc.plugincore.core.io.storage.Datable;
import net.tnemc.plugincore.core.io.storage.StorageConnector;
import net.tnemc.plugincore.core.io.storage.StorageEngine;
import net.tnemc.plugincore.core.io.storage.StorageProvider;
import net.tnemc.plugincore.core.io.storage.connect.SQLConnector;
import net.tnemc.plugincore.core.io.storage.connect.YAMLConnector;
import net.tnemc.plugincore.core.io.storage.engine.flat.YAML;
import net.tnemc.plugincore.core.io.storage.engine.sql.MySQL;
import net.tnemc.plugincore.core.io.storage.engine.sql.PostgreSQL;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * TNEStorageProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TNEStorageProvider implements StorageProvider {

  private StorageEngine engine;
  private StorageConnector<?> connector;

  @Override
  public void initialize(String engine) {

    final String prefix = DataConfig.yaml().getString("Data.Database.Prefix");
    switch(engine.toLowerCase()) {
      case "mysql" -> {

        boolean maria = false;

        try {
          Class.forName("org.mariadb.jdbc.Driver");
          maria = true;
        } catch(Exception ignore) {}

        try {
          Class.forName("org.mariadb.jdbc.MariaDbDataSource");
          maria = true;
        } catch(Exception ignore) {}

        if(maria) {
          this.engine = new MySQL(prefix, new MariaDialect(prefix));
          this.connector = new SQLConnector();
          break;
        }
        this.engine = new MySQL(prefix, new MySQLDialect(prefix));
        this.connector = new SQLConnector();
      }
      case "maria", "mariadb" -> {

        this.engine = new MySQL(prefix, new MariaDialect(prefix));
        this.connector = new SQLConnector();
      }
      case "maria-outdated" -> {

        PluginCore.log().warning("Using outdated database! Please note: Official Support for this version of TNE is limited.", DebugLevel.OFF);
        this.engine = new MySQL(prefix, new MariaOutdatedDialect(prefix));
        this.connector = new SQLConnector();
      }
      case "postgre" -> {
        this.engine = new PostgreSQL(new MySQLDialect(prefix));
        this.connector = new SQLConnector();
      }
      default -> {
        this.engine = new YAML();
        this.connector = new YAMLConnector();
      }
    }
  }

  @Override
  public StorageConnector<?> connector() {
    return this.connector;
  }

  @Override
  public StorageEngine engine() {
    return this.engine;
  }

  @Override
  public void storeAll(@NotNull String identifier) {
    final Optional<Datable<?>> data = Optional.ofNullable(engine.datables().get(HoldingsEntry.class));

    //Our account storeAll requires no identifier, so we set it to null
    data.ifPresent(datable->datable.storeAll(connector, identifier));
  }

  @Override
  public void storeAll() {
    final Optional<Datable<?>> data = Optional.ofNullable(engine.datables().get(Account.class));

    //Our account storeAll requires no identifier, so we set it to null
    data.ifPresent(datable->PluginCore.server().scheduler()
            .createDelayedTask(()->datable.storeAll(connector, null), new ChoreTime(0), ChoreExecution.SECONDARY));
  }
}
