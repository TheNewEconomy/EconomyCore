package net.tnemc.core.currency;

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

import net.tnemc.core.currency.item.ItemDenomination;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Represents a denomination for an {@link Currency currency}.
 *
 * @see Currency
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Denomination {

  private String single;
  private String plural;
  private BigDecimal weight;

  public Denomination(BigDecimal weight) {
    this.weight = weight;
    this.single = "Singular";
    this.plural = "Plural";
  }

  public static Denomination clone(final Denomination original, boolean item) {
    Denomination cloned = (item)? new ItemDenomination(original.weight()) : new Denomination(original.weight());

    cloned.setSingle(original.singular());
    cloned.setPlural(original.plural());
    cloned.setWeight(original.weight());

    if(cloned instanceof ItemDenomination clonedItem && original instanceof ItemDenomination itemDenomination) {

      clonedItem.setEnchantments(new ArrayList<>(itemDenomination.getEnchantments()));
      clonedItem.setFlags(new ArrayList<>(itemDenomination.getFlags()));
      clonedItem.setLore(new ArrayList<>(itemDenomination.getLore()));
      clonedItem.setMaterial(itemDenomination.getMaterial());
      clonedItem.setDamage(itemDenomination.getDamage());
      clonedItem.setName(itemDenomination.getName());
      clonedItem.setCustomModel(itemDenomination.getCustomModel());
      clonedItem.setTexture(itemDenomination.getTexture());
    }

    return cloned;
  }

  public boolean isItem() {
    return (this instanceof ItemDenomination);
  }

  public void setSingle(String single) {
    this.single = single;
  }

  public void setPlural(String plural) {
    this.plural = plural;
  }

  public String singular() {
    return single;
  }

  public String plural() {
    return plural;
  }

  public BigDecimal weight() {
    return weight;
  }

  public void setWeight(BigDecimal weight) {
    this.weight = weight;
  }
}