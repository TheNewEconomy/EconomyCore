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

import java.math.BigDecimal;
import java.util.Map;

/**
 * CalculationProvider
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
   * @param change The amount to remove from the player's account.
   *
   * @return The {@link BigDecimal} representation of the leftover amount that couldn't be removed
   * because there's no more materials left to remove.
   */
  public BigDecimal calculation(final CalculationData<I> data, final BigDecimal change) {

    data.getCalculator().initialize(data.getCurrency(), data.getInventoryMaterials());

    data.getCalculator().calculateDenominationCounts(change);

    for(final Map.Entry<BigDecimal, Integer> entry : data.getCalculator().getToAdd().entrySet()) {

      Integer amount = entry.getValue();

      final Integer removeAmount = data.getCalculator().getToRemove().getOrDefault(entry.getKey(), 0);

      if(removeAmount > 0 && amount > removeAmount) {

        amount = amount - removeAmount;
      }

      if(removeAmount > 0 && amount <= removeAmount) {

        continue;
      }

      //PluginCore.log().debug("calculation Amount getToAdd:" + entry.getKey().toPlainString() + " Count:" + amount, DebugLevel.DEVELOPER);

      data.provideMaterials(data.getDenominations().get(entry.getKey()), amount);
    }

    for(final Map.Entry<BigDecimal, Integer> entry : data.getCalculator().getToRemove().entrySet()) {

      //PluginCore.log().debug("calculation Amount getToRemove:" + entry.getKey().toPlainString() + " Count:" + entry.getValue(), DebugLevel.DEVELOPER);

      data.removeMaterials(data.getDenominations().get(entry.getKey()), entry.getValue());
    }


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
