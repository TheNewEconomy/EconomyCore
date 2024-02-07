package net.tnemc.core.api.response;
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

import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.actions.EconomyResponse;
import net.tnemc.core.api.TNEAPI;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * AccountAPIResponse represents an API response that is related to an account.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 * @see TNEAPI,Account,EconomyResponse
 */
public class AccountAPIResponse {

  private final @Nullable Account account;
  private final EconomyResponse response;

  public AccountAPIResponse(@Nullable Account account, EconomyResponse response) {
    this.account = account;
    this.response = response;
  }

  public Optional<Account> getAccount() {
    return Optional.ofNullable(account);
  }

  public Optional<PlayerAccount> getPlayerAccount() {
    if(account != null && (account instanceof PlayerAccount)) {
      return Optional.of((PlayerAccount)account);
    }
    return Optional.empty();
  }

  public EconomyResponse getResponse() {
    return response;
  }
}