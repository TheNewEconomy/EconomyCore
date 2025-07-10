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

import net.kyori.adventure.text.Component;
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.account.holdings.modify.HoldingsOperation;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.VirtualAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;
import org.spongepowered.api.service.economy.transaction.TransactionTypes;
import org.spongepowered.api.service.economy.transaction.TransferResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * SpongeVirtualAccount
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeVirtualAccount implements VirtualAccount {

  protected final Account account;

  public SpongeVirtualAccount(final Account account) {

    this.account = account;
  }

  @Override
  public Component displayName() {

    return Component.text(account.getName());
  }

  @Override
  public BigDecimal defaultBalance(final Currency currency) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    return TNECore.eco().currency().defaultCurrency().getStartingHoldings();
  }

  @Override
  public boolean hasBalance(final Currency currency, final Set<Context> contexts) { //Context: WORLD_KEY
    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    return account.getWallet().getHoldings(resolveRegion(contexts), TNECore.eco().currency().defaultCurrency().getUid()).isPresent();
  }

  @Override
  public boolean hasBalance(final Currency currency, final Cause cause) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    return account.getWallet().getHoldings(PluginCore.server().defaultWorld(), TNECore.eco().currency().defaultCurrency().getUid()).isPresent();
  }

  @Override
  public BigDecimal balance(final Currency currency, final Set<Context> contexts) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    final Optional<HoldingsEntry> entry = account.getWallet().getHoldings(resolveRegion(contexts), TNECore.eco().currency().defaultCurrency().getUid());
    if(entry.isPresent()) {
      return entry.get().getAmount();
    }
    return defaultBalance(currency);
  }

  @Override
  public BigDecimal balance(final Currency currency, final Cause cause) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    final Optional<HoldingsEntry> entry = account.getWallet().getHoldings(PluginCore.server().defaultWorld(), TNECore.eco().currency().defaultCurrency().getUid());
    if(entry.isPresent()) {
      return entry.get().getAmount();
    }
    return defaultBalance(currency);
  }

  @Override
  public Map<Currency, BigDecimal> balances(final Set<Context> contexts) {

    return balances(resolveRegion(contexts));
  }

  @Override
  public Map<Currency, BigDecimal> balances(final Cause cause) {

    return balances(PluginCore.server().defaultWorld());
  }

  public Map<Currency, BigDecimal> balances(final String region) {

    final Map<Currency, BigDecimal> balances = new HashMap<>();

    for(final net.tnemc.core.currency.Currency cur : TNECore.eco().currency().getCurrencies(region)) {

      final Optional<HoldingsEntry> entry = account.getWallet().getHoldings(region, cur.getUid());
      entry.ifPresent(holdingsEntry->balances.put(new SpongeCurrency(cur), holdingsEntry.getAmount()));
    }
    return balances;
  }

  @Override
  public TransactionResult setBalance(final Currency currency, final BigDecimal amount, final Set<Context> contexts) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);

    final HoldingsModifier modifier = new HoldingsModifier(resolveRegion(contexts),
                                                           TNECore.eco().currency().defaultCurrency().getUid(),
                                                           amount,
                                                           HoldingsOperation.SET);

    final Transaction transaction = new Transaction("set")
            .to(account, modifier)
            .processor(EconomyManager.baseProcessor());

    return transaction(transaction, amount, contexts, TransactionTypes.DEPOSIT.get());
  }

  @Override
  public TransactionResult setBalance(final Currency currency, final BigDecimal amount, final Cause cause) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);

    final HoldingsModifier modifier = new HoldingsModifier(PluginCore.server().defaultWorld(),
                                                           TNECore.eco().currency().defaultCurrency().getUid(),
                                                           amount,
                                                           HoldingsOperation.SET);

    final Transaction transaction = new Transaction("set")
            .to(account, modifier)
            .processor(EconomyManager.baseProcessor());

    return transaction(transaction, amount, new HashSet<>(), TransactionTypes.DEPOSIT.get());
  }

  @Override
  public Map<Currency, TransactionResult> resetBalances(final Set<Context> contexts) {

    return resetBalances(resolveRegion(contexts));
  }

  @Override
  public Map<Currency, TransactionResult> resetBalances(final Cause cause) {

    return resetBalances(PluginCore.server().defaultWorld());
  }

  public Map<Currency, TransactionResult> resetBalances(final String region) {

    final Map<Currency, TransactionResult> results = new HashMap<>();

    for(final net.tnemc.core.currency.Currency cur : TNECore.eco().currency().getCurrencies(region)) {
      final HoldingsModifier modifier = new HoldingsModifier(PluginCore.server().defaultWorld(),
                                                             TNECore.eco().currency().defaultCurrency().getUid(),
                                                             cur.getStartingHoldings(),
                                                             HoldingsOperation.SET);

      final Transaction transaction = new Transaction("set")
              .to(account, modifier)
              .processor(EconomyManager.baseProcessor());

      results.put(new SpongeCurrency(cur), transaction(transaction, cur.getStartingHoldings(), new HashSet<>(), TransactionTypes.DEPOSIT.get()));
    }
    return results;
  }

  @Override
  public TransactionResult resetBalance(final Currency currency, final Set<Context> contexts) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    return setBalance(currency, defaultBalance(currency), contexts);
  }

  @Override
  public TransactionResult resetBalance(final Currency currency, final Cause cause) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    return setBalance(currency, defaultBalance(currency), cause);
  }

  @Override
  public TransactionResult deposit(final Currency currency, final BigDecimal amount, final Set<Context> contexts) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    final HoldingsModifier modifier = new HoldingsModifier(resolveRegion(contexts),
                                                           TNECore.eco().currency().defaultCurrency().getUid(),
                                                           amount);

    final Transaction transaction = new Transaction("give")
            .to(account, modifier)
            .processor(EconomyManager.baseProcessor());
    return transaction(transaction, amount, contexts, TransactionTypes.DEPOSIT.get());
  }

  @Override
  public TransactionResult deposit(final Currency currency, final BigDecimal amount, final Cause cause) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    final HoldingsModifier modifier = new HoldingsModifier(PluginCore.server().defaultWorld(),
                                                           TNECore.eco().currency().defaultCurrency().getUid(),
                                                           amount);

    final Transaction transaction = new Transaction("give")
            .to(account, modifier)
            .processor(EconomyManager.baseProcessor());
    return transaction(transaction, amount, new HashSet<>(), TransactionTypes.DEPOSIT.get());
  }

  @Override
  public TransactionResult withdraw(final Currency currency, final BigDecimal amount, final Set<Context> contexts) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    final HoldingsModifier modifier = new HoldingsModifier(resolveRegion(contexts),
                                                           TNECore.eco().currency().defaultCurrency().getUid(),
                                                           amount
    );

    final Transaction transaction = new Transaction("take")
            .to(account, modifier.counter())
            .processor(EconomyManager.baseProcessor());

    return transaction(transaction, amount, contexts, TransactionTypes.WITHDRAW.get());
  }

  @Override
  public TransactionResult withdraw(final Currency currency, final BigDecimal amount, final Cause cause) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    final HoldingsModifier modifier = new HoldingsModifier(PluginCore.server().defaultWorld(),
                                                           TNECore.eco().currency().defaultCurrency().getUid(),
                                                           amount
    );

    final Transaction transaction = new Transaction("take")
            .to(account, modifier.counter())
            .processor(EconomyManager.baseProcessor());

    return transaction(transaction, amount, new HashSet<>(), TransactionTypes.WITHDRAW.get());
  }

  @Override
  public TransferResult transfer(final org.spongepowered.api.service.economy.account.Account to, final Currency currency, final BigDecimal amount, final Set<Context> contexts) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);

    final Optional<Account> toAccount = TNECore.eco().account().findAccount(to.identifier());
    if(toAccount.isEmpty()) return null;

    final HoldingsModifier modifier = new HoldingsModifier(resolveRegion(contexts),
                                                           TNECore.eco().currency().defaultCurrency().getUid(),
                                                           amount
    );

    final Transaction transaction = new Transaction("transfer")
            .to(toAccount.get(), modifier)
            .from(account, modifier.counter())
            .processor(EconomyManager.baseProcessor());

    return transaction(to, transaction, amount, contexts, TransactionTypes.TRANSFER.get());
  }

  @Override
  public TransferResult transfer(final org.spongepowered.api.service.economy.account.Account to, final Currency currency, final BigDecimal amount, final Cause cause) {

    PluginCore.log().warning("WARNING: One of your plugins is using the built-in Sponge API. Please note that this doesn't provide multi-currency support. Refer them to the TNE API: https://github.com/TheNewEconomy/EconomyCore/wiki/API#tne-api", DebugLevel.OFF);
    final Optional<Account> toAccount = TNECore.eco().account().findAccount(to.identifier());
    if(toAccount.isEmpty()) return null;

    final HoldingsModifier modifier = new HoldingsModifier(PluginCore.server().defaultWorld(),
                                                           TNECore.eco().currency().defaultCurrency().getUid(),
                                                           amount
    );

    final Transaction transaction = new Transaction("transfer")
            .to(toAccount.get(), modifier)
            .from(account, modifier.counter())
            .processor(EconomyManager.baseProcessor());

    return transaction(to, transaction, amount, new HashSet<>(), TransactionTypes.TRANSFER.get());
  }

  @Override
  public String identifier() {

    return account.getIdentifier().toString();
  }

  public Account getAccount() {

    return account;
  }

  private String resolveRegion(final Set<Context> context) {

    final Optional<String> conResolve = contextRegion(context);
    return conResolve.map(s->TNECore.eco().region().resolve(s)).orElseGet(()->PluginCore.server().defaultWorld());
  }

  protected SpongeTransferReceipt transaction(final org.spongepowered.api.service.economy.account.Account to, final Transaction transaction, final BigDecimal amount, final Set<Context> context, final TransactionType type) {

    SpongeTransferReceipt receipt;

    try {
      final net.tnemc.core.transaction.TransactionResult result = transaction.process();
      receipt = new SpongeTransferReceipt(result, this, new SpongeCurrency(TNECore.eco().currency().defaultCurrency()), amount, context, type, to);
    } catch(InvalidTransactionException e) {
      receipt = new SpongeTransferReceipt(new net.tnemc.core.transaction.TransactionResult(false, e.getMessage()), this, new SpongeCurrency(TNECore.eco().currency().defaultCurrency()), amount, context, type, to);
    }

    Sponge.eventManager().post(new SpongeTransactionEvent(receipt));

    return receipt;
  }

  protected SpongeReceipt transaction(final Transaction transaction, final BigDecimal amount, final Set<Context> context, final TransactionType type) {

    SpongeReceipt receipt;

    try {
      final net.tnemc.core.transaction.TransactionResult result = transaction.process();
      receipt = new SpongeReceipt(result, this, new SpongeCurrency(TNECore.eco().currency().defaultCurrency()), amount, context, type);
    } catch(InvalidTransactionException e) {
      receipt = new SpongeReceipt(new net.tnemc.core.transaction.TransactionResult(false, e.getMessage()), this, new SpongeCurrency(TNECore.eco().currency().defaultCurrency()), amount, context, type);
    }

    Sponge.eventManager().post(new SpongeTransactionEvent(receipt));

    return receipt;
  }

  private Optional<String> contextRegion(final Set<Context> context) {

    for(final Context con : context) {
      if(con.getKey().equalsIgnoreCase(Context.WORLD_KEY)) {
        return Optional.of(con.getValue());
      }
    }
    return Optional.empty();
  }
}
