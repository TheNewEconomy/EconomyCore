package net.tnemc.core.menu.impl.shared.consumer;
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

import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * AmountConfirmation
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public abstract class AmountConfirmation {

  /**
   * Ran when the confirm button is hit on the AmountSelection page.
   * @param player The uuid of the player that hit the button.
   * @param target The uuid of the player that is the target of the confirmation.
   * @param currency The identifier of the currency.
   * @param region The region involved.
   * @param amount The amount from the selection.
   */
  public abstract void confirm(UUID player, UUID target, String currency, String region, BigDecimal amount);

  protected static Optional<Receipt> processTransaction(PlayerProvider provider, Transaction transaction) {
    try {
      final TransactionResult result = transaction.process();

      if(!result.isSuccessful()) {
        provider.message(new MessageData(result.getMessage()));
        return Optional.empty();
      }
      System.out.println(result.getMessage());
      return result.getReceipt();
    } catch(InvalidTransactionException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }
}