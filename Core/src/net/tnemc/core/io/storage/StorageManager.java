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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.core.compatibility.scheduler.ChoreTime;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.io.storage.connect.SQLConnector;
import net.tnemc.core.io.storage.connect.YAMLConnector;
import net.tnemc.core.io.storage.dialect.MariaDialect;
import net.tnemc.core.io.storage.engine.flat.YAML;
import net.tnemc.core.io.storage.engine.sql.MySQL;
import net.tnemc.core.io.storage.engine.sql.PostgreSQL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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
  private StorageEngine engine;
  private final StorageConnector<?> connector;


  public StorageManager() {
    instance = this;

    final String engine = DataConfig.yaml().getString("Data.Database.Type");

    switch(engine.toLowerCase()) {
      case "mysql" -> {
        this.engine = new MySQL();
        this.connector = new SQLConnector();
      }
      case "maria" -> {

        final String prefix = DataConfig.yaml().getString("Data.Database.Prefix");
        this.engine = new MySQL(prefix, new MariaDialect(prefix));
        this.connector = new SQLConnector();
      }
      case "postgre" -> {
        this.engine = new PostgreSQL();
        this.connector = new SQLConnector();
      }
      default -> {
        this.engine = new YAML();
        this.connector = new YAMLConnector();
      }
    }

    initialize();
  }

  public void initialize() {

    //Initialize our connection.
    this.connector.initialize();
    this.engine.initialize(this.connector);
  }

  public static StorageManager instance() {
    return instance;
  }

  /**
   * Used to load this object.
   * @param object The class of the object to be loaded.
   * @param identifier The identifier used to identify the object to load.
   * @return The object to load.
   */
  public <T> Optional<T> load(Class<? extends T> object, @NotNull final String identifier) {
    final Datable<T> data = (Datable<T>)engine.datables().get(object);
    if(data != null) {
      return data.load(connector, identifier);
    }
    return Optional.empty();
  }

  /**
   * Used to load all objects of this type.
   * @param object The class of the object to be loaded.
   * @param identifier The identifier used to load objects, if they relate to a specific
   *                   identifier, otherwise this will be null.
   * @return A collection containing the objects loaded.
   */
  public <T> Collection<T> loadAll(Class<? extends T> object, @Nullable final String identifier) {
    final Datable<T> data = (Datable<T>)engine.datables().get(object);
    if(data != null) {
      return data.loadAll(connector, identifier);
    }
    return new ArrayList<>();
  }


  /**
   * Used to store this object.
   * @param object The object to be stored.
   * @param identifier An optional identifier for loading this object. Note: some Datables may require
   *                   this identifier.
   */
  public <T> void store(T object, @Nullable String identifier) {

    TNECore.log().inform("Storing Datable of type: " + object.getClass().getName(), DebugLevel.DEVELOPER);
    final Datable<T> data = (Datable<T>)engine.datables().get(object.getClass());
    if(data != null) {
      TNECore.server().scheduler().createDelayedTask(()->data.store(connector, object, identifier),
                                                     new ChoreTime(0), ChoreExecution.SECONDARY);
    }
  }

  /**
   * Used to store all data for an identifier in TNE.
   */
  public void storeAll(@NotNull final String identifier) {
    final Optional<Datable<?>> data = Optional.ofNullable(engine.datables().get(HoldingsEntry.class));

    //Our account storeAll requires no identifier, so we set it to null
    data.ifPresent(datable->TNECore.server().scheduler()
        .createDelayedTask(()->datable.storeAll(connector, identifier), new ChoreTime(0), ChoreExecution.SECONDARY));
  }

  /**
   * Used to store all data in TNE.
   */
  public void storeAll() {
    final Optional<Datable<?>> data = Optional.ofNullable(engine.datables().get(Account.class));

    //Our account storeAll requires no identifier, so we set it to null
    data.ifPresent(datable->TNECore.server().scheduler()
        .createDelayedTask(()->datable.storeAll(connector, null), new ChoreTime(0), ChoreExecution.SECONDARY));
  }

  /**
   * Used to purge TNE data.
   */
  public void purge() {
    for(Datable<?> data : engine.datables().values()) {
      TNECore.server().scheduler().createDelayedTask(()->data.purge(connector), new ChoreTime(0), ChoreExecution.SECONDARY);
    }
  }

  /**
   * Used to reset all data in TNE.
   */
  public void reset() {
    //call the reset method for all modules.
    TNECore.loader().getModules().values().forEach((moduleWrapper -> moduleWrapper.getModule().enableSave(this)));

    TNECore.server().scheduler().createDelayedTask(()->engine.reset(connector), new ChoreTime(0), ChoreExecution.SECONDARY);
  }

  /**
   * Used to back up data that is currently in the database.
   * @return True if the backup was successful, otherwise false.
   */
  public boolean backup() {
    //call the backup method for all modules.
    TNECore.loader().getModules().values().forEach((moduleWrapper -> moduleWrapper.getModule().enableSave(this)));

    TNECore.server().scheduler().createDelayedTask(()->engine.backup(connector), new ChoreTime(0), ChoreExecution.SECONDARY);
    return true;
  }

  public StorageEngine getEngine() {
    return engine;
  }

  public StorageConnector<?> getConnector() {
    return connector;
  }
}