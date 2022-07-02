package net.tnemc.core.manager;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.Account;

import java.util.Map;
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

  private final Map<UUID, String> ids = new ConcurrentHashMap<>();
  private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();


}