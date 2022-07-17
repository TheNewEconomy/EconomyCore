package net.tnemc.core.currency;

/*
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 10/16/2021.
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
public interface CurrencySaver {

  /**
   * Saves all currencies.
   * @param directory The directory used for saving.
   */
  void saveCurrencies(final File directory);

  /**
   * Saves a specific currency
   * @param directory The directory used for saving.
   * @param currency The currency to save.
   */
  void saveCurrency(final File directory, Currency currency);

  /**
   * Saves a specific currency denomination
   * @param directory The directory used for saving.
   * @param currency The currency of the denomination.
   * @param denomination The denomination to save.
   */
  void saveDenomination(final File directory, Currency currency, Denomination denomination);
}