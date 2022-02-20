package net.tnemc.core.currency.type;

import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyType;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 12/15/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MixedType implements CurrencyType {
  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  @Override
  public String name() {
    return "mixed";
  }

  /**
   * @return True if this currency requires setting the player's balance when they log in. This should be true for currency
   * types like item currency.
   */
  @Override
  public boolean loginCalculation() {
    return false;
  }

  /**
   * Used to get the holdings for a specific account from this currency type.
   *
   * @param account  The uuid of the account.
   * @param world    The name of the world.
   * @param currency The instance of the currency to use.
   * @param database True if the holdings should be taken from the database vs the inventory, if applicable.
   * @return The holdings for the specific account.
   */
  @Override
  public BigDecimal getHoldings(Account account, String world, Currency currency, boolean database) throws SQLException {
    return virtualHoldings(account, world, currency).add(itemHoldings(account, world, currency, database));
  }

  public BigDecimal virtualHoldings(Account account, String world, Currency currency) {
    return account.getHoldingsManager().getHoldings(world, currency);
  }

  public BigDecimal itemHoldings(Account account, String world, Currency currency, boolean database) {

    if(!database && account.isPlayer() && ((PlayerAccount)account).isOnline()) {
      final Optional<PlayerProvider> player = ((PlayerAccount)account).getPlayer();
      if(player.isPresent()) {
        return player.get().getItems(currency, player.get().getInventory(false));
      }
    }
    return BigDecimal.ZERO;
  }

  /**
   * @param account
   * @param world    The world to use for saving the holdings.
   * @param currency The instance of the currency to use.
   * @param amount   The amount to set the player's holdings to.
   * @param skipUpdate   Whether or not to skip updating. For item-based currency this skips updating the inventory.
   */
  @Override
  public void setHoldings(Account account, String world, Currency currency, BigDecimal amount, boolean skipUpdate) throws SQLException {
    final BigDecimal holdings = getHoldings(account, world, currency, false);

    if(holdings.compareTo(BigDecimal.ZERO) != 0 && holdings.compareTo(amount) > 0) {
      final BigDecimal virtualHoldings = virtualHoldings(account, world, currency);

      final boolean itemNeeded = (virtualHoldings.compareTo(amount) < 0);

      final BigDecimal toSet = (itemNeeded)? BigDecimal.ZERO : amount;

      if(itemNeeded) {
        final BigDecimal remaining = amount.subtract(virtualHoldings);

        setItemHoldings(account, world, currency, itemHoldings(account, world, currency, false).subtract(remaining));
      }

      setVirtualHoldings(account, world, currency, toSet, skipUpdate);

    } else if(holdings.compareTo(BigDecimal.ZERO) == 0) {
      setVirtualHoldings(account, world, currency, amount, skipUpdate);
    } else {
      setVirtualHoldings(account, world, currency, amount, skipUpdate);
    }
  }

  public void setVirtualHoldings(Account account, String world, Currency currency, BigDecimal amount, boolean skipUpdate) {
    account.getHoldingsManager().setHoldings(world, currency.getIdentifier(), amount, !skipUpdate);
  }

  public void setItemHoldings(Account account, String world, Currency currency, BigDecimal amount) {

    if(account.isPlayer()) {

      final Optional<PlayerProvider> player = ((PlayerAccount)account).getPlayer();
      player.ifPresent(playerProvider->playerProvider.setItems(account, currency, amount,
                                                               playerProvider.getInventory(false),
                                                               false));
    }
  }
}