package net.tnemc.sponge.impl.eco;
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

import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.Set;

/**
 * SpongeReceipt
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeReceipt implements TransactionResult {

  private final net.tnemc.core.transaction.TransactionResult result;
  private final Account account;
  private final SpongeCurrency currency;
  private final BigDecimal amount;
  private final Set<Context> contexts;
  private final TransactionType type;

  public SpongeReceipt(net.tnemc.core.transaction.TransactionResult result, Account account,
                       SpongeCurrency currency, BigDecimal amount, Set<Context> contexts,
                       TransactionType type) {
    this.result = result;
    this.account = account;
    this.currency = currency;
    this.amount = amount;
    this.contexts = contexts;
    this.type = type;
  }

  @Override
  public Account account() {
    return account;
  }

  @Override
  public Currency currency() {
    return currency;
  }

  @Override
  public BigDecimal amount() {
    return amount;
  }

  @Override
  public Set<Context> contexts() {
    return contexts;
  }

  @Override
  public ResultType result() {
    if(result.isSuccessful()) {
      return ResultType.SUCCESS;
    }
    return ResultType.FAILED;
  }

  @Override
  public TransactionType type() {
    return type;
  }
}
