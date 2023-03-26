package net.tnemc.core.menu.impl.mybal.consumer;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.source.PlayerSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.menu.impl.shared.consumer.AmountConfirmation;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.processor.BaseTransactionProcessor;
import net.tnemc.menu.core.MenuManager;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * ConvertConfirmation
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ConvertConfirmation extends AmountConfirmation {

  /**
   * Ran when the confirm button is hit on the AmountSelection page.
   *
   * @param player   The uuid of the player that hit the button.
   * @param target   The uuid of the player that is the target of the confirmation.
   * @param currency The identifier of the currency.
   * @param region   The region involved.
   * @param amount   The amount from the selection.
   */
  @Override
  public void confirm(UUID player, UUID target, UUID currency, String region, BigDecimal amount) {

    final Optional<PlayerProvider> provider = TNECore.server().findPlayer(player);
    final Optional<Account> account = TNECore.eco().account().findAccount(player);
    final Optional<Object> curID = MenuManager.instance().getViewerData(player, "selected_cur");

    if(account.isPresent() && provider.isPresent() && curID.isPresent()) {

      Optional<Currency> currencyFromOBJ = TNECore.eco().currency().findCurrency((UUID)curID.get());

      if(currencyFromOBJ.isPresent()) {
        final BigDecimal parsedTo = amount.multiply(BigDecimal.valueOf(currencyFromOBJ.get().getConversion().get(currencyFromOBJ.get().getIdentifier())));

        final HoldingsModifier modifier = new HoldingsModifier(provider.get().region(true),
                                                               currency,
                                                               parsedTo
        );

        final HoldingsModifier modifierFrom = new HoldingsModifier(provider.get().region(true),
                                                                   currencyFromOBJ.get().getUid(),
                                                                   amount.negate()
        );

        final Transaction transaction = new Transaction("convert")
            .from(account.get(), modifierFrom)
            .to(account.get(), modifier)
            .processor(new BaseTransactionProcessor())
            .source(new PlayerSource(provider.get().identifier()));

        Optional<Receipt> receipt = processTransaction(provider.get(), transaction);
      }
    }
  }
}