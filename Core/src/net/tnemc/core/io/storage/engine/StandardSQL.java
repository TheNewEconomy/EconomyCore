package net.tnemc.core.io.storage.engine;
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
import net.tnemc.core.io.storage.Datable;
import net.tnemc.core.io.storage.Dialect;
import net.tnemc.core.io.storage.SQLEngine;
import net.tnemc.core.io.storage.StorageConnector;
import net.tnemc.core.io.storage.StorageEngine;
import net.tnemc.core.io.storage.connect.SQLConnector;
import net.tnemc.core.io.storage.datables.sql.SQLAccount;
import net.tnemc.core.io.storage.datables.sql.SQLHoldings;
import net.tnemc.core.io.storage.datables.sql.SQLReceipt;
import net.tnemc.core.io.storage.dialect.MySQLDialect;
import net.tnemc.core.transaction.Receipt;

import java.util.HashMap;
import java.util.Map;

/**
 * StandardSQL
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public abstract class StandardSQL implements SQLEngine {
  
  private final Map<Class<?>, Datable<?>> datables = new HashMap<>();

  private final Dialect dialect;

  public StandardSQL() {
    this(new MySQLDialect(DataConfig.yaml().getString("Data.Database.Prefix")));
  }

  public StandardSQL(Dialect dialect) {
    this.dialect = dialect;

    //add our datables.
    datables.put(Account.class, new SQLAccount());
    datables.put(HoldingsEntry.class, new SQLHoldings());
    datables.put(Receipt.class, new SQLReceipt());
  }

  /**
   * The dialiect for this engine. This will be used for query purposes.
   *
   * @return The dialect for the engine.
   */
  @Override
  public Dialect dialect() {
    return dialect;
  }

  /**
   * Used to reset all data for this engine.
   */
  @Override
  public void reset() {
    //TODO: Reset all data.
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