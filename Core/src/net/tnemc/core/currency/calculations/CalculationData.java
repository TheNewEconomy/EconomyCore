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

import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * CalculationData is used to take the information for an {@link net.tnemc.core.currency.item.ItemCurrency}
 * and break it down in order to perform Item-based calculations.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CalculationData<I> {

  private final Map<BigDecimal, Integer> inventoryMaterials = new HashMap<>();

  //Our Calculator
  private final MonetaryCalculation calculator = new MonetaryCalculation();

  private final I inventory;
  private final ItemCurrency currency;
  private final UUID player;
  private boolean dropped = false;
  private boolean failedDrop = false;

  public CalculationData(final ItemCurrency currency, I inventory, final UUID player) {
    this.currency = currency;
    this.inventory = inventory;
    this.player = player;

    inventoryCounts();
  }

  public Map<BigDecimal, Integer> getInventoryMaterials() {
    return inventoryMaterials;
  }

  public TreeMap<BigDecimal, Denomination> getDenominations() {
    return currency.getDenominations();
  }

  public MonetaryCalculation getCalculator() {
    return calculator;
  }

  public ItemCurrency getCurrency() {
    return currency;
  }

  public void inventoryCounts() {
    for(Map.Entry<BigDecimal, Denomination> entry : currency.getDenominations().entrySet()) {
      if(entry.getValue() instanceof final ItemDenomination denomination) {

        final AbstractItemStack<?> stack = TNECore.instance().denominationToStack(denomination);
        final int count = PluginCore.server().calculations().count((AbstractItemStack<Object>)stack, inventory);

        if(count > 0) {
          inventoryMaterials.put(entry.getKey(), count);
        }
      }
    }
  }

  public void removeMaterials(Denomination denomination, Integer amount) {
    final AbstractItemStack<?> stack = TNECore.instance().denominationToStack((ItemDenomination)denomination);
    final int contains = inventoryMaterials.getOrDefault(denomination.weight(), 0);

    if(contains == amount) {
      inventoryMaterials.remove(denomination.weight());
      PluginCore.log().debug("CalculationData - removeMaterials - equals: Everything equals, remove all materials. Weight: " + denomination.weight(), DebugLevel.DEVELOPER);
      PluginCore.server().calculations().removeAll((AbstractItemStack<Object>)stack, inventory);
      return;
    }

    final int left = contains - amount;
    PluginCore.log().debug("CalculationData - removeMaterials - left: " + left + "Weight: " + denomination.weight(), DebugLevel.DEVELOPER);
    inventoryMaterials.put(denomination.weight(), left);
    final AbstractItemStack<?> stackClone = stack.amount(amount);
    PluginCore.server().calculations().removeItem((AbstractItemStack<Object>)stackClone, inventory);
  }

  public void provideMaterials(final Denomination denomination, Integer amount) {
    int contains = (inventoryMaterials.getOrDefault(denomination.weight(), amount));

    final AbstractItemStack<?> stack = TNECore.instance().denominationToStack((ItemDenomination)denomination).amount(amount);
    final Collection<AbstractItemStack<Object>> left = PluginCore.server().calculations().giveItems(Collections.singletonList((AbstractItemStack<Object>)stack), inventory);

    if(left.size() > 0) {
      contains = contains - left.stream().findFirst().get().amount();
      failedDrop = PluginCore.server().calculations().drop(left, player);

      dropped = true;
    }

    PluginCore.log().debug("Weight: " + denomination.weight() + " - Amount: " + amount, DebugLevel.DETAILED);

    inventoryMaterials.put(denomination.weight(), contains);
  }

  public boolean isDropped() {
    return dropped;
  }

  public boolean isFailedDrop() {
    return failedDrop;
  }
}