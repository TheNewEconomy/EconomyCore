package net.tnemc.core.account.holdings.handlers;


import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.currency.Currency;

import java.math.BigDecimal;
import java.util.Optional;

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
public class EChestHandler implements HoldingsHandler {
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
    if(currency.isItem() && currency.item().canEnderChest()) {
      if(account.isPlayer()) {

        final PlayerAccount playerAccount = (PlayerAccount)account;
        if(playerAccount.isOnline()) {

          final Optional<PlayerProvider> player = ((PlayerAccount)account).getPlayer();
          if(player.isPresent()) {
            return player.get().getItems(currency, player.get().getInventory(true));
          }

        } else {
          //TODO: Offline e-chest balance handling.
        }
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
    if(currency.isItem() && currency.item().canEnderChest()) {

      if(account.isPlayer()) {

        final PlayerAccount playerAccount = (PlayerAccount)account;
        if(playerAccount.isOnline()) {

          final Optional<PlayerProvider> player = ((PlayerAccount)account).getPlayer();
          if(player.isPresent()) {

            final BigDecimal holdings = player.get().getItems(currency,
                                                              player.get().getInventory(true));
            if(holdings.compareTo(amount) < 0) {
              player.get().setItems(account, currency, BigDecimal.ZERO,
                                    player.get().getInventory(true), false);
              return amount.subtract(holdings);
            }

            player.get().setItems(account, currency, holdings.subtract(amount),
                                  player.get().getInventory(true), false);
            return BigDecimal.ZERO;
          }
        } else {
          //TODO: Offline e-chest handling.
        }
      }
    }
    return amount;
  }
}
