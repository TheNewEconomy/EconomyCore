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
import net.tnemc.plugincore.PluginCore;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
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

    switch (value.toLowerCase()) {
      case "self_account":
      case "@s":
      case "@me":

        value = context.actor().name();
        break;
      case "@r":
        final List<Account> accounts = new ArrayList<>(TNECore.eco().account().getAccounts().values());

        if(!accounts.isEmpty()) {
          value = accounts.get(new Random().nextInt(accounts.size())).getName();
        }
        break;
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

    return context -> {
      final String partial = context.input().peekString().toLowerCase();

      //Add in our online players first
      final Set<String> onlineNames = PluginCore.server().onlinePlayersList().stream()
              .filter(name -> name.toLowerCase().startsWith(partial))
              .filter(name -> !TNECore.eco().account().excluded(name))
              .collect(Collectors.toSet());

      final Set<String> suggestions = new LinkedHashSet<>(onlineNames);

      //Matching offline account names (not online & not excluded)
      TNECore.eco().account().getAccounts().values().stream()
              .map(Account::getName)
              .filter(name -> name.toLowerCase().startsWith(partial))
              .filter(name -> !onlineNames.contains(name))
              .filter(name -> !TNECore.eco().account().excluded(name))
              .forEach(suggestions::add);

      //if("self_account".startsWith(partial) || "@s".startsWith(partial) || "@me".startsWith(partial)) {
      if("self_account".startsWith(partial)) {
        suggestions.add("SELF_ACCOUNT");
      }

      /*if("@r".startsWith(partial)) {
        suggestions.add("@r");
      }*/

      return new ArrayList<>(suggestions);
    };
  }
}
