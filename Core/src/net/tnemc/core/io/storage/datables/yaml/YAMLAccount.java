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
import net.tnemc.core.api.callback.account.AccountLoadCallback;
import net.tnemc.core.api.callback.account.AccountSaveCallback;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.currency.Currency;
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

    TNECore.log().debug("Saving Account with ID: " + identifier + " Name: " + account.getName(), DebugLevel.STANDARD);

    final File accFile = new File(TNECore.directory(), "accounts/" + account.getIdentifier() + ".yml");
    if(!accFile.exists()) {
      try {
        accFile.createNewFile();
      } catch(IOException e) {

        TNECore.log().error("Issue loading account file. Account: " + account.getName());
        return;
      }
    }


    YamlFile yaml = null;
    try {
      yaml = YamlFile.loadConfiguration(accFile);
    } catch(IOException ignore) {

      TNECore.log().error("Issue loading account file. Account: " + account.getName());
      return;
    }

    if(yaml != null) {
      yaml.set("Info.ID", account.getIdentifier());
      yaml.set("Info.Name", account.getName());
      yaml.set("Info.Type", account.type());
      yaml.set("Info.Status", account.getStatus().identifier());
      yaml.set("Info.CreationDate", account.getCreationDate());
      yaml.set("Info.Pin", account.getPin());

      if(account instanceof PlayerAccount playerAccount) {
        yaml.set("Info.LastOnline", playerAccount.getLastOnline());

        final Optional<PlayerProvider> provider = TNECore.server().findPlayer(playerAccount.getUUID());

        if(provider.isPresent()) {
          final String region = TNECore.eco().region().getMode().region(provider.get());
          for(Currency currency : TNECore.eco().currency().getCurrencies(region)) {

            if(currency.type().supportsItems()) {

              for(HoldingsEntry entry : account.getHoldings(region, currency.getUid())) {

                //account.get().setHoldings(entry, entry.getHandler());
                account.getWallet().setHoldings(entry);
              }
            }
          }
        }
      }

      if(account instanceof SharedAccount shared) {
        final String owner = (shared.getOwner() == null)? account.getIdentifier() :
            shared.getOwner().toString();

        yaml.set("Info.Owner", owner);

        for(Member member : shared.getMembers().values()) {
          for(Map.Entry<String, Boolean> entry : member.getPermissions().entrySet()) {

            yaml.set("Members." + member.getId().toString() + "." + entry.getKey(), entry.getValue());
          }
        }
      }
      try {
        yaml.save();

        final AccountSaveCallback callback = new AccountSaveCallback(account);
        TNECore.callbacks().call(callback);

        yaml = null;
      } catch(IOException e) {
        TNECore.log().error("Issue saving account file. Account: " + account.getName());
        return;
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
      TNECore.log().error("Null account file passed to YAMLAccount.load. Account: " + identifier);
      return Optional.empty();
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

      //Validate account file
      if(!yaml.contains("Info.Name") || !yaml.contains("Info.Type")) {

        TNECore.log().error("Invalid account file. Account: " + identifier, DebugLevel.OFF);
        return Optional.empty();
      }

      final String type = yaml.getString("Info.Type");

      //create our account from the type
      final AccountAPIResponse response = TNECore.eco().account().createAccount(identifier,
                                                                                yaml.getString("Info.Name"),
                                                                                !(type.equalsIgnoreCase("player") ||
                                                                                    type.equalsIgnoreCase("bedrock")));
      if(response.getResponse().success() && response.getAccount().isPresent()) {

        //load our basic account information
        account = response.getAccount().get();

        account.setStatus(TNECore.eco().account().findStatus(yaml.getString("Info.Status")));
        account.setCreationDate(yaml.getLong("Info.CreationDate"));
        account.setPin(yaml.getString("Info.Pin"));
      }

      if(account != null) {

        if(account instanceof PlayerAccount playerAccount) {
          playerAccount.setLastOnline(yaml.getLong("Info.LastOnline"));
        }

        if(account instanceof SharedAccount shared && yaml.contains("Members")) {
          final ConfigurationSection section = yaml.getConfigurationSection("Members");
          for(String member : section.getKeys(false)) {

            for(String permission : section.getConfigurationSection(member).getKeys(false)) {

              shared.addPermission(UUID.fromString(member), permission,
                                                     yaml.getBoolean("Members." + member +
                                                                         "." + permission));
            }
          }
        }

        final Collection<HoldingsEntry> holdings = TNECore.storage().loadAll(HoldingsEntry.class, identifier);
        for(HoldingsEntry entry : holdings) {
          account.getWallet().setHoldings(entry);
        }

        final AccountLoadCallback callback = new AccountLoadCallback(account);
        TNECore.callbacks().call(callback);
      }
      yaml = null;
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

      final Optional<Account> loaded = load(connector, file, file.getName().replace(".yml", ""));
      if(loaded.isPresent()) {
        accounts.add(loaded.get());
        TNECore.eco().account().uuidProvider().store(new UUIDPair(UUID.fromString(loaded.get().getIdentifier()), loaded.get().getName()));
      }
    }
    return accounts;
  }
}