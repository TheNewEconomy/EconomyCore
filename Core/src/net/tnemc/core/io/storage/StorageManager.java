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

import net.tnemc.core.account.Account;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.io.storage.connect.SQLConnector;
import net.tnemc.core.io.storage.engine.H2;
import net.tnemc.core.io.storage.engine.MySQL;
import net.tnemc.core.io.storage.engine.PostgreSQL;
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
  private final SQLConnector connector;


  public StorageManager() {
    instance = this;
    this.connector = new SQLConnector();

    initialize(DataConfig.yaml().getString(""));
  }

  public void initialize(final String engine) {
    switch(engine.toLowerCase()) {
      case "mysql" -> this.engine = new MySQL();
      case "postgre" -> this.engine = new PostgreSQL();
      default -> this.engine = new H2();
    }

    //Initialize our connection.
    this.connector.initialize();
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
    final Datable<T> data = (Datable<T>)engine.datables().get(object.getClass());
    if(data != null) {

      //TODO: Scheduling system
      data.store(connector, object, identifier);
    }
  }

  /**
   * Used to store all data in TNE.
   */
  public void storeAll() {
    final Optional<Datable<?>> data = Optional.ofNullable(engine.datables().get(Account.class));

    //Our account storeAll requires no identifier, so we set it to null
    //TODO: Scheduling System.
    data.ifPresent(datable->datable.storeAll(connector, null));
  }

  /**
   * Used to purge TNE data.
   */
  public void purge() {
    for(Datable<?> data : engine.datables().values()) {
      //TODO: Scheduling System.
      data.purge(connector);
    }
  }

  /**
   * Used to reset all data in TNE.
   */
  public void reset() {
    //TODO: Scheduling System.
    engine.reset();
  }

  /**
   * Used to back up data that is currently in the database.
   * @return True if the backup was successful, otherwise false.
   */
  public boolean backup() {
    //TODO: Backup storage engine.
    return false;
  }

  public StorageEngine getEngine() {
    return engine;
  }

  public SQLConnector getConnector() {
    return connector;
  }
}