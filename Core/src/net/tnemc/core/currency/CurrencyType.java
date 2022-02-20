package net.tnemc.core.currency;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/5/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

import net.tnemc.core.account.Account;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Represents a type of currency.
 *
 * @see Currency
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public interface CurrencyType {

  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  String name();

  /**
   * @return True if this currency type supports offline players, otherwise false.
   */
  default boolean offline() {
    return true;
  }

  /**
   * @return True if this currency requires setting the player's balance when they log in. This
   * should be true for currency types like item currency.
   */
  default boolean loginCalculation() {
    return true;
  }

  /**
   * @return If balances of this currency type should be saved to the database. Return false to do your own data handling.
   */
  default boolean database() {
    return true;
  }

  /**
   * Used to get the holdings for a specific account from this currency type.
   *
   * @param account The uuid of the account.
   * @param world The name of the world.
   * @param currency The instance of the currency to use.
   * @param database True if the holdings should be taken from the database, if applicable.
   * @return The holdings for the specific account.
   */
  BigDecimal getHoldings(Account account, String world, Currency currency, boolean database) throws SQLException;

  /**
   * Used to set the holdings for a specific account.
   *
   * @param world The world to use for saving the holdings.
   * @param currency The instance of the currency to use.
   * @param amount The amount to set the player's holdings to.
   * @param skipUpdate Whether or not to skip updating. For item-based currency this skips updating the inventory.
   */
  void setHoldings(Account account, String world, Currency currency, BigDecimal amount, boolean skipUpdate) throws SQLException;
}
