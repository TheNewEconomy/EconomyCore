package net.tnemc.core.currency.calculations;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.currency.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * MonetaryCalculation
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MonetaryCalculation {

  private final TreeSet<BigDecimal> denominationTypes = new TreeSet<>(Collections.reverseOrder());
  private final TreeMap<BigDecimal, Integer> inventoryMaterials = new TreeMap<>();

  private final TreeMap<BigDecimal, Integer> toAdd = new TreeMap<>();
  private final TreeMap<BigDecimal, Integer> toRemove = new TreeMap<>();

  int attempt = 1;

  public void initialize(Currency currency, Map<BigDecimal, Integer> inventoryMaterials) {
    reset();

    denominationTypes.addAll(currency.getDenominations().keySet());
    this.inventoryMaterials.putAll(inventoryMaterials);
  }

  private void reset() {
    attempt = 1;
    denominationTypes.clear();
    inventoryMaterials.clear();
    toAdd.clear();
    toRemove.clear();
  }

  /**
   * Calculates the amount of denominations needed to pay for a given amount. This method has the
   * possibility of running twice. After the first attempt, if the amount can't be paid for then
   * it will break down a larger denomination and sort through everything after the change is added
   * into the mix.
   *
   * @param amount The amount to attempt to pay for.
   */
  public void calculateDenominationCounts(BigDecimal amount) {
    Map<BigDecimal, Integer> result = new HashMap<>();

    BigDecimal workingAmount = amount;

    // Make a copy of the available denominations counts
    TreeMap<BigDecimal, Integer> counts = new TreeMap<>(inventoryMaterials);

    final SortedMap<BigDecimal, Integer> lowerThanAmount = counts.headMap(amount, true);

    // Iterate over denominations in descending order
    for (Map.Entry<BigDecimal, Integer> entry : new TreeMap<>(lowerThanAmount).descendingMap().entrySet()) {

      final BigDecimal denomination = entry.getKey();

      // Calculate the maximum number of this denomination that can be used
      int maxCount = lowerThanAmount.get(denomination);
      int count = workingAmount.divide(denomination, 0, RoundingMode.DOWN).intValue();
      if (count > maxCount) {
        count = maxCount;
      }


      TNECore.log().debug("Working 1: " + workingAmount.toPlainString(), DebugLevel.DEVELOPER);
      TNECore.log().debug("Denom: " + denomination.toPlainString(), DebugLevel.DEVELOPER);
      TNECore.log().debug("maxCount: " + maxCount, DebugLevel.DEVELOPER);
      TNECore.log().debug("count: " + count, DebugLevel.DEVELOPER);

      // Subtract the value of these denominations from the total amount
      workingAmount = workingAmount.subtract(denomination.multiply(BigDecimal.valueOf(count)));

      TNECore.log().debug("Working 2: " + workingAmount.toPlainString(), DebugLevel.DEVELOPER);

      // Update the result map
      if (count > 0) {
        result.put(denomination, count);
      }

      if(workingAmount.compareTo(BigDecimal.ZERO) <= 0) {

        TNECore.log().debug("Working is zero break out", DebugLevel.DEVELOPER);
        break;
      }
    }

    TNECore.log().debug("Calculate comparison:" + workingAmount.compareTo(BigDecimal.ZERO), DebugLevel.DEVELOPER);
    // If there is any amount left over, it was not possible to pay for the full amount
    if (workingAmount.compareTo(BigDecimal.ZERO) > 0) {

      if(attempt <= 1) {

        final Map<BigDecimal, Integer> calculated = calculateBreakdowns(amount);

        combineMaps(inventoryMaterials, calculated);
        combineMaps(toAdd, calculated);

        attempt = 2;

        calculateDenominationCounts(amount);
        return;
      }
      return;
    }

    combineMaps(toRemove, result);
  }


  public Map<BigDecimal, Integer> calculateBreakdowns(BigDecimal amount) {


    // Get the entry with the smallest key greater than the given amount
    final Map.Entry<BigDecimal, Integer> higherEntry = inventoryMaterials.higherEntry(amount);

    if(higherEntry == null) {
      return new HashMap<>();
    }

    /*if(higherEntry == null) {
      higherEntry = inventoryMaterials.ceilingEntry(amount);
    }*/

    // Reduce the value associated with the higherEntry key by 1
    toRemove.put(higherEntry.getKey(), 1);

    // Calculate the difference between the higherEntry key and the given amount
    final BigDecimal difference = higherEntry.getKey().subtract(amount);

    // Create a new HashMap called counts with the breakdown of the original amount
    final Map<BigDecimal, Integer> counts = new HashMap<>(breakdown(amount));

    // Combine the counts map with the breakdown of the difference
    combineMaps(counts, breakdown(difference));

    // Return the counts map
    return counts;
  }

  /**
   * Calculates the number of each denomination required to make up a given
   * monetary amount, using a currency's denomination values.
   *
   * @param amount the monetary amount to break down into denominations
   *
   * @return a map of BigDecimal denominations and the number of each
   *         denomination required to make up the amount
   */
  public Map<BigDecimal, Integer> breakdown(BigDecimal amount) {

    // Create a new map to store the breakdown counts
    final Map<BigDecimal, Integer> counts = new HashMap<>();

    // Create a copy of the amount to work with
    BigDecimal workingAmount = amount;

    // Iterate through the available denominations in the denominationTypes list
    for(BigDecimal denomination : denominationTypes) {

      // Calculate the number of this denomination required to make up the remaining amount
      final int count = workingAmount.divide(denomination, 0, RoundingMode.DOWN).intValue();

      // If any of this denomination are required, subtract their value from the working amount
      // and add the count to the counts map
      if(count > 0) {
        workingAmount = workingAmount.subtract(denomination.multiply(BigDecimal.valueOf(count)));
        counts.put(denomination, count);
      }

      // If we've made up the entire amount, exit the loop
      if(workingAmount.compareTo(BigDecimal.ZERO) <= 0) break;
    }
    // Return the counts map
    return counts;
  }

  /**
   * Combines two maps of BigDecimal keys and Integer values, adding the values
   * of matching keys and adding new key-value pairs as necessary.
   *
   * @param exist the map to add values to
   * @param toAdd the map containing the values to add
   */
  private void combineMaps(Map<BigDecimal, Integer> exist, Map<BigDecimal, Integer> toAdd) {

    // Iterate through the toAdd map
    for(Map.Entry<BigDecimal, Integer> entry : toAdd.entrySet()) {

      // If the exist map already contains the key from the current toAdd entry,
      // add the value from the toAdd entry to the value in the exist map
      if(exist.containsKey(entry.getKey())) {

        exist.put(entry.getKey(), (exist.get(entry.getKey()) + entry.getValue()));

      } else {

        // Otherwise, add the new key-value pair to the exist map
        exist.put(entry.getKey(), entry.getValue());
      }
    }
  }

  public TreeMap<BigDecimal, Integer> getToAdd() {
    return toAdd;
  }

  public TreeMap<BigDecimal, Integer> getToRemove() {
    return toRemove;
  }
}