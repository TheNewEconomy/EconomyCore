package net.tnemc.core.currency;

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

import net.tnemc.core.currency.item.ItemDenomination;

/**
 * Represents a denomination for an {@link Currency currency}.
 *
 * @see Currency
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Denomination {

  private boolean major;
  private String single;
  private String plural;
  private double weight;

  public boolean isItem() {
    return (this instanceof ItemDenomination);
  }

  public void setSingle(String single) {
    this.single = single;
  }

  public void setPlural(String plural) {
    this.plural = plural;
  }

  public void setWeight(double weight) {
    this.weight = weight;
    this.major = weight >= 1.0;
  }

  public void setMajor(boolean major) {
    this.major = major;
  }

  public String singular() {
    return single;
  }

  public String plural() {
    return plural;
  }

  public boolean isMajor() {
    return major;
  }

  public double weight() {
    return weight;
  }
}