package net.tnemc.core.manager;
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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.hook.papi.Placeholder;
import net.tnemc.core.utils.Identifier;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * PlaceholderManager
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class PlaceholderManager {

  private final Map<String, Placeholder> placeholders = new HashMap<>();

  /**
   * Retrieves the placeholder that applies to the given parameters.
   *
   * @param params The parameters to match against placeholders
   * @return Optional containing the matching placeholder if found, empty Optional otherwise
   */
  public Optional<Placeholder> placeholder(final String[] params) {

    for(final Placeholder holder : placeholders.values()) {

      if(holder.applies(params)) {

        return Optional.of(holder);
      }
    }
    return Optional.empty();
  }


  //Static

  /**
   * Parses the given ID string and returns the corresponding Identifier object.
   *
   * @param id The ID string to parse
   * @return The Identifier object representing the parsed ID
   */
  public static Identifier parseID(@NotNull final String id) {

    switch(id.toLowerCase(Locale.ROOT)) {
      case "inventory" -> { return EconomyManager.INVENTORY_ONLY; }
      case "virtual" -> { return EconomyManager.VIRTUAL; }
      case "ender" -> { return EconomyManager.E_CHEST; }
      default -> { return EconomyManager.NORMAL; }
    }
  }

  /**
   * Parses the holdings entries for a specific account based on region, currency, ID, and formatting preference.
   *
   * @param account The account to retrieve holdings from
   * @param region The region to get holdings for
   * @param currency The currency UUID for the holdings
   * @param id The ID string representing the type of holdings to retrieve
   * @param formatted Boolean indicating whether the holdings should be formatted
   * @return A string representation of the parsed holdings based on the given criteria
   */
  public static String parseHoldings(@NotNull final Account account, @NotNull final String region,
                                     final @NotNull UUID currency, @NotNull final String id,
                                     final boolean formatted) {

    final Identifier parsedID = parseID(id);

    final List<HoldingsEntry> entries = account.getHoldings(region, currency, parsedID);

    final HoldingsEntry entry = (!entries.isEmpty())? entries.get(0) : new HoldingsEntry(region, currency, BigDecimal.ZERO, parsedID);

    if(formatted) {
      return CurrencyFormatter.format(account, entry);
    }
    return entry.getAmount().toPlainString();
  }

  /**
   * Adds the provided placeholder to the PlaceholderManager for later retrieval.
   *
   * @param placeholder The Placeholder object to add
   */
  public void addPlaceholder(@NotNull final Placeholder placeholder) {

    placeholders.put(placeholder.identifier(), placeholder);
  }

  public Map<String, Placeholder> placeholders() {

    return placeholders;
  }
}