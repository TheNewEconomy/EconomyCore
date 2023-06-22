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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.shared.Member;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.io.storage.Datable;
import net.tnemc.core.io.storage.StorageConnector;
import net.tnemc.core.manager.id.UUIDPair;
import net.tnemc.core.utils.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * YAMLAccount
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class YAMLAccount implements Datable<Account> {

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
    //TODO: Yaml Purge
  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param account    The object to be stored.
   */
  @Override
  public void store(StorageConnector<?> connector, @NotNull Account account, @Nullable String identifier) {

    final File accFile = new File(TNECore.directory(), "accounts/" + account.getIdentifier() + ".yml");
    if(!accFile.exists()) {
      accFile.mkdirs();
    }


    YamlFile yaml = null;
    try {
      yaml = YamlFile.loadConfiguration(accFile);
    } catch(IOException ignore) {

      TNECore.log().error("Issue loading account file. Account: " + account.getName());
    }

    if(yaml != null) {
      yaml.set("Info.ID", account.getIdentifier());
      yaml.set("Info.Name", account.getName());
      yaml.set("Info.Type", account.type());
      yaml.set("Info.Status", account.getStatus().identifier());
      yaml.set("Info.CreationDate", account.getCreationDate());
      yaml.set("Info.Pin", account.getPin());

      if(account instanceof PlayerAccount) {
        yaml.set("Info.LastOnline", ((PlayerAccount)account).getLastOnline());
      }

      if(account instanceof SharedAccount) {
        final String owner = (((SharedAccount)account).getOwner() == null)? account.getIdentifier() :
            ((SharedAccount)account).getOwner().toString();

        yaml.set("Info.Owner", owner);

        for(Member member : ((SharedAccount)account).getMembers().values()) {
          for(Map.Entry<String, Boolean> entry : member.getPermissions().entrySet()) {

            yaml.set("Members." + member.getId().toString() + "." + entry.getKey(), entry.getValue());
          }
        }
      }
      TNECore.storage().storeAll(account.getIdentifier());
    }
  }

  /**
   * Used to store all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void storeAll(StorageConnector<?> connector, @Nullable String identifier) {
    for(Account account : TNECore.eco().account().getAccounts().values()) {
      store(connector, account, account.getIdentifier());
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

    final File accFile = new File(TNECore.directory(), "accounts/" + identifier + ".yml");
    if(!accFile.exists()) {
      accFile.mkdirs();
    }
    return load(connector, accFile, identifier);
  }

  public Optional<Account> load(StorageConnector<?> connector, File accFile, final String identifier) {
    if(accFile == null) {

      TNECore.log().error("Null account file passed to YAMLAccount.load. Account: " + identifier);
      return Optional.empty();
    }

    YamlFile yaml = null;
    try {
      yaml = YamlFile.loadConfiguration(accFile);
    } catch(IOException ignore) {

      TNECore.log().error("Issue loading account file. Account: " + identifier);
    }

    if(yaml != null) {

      Account account = null;

      final String type = yaml.getString("Info.Type");

      //create our account from the type
      final AccountAPIResponse response = TNECore.eco().account().createAccount(identifier,
                                                                                yaml.getString("Info.Name"),
                                                                                !(type.equalsIgnoreCase("player") ||
                                                                                    type.equalsIgnoreCase("bedrock")));
      if(response.getResponse().success()) {

        //load our basic account information
        if(response.getAccount().isPresent()) {
          account = response.getAccount().get();

          account.setStatus(TNECore.eco().account().findStatus(yaml.getString("Info.Status")));
          account.setCreationDate(yaml.getLong("Info.CreationDate"));
          account.setPin(yaml.getString("Info.Pin"));
        }
      }

      if(account != null) {

        if(account instanceof PlayerAccount) {
          ((PlayerAccount)account).setLastOnline(yaml.getLong("Info.LastOnline"));
        }

        if(account instanceof SharedAccount) {
          if(yaml.contains("Members")) {
            final ConfigurationSection section = yaml.getConfigurationSection("Members");
            for(String member : section.getKeys(false)) {

              for(String permission : section.getConfigurationSection(member).getKeys(false)) {

                ((SharedAccount)account).addPermission(UUID.fromString(member), permission,
                                                       yaml.getBoolean("Members." + member +
                                                                           "." + permission));
              }
            }
          }
        }

        Collection<HoldingsEntry> holdings = TNECore.storage().loadAll(HoldingsEntry.class, identifier);
        for(HoldingsEntry entry : holdings) {
          account.getWallet().setHoldings(entry);
        }
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
  public Collection<Account> loadAll(StorageConnector<?> connector, @Nullable String identifier) {
    final Collection<Account> accounts = new ArrayList<>();

    for(File file : IOUtil.getYAMLs(new File(TNECore.directory(), "accounts"))) {

      final Optional<Account> loaded = load(connector, file, file.getName());
      if(loaded.isPresent()) {
        accounts.add(loaded.get());
        TNECore.eco().account().uuidProvider().store(new UUIDPair(UUID.fromString(loaded.get().getIdentifier()), loaded.get().getName()));
      }
    }
    return accounts;
  }
}