package net.tnemc.core.currency;

/*
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 9/22/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

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
   */
  void loadCurrencies(final File directory);

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