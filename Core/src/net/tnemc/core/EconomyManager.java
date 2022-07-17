package net.tnemc.core;

/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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