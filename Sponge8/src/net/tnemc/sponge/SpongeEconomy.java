package net.tnemc.sponge;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.sponge.impl.eco.SpongeCurrency;
import net.tnemc.sponge.impl.eco.SpongeUniqueAccount;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.AccountDeletionResultType;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;

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
  public Optional<UniqueAccount> findOrCreateAccount(UUID uuid) {
    final Optional<PlayerAccount> account = TNECore.eco().account().findPlayerAccount(uuid);
    if(account.isEmpty()) {
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
    return null;
  }

  @Override
  public Stream<VirtualAccount> streamVirtualAccounts() {
    return virtualAccounts().stream();
  }

  @Override
  public Collection<VirtualAccount> virtualAccounts() {
    return null;
  }

  @Override
  public AccountDeletionResultType deleteAccount(UUID uuid) {
    return null;
  }

  @Override
  public AccountDeletionResultType deleteAccount(String identifier) {
    return null;
  }
}