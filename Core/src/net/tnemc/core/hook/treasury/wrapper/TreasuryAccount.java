package net.tnemc.core.hook.treasury.wrapper;
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

import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.account.holdings.modify.HoldingsOperation;
import net.tnemc.core.actions.source.PluginSource;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * TreasuryAccount
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public abstract class TreasuryAccount implements Account {

  protected final net.tnemc.core.account.Account account;

  public TreasuryAccount(net.tnemc.core.account.Account account) {
    this.account = account;
  }

  @Override
  public @NotNull Optional<String> getName() {
    return Optional.of(account.getName());
  }

  @Override
  public @NotNull CompletableFuture<BigDecimal> retrieveBalance(@NotNull Currency currency) {
    final Optional<net.tnemc.core.currency.Currency> curOpt = TNECore.eco().currency().findCurrency(currency.getIdentifier());
    if(curOpt.isPresent()) {
      return CompletableFuture.completedFuture(account.getHoldingsTotal(TNECore.server().defaultRegion(), curOpt.get().getUid()));
    }

    return CompletableFuture.failedFuture(new IllegalArgumentException("Invalid currency identifier provided. Please make sure this currency is registered with TNE before performing operations!"));
  }

  @Override
  public @NotNull CompletableFuture<BigDecimal> doTransaction(@NotNull EconomyTransaction ecoTrans) {
    final Optional<net.tnemc.core.currency.Currency> curOpt = TNECore.eco().currency().findCurrency(ecoTrans.getCurrencyId());
    if(curOpt.isEmpty()) {
      return CompletableFuture.failedFuture(new IllegalArgumentException("Invalid currency identifier provided. Please make sure this currency is registered with TNE before performing operations!"));
    }

    final HoldingsModifier modifier = new HoldingsModifier(TNECore.server().defaultRegion(),
            curOpt.get().getUid(),
            ecoTrans.getAmount().setScale(curOpt.get().getDecimalPlaces(), RoundingMode.DOWN),
            typeToTNEOperation(ecoTrans.getType()));

    final Transaction transaction = new Transaction(typeToTNE(ecoTrans.getType()))
            .to(account, modifier)
            .processor(EconomyManager.baseProcessor())
            .source(new PluginSource("Treasury"));

    try {

      final TransactionResult result = transaction.process();
      if(result.getReceipt().isPresent()) {
        return CompletableFuture.completedFuture(result.getReceipt().get().getTo().getCombinedEnding());
      } else {
        return CompletableFuture.failedFuture(new InvalidTransactionException("Transaction Failed: " + result.getMessage()));
      }

    } catch(InvalidTransactionException e) {

      return CompletableFuture.failedFuture(e);
    }
  }

  @Override
  public @NotNull CompletableFuture<Boolean> deleteAccount() {
    return CompletableFuture.completedFuture(TNECore.eco().account().deleteAccount(account.getIdentifier()).success());
  }

  @Override
  public @NotNull CompletableFuture<Collection<String>> retrieveHeldCurrencies() {
    return CompletableFuture.supplyAsync(()->{

      final Collection<String> curs = new ArrayList<>();

      account.getWallet().getHoldings().forEach((regionName, regionHoldings)->{
        regionHoldings.getHoldings().forEach((curID, curHoldings)->{

          final Optional<net.tnemc.core.currency.Currency> curOpt = TNECore.eco().currency().findCurrency(curID);
          curOpt.ifPresent(currency -> curs.add(currency.getIdentifier()));
        });
      });
      return curs;
    });
  }

  @Override
  public @NotNull CompletableFuture<Collection<EconomyTransaction>> retrieveTransactionHistory(int transactionCount, @NotNull Temporal from, @NotNull Temporal to) {
    return CompletableFuture.failedFuture(new UnsupportedOperationException("TNE requires usage of the TNEAPI to access transaction logs."));
  }

  public String typeToTNE(final EconomyTransactionType type) {
    return switch(type) {
      case SET -> "set";
      case WITHDRAWAL -> "take";
      default -> "give";
    };
  }

  public HoldingsOperation typeToTNEOperation(final EconomyTransactionType type) {
    return switch(type) {
      case SET -> HoldingsOperation.SET;
      case WITHDRAWAL -> HoldingsOperation.SUBTRACT;
      default -> HoldingsOperation.ADD;
    };
  }
}
