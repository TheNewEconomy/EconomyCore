package net.tnemc.sponge.impl.eco;
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

import net.kyori.adventure.text.Component;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.VirtualAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * SpongeVirtualAccount
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeVirtualAccount implements VirtualAccount {

  protected final Account account;

  public SpongeVirtualAccount(Account account) {
    this.account = account;
  }

  @Override
  public Component displayName() {
    return null;
  }

  @Override
  public BigDecimal defaultBalance(Currency currency) {
    return null;
  }

  @Override
  public boolean hasBalance(Currency currency, Set<Context> contexts) { //Context: WORLD_KEY
    return false;
  }

  @Override
  public boolean hasBalance(Currency currency, Cause cause) {
    return false;
  }

  @Override
  public BigDecimal balance(Currency currency, Set<Context> contexts) {
    return null;
  }

  @Override
  public BigDecimal balance(Currency currency, Cause cause) {
    return null;
  }

  @Override
  public Map<Currency, BigDecimal> balances(Set<Context> contexts) {
    return null;
  }

  @Override
  public Map<Currency, BigDecimal> balances(Cause cause) {
    return null;
  }

  @Override
  public TransactionResult setBalance(Currency currency, BigDecimal amount, Set<Context> contexts) {
    return null;
  }

  @Override
  public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause) {
    return null;
  }

  @Override
  public Map<Currency, TransactionResult> resetBalances(Set<Context> contexts) {
    return null;
  }

  @Override
  public Map<Currency, TransactionResult> resetBalances(Cause cause) {
    return null;
  }

  @Override
  public TransactionResult resetBalance(Currency currency, Set<Context> contexts) {
    return null;
  }

  @Override
  public TransactionResult resetBalance(Currency currency, Cause cause) {
    return null;
  }

  @Override
  public TransactionResult deposit(Currency currency, BigDecimal amount, Set<Context> contexts) {
    return null;
  }

  @Override
  public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause) {
    return null;
  }

  @Override
  public TransactionResult withdraw(Currency currency, BigDecimal amount, Set<Context> contexts) {
    return null;
  }

  @Override
  public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause) {
    return null;
  }

  @Override
  public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Set<Context> contexts) {
    return null;
  }

  @Override
  public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause) {
    return null;
  }

  @Override
  public String identifier() {
    return account.identifier();
  }
}
