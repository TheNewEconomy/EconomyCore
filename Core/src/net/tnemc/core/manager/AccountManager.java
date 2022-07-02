package net.tnemc.core.manager;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.Account;
import net.tnemc.core.id.UUIDPair;
import net.tnemc.core.id.UUIDProvider;
import net.tnemc.core.id.impl.provider.BaseUUIDProvider;

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

  private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

  protected UUIDProvider uuidProvider = new BaseUUIDProvider();

  /**
   * Used to find an {@link Account account} from a {@link UUID unique identifier}.
   * @param id The id to use in the search.
   * @return An optional containing the {@link Account account} if it exists, otherwise an empty
   * optional.
   */
  public Optional<Account> findAccount(final UUID id) {
    return Optional.ofNullable(accounts.get(id));
  }

  /**
   * Used to find an {@link Account account} from a name.
   * @param name The name to use in the search.
   * @return An optional containing the {@link Account account} if it exists, otherwise an empty
   * optional.
   */
  public Optional<Account> findAccount(final String name) {
    final Optional<UUIDPair> id = uuidProvider.retrieve(name);

    if(id.isPresent()) {
      return findAccount(id.get().getIdentifier());
    }
    return Optional.empty();
  }

  public UUIDProvider uuidProvider() {
    return uuidProvider;
  }
}