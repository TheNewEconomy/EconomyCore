package net.tnemc.core.currency.item;

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

import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Denomination;

import java.util.Locale;
import java.util.Optional;

/**
 * Represents a {@link Currency currency} that is represented by physical items in game.
 *
 * @author creatorfromhell
 * @see Currency
 * @since 0.1.2.0
 */
public class ItemCurrency extends Currency {

  protected boolean enderChest;
  protected boolean enderFill;
  protected boolean importItem;
  protected boolean blockCraft;
  protected boolean shulker;
  protected boolean bundle;

  public ItemCurrency(final String identifier) {

    super(identifier);

    this.enderChest = true;
    this.enderFill = true;
    this.importItem = true;
    this.blockCraft = false;
    this.shulker = false;
    this.bundle = false;
  }

  public Optional<ItemDenomination> getDenominationByMaterial(final String material) {

    for(final Denomination denom : getDenominations().values()) {

      final ItemDenomination item = (ItemDenomination)denom;

      if(item.material().equalsIgnoreCase(material)
         || item.material().toLowerCase(Locale.ROOT).contains(material.toLowerCase(Locale.ROOT))
         || material.toLowerCase(Locale.ROOT).contains(item.material().toLowerCase(Locale.ROOT))) {
        return Optional.of(item);
      }
    }
    return Optional.empty();
  }

  public boolean canEnderChest() {

    return enderChest;
  }

  public void setEnderChest(final boolean enderChest) {

    this.enderChest = enderChest;
  }

  public boolean isEnderFill() {

    return enderFill;
  }

  public void setEnderFill(final boolean enderFill) {

    this.enderFill = enderFill;
  }

  public boolean isImportItem() {

    return importItem;
  }

  public void setImportItem(final boolean importItem) {

    this.importItem = importItem;
  }

  public boolean blockCraft() {

    return blockCraft;
  }

  public void blockCraft(final boolean blockCraft) {

    this.blockCraft = blockCraft;
  }

  public boolean bundle() {

    return bundle;
  }

  public void bundle(final boolean bundle) {

    this.bundle = bundle;
  }

  public boolean shulker() {

    return shulker;
  }

  public void shulker(final boolean shulker) {

    this.shulker = shulker;
  }
}