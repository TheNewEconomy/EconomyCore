package net.tnemc.core.currency.loader;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 2/20/2022.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyLoader;

/**
 * Used to load currencies without the concept of "major" or "minor" denominations.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class ModernCurrencyLoader implements CurrencyLoader {

  /**
   * Loads all currencies.
   */
  @Override
  public void loadCurrencies() {

  }

  /**
   * Loads all denominations for the specified currency.
   *
   * @param currency The currency to load the denominations of.
   * @return
   */
  @Override
  public boolean loadDenominations(Currency currency) {
    return false;
  }
}
