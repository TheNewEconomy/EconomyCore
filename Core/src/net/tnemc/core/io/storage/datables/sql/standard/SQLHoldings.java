package net.tnemc.core.io.storage.datables.sql.standard;

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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.CurrencyHoldings;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.RegionHoldings;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.io.storage.dialect.TNEDialect;
import net.tnemc.core.utils.Identifier;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.io.storage.Datable;
import net.tnemc.plugincore.core.io.storage.StorageConnector;
import net.tnemc.plugincore.core.io.storage.connect.SQLConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * SQLHoldings
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SQLHoldings implements Datable<HoldingsEntry> {

  /**
   * The class that is represented by the O parameter.
   *
   * @return The class that represents the parameter.
   */
  @Override
  public Class<? extends HoldingsEntry> clazz() {

    return HoldingsEntry.class;
  }

  /**
   * USed to purge the objects of this datable.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void purge(final StorageConnector<?> connector) {
    //This isn't required, it'll be deleted with the account.
  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param object    The object to be stored.
   */
  @Override
  public void store(final StorageConnector<?> connector, @NotNull final HoldingsEntry object, @Nullable final String identifier) {

    if(connector instanceof final SQLConnector sql && sql.dialect() instanceof final TNEDialect tne && identifier != null) {

      PluginCore.log().debug("Storing holdings for Identifier: " + identifier);

      sql.executeUpdate(tne.saveHoldings(),
                        new Object[]{
                                identifier,
                                MainConfig.yaml().getString("Core.Server.Name"),
                                object.getRegion(),
                                object.getCurrency().toString(),
                                object.getHandler().asID(),
                                object.getAmount(),
                                object.getAmount()
                        });
    }
  }

  /**
   * Used to store all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void storeAll(final StorageConnector<?> connector, @Nullable final String identifier) {

    if(connector instanceof final SQLConnector sql && identifier != null) {

      final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
      if(account.isPresent()) {


        for(final Map.Entry<String, RegionHoldings> region : account.get().getWallet().getHoldings().entrySet()) {
          for(final Map.Entry<UUID, CurrencyHoldings> currency : region.getValue().getHoldings().entrySet()) {
            for(final HoldingsEntry entry : account.get().getHoldings(region.getKey(), currency.getKey())) {
              store(connector, entry, identifier);
            }
          }
        }
      }

    }
  }

  @Override
  public void delete(final StorageConnector<?> connector, @NotNull final String identifier) {
    //nothing to do here
  }

  /**
   * Used to load this object.
   *
   * @param connector  The storage connector to use for this transaction.
   * @param identifier The identifier used to identify the object to load.
   *
   * @return The object to load.
   *
   * @throws UnsupportedOperationException as this method is not valid for holdings.
   */
  @Override
  public Optional<HoldingsEntry> load(final StorageConnector<?> connector, @NotNull final String identifier) {

    throw new UnsupportedOperationException("load for HoldingsEntry is not a supported operation.");
  }

  /**
   * Used to load all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   *
   * @return A collection containing the objects loaded.
   */
  @Override
  public Collection<HoldingsEntry> loadAll(final StorageConnector<?> connector, @Nullable final String identifier) {

    final Collection<HoldingsEntry> holdings = new ArrayList<>();

    if(connector instanceof final SQLConnector sql && sql.dialect() instanceof final TNEDialect tne && identifier != null) {
      PluginCore.log().debug("SQLHoldings-loadAll-Account ID:" + identifier, DebugLevel.DEVELOPER);
      try(final ResultSet result = sql.executeQuery(tne.loadHoldings(),
                                                    new Object[]{
                                                            identifier,
                                                            MainConfig.yaml().getString("Core.Server.Name")
                                                    })) {
        while(result.next()) {

          final String currency = result.getString("currency");

          if(TNECore.eco().currency().find(currency).isEmpty()) {
            EconomyManager.invalidCurrencies().add(currency);
          }

          //region, currency, amount, type
          final HoldingsEntry entry = new HoldingsEntry(result.getString("region"),
                                                        UUID.fromString(currency),
                                                        result.getBigDecimal("holdings"),
                                                        Identifier.fromID(result.getString("holdings_type")));

          PluginCore.log().debug("SQLHoldings-loadAll-Entry ID:" + entry.getHandler(), DebugLevel.DEVELOPER);
          PluginCore.log().debug("SQLHoldings-loadAll-Entry AMT:" + entry.getAmount().toPlainString(), DebugLevel.DEVELOPER);
          holdings.add(entry);
        }
      } catch(final SQLException e) {
        e.printStackTrace();
      }
    }
    return holdings;
  }
}