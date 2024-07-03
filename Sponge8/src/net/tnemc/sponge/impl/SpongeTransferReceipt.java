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

import net.tnemc.core.transaction.TransactionResult;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import java.math.BigDecimal;
import java.util.Set;

/**
 * SpongeTransferReceipt
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeTransferReceipt extends SpongeReceipt implements TransferResult {

  private final Account accountTo;

  public SpongeTransferReceipt(TransactionResult result, Account account, SpongeCurrency currency, BigDecimal amount, Set<Context> contexts, TransactionType type, Account accountTo) {
    super(result, account, currency, amount, contexts, type);
    this.accountTo = accountTo;
  }

  @Override
  public Account accountTo() {
    return accountTo;
  }
}
