package net.tnemc.core.compatibility.helper;
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

import net.tnemc.core.currency.item.ItemDenomination;

import java.util.HashMap;
import java.util.Map;

/**
 * CraftingRecipe represents a crafting recipe that needs to be registered.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CraftingRecipe {

  private final Map<Character, String> ingredients = new HashMap<>();

  private final String[] rows = new String[3];

  private final boolean shaped;
  private final int amount;

  private final ItemDenomination result;

  public CraftingRecipe(boolean shaped, int amount, ItemDenomination result) {
    this.shaped = shaped;
    this.amount = amount;
    this.result = result;
  }

  public Map<Character, String> getIngredients() {
    return ingredients;
  }

  public ItemDenomination getResult() {
    return result;
  }

  public String[] getRows() {
    return rows;
  }

  public boolean isShaped() {
    return shaped;
  }

  public int getAmount() {
    return amount;
  }
}