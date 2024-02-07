package net.tnemc.core.hook.treasury;
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

import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.EconomyProvider;
import me.lokka30.treasury.api.economy.account.AccountData;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor;
import me.lokka30.treasury.api.economy.currency.Currency;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.hook.treasury.impl.TreasuryNonPlayerAccessor;
import net.tnemc.core.hook.treasury.impl.TreasuryPlayerAccessor;
import net.tnemc.core.hook.treasury.wrapper.TreasuryCurrency;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * TNETreasury
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TNETreasury implements EconomyProvider, AccountAccessor {


  final Set<Currency> currencies = new HashSet<>();
  final TreasuryPlayerAccessor playerAccessor = new TreasuryPlayerAccessor();
  final TreasuryNonPlayerAccessor nonPlayerAccessor = new TreasuryNonPlayerAccessor();

  @Override
  public @NotNull AccountAccessor accountAccessor() {
    return this;
  }

  @Override
  public @NotNull CompletableFuture<Boolean> hasAccount(@NotNull AccountData accountData) {

    return CompletableFuture.supplyAsync(()->{
      final String id = (accountData.isPlayerAccount())? accountData.getPlayerIdentifier().get().toString() :
              accountData.getNonPlayerIdentifier().get().toString().replace("tne:", "");
      return TNECore.eco().account().findAccount(id).isPresent();
    });
  }

  @Override
  public @NotNull CompletableFuture<Collection<UUID>> retrievePlayerAccountIds() {

    return CompletableFuture.supplyAsync(()->{
      final Collection<UUID> ids = new ArrayList<>();
      for(Account account : TNECore.eco().account().getAccounts().values()) {
        if(account instanceof PlayerAccount) ids.add(((PlayerAccount)account).getUUID());
      }
      return ids;
    });
  }

  @Override
  public @NotNull CompletableFuture<Collection<NamespacedKey>> retrieveNonPlayerAccountIds() {

    return CompletableFuture.supplyAsync(()->{
      final Collection<NamespacedKey> ids = new ArrayList<>();
      for(Account account : TNECore.eco().account().getAccounts().values()) {
        if(account instanceof SharedAccount) ids.add(NamespacedKey.fromString("tne:" + account.getIdentifier()));
      }
      return ids;
    });
  }

  @Override
  public @NotNull Currency getPrimaryCurrency() {
    return new TreasuryCurrency(TNECore.eco().currency().getDefaultCurrency());
  }

  @Override
  public @NotNull Optional<Currency> findCurrency(@NotNull String identifier) {

    for(Currency cur : currencies) {
      if(cur.getIdentifier().equalsIgnoreCase(identifier)) return Optional.of(cur);
    }
    return Optional.empty();
  }

  @Override
  public @NotNull Set<Currency> getCurrencies() {

    if(currencies.size() == 0) {
      for(net.tnemc.core.currency.Currency cur : TNECore.eco().currency().currencies()) {
        currencies.add(new TreasuryCurrency(cur));
      }
    }
    return currencies;
  }

  @Override
  public @NotNull CompletableFuture<TriState> registerCurrency(@NotNull Currency currency) {
    //TODO: complete this.
    return null;
  }

  @Override
  public @NotNull CompletableFuture<TriState> unregisterCurrency(@NotNull Currency currency) {
    //TODO: complete this.
    return null;
  }

  @Override
  public @NotNull PlayerAccountAccessor player() {
    return playerAccessor;
  }

  @Override
  public @NotNull NonPlayerAccountAccessor nonPlayer() {
    return nonPlayerAccessor;
  }
}