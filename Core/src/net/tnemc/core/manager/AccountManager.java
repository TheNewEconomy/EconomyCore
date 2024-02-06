package net.tnemc.core.manager;


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
import net.tnemc.core.account.AccountStatus;
import net.tnemc.core.account.GeyserAccount;
import net.tnemc.core.account.NonPlayerAccount;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.status.AccountLockedStatus;
import net.tnemc.core.account.status.AccountNormalStatus;
import net.tnemc.core.account.status.AccountRestrictedStatus;
import net.tnemc.core.actions.EconomyResponse;
import net.tnemc.core.actions.response.AccountResponse;
import net.tnemc.core.api.callback.account.AccountCreateCallback;
import net.tnemc.core.api.callback.account.AccountDeleteCallback;
import net.tnemc.core.api.callback.account.AccountTypesCallback;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.config.MainConfig;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.id.UUIDPair;
import net.tnemc.plugincore.core.id.UUIDProvider;
import net.tnemc.plugincore.core.id.impl.provider.BaseUUIDProvider;
import net.tnemc.plugincore.core.io.maps.EnhancedHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Manages everything related to accounts.
 *
 * @see Account
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AccountManager {

  private final EnhancedHashMap<String, Account> accounts = new EnhancedHashMap<>();

  private final EnhancedHashMap<String, AccountStatus> statuses = new EnhancedHashMap<>();

  private final LinkedHashMap<Class<? extends SharedAccount>, Function<String, Boolean>> types = new LinkedHashMap<>();

  protected UUIDProvider uuidProvider = new BaseUUIDProvider();

  /*
   * list for if player accounts are loading in this will mean that players in this list
   * will have balances loaded from the database for item-based currencies.
   */
  protected final List<String> loading = new ArrayList<>();

  /*
   * List for players that are loading in, but need to have their item currency imported from the inventory
   * not the DB since they are new, or the currency doesn't exist.
   */
  protected final List<String> importing = new ArrayList<>();

  /**
   * Used to create a new non-player account based on the provided name.
   * @param name The name to use for this account.
   * @return A correlating {@link AccountAPIResponse response} containing the results.
   */
  public AccountAPIResponse createAccount(final String name) {
    return createAccount(name, name, true);
  }

  /**
   * Used to create a new account based on the provided identifier and name.
   * @param identifier The identifier to use for the creation, if this is a player then this should
   *                   be the String value of the UUID for that player.
   * @param name The name to use for this account.
   * @return A correlating {@link AccountAPIResponse response} containing the results.
   */
  public AccountAPIResponse createAccount(final String identifier, final String name) {
    return createAccount(identifier, name, false);
  }

  /**
   * Used to create a new account based on the provided identifier and name.
   * @param identifier The identifier to use for the creation, if this is a player then this should
   *                   be the String value of the UUID for that player.
   * @param name The name to use for this account.
   * @param nonPlayer True if the new account should be a non-player account.
   * @return A correlating {@link AccountAPIResponse response} containing the results.
   */
  public AccountAPIResponse createAccount(final String identifier, final String name, boolean nonPlayer) {
    if(identifier != null && accounts.containsKey(identifier)) {
      PluginCore.log().debug("Account Exists Already. ID: " + identifier);

      return new AccountAPIResponse(accounts.get(identifier), AccountResponse.ALREADY_EXISTS);
    }

    final Optional<UUIDPair> pair = uuidProvider.retrieve(name);
    if(MainConfig.yaml().getBoolean("Core.Offline", false) && pair.isPresent() && accounts.containsKey(pair.get().getIdentifier().toString())) {
      PluginCore.log().debug("Offline Account Exists Already. ID: " + identifier);

      return new AccountAPIResponse(accounts.get(pair.get().getIdentifier().toString()), AccountResponse.ALREADY_EXISTS);
    }

    Account account;

    if(!nonPlayer && UUIDProvider.validate(name)) {
      try {

        if(identifier == null) {
          //Throw NPE if identifier is null to push down to our catch block.
          PluginCore.log().debug("Tried to createAccount for player using null identifier. Name: " + name);
          throw new NullPointerException("Tried to createAccount for player using null identifier.");
        }

        final UUID id = UUID.fromString(identifier);
        account = new PlayerAccount(id, name);

        uuidProvider.store(new UUIDPair(id, name));
      } catch(Exception ignore) {

        //Our identifier is an invalid UUID, let's search for it.
        final Optional<UUID> id = PluginCore.server().fromName(name);

        if(id.isPresent()) {
          account = new PlayerAccount(id.get(), name);

          uuidProvider.store(new UUIDPair(id.get(), name));
        } else {

          return new AccountAPIResponse(null, AccountResponse.CREATION_FAILED);
        }
      }
    } else if(!nonPlayer && name.startsWith(MainConfig.yaml().getString("Core.Server.Geyser", ".")) || !nonPlayer && PluginCore.server().online(name)) {

      //This is most definitely a geyser player, they're online but not of valid names
      try {

        if(identifier == null) {
          //Throw NPE if identifier is null to push down to our catch block.
          PluginCore.log().debug("Tried to createAccount for player using null identifier. Name: " + name);
          throw new NullPointerException("Tried to createAccount for player using null identifier.");
        }

        final UUID id = UUID.fromString(identifier);
        account = new GeyserAccount(id, name);

        uuidProvider.store(new UUIDPair(id, name));
      } catch(Exception ignore) {
        return new AccountAPIResponse(null, AccountResponse.CREATION_FAILED);
      }
    } else {
      final Optional<SharedAccount> nonPlayerAccount = createNonPlayerAccount(name);

      if(nonPlayerAccount.isEmpty()) {
        return new AccountAPIResponse(null, AccountResponse.CREATION_FAILED);
      }
      account = nonPlayerAccount.get();
    }

    final AccountCreateCallback callback = new AccountCreateCallback(account);
    if(PluginCore.callbacks().call(callback)) {
      return new AccountAPIResponse(account, AccountResponse.CREATION_FAILED_PLUGIN);
    }

    accounts.put(account.getIdentifier(), account);

    TNECore.instance().storage().store(account, account.getIdentifier());

    try {
      uuidProvider.store(new UUIDPair(UUID.fromString(account.getIdentifier()), account.getName()));
    } catch(Exception ignore) {
      //identifier isn't an uuid, so it'll be a string, most likely a non-player.
    }

    return new AccountAPIResponse(account, AccountResponse.CREATED);
  }

  /**
   * Used to create a Non-Player account based on the name. This method will search the
   * {@link #types} map for a suitable alternative.
   *
   * @param name The name to use for the creation.
   * @return An Optional containing the new account class if it was able to be created, otherwise an
   * empty Optional.
   */
  public Optional<SharedAccount> createNonPlayerAccount(final String name) {

    for(Map.Entry<Class<? extends SharedAccount>, Function<String, Boolean>> entry : types.entrySet()) {
      if(entry.getValue().apply(name)) {
        try {
          return Optional.of(entry.getKey().getDeclaredConstructor(String.class, String.class)
                                  .newInstance(name, name));
        } catch(Exception e) {
          PluginCore.log().error("An error occured while trying to create a new NonPlayer Account" +
                                  "for : " + name, e, DebugLevel.STANDARD);
        }
      }
    }

    return Optional.empty();
  }


  /**
   * Used to delete an {@link Account account} from an identifier.
   *
   * @param identifier The identifier to use for the search.
   * @return The corresponding {@link EconomyResponse response}.
   */
  public EconomyResponse deleteAccount(@NotNull final UUID identifier) {
    return deleteAccount(identifier.toString());
  }

  /**
   * Used to delete an {@link Account account} from an identifier.
   *
   * @param identifier The identifier to use for the search.
   * @return The corresponding {@link EconomyResponse response}.
   */
  public EconomyResponse deleteAccount(@NotNull final String identifier) {
    if(!accounts.containsKey(identifier)) {
      return AccountResponse.DOESNT_EXIST;
    }

    final AccountDeleteCallback callback = new AccountDeleteCallback(identifier);
    PluginCore.callbacks().call(callback);

    accounts.remove(identifier);
    return AccountResponse.DELETED;
  }

  /**
   * Used to find an {@link Account account} from a {@link UUID unique identifier}.
   * @param id The id to use in the search.
   * @return An optional containing the {@link Account account} if it exists, otherwise an empty
   * optional.
   */
  public Optional<Account> findAccount(final UUID id) {
    return Optional.ofNullable(accounts.get(id.toString()));
  }

  /**
   * Used to find an {@link PlayerAccount account} from a {@link UUID unique identifier}.
   * @param id The id to use in the search.
   * @return An optional containing the {@link PlayerAccount account} if it exists, otherwise an empty
   * optional.
   */
  public Optional<PlayerAccount> findPlayerAccount(final UUID id) {
    final Account account = accounts.get(id.toString());

    if((account instanceof PlayerAccount)) {
      return Optional.of((PlayerAccount)account);
    }
    return Optional.empty();
  }

  /**
   * Used to find an {@link Account account} from a string identifier, this could be a name or a
   * different identifier.
   * @param identifier The identifier to use in the search.
   * @return An optional containing the {@link Account account} if it exists, otherwise an empty
   * optional.
   */
  public Optional<Account> findAccount(final String identifier) {

    //Check first to see if the identifier is in the accounts map.
    //This would return non-player accounts.
    final Account account = accounts.get(identifier);
    if(account != null) {
      return Optional.of(account);
    }

    final Optional<UUIDPair> id = uuidProvider.retrieve(identifier);

    if(id.isPresent()) {
      return findAccount(id.get().getIdentifier());
    }
    return Optional.empty();
  }

  public AccountStatus findStatus(final String identifier) {
    if(statuses.containsKey(identifier)) {
      return statuses.get(identifier);
    }
    return statuses.get("normal");
  }

  /**
   * Adds a new {@link AccountStatus} status.
   * @param status The account status to add
   */
  public void addAccountStatus(final AccountStatus status) {
    statuses.put(status);
  }

  /**
   * Adds a new {@link Account} type. These should extend the {@link SharedAccount}.
   * @param type The class for this type.
   * @param check The function that should be used to check if a given String identifier, usually name,
   *              is valid for this account type.
   */
  public void addAccountType(final Class<? extends NonPlayerAccount> type, Function<String, Boolean> check) {
    types.put(type, check);
  }

  /**
   * Adds our default built-in account types.
   */
  public void addDefaultTypes() {

    PluginCore.callbacks().call(new AccountTypesCallback());

    addAccountType(NonPlayerAccount.class, (value)->true);
  }

  /**
   * Adds our default built-in account types.
   */
  public void addDefaultStatuses() {

    addAccountStatus(new AccountLockedStatus());
    addAccountStatus(new AccountNormalStatus());
    addAccountStatus(new AccountRestrictedStatus());
  }

  public EnhancedHashMap<String, AccountStatus> getStatuses() {
    return statuses;
  }

  public EnhancedHashMap<String, Account> getAccounts() {
    return accounts;
  }

  public UUIDProvider uuidProvider() {
    return uuidProvider;
  }

  public List<String> getLoading() {
    return loading;
  }

  public List<String> getImporting() {
    return importing;
  }
}