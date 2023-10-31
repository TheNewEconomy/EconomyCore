package net.tnemc.core.io.storage.datables.yaml;
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

import net.tnemc.core.io.storage.Datable;
import net.tnemc.core.io.storage.StorageConnector;
import net.tnemc.core.transaction.Receipt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

/**
 * YAMLLog
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class YAMLReceipt implements Datable<Receipt> {
  /**
   * The class that is represented by the O parameter.
   *
   * @return The class that represents the parameter.
   */
  @Override
  public Class<? extends Receipt> clazz() {
    return Receipt.class;
  }

  /**
   * USed to purge the objects of this datable.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void purge(StorageConnector<?> connector) {

  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param object The object to be stored.
   * @param identifier An optional identifier for loading this object. Note: some Datables may
   * require this identifier.
   */
  @Override
  public void store(StorageConnector<?> connector, @NotNull Receipt object, @Nullable String identifier) {

  }

  /**
   * Used to store all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   * @param identifier The identifier used to load objects, if they relate to a specific identifier,
   * otherwise this will be null.
   */
  @Override
  public void storeAll(StorageConnector<?> connector, @Nullable String identifier) {

  }

  /**
   * Used to load this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param identifier The identifier used to identify the object to load.
   *
   * @return The object to load.
   */
  @Override
  public Optional<Receipt> load(StorageConnector<?> connector, @NotNull String identifier) {
    return Optional.empty();
  }

  /**
   * Used to load all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   * @param identifier The identifier used to load objects, if they relate to a specific identifier,
   * otherwise this will be null.
   *
   * @return A collection containing the objects loaded.
   */
  @Override
  public Collection<Receipt> loadAll(StorageConnector<?> connector, @Nullable String identifier) {
    return null;
  }
}