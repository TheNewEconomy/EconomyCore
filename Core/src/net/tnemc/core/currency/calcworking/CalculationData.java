package net.tnemc.core.currency.calcworking;
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
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.providers.CalculationsProvider;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * CalculationData is used to take the information for an {@link net.tnemc.core.currency.item.ItemCurrency}
 * and break it down in order to perform Item-based calculations.
 *
 * @param <INV> Represents the server implementation's Inventory object.
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public abstract class CalculationData<INV, IT extends AbstractItemStack<S>, S, C extends CalculationsProvider<IT, S, INV>> {

  private final Map<String, Integer> inventoryMaterials = new HashMap<>();

  private final TreeMap<BigDecimal, Denomination> denomValues = new TreeMap<>();

  public CalculationData(ItemCurrency currency, INV inventory, C calcs) {
    this.denomValues.putAll(currency.getDenominations());

    inventoryCounts(inventory, calcs);
  }

  private void inventoryCounts(INV inventory, C calcs) {
    for(Map.Entry<BigDecimal, Denomination> entry : denomValues.entrySet()) {
      if(entry.getValue() instanceof ItemDenomination) {
        final ItemDenomination<S> denom = (ItemDenomination<S>)entry.getValue();
        inventoryMaterials.put(denom.getName(), calcs.count((IT)denom.buildStack(), inventory));
      }
    }
  }
}