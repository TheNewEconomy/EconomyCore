package net.tnemc.core.hook.papi.impl.accbalance;
/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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
import net.tnemc.core.account.Account;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.hook.papi.Placeholder;
import net.tnemc.core.manager.PlaceholderManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * BalancePlaceholder
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class AccountBalanceCurRegStorPlaceholder implements Placeholder {

  /**
   * Retrieves the identifier associated with this symbol.
   *
   * @return The identifier as a string
   */
  @Override
  public String identifier() {

    return "tne_accbalance_curregstor";
  }

  /**
   * Checks if the specified parameters are valid for this method.
   *
   * @param params The parameters to be checked
   *
   * @return True if the parameters are valid, false otherwise
   */
  @Override
  public boolean applies(final String[] params) {

    return params[0].equalsIgnoreCase("accbalance") && params.length >= 4
           && params[1].equalsIgnoreCase("curregstor");
  }

  /**
   * Processes a request with the given account and parameters.
   *
   * @param account Optional account associated with the request
   * @param params  Required parameters for the request
   *
   * @return Nullable string response from the request process
   */
  @Override
  public @Nullable String onRequest(@Nullable final String account, @NotNull final String[] params) {

    final Optional<Account> accountOptional = TNECore.eco().account().findAccount(params[1]);

    if(accountOptional.isEmpty()) {
      return null;
    }

    final Optional<Currency> currency = TNECore.eco().currency().find(params[3]);
    if(currency.isEmpty()) {

      return null;
    }

    final String region = TNECore.eco().region().resolve(params[4]);
    final boolean formatted = (params[params.length - 1].equalsIgnoreCase("formatted"));

    return PlaceholderManager.parseHoldings(accountOptional.get(), region,
                                            currency.get().getUid(),
                                            params[5], formatted);
  }
}