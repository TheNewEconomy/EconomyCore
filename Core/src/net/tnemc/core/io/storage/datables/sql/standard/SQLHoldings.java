package net.tnemc.core.io.storage.datables.sql.standard;

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
import net.tnemc.core.account.holdings.CurrencyHoldings;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.RegionHoldings;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.io.storage.Datable;
import net.tnemc.core.io.storage.StorageConnector;
import net.tnemc.core.io.storage.connect.SQLConnector;
import net.tnemc.core.utils.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
  public void purge(StorageConnector<?> connector) {
    //This isn't required, it'll be deleted with the account.
  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param object    The object to be stored.
   */
  @Override
  public void store(StorageConnector<?> connector, @NotNull HoldingsEntry object, @Nullable String identifier) {
    if(connector instanceof SQLConnector && identifier != null) {

      ((SQLConnector)connector).executeUpdate(((SQLConnector)connector).dialect().saveHoldings(),
                                              new Object[] {
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
  public void storeAll(StorageConnector<?> connector, @Nullable String identifier) {
    if(connector instanceof SQLConnector && identifier != null) {

      final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
      if(account.isPresent()) {
        for(RegionHoldings region : account.get().getWallet().getHoldings().values()) {
          for(CurrencyHoldings currency : region.getHoldings().values()) {
            for(HoldingsEntry entry : currency.getHoldings().values()) {
              store(connector, entry, identifier);
            }
          }
        }
      }

    }
  }

  /**
   * Used to load this object.
   *
   * @param connector  The storage connector to use for this transaction.
   * @param identifier The identifier used to identify the object to load.
   *
   * @throws UnsupportedOperationException as this method is not valid for holdings.
   * @return The object to load.
   */
  @Override
  public Optional<HoldingsEntry> load(StorageConnector<?> connector, @NotNull String identifier) {
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
  public Collection<HoldingsEntry> loadAll(StorageConnector<?> connector, @Nullable String identifier) {
    final Collection<HoldingsEntry> holdings = new ArrayList<>();

    if(connector instanceof SQLConnector && identifier != null) {
      TNECore.log().debug("SQLHoldings-loadAll-Account ID:" + identifier, DebugLevel.DEVELOPER);
      try(ResultSet result = ((SQLConnector)connector).executeQuery(((SQLConnector)connector).dialect().loadHoldings(),
                                                                    new Object[] {
                                                                        identifier,
                                                                        MainConfig.yaml().getString("Core.Server.Name")
                                                                    })) {
        while(result.next()) {

          //region, currency, amount, type
          final HoldingsEntry entry = new HoldingsEntry(result.getString("region"),
                                                        UUID.fromString(result.getString("currency")),
                                                        result.getBigDecimal("holdings"),
                                                        Identifier.fromID(result.getString("holdings_type")));

          TNECore.log().debug("SQLHoldings-loadAll-Entry ID:" + entry.getHandler(), DebugLevel.DEVELOPER);
          TNECore.log().debug("SQLHoldings-loadAll-Entry AMT:" + entry.getAmount().toPlainString(), DebugLevel.DEVELOPER);
          holdings.add(entry);
        }
      } catch(SQLException e) {
        e.printStackTrace();
      }
    }
    return holdings;
  }
}