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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.shared.Member;
import net.tnemc.core.api.callback.account.AccountLoadCallback;
import net.tnemc.core.api.callback.account.AccountSaveCallback;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.io.storage.dialect.TNEDialect;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.id.UUIDPair;
import net.tnemc.plugincore.core.io.storage.Datable;
import net.tnemc.plugincore.core.io.storage.StorageConnector;
import net.tnemc.plugincore.core.io.storage.connect.SQLConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
  public void purge(final StorageConnector<?> connector) {

    if(connector instanceof final SQLConnector sql && sql.dialect() instanceof final TNEDialect tne) {
      sql.executeUpdate(tne.accountPurge(
                                DataConfig.yaml().getInt("Data.Purge.Accounts.Days")
                                        ),
                        new Object[]{});
    }
  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param account   The object to be stored.
   */
  @Override
  public void store(final StorageConnector<?> connector, @NotNull final Account account, @Nullable final String identifier) {

    if(connector instanceof final SQLConnector sql && sql.dialect() instanceof final TNEDialect tne) {

      PluginCore.log().debug("Saving Account with ID: " + identifier + " Name: " + account.getName(), DebugLevel.STANDARD);

      //store the basic account information(accounts table)
      final int test = sql.executeUpdate(tne.saveAccount(),
                                         new Object[]{
                                                 account.getIdentifier().toString(),
                                                 account.getName(),
                                                 (account.type()),
                                                 new java.sql.Timestamp(account.getCreationDate()),
                                                 account.getPin(),
                                                 account.getStatus().identifier(),
                                                 account.getName(),
                                                 account.getPin(),
                                                 account.getStatus().identifier(),
                                                 });

      PluginCore.log().debug("Account Insert Executed correctly: " + test + " - " + identifier, DebugLevel.DETAILED);

      if(account instanceof final PlayerAccount playerAccount) {

        //Player account storage.(players_accounts table)
        final int test2 = sql.executeUpdate(tne.savePlayer(),
                                            new Object[]{
                                                    account.getIdentifier().toString(),
                                                    new java.sql.Timestamp(playerAccount.getLastOnline()),
                                                    new java.sql.Timestamp(playerAccount.getLastOnline())
                                            });
        PluginCore.log().debug("Player Account Insert Executed correctly: " + test2 + " - " + identifier, DebugLevel.DETAILED);

      }

      if(account instanceof final SharedAccount shared) {

        //Non-player accounts.(non_players_accounts table)
        final String owner = (shared.getOwner() == null)? account.getIdentifier().toString() :
                             shared.getOwner().toString();
        sql.executeUpdate(tne.saveNonPlayer(),
                          new Object[]{
                                  account.getIdentifier().toString(),
                                  owner,
                                  owner
                          });

        //Account members(account_members table)
        for(final Member member : shared.getMembers().values()) {
          for(final Map.Entry<String, Boolean> entry : member.getPermissions().entrySet()) {
            sql.executeUpdate(tne.saveMembers(),
                              new Object[]{
                                      member.getId().toString(),
                                      account.getIdentifier().toString(),
                                      entry.getKey(),
                                      entry.getValue(),
                                      entry.getValue()
                              }
                             );
          }
        }
      }

      final AccountSaveCallback callback = new AccountSaveCallback(account);
      PluginCore.callbacks().call(callback);

      TNECore.instance().storage().storeAll(account.getIdentifier().toString());
    }
  }

  /**
   * Used to store all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void storeAll(final StorageConnector<?> connector, @Nullable final String identifier) {

    if(connector instanceof SQLConnector) {
      for(final Account account : TNECore.eco().account().getAccounts().values()) {
        store(connector, account, account.getIdentifier().toString());
      }
    }
  }

  @Override
  public void delete(final StorageConnector<?> connector, @NotNull final String identifier) {

    PluginCore.log().debug("Deleting Account with ID: " + identifier, DebugLevel.STANDARD);

    if(connector instanceof final SQLConnector sql && sql.dialect() instanceof final TNEDialect tne) {

      sql.executeUpdate(tne.deleteAccount(), new Object[]{ identifier });
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
  public Optional<Account> load(final StorageConnector<?> connector, @NotNull final String identifier) {

    if(connector instanceof final SQLConnector sql && sql.dialect() instanceof final TNEDialect tne) {

      Account account = null;

      //Loading/creating our account object.
      try(final ResultSet result = sql.executeQuery(tne.loadAccount(),
                                                    new Object[]{
                                                      identifier
                                              })) {
        if(result.next()) {
          final String type = result.getString("account_type");

          //create our account from the type
          final AccountAPIResponse response = TNECore.eco().account().createAccount(identifier,
                                                                                    result.getString("username"),
                                                                                    !(type.equalsIgnoreCase("player") ||
                                                                                      type.equalsIgnoreCase("bedrock")));
          if(response.getResponse().success()) {

            //load our basic account information
            if(response.getAccount().isPresent()) {
              account = response.getAccount().get();

              account.setStatus(TNECore.eco().account().findStatus(result.getString("status")));
              account.setCreationDate(result.getTimestamp("created").getTime());
              account.setPin(result.getString("pin"));
            }
          }
        }

      } catch(final SQLException e) {
        e.printStackTrace();
      }

      if(account != null) {

        //Load our player account info
        if(account instanceof final PlayerAccount playerAccount) {
          try(final ResultSet result = sql.executeQuery(tne.loadPlayer(),
                                                        new Object[]{
                                                          identifier
                                                  })) {
            if(result.next()) {
              playerAccount.setLastOnline(result.getTimestamp("last_online").getTime());
            }
          } catch(final SQLException e) {
            e.printStackTrace();
          }
        }

        //load our shared account info
        if(account instanceof final SharedAccount shared) {
          try(final ResultSet result = sql.executeQuery(tne.loadNonPlayer(),
                                                        new Object[]{
                                                          identifier
                                                  })) {
            if(result.next()) {
              shared.setOwner(UUID.fromString(result.getString("owner")));
            }
          } catch(final SQLException e) {
            e.printStackTrace();
          }

          //Load our members for shared accounts
          try(final ResultSet result = sql.executeQuery(tne.loadMembers(),
                                                        new Object[]{
                                                          identifier
                                                  })) {
            while(result.next()) {
              shared.addPermission(UUID.fromString(result.getString("uid")),
                                   result.getString("perm"),
                                   result.getBoolean("perm_value")
                                  );
            }
          } catch(final SQLException e) {
            e.printStackTrace();
          }

        }

        final Collection<HoldingsEntry> holdings = TNECore.instance().storage().loadAll(HoldingsEntry.class, identifier);
        for(final HoldingsEntry entry : holdings) {
          account.getWallet().setHoldings(entry);
        }

        final AccountLoadCallback callback = new AccountLoadCallback(account);
        PluginCore.callbacks().call(callback);
      }

      return Optional.ofNullable(account);
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
  public Collection<Account> loadAll(final StorageConnector<?> connector, @Nullable final String identifier) {

    final Collection<Account> accounts = new ArrayList<>(); // is this required? Not entirely sure it is - seems maybe a waste

    if(connector instanceof final SQLConnector sql && sql.dialect() instanceof final TNEDialect tne) {

      final List<String> ids = new ArrayList<>();
      try(final ResultSet result = sql.executeQuery(tne.loadAccounts(),
                                                    new Object[]{})) {
        while(result.next()) {
          ids.add(result.getString("uid"));
        }

      } catch(final SQLException e) {
        e.printStackTrace();
      }

      for(final String id : ids) {

        final Optional<Account> loaded = load(connector, id);
        if(loaded.isPresent()) {
          accounts.add(loaded.get());
          TNECore.eco().account().uuidProvider().store(new UUIDPair(loaded.get().getIdentifier(), loaded.get().getName()));
        }
      }
    }
    return accounts;
  }
}