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

import me.lokka30.treasury.api.common.NamespacedKey;
import me.lokka30.treasury.api.common.misc.TriState;
import me.lokka30.treasury.api.economy.account.AccountPermission;
import me.lokka30.treasury.api.economy.account.NonPlayerAccount;
import net.tnemc.core.account.SharedAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * TreasuryNonPlayerAccount
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TreasuryNonPlayerAccount extends TreasuryAccount implements NonPlayerAccount {

  public TreasuryNonPlayerAccount(SharedAccount account) {
    super(account);
  }

  @Override
  public @NotNull NamespacedKey identifier() {
    if(account.getIdentifier().contains(":")) {
      return NamespacedKey.fromString(account.getIdentifier());
    }
    return NamespacedKey.of("tne", account.getIdentifier());
  }

  @Override
  public @NotNull CompletableFuture<Boolean> setName(@Nullable String name) {
    return CompletableFuture.failedFuture(new IllegalStateException("TNE does not support changing names of accounts through Treasury."));
  }

  @Override
  public @NotNull CompletableFuture<Collection<UUID>> retrieveMemberIds() {
    return CompletableFuture.completedFuture(((SharedAccount)account).getMembers().keySet());
  }

  @Override
  public @NotNull CompletableFuture<Boolean> isMember(@NotNull UUID player) {
    return CompletableFuture.completedFuture(((SharedAccount)account).isMember(player));
  }

  @Override
  public @NotNull CompletableFuture<Boolean> setPermissions(@NotNull UUID player, @NotNull Map<AccountPermission, TriState> permissionsMap) {
    return CompletableFuture.failedFuture(new UnsupportedOperationException("This functionality is currently unsupported by TNE."));
  }

  @Override
  public @NotNull CompletableFuture<Map<AccountPermission, TriState>> retrievePermissions(@NotNull UUID player) {
    return CompletableFuture.failedFuture(new UnsupportedOperationException("This functionality is currently unsupported by TNE."));
  }

  @Override
  public @NotNull CompletableFuture<Map<UUID, Map<AccountPermission, TriState>>> retrievePermissionsMap() {
    return CompletableFuture.failedFuture(new UnsupportedOperationException("This functionality is currently unsupported by TNE."));
  }

  @Override
  public @NotNull CompletableFuture<TriState> hasPermissions(@NotNull UUID player, @NotNull AccountPermission @NotNull ... permissions) {
    return CompletableFuture.failedFuture(new UnsupportedOperationException("This functionality is currently unsupported by TNE."));
  }
}