package net.tnemc.core.currency.calculations;
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

import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.item.ItemDenomination;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CalculationProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface CalculationProvider {

  /**
   * Used to calculate the holdings of the inventory materials present.
   * @return The {@link BigDecimal} representation of the inventory materials balance value.
   */
  default BigDecimal calculateHoldings(CurrencyDataOld data) {
    BigDecimal holdings = BigDecimal.ZERO;

    for(Map.Entry<BigDecimal, Denomination> entry : data.getMaterialValues().entrySet()) {
      final int amount = data.getInventoryMaterials().getOrDefault(((ItemDenomination)entry.getValue()).getMaterial(), 0);

      if(amount > 0) {
        holdings = holdings.add(entry.getKey().multiply(new BigDecimal("" + amount)));
      }
    }
    return holdings;
  }

  default void clearItems(CurrencyDataOld data) {
    for(Map.Entry<String, Integer> entry : data.getInventoryMaterials().entrySet()) {
      final Optional<ItemDenomination> tier = data.getCurrency().getDenominationByMaterial(entry.getKey());

      tier.ifPresent(tneTier -> removeMaterials(data, tneTier, entry.getValue()));
    }
  }

  default void setItems(CurrencyDataOld data, BigDecimal amount) {
    final BigDecimal holdings = calculateHoldings(data);

    if(holdings.compareTo(amount) == 0) return;

    if(holdings.compareTo(amount) > 0) {
      calculateChange(data, holdings.subtract(amount));
    }
    provideMaterials(data, amount.subtract(holdings), null);
  }

  /**
   * Used to calculate the materials that need to be removed when a player in an item-based economy
   * has money taken from their account.
   * @param change The amount to remove from the player's account.
   * @return The {@link BigDecimal} representation of the leftover amount that couldn't be removed
   * because there's no more materials left to remove.
   */
  default BigDecimal calculateChange(CurrencyDataOld data, BigDecimal change) {
    BigDecimal workingAmount = change;

    final NavigableMap<BigDecimal, Denomination> values = data.getMaterialValues().descendingMap();
    Map.Entry<BigDecimal, Denomination> lowestWhole = findLowestGreaterThan(data, BigDecimal.ONE);

    int instance = 0;
    do {

      if(instance > 0) {
        if(!data.getInventoryMaterials().containsKey(((ItemDenomination)lowestWhole.getValue()).getMaterial())) {
          lowestWhole = findLowestGreaterThan(data, BigDecimal.ONE);
        }
        provideMaterials(data, workingAmount, BigDecimal.ONE);
        provideMaterials(data, lowestWhole.getKey().subtract(workingAmount), BigDecimal.ONE);
        removeMaterials(data, lowestWhole.getValue(), 1);
      }

      for(Map.Entry<BigDecimal, Denomination> entry : values.entrySet()) {

        if(entry.getKey().compareTo(workingAmount) > 0) {
          continue;
        }

        if(workingAmount.compareTo(BigDecimal.ZERO) == 0) {
          continue;
        }

        if(!data.getInventoryMaterials().containsKey(((ItemDenomination)entry.getValue()).getMaterial())) {
          continue;
        }

        final int max = data.getInventoryMaterials().get(((ItemDenomination)entry.getValue()).getMaterial());

        int amountPossible = workingAmount.divide(entry.getKey(), RoundingMode.DOWN).toBigInteger().intValue();

        if(amountPossible > max) {
          amountPossible = max;
        }

        workingAmount = workingAmount.subtract(entry.getKey().multiply(new BigDecimal(amountPossible)));
        removeMaterials(data, entry.getValue(), amountPossible);
      }
      instance++;
    } while(workingAmount.compareTo(BigDecimal.ZERO) != 0 && data.getInventoryMaterials().size() > 0);
    return workingAmount;
  }

  /**
   * Used to exchange an amount to inventory items. This is mostly used for when a larger denomination
   * needs to be broken into smaller denominations for calculation purposes.
   * @param amount The amount that the items should add up to.
   * @param threshold The threshold that all items should be valued under when given back. I.E. if
   * the threshold is 1 then all denominations given to the inventory should be less than 1. If you don't wish
   * to use a threshold then you should pass null.
   */
  default void provideMaterials(CurrencyDataOld data, BigDecimal amount, @Nullable BigDecimal threshold) {
    BigDecimal workingAmount = amount;
    final NavigableMap<BigDecimal, Denomination> values = data.getMaterialValues().descendingMap();

    for(Map.Entry<BigDecimal, Denomination> entry : values.entrySet()) {

      if(threshold != null && entry.getKey().compareTo(threshold) >= 0) {
        continue;
      }

      if(entry.getKey().compareTo(workingAmount) > 0) {
        continue;
      }

      if(workingAmount.compareTo(BigDecimal.ZERO) == 0) {
        continue;
      }


      final int amountPossible = workingAmount.divide(entry.getKey(), RoundingMode.DOWN).toBigInteger().intValue();
      workingAmount = workingAmount.subtract(entry.getKey().multiply(new BigDecimal(amountPossible)));
      int holding = data.getInventoryMaterials().getOrDefault(((ItemDenomination)entry.getValue()).getMaterial(), 0);
      data.getInventoryMaterials().put(((ItemDenomination)entry.getValue()).getMaterial(), holding + amountPossible);
      data.provideMaterials(entry.getValue(), amountPossible);
    }
  }

  /**
   * Used to remove tiers from the {@link CurrencyDataOld} being worked with.
   * @param tier The tier name in String form.
   * @param amount The amount of the material to remove from working materials.
   */
  default void removeMaterials(CurrencyDataOld data, Denomination tier, Integer amount) {
    data.removeMaterials(tier, amount);
  }

  /**
   * Finds the lowest denomination that is greater than the specified value.
   * @param value The value to utilize for the comparison.
   * @return The Map Entry containing the lowest denomination information.
   */
  default Map.Entry<BigDecimal, Denomination> findLowestGreaterThan(CurrencyDataOld data, BigDecimal value) {
    Map.Entry<BigDecimal, Denomination> entryLowest = null;
    for(Map.Entry<BigDecimal, Denomination> entry : data.getMaterialValues().entrySet()
        .stream().filter(entry->entry.getKey().compareTo(value) >= 0)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).entrySet()) {

      if(entryLowest == null || entryLowest.getKey().compareTo(entry.getKey()) > 0) {
        if(data.getInventoryMaterials().containsKey(((ItemDenomination)entry.getValue()).getMaterial())) {
          entryLowest = entry;
        }
      }
    }
    return entryLowest;
  }
}
