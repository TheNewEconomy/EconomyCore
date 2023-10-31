package net.tnemc.core.io.storage.engine.flat;
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
import net.tnemc.core.account.GeyserAccount;
import net.tnemc.core.account.NonPlayerAccount;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.io.storage.Datable;
import net.tnemc.core.io.storage.StorageConnector;
import net.tnemc.core.io.storage.StorageEngine;
import net.tnemc.core.io.storage.datables.yaml.YAMLAccount;
import net.tnemc.core.io.storage.datables.yaml.YAMLHoldings;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * YAML
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class YAML implements StorageEngine {

  protected final Map<Class<?>, Datable<?>> datables = new HashMap<>();

  public YAML() {
    final YAMLAccount account = new YAMLAccount();
    datables.put(Account.class, account);
    datables.put(NonPlayerAccount.class, account);
    datables.put(SharedAccount.class, account);
    datables.put(GeyserAccount.class, account);
    datables.put(PlayerAccount.class, account);

    datables.put(HoldingsEntry.class, new YAMLHoldings());
  }

  /**
   * The name of this engine.
   *
   * @return The engine name.
   */
  @Override
  public String name() {
    return "yaml";
  }

  /**
   * Called after the connection is initialized, so we can do any actions that need done immediately
   * after connecting.
   *
   * @param connector The {@link StorageConnector connector} used for initialization.
   */
  @Override
  public void initialize(StorageConnector<?> connector) {

  }

  /**
   * Used to reset all data for this engine.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void reset(StorageConnector<?> connector) {
    final File directory = new File(TNECore.directory(), "accounts");
    if(directory.exists()) {
      for(File file : Objects.requireNonNull(directory.listFiles())) {
        file.delete();
      }
    }
    final File transactionDirectory = new File(TNECore.directory(), "transactions");
    if(transactionDirectory.exists()) {
      for(File file : Objects.requireNonNull(transactionDirectory.listFiles())) {
        file.delete();
      }
    }
  }

  /**
   * Used to back up all data in the database for this engine.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void backup(StorageConnector<?> connector) {

  }

  /**
   * Used to get the {@link Datable} classes for this engine.
   *
   * @return A map with the datables.
   */
  @Override
  public Map<Class<?>, Datable<?>> datables() {
    return datables;
  }
}