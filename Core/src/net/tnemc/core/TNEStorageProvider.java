package net.tnemc.core;

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

import net.tnemc.core.account.Account;
import net.tnemc.core.account.GeyserAccount;
import net.tnemc.core.account.NonPlayerAccount;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.io.storage.datables.sql.standard.SQLAccount;
import net.tnemc.core.io.storage.datables.sql.standard.SQLHoldings;
import net.tnemc.core.io.storage.datables.sql.standard.SQLReceipt;
import net.tnemc.core.io.storage.datables.yaml.YAMLAccount;
import net.tnemc.core.io.storage.datables.yaml.YAMLHoldings;
import net.tnemc.core.io.storage.datables.yaml.YAMLReceipt;
import net.tnemc.core.io.storage.dialect.TNEDialect;
import net.tnemc.core.io.storage.dialect.impl.MariaDialect;
import net.tnemc.core.io.storage.dialect.impl.MariaOutdatedDialect;
import net.tnemc.core.io.storage.dialect.impl.MySQLDialect;
import net.tnemc.core.transaction.Receipt;
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
import net.tnemc.plugincore.core.io.storage.engine.StandardSQL;
import net.tnemc.plugincore.core.io.storage.engine.flat.YAML;
import net.tnemc.plugincore.core.io.storage.engine.sql.MariaDB;
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
  public void initialize(final String engine) {

    final String prefix = DataConfig.yaml().getString("Data.Database.Prefix");

    boolean maria = false;

    try {
      Class.forName("org.mariadb.jdbc.Driver");
      maria = true;
    } catch(Exception ignore) { }

    try {
      Class.forName("org.mariadb.jdbc.MariaDbDataSource");
      maria = true;
    } catch(Exception ignore) { }

    switch(engine.toLowerCase()) {
      case "mysql" -> {

        if(maria) {
          this.engine = new MariaDB(prefix, new MariaDialect(prefix));
          this.connector = new SQLConnector();
          break;
        }
        this.engine = new MySQL(prefix, new MySQLDialect(prefix));
        this.connector = new SQLConnector();
      }
      case "maria", "mariadb" -> {

        if(maria) {
          this.engine = new MariaDB(prefix, new MariaDialect(prefix));
          this.connector = new SQLConnector();
          break;
        }

        this.engine = new MySQL(prefix, new MariaDialect(prefix));
        this.connector = new SQLConnector();
      }
      case "maria-outdated" -> {
        PluginCore.log().warning("Using outdated database! Please note: Official Support for this version of TNE is limited.", DebugLevel.OFF);

        if(maria) {
          this.engine = new MariaDB(prefix, new MariaOutdatedDialect(prefix));
          this.connector = new SQLConnector();
          break;
        }

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

    final Datable<Account> account = (this.engine instanceof StandardSQL)? new SQLAccount() : new YAMLAccount();
    final Datable<HoldingsEntry> entry = (this.engine instanceof StandardSQL)? new SQLHoldings() : new YAMLHoldings();
    final Datable<Receipt> receipt = (this.engine instanceof StandardSQL)? new SQLReceipt() : new YAMLReceipt();

    this.engine.datables().put(Account.class, account);
    this.engine.datables().put(NonPlayerAccount.class, account);
    this.engine.datables().put(SharedAccount.class, account);
    this.engine.datables().put(GeyserAccount.class, account);
    this.engine.datables().put(PlayerAccount.class, account);

    this.engine.datables().put(HoldingsEntry.class, entry);
    this.engine.datables().put(Receipt.class, receipt);
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
  public void initialize() {

    if(connector instanceof SQLConnector sql
       && this.engine instanceof StandardSQL sqlEngine
       && sqlEngine.dialect() instanceof TNEDialect tneDialect) {

      sql.executeUpdate(tneDialect.accountsTable(), new Object[]{});
      sql.executeUpdate(tneDialect.accountsNonPlayerTable(), new Object[]{});
      sql.executeUpdate(tneDialect.accountsPlayerTable(), new Object[]{});
      sql.executeUpdate(tneDialect.accountMembersTable(), new Object[]{});
      sql.executeUpdate(tneDialect.holdingsTable(), new Object[]{});
      sql.executeUpdate(tneDialect.receiptsTable(), new Object[]{});
      sql.executeUpdate(tneDialect.receiptsHoldingsTable(), new Object[]{});
      sql.executeUpdate(tneDialect.receiptsParticipantsTable(), new Object[]{});
      sql.executeUpdate(tneDialect.receiptsModifiersTable(), new Object[]{});
    }
  }

  @Override
  public void storeAll(@NotNull final String identifier) {

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


    final Optional<Datable<?>> receiptData = Optional.ofNullable(engine.datables().get(Receipt.class));

    //Our account storeAll requires no identifier, so we set it to null
    receiptData.ifPresent(datable->PluginCore.server().scheduler()
            .createDelayedTask(()->datable.storeAll(connector, null), new ChoreTime(0), ChoreExecution.SECONDARY));
  }

  @Override
  public void purge() {

  }

  @Override
  public void reset() {

    TNECore.eco().clearCache();
  }

  @Override
  public void backup() {

  }
}
