package net.tnemc.core;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.manager.AccountManager;
import net.tnemc.core.manager.CurrencyManager;
import net.tnemc.core.manager.DataManager;
import net.tnemc.core.manager.TransactionManager;
import net.tnemc.core.manager.TranslationManager;

/**
 * This class manages everything for the economy plugin, from language storage to holding instances
 * to other managers.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class EconomyManager {

  private final AccountManager accountManager;
  private final CurrencyManager currencyManager;
  private final TransactionManager transactionManager;
  private final TranslationManager translationManager;
  private final DataManager dataManager;

  private static EconomyManager instance;

  public EconomyManager() {
    instance = this;

    this.accountManager = new AccountManager();
    this.currencyManager = new CurrencyManager();
    this.transactionManager = new TransactionManager();
    this.translationManager = new TranslationManager();
    this.dataManager = new DataManager();
  }

  public AccountManager account() {
    return accountManager;
  }

  public CurrencyManager currency() {
    return currencyManager;
  }

  public TranslationManager translation() {
    return translationManager;
  }

  public TransactionManager transaction() {
    return transactionManager;
  }

  public DataManager data() {
    return dataManager;
  }

  public static EconomyManager instance() {
    return instance;
  }
}