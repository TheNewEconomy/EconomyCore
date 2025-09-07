package net.tnemc.core.currency.calcs;


/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.calculations.CalculationData;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * MoneyCalculationProvider
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public interface MoneyCalculationProvider<I> {

  /**
   * Provides an instance of {@link ChangeCalculator}, which is responsible for calculating
   * the change to be returned in various denominations based on specific calculations.
   *
   * @return A non-null instance of {@link ChangeCalculator}.
   */
  @NotNull
  ChangeCalculator changeCalculator();

  /**
   * Used to calculate the holdings of the inventory materials present.
   *
   * @return The {@link BigDecimal} representation of the inventory materials balance value.
   */
  BigDecimal calculateHoldings(final @NotNull CalculationData<I> data);

  /**
   * Sets the specified items in the calculation data with the specified amount.
   * This method processes the given data and adjusts the inventory materials according to the provided amount.
   *
   * @param data The calculation data containing inventory, denominations, and associated currency details.
   * @param amount The number of items to set in the calculation data.
   */
  void setItems(final @NotNull CalculationData<I> data, final @NotNull BigDecimal amount);

  /**
   * Used to calculate the materials that need to be removed when a player in an item-based economy
   * has money taken from their account.
   *
   * @param data The calculation data containing inventory, denominations, and associated currency details.
   * @param change The amount to remove from the player's account.
   *
   * @return The {@link BigDecimal} representation of the leftover amount that couldn't be removed
   * because there's no more materials left to remove.
   */
  BigDecimal calculateRemoval(final @NotNull CalculationData<I> data, final @NotNull BigDecimal change);

  /**
   * Provides a specific number of materials to the inventory, updating its internal tracking.
   * This method ensures that the required quantity is properly allocated and handles scenarios
   * where the inventory might be full or items need to be dropped externally.
   *
   * @param data The calculation data containing inventory and currency-related information.
   * @param amount The total number of materials to provide.
   */
  void provideMaterials(final @NotNull CalculationData<I> data, final @NotNull BigDecimal amount);

  /**
   * Removes a specific number of materials from the inventory based on the provided calculation data,
   * denomination, and amount. This method updates the internal tracking of inventory materials and
   * performs the necessary operations to remove the designated items.
   *
   * @param data The calculation data containing inventory and currency-related information.
   * @param denom The denomination of the materials to be removed.
   * @param amount The number of items to remove for the given denomination.
   */
  void removeMaterials(final @NotNull CalculationData<I> data, final @NotNull Denomination denom, final @NotNull Integer amount);
}