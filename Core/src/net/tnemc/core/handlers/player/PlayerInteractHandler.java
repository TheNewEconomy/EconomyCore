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

import net.kyori.adventure.text.Component;
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.source.PlayerSource;
import net.tnemc.core.config.MessageConfig;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.utils.HandlerResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * PlayerInteractHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerInteractHandler {

  /**
   * Used to handle a PlayerInteractEvent using the specified {@link PlayerProvider} class.
   *
   * @param provider The {@link PlayerProvider} associated with the platform event.
   *
   * @return True if the event should be cancelled, otherwise false.
   */
  public HandlerResponse handle(final PlayerProvider provider, final AbstractItemStack<?> item) {

    final HandlerResponse response = new HandlerResponse("", false);

    final Optional<Account> account = TNECore.eco().account().findAccount(provider.identifier());
    if(account.isPresent() && (account.get() instanceof PlayerAccount) && item.display() != null
       && Component.EQUALS.test(item.display(), Component.text(MessageConfig.yaml().getString("Messages.Note.Name")))) {

      String currency = null;
      String region = null;
      String amount = null;

      final String curCompare = MessageConfig.yaml().getString("Messages.Note.Currency").split(":")[0];
      final String regionCompare = MessageConfig.yaml().getString("Messages.Note.Region").split(":")[0];
      final String amtCompare = MessageConfig.yaml().getString("Messages.Note.Amount").split(":")[0];

      final List<Component> lore = item.lore();

      for(final Component component : lore) {
        //TODO: Fix
        final String[] info = component.toString().split(":");
        if(info.length < 2) continue;


        if(info[0].contains(curCompare)) {
          currency = info[1].split("\"")[0].trim();
        } else if(info[0].contains(regionCompare)) {
          region = info[1].split("\"")[0].trim();
        } else if(info[0].contains(amtCompare)) {
          amount = info[1].split("\"")[0].trim();
        }

      }

      if(currency == null || region == null || amount == null) return response;


      final Optional<Currency> curObj = TNECore.eco().currency().findCurrency(currency);
      if(curObj.isEmpty() || curObj.get().getNote().isEmpty()) return response;

      final BigDecimal value = new BigDecimal(amount);

      final HoldingsModifier modifier = new HoldingsModifier(region, curObj.get().getUid(), value);

      final Transaction transaction = new Transaction("note")
              .from(account.get(), modifier)
              .processor(EconomyManager.baseProcessor())
              .source(new PlayerSource(provider.identifier()));

      try {
        final TransactionResult result = transaction.process();

        if(!result.isSuccessful()) {
          provider.message(new MessageData(result.getMessage()));
          return response;
        }

        if(result.getReceipt().isPresent()) {

          PluginCore.server().calculations().removeItem(curObj.get().getNote().get().stack(currency, region, value), provider.identifier());

          final MessageData claimed = new MessageData("Messages.Note.Claimed");
          claimed.addReplacement("$currency", currency);
          claimed.addReplacement("$amount", amount);
          provider.message(claimed);
          return response;
        }
      } catch(final InvalidTransactionException e) {
        e.printStackTrace();
      }
      provider.message(new MessageData("Messages.Note.Failed"));
    }
    return response;
  }
}