package net.tnemc.core.handlers.player;

/*
 * The New Economy
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
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
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.utils.HandlerResponse;

import java.util.Optional;

/**
 * Represents an event where a player is joining.
 *
 * @since 0.1.2
 * @author creatorfromhell
 */
public class PlayerJoinHandler {

  /**
   * Used to handle a PlayerJoinEvent using the specified {@link PlayerProvider} class.
   * @param provider The {@link PlayerProvider} associated with the platform event.
   * @return True if the event should be cancelled, otherwise false.
   */
  public HandlerResponse handle(PlayerProvider provider) {
    final HandlerResponse response = new HandlerResponse("", false);

    final Optional<Account> account = TNECore.eco().account().findAccount(provider.identifier());

    boolean firstJoin = false;
    AccountAPIResponse apiResponse = null;

    //Our account doesn't exist, so now we need to continue from here
    if(account.isEmpty()) {
      firstJoin = true;

      apiResponse = TNECore.eco().account().createAccount(provider.identifier().toString(),
                                                          provider.getName());

      if(!apiResponse.getResponse().success()) {
        response.setResponse(response.getResponse());
        response.setCancelled(true);
        return response;
      }
    }

    final Optional<Account> acc = (apiResponse == null)? account :
                                                   apiResponse.getAccount();

    final String id = provider.identifier().toString();
    if(acc.isPresent()) {

      if(firstJoin) {
        for(Currency currency : TNECore.eco().currency().currencies()) {
          acc.get().setHoldings(new HoldingsEntry(TNECore.eco().region().getMode().region(provider),
                                                  currency.getUid(),
                                                  currency.getStartingHoldings(),
                                                  EconomyManager.NORMAL
          ));
        }
      } else {
        TNECore.eco().account().getLoading().add(id);

        final String region = TNECore.eco().region().getMode().region(provider);
        for(Currency currency : TNECore.eco().currency().getCurrencies(region)) {

          if(currency.type().supportsItems()) {

            for(HoldingsEntry entry : acc.get().getHoldings(region, currency.getUid())) {

              acc.get().setHoldings(entry, entry.getHandler());
            }
          }
        }
      }
      TNECore.eco().account().getLoading().remove(id);

      final long last_online = ((PlayerAccount)acc.get()).getLastOnline();
      //TODO: Check for transactions that happened while away if player has notify settings active.

      if(provider.hasPermission("tne.admin.update")) {
        if(MainConfig.yaml().getBoolean("Core.Update.Notify") && TNECore.update() != null) {

          if(TNECore.update().needsUpdate()) {
            provider.message(new MessageData("<red>[TNE] Update Available! Latest: <white>" + TNECore.update().getBuild()));
          }

          if(TNECore.update().isEarlyBuild()) {
            provider.message(new MessageData("<gold>[TNE] Thank You for testing this pre-release version!"));
          }
        }


        if(TNECore.eco().transaction().isTrack()) {
          //TODO: Any warnings? Balance jumps?
        }
      }
    }
    return response;
  }
}