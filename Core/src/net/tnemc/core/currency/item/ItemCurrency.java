package net.tnemc.core.currency.item;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.currency.Currency;

/**
 * Represents a {@link Currency currency} that is able to be represented by physical items in game.
 *
 * @see Currency
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ItemCurrency extends Currency {

  protected boolean enderChest;
  protected boolean consolidate;

  public boolean canEnderChest() {
    return enderChest;
  }

  public void setEnderChest(boolean enderChest) {
    this.enderChest = enderChest;
  }

  public boolean isConsolidate() {
    return consolidate;
  }

  public void setConsolidate(boolean consolidate) {
    this.consolidate = consolidate;
  }
}