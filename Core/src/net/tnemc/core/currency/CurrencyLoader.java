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
   */
  void loadCurrencies();

  /**
   * Loads a specific currency.
   * @param currency The name of the currency to load.
   */
  boolean loadCurrency(final String currency);

  /**
   * Loads all denominations for a currency.
   * @param currency The currency of the denomination.
   */
  boolean loadDenominations(Currency currency);

  /**
   * Loads all denominations for a currency.
   * @param currency The currency of the denomination.
   * @param denomination The name of the denomination to load.
   */
  boolean loadDenomination(Currency currency, final String denomination);
}