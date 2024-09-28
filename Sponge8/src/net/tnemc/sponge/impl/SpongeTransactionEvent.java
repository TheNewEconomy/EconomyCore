package net.tnemc.sponge.impl;

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

import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.economy.EconomyTransactionEvent;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

/**
 * SpongeTransactionEvent
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeTransactionEvent implements EconomyTransactionEvent {

  private final SpongeReceipt receipt;

  public SpongeTransactionEvent(SpongeReceipt receipt) {

    this.receipt = receipt;
  }

  @Override
  public TransactionResult transactionResult() {

    return receipt;
  }

  @Override
  public Cause cause() {

    return null;
  }
}