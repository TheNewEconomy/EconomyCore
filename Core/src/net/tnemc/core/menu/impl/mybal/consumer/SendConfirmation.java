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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.source.PlayerSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.menu.impl.shared.consumer.AmountConfirmation;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.processor.BaseTransactionProcessor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * SendConfirmation
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SendConfirmation extends AmountConfirmation {

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

    final Optional<PlayerProvider> targetProvider = TNECore.server().findPlayer(target);
    final Optional<PlayerProvider> provider = TNECore.server().findPlayer(player);

    final Optional<Account> account = TNECore.eco().account().findAccount(target);
    final Optional<Account> senderAccount = TNECore.eco().account().findAccount(player);

    if(account.isPresent() && senderAccount.isPresent()
    && provider.isPresent() && targetProvider.isPresent()) {

      final HoldingsModifier modifier = new HoldingsModifier(TNECore.eco().region().getMode().region(provider.get()),
                                                             currency,
                                                             amount
      );

      final Transaction transaction = new Transaction("pay")
          .to(account.get(), modifier)
          .from(senderAccount.get(), modifier.counter())
          .processor(EconomyManager.baseProcessor())
          .source(new PlayerSource(player));

      final Optional<Receipt> receipt = processTransaction(provider.get(), transaction);
      if(receipt.isPresent()) {
        //TODO: Success
      }
    }
  }
}