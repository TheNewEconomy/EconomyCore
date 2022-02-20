package net.tnemc.core.account.holdings.handlers;


import net.tnemc.core.account.Account;
import net.tnemc.core.currency.Currency;

import java.math.BigDecimal;
import java.sql.SQLException;

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
public class CoreHoldingsHandler implements HoldingsHandler {

  /**
   * Whether or not this is a core handler that adds to the player's inventory vs handles its own data. Don't return true
   * unless you know what you're doing as it could disrupt player balances.
   *
   * @return True if this is a core handler, otherwise false.
   */
  @Override
  public boolean coreHandler() {
    return true;
  }

  /**
   * Used for determining the order for which ExternalHoldings are called in relation to others, and the core holdings.
   * The core holdings priority = 5, making your priority > 5 will have it be called before the core holdings.
   *
   * @return The priority for this holdings handler.
   */
  @Override
  public int priority() {
    return 5;
  }

  /**
   * Used to get the holdings for a specific account from this {@link HoldingsHandler}.
   *
   * @param account  The uuid of the account.
   * @param world    The name of the world.
   * @param currency The name of the currency to use.
   * @return The holdings for the specific account in accordance to this {@link HoldingsHandler}.
   */
  @Override
  public BigDecimal getHoldings(Account account, String world, Currency currency, boolean database) {
    if(currency.getType().isPresent()) {
      try {
        return currency.getType().get().getHoldings(account, world, currency, database);
      } catch(SQLException ignore) {
        return BigDecimal.ZERO;
      }
    }
    return BigDecimal.ZERO;
  }

  /**
   * Used to remove holdings from this {@link HoldingsHandler}.
   *
   * @param account  The uuid of the account.
   * @param world    The name of the world.
   * @param currency The name of the currency to use.
   * @param amount The amount still left to remove, could be zero.
   * @return The left over holdings that's still needed to be removed after removing the account's holdings for this
   * {@link HoldingsHandler}.
   */
  @Override
  public BigDecimal removeHoldings(Account account, String world, Currency currency, BigDecimal amount) {
    if(currency.getType().isPresent()) {
      final BigDecimal holdings = getHoldings(account, world, currency, false);

      if(holdings.compareTo(amount) < 0) {
        try {
          currency.getType().get().setHoldings(account, world, currency, BigDecimal.ZERO, false);
        } catch(SQLException ignore) {
          return amount;
        }
        return amount.subtract(holdings);
      }

      try {
        currency.getType().get().setHoldings(account, world, currency, holdings.subtract(amount),
                                             false);
        return BigDecimal.ZERO;
      } catch(SQLException ignore) {
        return amount;
      }

    }
    return amount;
  }
}