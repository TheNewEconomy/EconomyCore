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
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.channel.SyncHandler;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.manager.TransactionManager;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.history.AwayHistory;
import net.tnemc.core.utils.MISCUtils;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreTime;
import net.tnemc.plugincore.core.id.UUIDPair;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.utils.HandlerResponse;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents an event where a player is joining.
 *
 * @author creatorfromhell
 * @since 0.1.2
 */
public class PlayerJoinHandler {

  /**
   * Used to handle a PlayerJoinEvent using the specified {@link PlayerProvider} class.
   *
   * @param provider The {@link PlayerProvider} associated with the platform event.
   *
   * @return True if the event should be cancelled, otherwise false.
   */
  public HandlerResponse handle(final PlayerProvider provider) {

    final HandlerResponse response = new HandlerResponse("", false);

    PluginCore.log().debug("Player Join ID: " + provider.identifier());

    final Optional<Account> account = TNECore.eco().account().findAccount(provider.identifier());

    boolean firstJoin = false;
    AccountAPIResponse apiResponse = null;

    PluginCore.log().debug("Join Account Check: " + account.isPresent());

    //Our account doesn't exist, so now we need to continue from here
    if(account.isEmpty()) {
      firstJoin = true;

      apiResponse = TNECore.eco().account().createAccount(provider.identifier().toString(),
                                                          provider.getName());

      PluginCore.log().debug("API Join Check. Account Exists: " + apiResponse.getAccount().isPresent());
      if(apiResponse.getAccount().isEmpty()) {
        response.setResponse(response.getResponse());
        response.setCancelled(true);
        return response;
      } else {
        if(!apiResponse.getResponse().success() && apiResponse.getAccount().isPresent()) {
          firstJoin = false;
        }
      }
    }

    PluginCore.log().debug("First Join: " + firstJoin);

    final Optional<Account> acc = (apiResponse == null)? account :
                                  apiResponse.getAccount();

    final UUID id = provider.identifier();
    if(acc.isPresent()) {

      if(!acc.get().getName().equalsIgnoreCase(provider.getName())) {
        acc.get().setName(provider.getName());

        TNECore.eco().account().uuidProvider().store(new UUIDPair(provider.identifier(), provider.getName()));
      }

      if(firstJoin || acc.get().getCreationDate() == ((PlayerAccount)acc.get()).getLastOnline()) {

        final String region = TNECore.eco().region().getMode().region(provider);
        for(final Currency currency : TNECore.eco().currency().currencies()) {

          if(currency.type().supportsItems() && MainConfig.yaml().getBoolean("Core.Server.ImportItems", true)) {

            TNECore.eco().account().getImporting().add(id);

            for(final HoldingsEntry entry : acc.get().getHoldings(region, currency.getUid())) {

              acc.get().setHoldings(entry, entry.getHandler());
            }

            TNECore.eco().account().getImporting().remove(id);
            continue;
          }

          PluginCore.log().debug("Setting Balance to Starting Holdings Currency: " + currency.getIdentifier());

          if(firstJoin) {
            acc.get().setHoldings(new HoldingsEntry(region,
                                                    currency.getUid(),
                                                    currency.getStartingHoldings(),
                                                    EconomyManager.NORMAL
            ));
          }
        }
      } else {
        TNECore.eco().account().getLoading().add(id);

        final String region = TNECore.eco().region().getMode().region(provider);
        for(final Currency currency : TNECore.eco().currency().getCurrencies(region)) {

          if(currency instanceof final ItemCurrency itemCurrency) {

            if(!acc.get().getWallet().contains(region, currency.getUid())) {

              if(itemCurrency.isImportItem()) {

                TNECore.eco().account().getImporting().add(id);

                for(final HoldingsEntry entry : acc.get().getHoldings(region, currency.getUid())) {

                  acc.get().setHoldings(entry, entry.getHandler());
                }

                TNECore.eco().account().getImporting().remove(id);
              } else {
                acc.get().setHoldings(new HoldingsEntry(region,
                                                        currency.getUid(),
                                                        currency.getStartingHoldings(),
                                                        EconomyManager.NORMAL
                ));
              }
            } else {

              for(final HoldingsEntry entry : acc.get().getHoldings(region, currency.getUid())) {

                acc.get().setHoldings(entry, entry.getHandler());
              }
            }
          }
        }
      }
      TNECore.eco().account().getLoading().remove(id);

      PluginCore.server().scheduler().createDelayedTask(()->{

        final Optional<AwayHistory> away = acc.get().away(((PlayerAccount)acc.get()).getUUID());
        if(away.isPresent()) {
          provider.message(new MessageData("Messages.Transaction.AwayJoin"));
        }

      }, new ChoreTime(0), ChoreExecution.SECONDARY);

      if(provider.hasPermission("tne.admin.update")) {
        if(MainConfig.yaml().getBoolean("Core.Update.Notify") && TNECore.instance().update() != null) {

          if(TNECore.instance().update().needsUpdate()) {
            provider.message(new MessageData("<red>[TNE] Update Available! Latest: <white>" + TNECore.instance().update().getBuild()));
          }

          if(TNECore.instance().update().isEarlyBuild()) {
            provider.message(new MessageData("<gold>[TNE] Thank You for testing this pre-release version!"));
          }
        }


        if(TNECore.eco().transaction().isTrack()) {
          //TODO: Any warnings? Balance jumps?
        }
      }

      //If this is the first player online, sync balances.
      if(PluginCore.server().onlinePlayers() == 1) {
        //check if server own wants db reloaded
        if(DataConfig.yaml().getBoolean("Data.Sync.Reload.Enabled", false)) {

          if(MISCUtils.isTimeDifferenceGreaterOrEqual(new Date(TNECore.eco().getReloadTime()), DataConfig.yaml().getInt("Data.Sync.Reload.Time", 120))) {

            TNECore.eco().account().getAccounts().clear();
            TransactionManager.receipts().getReceipts().clear();

            TNECore.instance().storage().loadAll(Account.class, "");
            TNECore.instance().storage().loadAll(Receipt.class, "");

            TNECore.eco().setReloadTime(new Date().getTime());
          }
        } else {
          SyncHandler.send(acc.get().getIdentifier().toString());
        }
      }
    }
    return response;
  }
}