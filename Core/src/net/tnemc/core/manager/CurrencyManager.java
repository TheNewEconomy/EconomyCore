package net.tnemc.core.manager;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * CurrencyManager
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyManager {

  private final Map<String, Currency> currencies = new HashMap<>();
  private final Map<String, CurrencyType> types = new HashMap<>();

  /**
   * Used to find a currency based on its identifier.
   * @param identifier The identifier to look for.
   * @return An Optional containing the currency if it exists, otherwise an empty Optional.
   */
  public Optional<Currency> findCurrency(final String identifier) {
    return Optional.ofNullable(currencies.get(identifier));
  }

  /**
   * Used to find a currency type based on its identifier.
   * @param identifier The identifier to look for.
   * @return An Optional containing the currency type if it exists, otherwise an empty Optional.
   */
  public Optional<CurrencyType> findType(final String identifier) {
    return Optional.ofNullable(types.get(identifier));
  }
}