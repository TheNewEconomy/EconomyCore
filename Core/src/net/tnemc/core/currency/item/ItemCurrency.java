package net.tnemc.core.currency.item;

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

import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Denomination;

import java.util.Optional;

/**
 * Represents a {@link Currency currency} that is able to be represented by physical items in game.
 *
 * @see Currency
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ItemCurrency extends Currency {

  protected boolean enderChest;

  public Optional<ItemDenomination> getDenominationByMaterial(final String material) {
    for(Denomination denom : getDenominations().values()) {
      final ItemDenomination item = (ItemDenomination)denom;

      if(item.getMaterial().equalsIgnoreCase(material)) {
        return Optional.of(item);
      }
    }
    return Optional.empty();
  }

  public boolean canEnderChest() {
    return enderChest;
  }

  public void setEnderChest(boolean enderChest) {
    this.enderChest = enderChest;
  }
}