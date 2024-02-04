package net.tnemc.core.handlers.player;
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
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.utils.HandlerResponse;

import java.util.Optional;

/**
 * Represents an event where a player is closing an ender chest.
 *
 * @since 0.1.2
 * @author creatorfromhell
 */
public class PlayerCloseEChestHandler {

  /**
   * Used to handle a Player closing an ender chest using the specified {@link PlayerProvider} class.
   * @param provider The {@link PlayerProvider} associated with the platform event.
   * @return True if the event should be cancelled, otherwise false.
   */
  public HandlerResponse handle(PlayerProvider provider) {

    final Optional<Account> account = TNECore.eco().account().findAccount(provider.identifier());
    if(account.isPresent()) {

      final String region = TNECore.eco().region().getMode().region(provider);
      for(Currency currency : TNECore.eco().currency().getCurrencies(region)) {

        if(currency.type().supportsItems()) {

          for(HoldingsEntry entry : account.get().getHoldings(region, currency.getUid(), EconomyManager.E_CHEST)) {

            account.get().getWallet().setHoldings(entry);
          }
        }
      }
    }
    return new HandlerResponse("", false);
  }
}