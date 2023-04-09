package net.tnemc.core.io.storage.datables.sql;
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
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.shared.Member;
import net.tnemc.core.io.storage.Datable;
import net.tnemc.core.io.storage.StorageConnector;
import net.tnemc.core.io.storage.connect.SQLConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * SQLAccount
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SQLAccount implements Datable<Account> {

  /**
   * The class that is represented by the O parameter.
   *
   * @return The class that represents the parameter.
   */
  @Override
  public Class<? extends Account> clazz() {
    return Account.class;
  }

  /**
   * USed to purge the objects of this datable.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void purge(StorageConnector<?> connector) {
    if(connector instanceof SQLConnector) {
    }
  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param account    The object to be stored.
   */
  @Override
  public void store(StorageConnector<?> connector, @NotNull Account account, @Nullable String identifier) {
    if(connector instanceof SQLConnector) {

      //store the basic account information(accounts table)
      ((SQLConnector)connector).executeUpdate(((SQLConnector)connector).dialect().saveAccount(),
                                              new Object[] {
                                                  account.getIdentifier(),
                                                  account.getName(),
                                                  account.getClass().getName(),
                                                  account.getCreationDate(),
                                                  account.getPin(),
                                                  account.getStatus().identifier(),
                                                  account.getName(),
                                                  account.getPin(),
                                                  account.getStatus().identifier(),
                                              });

      if(account instanceof PlayerAccount) {

        //store our uuid and username(player_names table)
        ((SQLConnector)connector).executeUpdate(((SQLConnector)connector).dialect().saveName(),
                                                new Object[]{
                                                    account.getIdentifier(),
                                                    account.getName(),
                                                    account.getName()
                                                });

        //Player account storage.(players_accounts table)
        ((SQLConnector)connector).executeUpdate(((SQLConnector)connector).dialect().savePlayer(),
                                                new Object[]{
                                                    account.getIdentifier(),
                                                    ((PlayerAccount)account).getLastOnline(),
                                                    ((PlayerAccount)account).getLastOnline()
                                                });

      }

      if(account instanceof SharedAccount) {

        //Non-player accounts.(non_players_accounts table)
        ((SQLConnector)connector).executeUpdate(((SQLConnector)connector).dialect().saveNonPlayer(),
                                                new Object[]{
                                                    account.getIdentifier(),
                                                    ((SharedAccount)account).getOwner().toString(),
                                                    ((SharedAccount)account).getOwner().toString()
                                                });

        //Account members(account_members table)
        for(Member member : ((SharedAccount)account).getMembers().values()) {
          for(Map.Entry<String, Boolean> entry : member.getPermissions().entrySet()) {
            ((SQLConnector)connector).executeUpdate(((SQLConnector)connector).dialect().saveMembers(),
                                                    new Object[]{
                                                        member.getId().toString(),
                                                        account.getIdentifier(),
                                                        entry.getKey(),
                                                        entry.getValue(),
                                                        entry.getValue()
                                                    }
            );
          }
        }
      }
    }
  }

  /**
   * Used to store all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void storeAll(StorageConnector<?> connector, @Nullable String identifier) {
    if(connector instanceof SQLConnector) {
      for(Account account : TNECore.eco().account().getAccounts().values()) {
        store(connector, account, account.getIdentifier());
      }
    }
  }

  /**
   * Used to load this object.
   *
   * @param connector  The storage connector to use for this transaction.
   * @param identifier The identifier used to identify the object to load.
   *
   * @return The object to load.
   */
  @Override
  public Optional<Account> load(StorageConnector<?> connector, @NotNull String identifier) {
    if(connector instanceof SQLConnector) {

    }
    return Optional.empty();
  }

  /**
   * Used to load all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   *
   * @return A collection containing the objects loaded.
   */
  @Override
  public Collection<Account> loadAll(StorageConnector<?> connector, @Nullable String identifier) {
    final Collection<Account> accounts = new ArrayList<>();

    if(connector instanceof SQLConnector) {

    }
    return accounts;
  }
}