package net.tnemc.core.currency;

/*
 * The New Economy
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
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