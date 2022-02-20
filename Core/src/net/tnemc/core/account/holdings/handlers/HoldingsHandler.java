package net.tnemc.core.account.holdings.handlers;

import net.tnemc.core.account.Account;
import net.tnemc.core.currency.Currency;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 6/3/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

/**
 * Represents a handling to deal with the setting and removing of holdings. This allows for other
 * handlers to be added with ease. This could be for external bank plugins or other instances where
 * a plugin may store holdings for an account.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public interface HoldingsHandler {

  /**
   * @return The string the username of the account holder should contain. Empty string "" for no search.
   */
  default String userContains() {
    return "";
  }

  /**
   * Whether or not this is a core handler that adds to the player's inventory vs handles its own data. Don't return true
   * unless you know what you're doing as it could disrupt player balances.
   *
   * @return True if this is a core handler, otherwise false.
   */
  default boolean coreHandler() {
    return false;
  }


  /**
   * Used for determining the order for which ExternalHoldings are called in relation to others, and the core holdings.
   * The core holdings priority = 5, making your priority > 5 will have it be called before the core holdings.
   *
   * @return The priority for this holdings handler.
   */
  default int priority() {
    return 4;
  }

  /**
   * Used to get the holdings for a specific account from this {@link HoldingsHandler}.
   *
   * @param account The uuid of the account.
   * @param world The name of the world.
   * @param currency The instance of the currency to use.
   * @param database True if the holdings should be taken from the database vs the inventory, if applicable.
   * @return The holdings for the specific account in accordance to this {@link HoldingsHandler}.
   */
  BigDecimal getHoldings(Account account, String world, Currency currency, boolean database);

  /**
   * Used to remove holdings from this {@link HoldingsHandler}.
   *
   * @param account The uuid of the account.
   * @param world The name of the world.
   * @param currency The instance of the currency to use.
   * @param amount The amount still left to remove, could be zero.
   * @return The left over holdings that's still needed to be removed after removing the account's holdings for this
   * {@link HoldingsHandler}.
   */
  BigDecimal removeHoldings(Account account, String world, Currency currency, BigDecimal amount);
}