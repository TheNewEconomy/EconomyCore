package net.tnemc.core.manager;

/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Manages everything related to accounts.
 *
 * @author creatorfromhell
 * @see Account
 * @since 0.1.2.0
 */
public class AccountManager {

  protected final Map<String, UUID> accountSwaps = new HashMap<>();
  protected final UUIDProvider uuidProvider = new BaseUUIDProvider();
  //exclusion lists for account argument parameters.
  protected final List<Pattern> regexExclusions = new ArrayList<>();
  protected final List<String> exclusions = new ArrayList<>();
  /*
   * list for if player accounts are loading in this will mean that players in this list
   * will have balances loaded from the database for item-based currencies.
   */
  protected final List<UUID> loading = new ArrayList<>();
  /*
   * List for players that are loading in, but need to have their item currency imported from the inventory
   * not the DB since they are new, or the currency doesn't exist.
   */
  protected final List<UUID> importing = new ArrayList<>();
  private final EnhancedHashMap<String, Account> accounts = new EnhancedHashMap<>();
  private final EnhancedHashMap<String, AccountStatus> statuses = new EnhancedHashMap<>();
  private final LinkedHashMap<Class<? extends SharedAccount>, Function<String, Boolean>> types = new LinkedHashMap<>();

  /**
   * Used to create a new non-player account based on the provided name.
   *
   * @param name The name to use for this account.
   *
   * @return A correlating {@link AccountAPIResponse response} containing the results.
   */
  public AccountAPIResponse createAccount(final String name) {

    return createAccount(name, name, true);
  }

  /**
   * Used to create a new account based on the provided identifier and name.
   *
   * @param identifier The identifier to use for the creation, if this is a player then this should
   *                   be the String value of the UUID for that player.
   * @param name       The name to use for this account.
   *
   * @return A correlating {@link AccountAPIResponse response} containing the results.
   */
  public AccountAPIResponse createAccount(final String identifier, final String name) {

    return createAccount(identifier, name, false);
  }

  /**
   * Used to create a new account based on the provided identifier and name.
   *
   * @param identifier The identifier to use for the creation, if this is a player then this should
   *                   be the String value of the UUID for that player.
   * @param name       The name to use for this account.
   * @param nonPlayer  True if the new account should be a non-player account.
   *
   * @return A correlating {@link AccountAPIResponse response} containing the results.
   */
  public AccountAPIResponse createAccount(final String identifier, final String name, final boolean nonPlayer) {
    return createAccount(identifier, name, nonPlayer, false);
  }

  /**
   * Create an account based on the provided identifier, name, and flags.
   *
   * @param identifier The identifier for the account.
   * @param name The name of the account.
   * @param nonPlayer A boolean flag indicating if the account is for a non-player.
   * @param skipDB A boolean flag to skip the database saving on creation.
   * @return An AccountAPIResponse object with the result of the account creation process.
   */
  public AccountAPIResponse createAccount(final String identifier, final String name, final boolean nonPlayer, final boolean skipDB) {

    PluginCore.log().debug("Create Account Called! ID: " + identifier + " Name: " + name);
    if(name.contains("§")) {
      PluginCore.log().debug("==== AccountAPIResponse with color code! ====", DebugLevel.DEVELOPER);

      final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
      for(int i = 0; i < elements.length; i++) {
        final StackTraceElement s = elements[i];
        PluginCore.log().debug("\tat " + s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + ")", DebugLevel.DEVELOPER);
      }
      PluginCore.log().debug("==== End Stack Print ====", DebugLevel.DEVELOPER);
    }

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
      } catch(final Exception ignore) {

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
      } catch(final Exception ignore) {
        return new AccountAPIResponse(null, AccountResponse.CREATION_FAILED);
      }
    } else {
      final Optional<SharedAccount> nonPlayerAccount = createNonPlayerAccount(identifier, name);

      if(nonPlayerAccount.isEmpty()) {
        return new AccountAPIResponse(null, AccountResponse.CREATION_FAILED);
      }
      account = nonPlayerAccount.get();
    }

    final AccountCreateCallback callback = new AccountCreateCallback(account);
    if(PluginCore.callbacks().call(callback)) {
      return new AccountAPIResponse(account, AccountResponse.CREATION_FAILED_PLUGIN);
    }

    accounts.put(account.getIdentifier().toString(), account);

    if(!skipDB) {
      TNECore.instance().storage().store(account, account.getIdentifier().toString());
    }

    try {
      uuidProvider.store(new UUIDPair(account.getIdentifier(), account.getName()));
    } catch(final Exception ignore) {
      //identifier isn't an uuid, so it'll be a string, most likely a non-player.
    }
    return new AccountAPIResponse(account, AccountResponse.CREATED);
  }

  /**
   * Used to create a Non-Player account based on the name. This method will search the
   * {@link #types} map for a suitable alternative.
   *
   * @param name The name to use for the creation.
   *
   * @return An Optional containing the new account class if it was able to be created, otherwise an
   * empty Optional.
   */
  public Optional<SharedAccount> createNonPlayerAccount(final String identifier, final String name) {

    UUID uuid = UUID.randomUUID();

    try {
      uuid = UUID.fromString(identifier);
    } catch(final Exception ignore) {
      //stay with the random UUID if the identifier passed isn't a UUID.
    }

    for(final Map.Entry<Class<? extends SharedAccount>, Function<String, Boolean>> entry : types.entrySet()) {

      if(entry.getValue().apply(name)) {
        try {

          final SharedAccount account = entry.getKey().getDeclaredConstructor(UUID.class, String.class).newInstance(uuid, name);

          return Optional.of(account);
        } catch(final Exception e) {
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
   *
   * @return The corresponding {@link EconomyResponse response}.
   */
  public EconomyResponse deleteAccount(@NotNull final UUID identifier) {

    return deleteAccount(identifier.toString());
  }

  /**
   * Used to delete an {@link Account account} from an identifier.
   *
   * @param identifier The identifier to use for the search.
   *
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
   *
   * @param id The id to use in the search.
   *
   * @return An optional containing the {@link Account account} if it exists, otherwise an empty
   * optional.
   */
  public Optional<Account> findAccount(final UUID id) {

    return Optional.ofNullable(accounts.get(id.toString()));
  }

  /**
   * Used to find an {@link PlayerAccount account} from a {@link UUID unique identifier}.
   *
   * @param id The id to use in the search.
   *
   * @return An optional containing the {@link PlayerAccount account} if it exists, otherwise an
   * empty optional.
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
   *
   * @param identifier The identifier to use in the search.
   *
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
   *
   * @param status The account status to add
   */
  public void addAccountStatus(final AccountStatus status) {

    statuses.put(status);
  }

  /**
   * Adds a new {@link Account} type. These should extend the {@link SharedAccount}.
   *
   * @param type  The class for this type.
   * @param check The function that should be used to check if a given String identifier, usually
   *              name, is valid for this account type.
   */
  public void addAccountType(final Class<? extends NonPlayerAccount> type, final Function<String, Boolean> check) {

    types.put(type, check);
  }

  /**
   * Adds our default built-in account types.
   */
  public void addDefaultTypes() {

    PluginCore.callbacks().call(new AccountTypesCallback());

    for(final Class<? extends SharedAccount> account : types.keySet()) {
      PluginCore.log().debug("Adding default account types: " + account.getClass().getSimpleName());
    }

    addAccountType(NonPlayerAccount.class, (value)->true);
  }

  public void addSwap(final String swapType, final UUID account, final UUID swapAccount) {

    accountSwaps.put(swapType + ":" + account.toString(), swapAccount);
  }

  public void removeSwap(final String swapType, final UUID account) {

    accountSwaps.remove(swapType + ":" + account.toString());
  }

  public UUID swap(final String swapType, final UUID account) {

    return accountSwaps.getOrDefault(swapType + ":" + account.toString(), account);
  }

  public boolean excluded(final String name) {

    for(final Pattern pattern : regexExclusions) {
      if(pattern.matcher(name).matches()) return true;
    }

    for(final String str : exclusions) {
      if(name.contains(str)) return true;
    }
    return false;
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

  public List<UUID> getLoading() {

    return loading;
  }

  public List<UUID> getImporting() {

    return importing;
  }

  public List<Pattern> regexExclusions() {

    return regexExclusions;
  }

  public List<String> exclusions() {

    return exclusions;
  }
}