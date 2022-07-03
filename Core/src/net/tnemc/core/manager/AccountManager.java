package net.tnemc.core.manager;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.Account;
import net.tnemc.core.account.NonPlayerAccount;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.actions.EconomyResponse;
import net.tnemc.core.actions.response.AccountResponse;
import net.tnemc.core.manager.id.UUIDPair;
import net.tnemc.core.manager.id.UUIDProvider;
import net.tnemc.core.manager.id.impl.provider.BaseUUIDProvider;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages everything related to accounts.
 *
 * @see Account
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AccountManager {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  protected UUIDProvider uuidProvider = new BaseUUIDProvider();

  public EconomyResponse createAccount(final String identifier, final String name) {
    if(accounts.containsKey(identifier)) {
      return AccountResponse.ALREADY_EXISTS;
    }

    Account account;

    if(UUIDProvider.validate(name)) {
      try {
        final UUID id = UUID.fromString(identifier);
        account = new PlayerAccount(id, name);

        uuidProvider.store(new UUIDPair(id, name));
      } catch(Exception ignore) {
        return AccountResponse.CREATION_FAILED;
      }
    } else {

      //TODO: Create Non-Player Accounts.
      account = new NonPlayerAccount("", "", null);
    }

    accounts.put(account.getIdentifier(), account);
    return AccountResponse.CREATED;
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

  public UUIDProvider uuidProvider() {
    return uuidProvider;
  }
}