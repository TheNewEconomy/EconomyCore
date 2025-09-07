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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * ItemsCalculation
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class ItemsCalculation {

  private final TreeSet<BigDecimal> denominationTypes = new TreeSet<>(Collections.reverseOrder());
  private final TreeMap<BigDecimal, Integer> inventoryMaterials = new TreeMap<>();

  private final TreeMap<BigDecimal, Integer> toAdd = new TreeMap<>();
  private final TreeMap<BigDecimal, Integer> toRemove = new TreeMap<>();

  //Calculate denominations amounts

  //Calculate the amount of items needed to pay for a given amount. This method has the
  //potential to fail if the player has items that are not in the currency's itemModel.
}