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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.api.response.AccountAPIResponse;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.AccountDeletionResultType;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * SpongeEconomy
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeEconomy implements EconomyService {

  @Override
  public Currency defaultCurrency() {
    return new SpongeCurrency(TNECore.eco().currency().getDefaultCurrency());
  }

  @Override
  public boolean hasAccount(UUID identifier) {
    return TNECore.eco().account().findAccount(identifier).isPresent();
  }

  @Override
  public boolean hasAccount(String identifier) {
    return TNECore.eco().account().findAccount(identifier).isPresent();
  }

  @Override
  public Optional<UniqueAccount> findOrCreateAccount(UUID identifier) {

    final Optional<PlayerAccount> account = TNECore.eco().account().findPlayerAccount(identifier);
    if(account.isEmpty()) {

      final Optional<ServerPlayer> playerProvider = Sponge.server().player(identifier);
      if(playerProvider.isPresent()) {

        final AccountAPIResponse response = TNECore.eco().account().createAccount(identifier.toString(), playerProvider.get().name());
        if(response.getResponse().success() && response.getPlayerAccount().isPresent()) {
          return Optional.of(new SpongeUniqueAccount(response.getPlayerAccount().get()));
        }
      }
      return Optional.empty();
    }
    return Optional.of(new SpongeUniqueAccount(account.get()));
  }

  @Override
  public Optional<Account> findOrCreateAccount(String identifier) {
    return Optional.empty();
  }

  @Override
  public Stream<UniqueAccount> streamUniqueAccounts() {
    return uniqueAccounts().stream();
  }

  @Override
  public Collection<UniqueAccount> uniqueAccounts() {
    return new ArrayList<>();
  }

  @Override
  public Stream<VirtualAccount> streamVirtualAccounts() {
    return virtualAccounts().stream();
  }

  @Override
  public Collection<VirtualAccount> virtualAccounts() {
    return new ArrayList<>();
  }

  @Override
  public AccountDeletionResultType deleteAccount(UUID identifier) {
    return new SpongeDeletionResult(TNECore.eco().account().deleteAccount(identifier));
  }

  @Override
  public AccountDeletionResultType deleteAccount(String identifier) {
    final Optional<net.tnemc.core.account.Account> account = TNECore.eco().account().findAccount(identifier);

    return new SpongeDeletionResult(TNECore.eco().account().deleteAccount(account.get().getIdentifier()));
  }
}