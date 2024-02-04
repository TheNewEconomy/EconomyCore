package net.tnemc.core.currency;

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

import net.tnemc.core.utils.exceptions.NoValidCurrenciesException;

import java.io.File;

/**
 * Used to load currencies.
 *
 * @see Currency
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface CurrencyLoader {

  /**
   * Loads all currencies.
   * @param directory The directory to load the currencies from.
   * @throws NoValidCurrenciesException When no valid currencies can be loaded.
   */
  void loadCurrencies(final File directory) throws NoValidCurrenciesException;

  /**
   * Loads a specific currency.
   * @param directory The directory to load the currency from.
   * @param name The name of the currency to load.
   */
  boolean loadCurrency(final File directory, final String name);

  /**
   * Loads a specific currency.
   * @param directory The directory to load the currency from.
   * @param curDirectory The file of the currency
   */
  boolean loadCurrency(final File directory, final File curDirectory);

  /**
   * Loads all denominations for a currency.
   * @param directory The directory to load the denominations from.
   * @param currency The currency of the denomination.
   */
  boolean loadDenominations(final File directory, Currency currency);

  /**
   * Loads all denominations for a currency.
   *
   * @param directory The directory to load the denomination from.
   * @param currency     The currency of the denomination.
   * @param name The name of the denomination to load.
   */
  boolean loadDenomination(final File directory, Currency currency, final String name);

  /**
   * Loads all denominations for a currency.
   *
   * @param currency     The currency of the denomination.
   * @param denomFile The file of the denomination to load.
   */
  boolean loadDenomination(Currency currency, final File denomFile);
}