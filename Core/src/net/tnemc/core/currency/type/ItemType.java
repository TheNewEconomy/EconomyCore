package net.tnemc.core.currency.type;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsType;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyType;

import java.math.BigDecimal;

/**
 * Represents our currency type that is based on items.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ItemType implements CurrencyType {
  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  @Override
  public String name() {
    return "item";
  }

  /**
   * Used to get the holdings for a specific account from this currency type.
   *
   * @param account  The uuid of the account.
   * @param type
   * @param region   The name of the region involved. This is usually a world, but could be something
   *                 else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   *
   * @return The holdings for the specific account.
   */
  @Override
  public BigDecimal getHoldings(Account account, String region, Currency currency, HoldingsType type) {
    return null;
  }

  /**
   * Used to set the holdings for a specific account.
   *
   * @param account  The Account to set the holdings for.
   * @param type     The holdings type to set the holdings of.
   * @param region   The name of the region involved. This is usually a world, but could be something
   *                 else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param amount   The amount to set the player's holdings to.
   *
   * @return True if the holdings have been set, otherwise false.
   */
  @Override
  public boolean setHoldings(Account account, String region, Currency currency, HoldingsType type, BigDecimal amount) {
    return false;
  }
}