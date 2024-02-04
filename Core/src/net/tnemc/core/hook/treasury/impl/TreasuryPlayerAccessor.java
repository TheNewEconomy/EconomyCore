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

import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor;
import net.tnemc.core.TNECore;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.hook.treasury.wrapper.TreasuryPlayerAccount;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * TreasuryPlayerAccessor
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TreasuryPlayerAccessor extends PlayerAccountAccessor {

  @Override
  protected @NotNull CompletableFuture<PlayerAccount> getOrCreate(@NotNull PlayerAccountCreateContext context) {

    final UUID id = context.getUniqueId();
    final Optional<String> name = TNECore.server().fromID(id);
    if(name.isEmpty()) {
      return CompletableFuture.failedFuture(new IllegalArgumentException("Invalid player UUID. Not located on server before!"));
    }

    final AccountAPIResponse response = TNECore.eco().account().createAccount(id.toString(), name.get());
    if(response.getPlayerAccount().isEmpty()) {
      return CompletableFuture.failedFuture(new IllegalArgumentException("Player account creation failed: " + response.getResponse().response()));
    }
    return CompletableFuture.completedFuture(new TreasuryPlayerAccount(response.getPlayerAccount().get()));
  }
}
