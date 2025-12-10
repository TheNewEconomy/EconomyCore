package net.tnemc.core.currency.calculations;

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

import net.tnemc.core.currency.Denomination;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

/**
 * ItemCalculations
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ItemCalculations<I> {

  /**
   * Used to calculate the holdings of the inventory materials present.
   *
   * @return The {@link BigDecimal} representation of the inventory materials balance value.
   */
  public BigDecimal calculateHoldings(final CalculationData<I> data) {

    BigDecimal holdings = BigDecimal.ZERO;

    for(final Map.Entry<BigDecimal, Denomination> entry : data.getDenominations().entrySet()) {
      final int amount = data.getInventoryMaterials().getOrDefault(entry.getKey(), 0);

      if(amount > 0) {
        holdings = holdings.add(entry.getKey().multiply(new BigDecimal(amount)));
      }
    }
    return holdings;
  }

  public void setItems(final CalculationData<I> data, final BigDecimal amount) {

    final BigDecimal holdings = calculateHoldings(data);

    if(holdings.compareTo(amount) == 0) return;

    if(holdings.compareTo(amount) > 0) {
      calculation(data, holdings.subtract(amount));
      return;
    }
    provideMaterials(data, amount.subtract(holdings));
  }

  /**
   * Used to calculate the materials that need to be removed when a player in an item-based economy
   * has money taken from their account.
   *
   * This is a rewritten version that fixes the change calculation bug where players were not
   * receiving the correct amount of change when paying with larger denominations.
   *
   * @param amountToRemove The amount to remove from the player's account.
   *
   * @return The {@link BigDecimal} representation of the leftover amount that couldn't be removed
   * because there's no more materials left to remove.
   */
  public BigDecimal calculation(final CalculationData<I> data, final BigDecimal amountToRemove) {

    PluginCore.log().debug("=== Starting Calculation ===", DebugLevel.DEVELOPER);
    PluginCore.log().debug("Amount to remove: " + amountToRemove.toPlainString(), DebugLevel.DEVELOPER);

    // Track what we can pay with directly without breaking down larger denominations
    BigDecimal paidAmount = BigDecimal.ZERO;
    final TreeMap<BigDecimal, Integer> toRemoveDirectly = new TreeMap<>();

    // Create a copy of inventory materials
    final TreeMap<BigDecimal, Integer> availableMaterials = new TreeMap<>(data.getInventoryMaterials());

    // First pass: try to pay using existing denominations (largest to smallest, but only those <= amountToRemove)
    BigDecimal remaining = amountToRemove;

    for(final Map.Entry<BigDecimal, Integer> entry : availableMaterials.descendingMap().entrySet()) {

      final BigDecimal denomination = entry.getKey();

      // skip denominations larger than what we need to pay
      if(denomination.compareTo(remaining) > 0) {
        continue;
      }

      final int available = entry.getValue();

      // Calculate how many of this denominations we can use
      final int needed = remaining.divide(denomination, 0, RoundingMode.DOWN).intValue();
      final int toUse = Math.min(needed, available);

      if(toUse > 0) {
        toRemoveDirectly.put(denomination, toUse);
        final BigDecimal value = denomination.multiply(new BigDecimal(toUse));
        paidAmount = paidAmount.add(value);
        remaining = remaining.subtract(value);

        PluginCore.log().debug("Using " + toUse + "x " + denomination.toPlainString() + " = " + value.toPlainString(), DebugLevel.DEVELOPER);
      }

      if(remaining.compareTo(BigDecimal.ZERO) == 0) {
        break;
      }
    }

    PluginCore.log().debug("After direct payment - paid: " + paidAmount.toPlainString() + ", Remaining: " + remaining.toPlainString(), DebugLevel.DEVELOPER);

    // If more is still needed to pay, break down a larger denomination
    BigDecimal changeToGiveBack = BigDecimal.ZERO;
    Denomination denominationToBreakDown = null;

    if(remaining.compareTo(BigDecimal.ZERO) > 0) {

      // Find the smallest denomination larger than the remaining amount
      for(final Map.Entry<BigDecimal, Integer> entry : availableMaterials.entrySet()) {

        final BigDecimal denomination = entry.getKey();
        final int available = entry.getValue();

        // Check if this denomination is larger than what is needed
        if(denomination.compareTo(remaining) > 0 && available > 0) {

          // Check if all of this denomination has already been used
          final int alreadyUsed = toRemoveDirectly.getOrDefault(denomination, 0);
          if(available > alreadyUsed) {

            denominationToBreakDown = data.getDenominations().get(denomination);
            changeToGiveBack = denomination.subtract(remaining);

            PluginCore.log().debug("Breaking down 1x " + denomination.toPlainString() + ", change: " + changeToGiveBack.toPlainString(), DebugLevel.DEVELOPER);

            // Mark denomination for removal
            toRemoveDirectly.put(denomination, alreadyUsed + 1);
            break;
          }
        }
      }

      if(denominationToBreakDown == null) {
        PluginCore.log().debug("Cannot pay - insufficient funds", DebugLevel.DEVELOPER);
        // Cannot pay because insufficient funds
        return remaining;
      }
    }

    // Execute the removals
    for(final Map.Entry<BigDecimal, Integer> entry : toRemoveDirectly.entrySet()) {

      final Denomination denom = data.getDenominations().get(entry.getKey());
      final int amount = entry.getValue();

      PluginCore.log().debug("Removing " + amount + "x " + entry.getKey().toPlainString(), DebugLevel.DEVELOPER);
      data.removeMaterials(denom, amount);
    }

    // Give back change if any
    if(changeToGiveBack.compareTo(BigDecimal.ZERO) > 0) {

      PluginCore.log().debug("Providing change: " + changeToGiveBack.toPlainString(), DebugLevel.DEVELOPER);
      provideMaterials(data, changeToGiveBack);
    }

    PluginCore.log().debug("=== Calculation Completed ===", DebugLevel.DEVELOPER);
    return BigDecimal.ZERO;
  }

  /**
   * Used to exchange an amount to inventory items. This is mostly used for when a larger
   * denomination needs to be broken into smaller denominations for calculation purposes.
   *
   * @param amount The amount that the items should add up to.
   */
  public void provideMaterials(final CalculationData<I> data, final BigDecimal amount) {

    data.getCalculator().initialize(data.getCurrency(), data.getInventoryMaterials());

    for(final Map.Entry<BigDecimal, Integer> entry : data.getCalculator().breakdown(amount).entrySet()) {

      final int holding = data.getInventoryMaterials().getOrDefault(entry.getKey(), 0);
      data.getInventoryMaterials().put(entry.getKey(), holding + entry.getValue());
      data.provideMaterials(data.getDenominations().get(entry.getKey()), entry.getValue());
    }
  }

  /**
   * Used to remove tiers from the {@link CalculationData} being worked with.
   *
   * @param denom  The denom name in String form.
   * @param amount The amount of the material to remove from working materials.
   */
  public void removeMaterials(final CalculationData<I> data, final Denomination denom, final Integer amount) {

    data.removeMaterials(denom, amount);
  }
}
