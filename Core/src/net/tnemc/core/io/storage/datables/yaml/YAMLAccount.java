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

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.shared.Member;
import net.tnemc.core.api.callback.account.AccountLoadCallback;
import net.tnemc.core.api.callback.account.AccountSaveCallback;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.id.UUIDPair;
import net.tnemc.plugincore.core.io.storage.Datable;
import net.tnemc.plugincore.core.io.storage.StorageConnector;
import net.tnemc.plugincore.core.utils.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  public void purge(final StorageConnector<?> connector) {
    //TODO: Yaml Purge
  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param account   The object to be stored.
   */
  @Override
  public void store(final StorageConnector<?> connector, @NotNull final Account account, @Nullable final String identifier) {

    PluginCore.log().debug("Saving Account with ID: " + identifier + " Name: " + account.getName(), DebugLevel.STANDARD);

    //check if our file is in use.
    final String file = "accounts/" + identifier + ".yml";
    while(TNECore.yaml().inUse(file)) {

      try {
        Thread.sleep(1000);
      } catch(InterruptedException e) {
        e.printStackTrace();
      }
    }

    TNECore.yaml().add(file);

    final File accFile = new File(PluginCore.directory(), file);
    if(!accFile.exists()) {
      try {
        accFile.createNewFile();
      } catch(IOException ignore) {

        PluginCore.log().error("Issue creating account file. Account: " + account.getName(), DebugLevel.OFF);
        return;
      }
    }


    YamlDocument yaml = null;
    try {
      yaml = YamlDocument.create(accFile);
    } catch(IOException ignore) {

      PluginCore.log().error("Issue loading account file. Account: " + account.getName(), DebugLevel.OFF);
      return;
    }

    yaml.set("Info.ID", account.getIdentifier().toString());
    yaml.set("Info.Name", account.getName());
    yaml.set("Info.Type", account.type());
    yaml.set("Info.Status", account.getStatus().identifier());
    yaml.set("Info.CreationDate", account.getCreationDate());
    yaml.set("Info.Pin", account.getPin());

    if(account instanceof PlayerAccount playerAccount) {
      yaml.set("Info.LastOnline", playerAccount.getLastOnline());
    }

    if(account instanceof SharedAccount shared) {
      final String owner = (shared.getOwner() == null)? account.getIdentifier().toString() :
                           shared.getOwner().toString();

      yaml.set("Info.Owner", owner);

      for(final Member member : shared.getMembers().values()) {
        for(final Map.Entry<String, Boolean> entry : member.getPermissions().entrySet()) {

          yaml.set("Members." + member.getId().toString() + "." + entry.getKey(), entry.getValue());
        }
      }
    }
    try {
      yaml.save();

      final AccountSaveCallback callback = new AccountSaveCallback(account);
      PluginCore.callbacks().call(callback);

      yaml = null;
    } catch(IOException ignore) {
      PluginCore.log().error("Issue saving account file. Account: " + account.getName());
      return;
    }
    TNECore.yaml().remove(file);

    TNECore.instance().storage().storeAll(account.getIdentifier().toString());
  }

  /**
   * Used to store all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void storeAll(final StorageConnector<?> connector, @Nullable final String identifier) {

    for(final Account account : TNECore.eco().account().getAccounts().values()) {
      store(connector, account, account.getIdentifier().toString());
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

    final File accFile = new File(PluginCore.directory(), "accounts/" + identifier + ".yml");
    if(!accFile.exists()) {
      PluginCore.log().error("Null account file passed to YAMLAccount.load. Account: " + identifier, DebugLevel.OFF);
      return Optional.empty();
    }
    return load(accFile, identifier);
  }

  public Optional<Account> load(final File accFile, final String identifier) {

    if(accFile == null) {

      PluginCore.log().error("Null account file passed to YAMLAccount.load. Account: " + identifier, DebugLevel.OFF);
      return Optional.empty();
    }

    YamlDocument yaml = null;
    try {
      yaml = YamlDocument.create(accFile);
    } catch(Exception ignore) {

      PluginCore.log().error("Issue loading account file. Account: " + identifier + ". You may need to remove this account file! This is due to a previous server crash or improper shutdown and not a bug.", DebugLevel.OFF);
      return Optional.empty();
    }

    if(yaml != null) {

      Account account = null;

      //Validate account file
      if(!yaml.contains("Info.Name") || !yaml.contains("Info.Type")) {

        PluginCore.log().error("Invalid account file. Account: " + identifier + ". You may need to remove this account file! This is due to a previous server crash or improper shutdown and not a bug.", DebugLevel.OFF);
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

          final Section section = yaml.getSection("Members");
          for(final Object memberObj : section.getKeys()) {

            final String member = (String)memberObj;
            for(final Object permissionObj : section.getSection(member).getKeys()) {

              final String permission = (String)permissionObj;
              shared.addPermission(UUID.fromString(member), permission,
                                   yaml.getBoolean("Members." + member +
                                                   "." + permission));
            }
          }
        }

        final Collection<HoldingsEntry> holdings = TNECore.instance().storage().loadAll(HoldingsEntry.class, identifier);
        for(final HoldingsEntry entry : holdings) {
          account.getWallet().setHoldings(entry);
        }

        final AccountLoadCallback callback = new AccountLoadCallback(account);
        PluginCore.callbacks().call(callback);
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
  public Collection<Account> loadAll(final StorageConnector<?> connector, @Nullable final String identifier) {

    final Collection<Account> accounts = new ArrayList<>();

    for(final File file : IOUtil.getYAMLs(new File(PluginCore.directory(), "accounts"))) {

      try {
        final Optional<Account> loaded = load(file, file.getName().replace(".yml", ""));
        if(loaded.isPresent()) {
          accounts.add(loaded.get());
          TNECore.eco().account().uuidProvider().store(new UUIDPair(loaded.get().getIdentifier(), loaded.get().getName()));
        }
      } catch(Exception ignore) {
        PluginCore.log().error("Issue loading account file. File: " + file.getName() + ". You may need to remove this account file! This is due to a previous server crash or improper shutdown and not a bug.", DebugLevel.OFF);
      }
    }
    return accounts;
  }
}