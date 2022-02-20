package net.tnemc.core.managers;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 1/2/2022.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

import net.tnemc.core.account.Account;
import net.tnemc.core.io.cache.RefreshConcurrentMap;
import net.tnemc.core.io.cache.listeners.AccountMapListener;

/**
 * Class used to manage all {@link Account accounts}. This should hold all methods used to create, access, and modify
 * accounts.
 *
 * @see Account
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class AccountManager {

  private final RefreshConcurrentMap<String, Account> accounts;

  public AccountManager() {
    accounts = new RefreshConcurrentMap<>(true, 300, new AccountMapListener());
  }


}