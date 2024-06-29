package net.tnemc.core.account.holdings.handlers;

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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.HoldingsHandler;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.currency.calculations.CalculationData;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.utils.Identifier;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * InventoryHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class InventoryHandler implements HoldingsHandler {

  /**
   * The identifier for this handler.
   *
   * @return The identifier that represents this handler.
   */
  @Override
  public Identifier identifier() {
    return EconomyManager.INVENTORY_ONLY;
  }

  /**
   * Used to determine if this handler may be used for the specified {@link CurrencyType}.
   *
   * @param type The currency type.
   *
   * @return True if it supports the currency type, otherwise false.
   */
  @Override
  public boolean supports(CurrencyType type) {
    return type.supportsItems();
  }

  /**
   * Used to set the holdings for a specific account.
   *
   * @param account  The account.
   * @param region   The name of the region involved. This is usually a world, but could be something
   *                 else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param type     The currency type.
   * @param amount   The amount to set the player's holdings to.
   *
   * @return True if the holdings have been set, otherwise false.
   */
  @Override
  public boolean setHoldings(Account account, String region, Currency currency, CurrencyType type, BigDecimal amount) {
    account.getWallet().setHoldings(new HoldingsEntry(region, currency.getUid(), amount, identifier()));

    if(account.isPlayer() && PluginCore.server().online(account.getIdentifier().toString()) && !TNECore.eco().account().getImporting().contains(account.getIdentifier())) {
      final CalculationData<Object> data = new CalculationData<>((ItemCurrency)currency,
                                                                 ((PlayerAccount)account).getPlayer()
                                                                     .get().inventory().getInventory(false),
                                                                 ((PlayerAccount)account).getUUID());
      TNECore.instance().itemCalculations().setItems(data, amount);
      return true;
    }
    return true;
  }

  /**
   * Used to get the holdings for a specific account from this handler.
   *
   * @param account  The Account.
   * @param region   The name of the region involved. This is usually a world, but could be something
   *                 else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param type     The currency type.
   *
   * @return The holdings for the specific account.
   */
  @Override
  public HoldingsEntry getHoldings(Account account, String region, Currency currency, CurrencyType type) {
    if((currency instanceof ItemCurrency)) {

      if(!account.isPlayer() || !PluginCore.server().online(account.getIdentifier().toString()) ||
          TNECore.eco().account().getLoading().contains(account.getIdentifier())
                  && !TNECore.eco().account().getImporting().contains(account.getIdentifier())) {

        //Offline players have their balances saved to their wallet so check it.
        final Optional<HoldingsEntry> holdings = account.getWallet().getHoldings(region,
                                                                                 currency.getUid(),
                                                                                 identifier()
        );
        PluginCore.log().debug("Getting holdings from Inventory DB", DebugLevel.DEVELOPER);

        if(holdings.isPresent()) {
          return holdings.get();
        }
        return new HoldingsEntry(region,
                                 currency.getUid(),
                                 BigDecimal.ZERO,
                                 identifier());
      }
      PluginCore.log().debug("Getting holdings from Inventory", DebugLevel.DEVELOPER);
      final CalculationData<Object> data = new CalculationData<>((ItemCurrency)currency,
                                                                 ((PlayerAccount)account).getPlayer()
                                                                     .get().inventory().getInventory(false),
                                                                 ((PlayerAccount)account).getUUID());

      return new HoldingsEntry(region, currency.getUid(),
                               TNECore.instance().itemCalculations().calculateHoldings(data), identifier());
    }
    //not item currency? then return zero... should never happen.
    return new HoldingsEntry(region,
                             currency.getUid(),
                             BigDecimal.ZERO,
                             identifier());
  }
}