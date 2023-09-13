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
import net.tnemc.core.channel.handlers.BalanceHandler;
import net.tnemc.core.channel.handlers.CreateHandler;
import net.tnemc.core.channel.handlers.SyncHandler;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.core.compatibility.scheduler.ChoreTime;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.transaction.history.AwayHistory;
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

      if(firstJoin || acc.get().getCreationDate() == ((PlayerAccount)acc.get()).getLastOnline()) {

        final String region = TNECore.eco().region().getMode().region(provider);
        for(Currency currency : TNECore.eco().currency().currencies()) {

          if(currency.type().supportsItems() && MainConfig.yaml().getBoolean("Core.Server.ImportItems", true)) {

            TNECore.eco().account().getImporting().add(id);

            for(HoldingsEntry entry : acc.get().getHoldings(region, currency.getUid())) {

              acc.get().setHoldings(entry, entry.getHandler());
            }

            TNECore.eco().account().getImporting().remove(id);
            continue;
          }

          acc.get().setHoldings(new HoldingsEntry(region,
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

            /*System.out.println("Region: " + acc.get().getWallet().getHoldings(region).isEmpty());
            System.out.println("Currency: " + !acc.get().getWallet().getHoldings(region).get().getHoldings().containsKey(currency.getUid()));
            if(MainConfig.yaml().getBoolean("Core.Server.ImportItems", true)
                    && acc.get().getWallet().getHoldings(region).isEmpty()
                    || MainConfig.yaml().getBoolean("Core.Server.ImportItems", true)
                    && !acc.get().getWallet().getHoldings(region).get().getHoldings().containsKey(currency.getUid())) {

              System.out.println("Importing Item Currency.");
              TNECore.eco().account().getImporting().add(id);

              for(HoldingsEntry entry : acc.get().getHoldings(region, currency.getUid())) {

                acc.get().setHoldings(entry, entry.getHandler());
              }

              TNECore.eco().account().getImporting().remove(id);
              continue;
            }*/

            for(HoldingsEntry entry : acc.get().getHoldings(region, currency.getUid())) {

              acc.get().setHoldings(entry, entry.getHandler());
            }
          }
        }
      }
      TNECore.eco().account().getLoading().remove(id);

      TNECore.server().scheduler().createDelayedTask(()->{

        final Optional<AwayHistory> away = acc.get().away(((PlayerAccount)acc.get()).getUUID());
        if(away.isPresent()) {
          provider.message(new MessageData("Messages.Transaction.AwayJoin"));
        }

      }, new ChoreTime(0), ChoreExecution.SECONDARY);

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

      //If this is the first player online, sync balances.
      if(TNECore.server().onlinePlayers() == 1) {
        SyncHandler.send(acc.get().getIdentifier());
      }
    }
    return response;
  }
}