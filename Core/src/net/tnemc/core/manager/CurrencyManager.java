package net.tnemc.core.manager;

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