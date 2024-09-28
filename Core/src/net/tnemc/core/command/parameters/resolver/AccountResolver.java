package net.tnemc.core.command.parameters.resolver;

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
import net.tnemc.core.account.Account;
import net.tnemc.core.utils.exceptions.InvalidAccountException;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.process.ValueResolver;

import java.util.Optional;

/**
 * AccountResolver
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AccountResolver implements ValueResolver<Account> {

  @Override
  public Account resolve(@NotNull final ValueResolverContext context) throws Throwable {

    String value = context.arguments().pop();

    if(value.equalsIgnoreCase("SELF_ACCOUNT")) {
      value = context.actor().getName();
    }

    final Optional<Account> account = TNECore.eco().account().findAccount(value);
    if(account.isPresent()) {
      return account.get();
    }
    throw new InvalidAccountException(value);
  }
}
