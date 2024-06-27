package net.tnemc.core.io.storage.datables.yaml;
/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
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
import net.tnemc.core.manager.TransactionManager;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.id.UUIDPair;
import net.tnemc.plugincore.core.io.storage.Datable;
import net.tnemc.plugincore.core.io.storage.StorageConnector;
import net.tnemc.plugincore.core.io.storage.connect.SQLConnector;
import net.tnemc.plugincore.core.utils.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    if(connector instanceof SQLConnector && identifier != null) {

      final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
      if(account.isPresent()) {
        for(Receipt receipt : TransactionManager.receipts().getReceiptsByParticipant(account.get().getIdentifier())) {
          store(connector, receipt, identifier);
        }
      }
    }
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

    final File file = new File(PluginCore.directory(), "transactions/" + identifier + ".yml");
    if(!file.exists()) {

      PluginCore.log().error("Null receipt file passed to YAMLReceipt.load. Receipt: " + identifier);
      return Optional.empty();
    }
    return load(connector, file, identifier);
  }

  public Optional<Receipt> load(StorageConnector<?> connector, File file, final String identifier) {
    if(file == null) {

      PluginCore.log().error("Null account file passed to YAMLReceipt.load. Receipt: " + identifier);
      return Optional.empty();
    }

    YamlFile yaml = null;
    try {
      yaml = YamlFile.loadConfiguration(file);
    } catch(IOException ignore) {

      PluginCore.log().error("Issue loading account file. Receipt: " + identifier);
    }

    if(yaml != null) {

      Receipt receipt = null;

      return Optional.ofNullable(receipt);
    }
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
    final Collection<Receipt> receipts = new ArrayList<>();

    for(File file : IOUtil.getYAMLs(new File(PluginCore.directory(), "transactions"))) {

      final Optional<Receipt> loaded = load(connector, file, file.getName().replace(".yml", ""));
      if(loaded.isPresent()) {
        receipts.add(loaded.get());
        TransactionManager.receipts().log(loaded.get());
      }
    }
    return receipts;
  }
}