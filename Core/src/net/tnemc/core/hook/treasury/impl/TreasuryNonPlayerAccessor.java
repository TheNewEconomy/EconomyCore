package net.tnemc.core.hook.treasury.impl;
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

import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.hook.treasury.wrapper.TreasuryNonPlayerAccount;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * TreasuryNonPlayerAccessor
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TreasuryNonPlayerAccessor extends NonPlayerAccountAccessor {
  @Override
  protected @NotNull CompletableFuture<NonPlayerAccount> getOrCreate(@NotNull NonPlayerAccountCreateContext context) {

    final AccountAPIResponse response = TNECore.eco().account().createAccount(context.getIdentifier().toString(), context.getName());
    if(response.getAccount().isEmpty() || !(response.getAccount().get() instanceof SharedAccount)) {
      return CompletableFuture.failedFuture(new IllegalArgumentException("Account creation failed: " + response.getResponse().response()));
    }
    return CompletableFuture.completedFuture(new TreasuryNonPlayerAccount((SharedAccount)response.getAccount().get()));
  }
}
