package net.tnemc.core.command.parameters.resolver;

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
import net.tnemc.core.account.AccountStatus;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * AccountResolver
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AccountResolver implements ParameterType<CommandActor, Account> {

  @Override
  public Account parse(@NotNull final MutableStringStream input, @NotNull final ExecutionContext<CommandActor> context) {

    String value = input.readString();

    if(value.equalsIgnoreCase("SELF_ACCOUNT")) {
      value = context.actor().name();
    }


    if(!TNECore.eco().account().excluded(value)) {

      final Optional<Account> account = TNECore.eco().account().findAccount(value);
      if(account.isPresent()) {
        return account.get();
      }
    }
    return null;
  }

  @Override
  public @NotNull SuggestionProvider<@NotNull CommandActor> defaultSuggestions() {

    return (context)->List.copyOf(TNECore.eco().account().getAccounts().keySet());
  }
}
