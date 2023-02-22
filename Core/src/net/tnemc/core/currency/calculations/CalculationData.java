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

import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.item.AbstractItemStack;

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

//TODO: Test this class.
public class CalculationData<INV> {

  private final Map<String, Integer> inventoryMaterials = new HashMap<>();

  private final TreeMap<BigDecimal, Denomination> denomValues = new TreeMap<>();

  INV inventory;
  final ItemCurrency currency;
  final UUID player;
  boolean dropped = false;
  boolean failedDrop = false;

  public CalculationData(final ItemCurrency currency, INV inventory, final UUID player) {
    this.denomValues.putAll(currency.getDenominations());
    this.currency = currency;
    this.inventory = inventory;
    this.player = player;

    inventoryCounts();
  }

  public Map<String, Integer> getInventoryMaterials() {
    return inventoryMaterials;
  }

  public TreeMap<BigDecimal, Denomination> getDenomValues() {
    return denomValues;
  }

  public ItemCurrency getCurrency() {
    return currency;
  }

  private void inventoryCounts() {
    for(Map.Entry<BigDecimal, Denomination> entry : denomValues.entrySet()) {
      if(entry.getValue() instanceof final ItemDenomination denomination) {
        final AbstractItemStack<Object> stack = TNECore.server().denominationToStack(denomination);
        inventoryMaterials.put(denomination.singular(), TNECore.server().calculations().count(stack, inventory));
      }
    }
  }

  public void removeMaterials(Denomination denomination, Integer amount) {
    final AbstractItemStack<Object> stack = TNECore.server().denominationToStack((ItemDenomination)denomination);
    final int contains = inventoryMaterials.getOrDefault(denomination.singular(), 0);
    if(contains == amount) {
      inventoryMaterials.remove(denomination.singular());
      TNECore.server().calculations().removeAll(stack, inventory);
      return;
    }
    final int left = contains - amount;
    inventoryMaterials.put(denomination.singular(), left);
    final AbstractItemStack<Object> stackClone = stack.amount(left);
    TNECore.server().calculations().removeItem(stackClone, inventory);
  }

  public void provideMaterials(final Denomination denomination, Integer amount) {
    final AbstractItemStack<Object> stack = TNECore.server().denominationToStack((ItemDenomination)denomination).amount(amount);
    Collection<AbstractItemStack<Object>> left = TNECore.server().calculations().giveItems(Collections.singletonList(stack), inventory);

    if(left.size() > 0) {
      failedDrop = TNECore.server().calculations().drop(left, player);

      dropped = true;
    }
  }
}