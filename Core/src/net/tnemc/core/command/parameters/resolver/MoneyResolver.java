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
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.parser.ParseMoney;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * PercentDecimalResolver
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class MoneyResolver implements ParameterType<CommandActor, ParseMoney> {

  public static String trimQuotes(final String input) {

    if(input == null || input.length() < 2) return input;

    final char first = input.charAt(0);
    final char last = input.charAt(input.length() - 1);

    // Check if first and last characters are matching quotes
    if((first == '\'' || first == '"') && first == last) {
      return input.substring(1, input.length() - 1);
    }

    return input;
  }

  @Override
  public ParseMoney parse(@NotNull final MutableStringStream input, @NotNull final ExecutionContext<CommandActor> context) {


    final String value = trimQuotes(input.readString());

    final String region = (context.getResolvedArgumentOrNull("region") == null)? TNECore.eco().region().defaultRegion() : context.getResolvedArgumentOrNull("region");
    final Currency currency = (context.getResolvedArgumentOrNull("currency") == null)? TNECore.eco().currency().defaultCurrency(region) : context.getResolvedArgumentOrNull("currency");

    return TNECore.eco().currency().parser().parse(region, currency, value);
  }

  @Override
  public @NotNull SuggestionProvider<@NotNull CommandActor> defaultSuggestions() {

    return context->{

      final String partial = trimQuotes(context.input().peekString().toLowerCase());

      final Set<String> suggestionsParsing = new LinkedHashSet<>();

      for(final Currency currency : TNECore.eco().currency().getCurrencies().values()) {

        final String symbol = currency.getSymbol();
        if(currency.getDecimalPlaces() >= 2) {
          suggestionsParsing.add(symbol + "0.01");
        }

        suggestionsParsing.add(symbol + "1");
        suggestionsParsing.add(symbol + "10");
        suggestionsParsing.add(symbol + "100");
        suggestionsParsing.add(symbol + "1000");
        suggestionsParsing.add(symbol + "1000000");

        String prefix = String.valueOf(currency.getPrefixes().charAt(0));
        suggestionsParsing.add(symbol + "1" + prefix);     //1,000
        suggestionsParsing.add(symbol + "10" + prefix);    //10,000
        suggestionsParsing.add(symbol + "100" + prefix);   //100,000

        prefix = String.valueOf(currency.getPrefixes().charAt(1));
        suggestionsParsing.add(symbol + "1" + prefix);     //1,000,000
        suggestionsParsing.add(symbol + "10" + prefix);    //10,000,000
        suggestionsParsing.add(symbol + "100" + prefix);    //100,000,000

        prefix = String.valueOf(currency.getPrefixes().charAt(2));
        suggestionsParsing.add(symbol + "1" + prefix);     //1,000,000,000
      }


      final Set<String> suggestions = new LinkedHashSet<>();
      for(final String str : suggestionsParsing) {

        if(str.toLowerCase().startsWith(partial)) {

          suggestions.add(str);
        }
      }

      return new ArrayList<>(suggestions);
    };
  }
}