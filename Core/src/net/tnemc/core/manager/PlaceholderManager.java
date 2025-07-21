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
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.source.PluginSource;
import net.tnemc.core.channel.MessageHandler;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.hook.papi.Placeholder;
import net.tnemc.core.hook.papi.impl.accbalance.AccountBalanceCurRegPlaceholder;
import net.tnemc.core.hook.papi.impl.accbalance.AccountBalanceCurRegStorPlaceholder;
import net.tnemc.core.hook.papi.impl.accbalance.AccountBalanceCurStorPlaceholder;
import net.tnemc.core.hook.papi.impl.accbalance.AccountBalanceCurrencyPlaceholder;
import net.tnemc.core.hook.papi.impl.accbalance.AccountBalancePlaceholder;
import net.tnemc.core.hook.papi.impl.accbalance.AccountBalanceRegStorPlaceholder;
import net.tnemc.core.hook.papi.impl.accbalance.AccountBalanceRegionPlaceholder;
import net.tnemc.core.hook.papi.impl.accbalance.AccountBalanceStoragePlaceholder;
import net.tnemc.core.hook.papi.impl.balance.BalanceCurRegPlaceholder;
import net.tnemc.core.hook.papi.impl.balance.BalanceCurRegStorPlaceholder;
import net.tnemc.core.hook.papi.impl.balance.BalanceCurStorPlaceholder;
import net.tnemc.core.hook.papi.impl.balance.BalanceCurrencyPlaceholder;
import net.tnemc.core.hook.papi.impl.balance.BalancePlaceholder;
import net.tnemc.core.hook.papi.impl.balance.BalanceRegStorPlaceholder;
import net.tnemc.core.hook.papi.impl.balance.BalanceRegionPlaceholder;
import net.tnemc.core.hook.papi.impl.balance.BalanceStoragePlaceholder;
import net.tnemc.core.hook.papi.impl.currency.CurrencyNamePlaceholder;
import net.tnemc.core.hook.papi.impl.currency.CurrencyPrecisionPlaceholder;
import net.tnemc.core.hook.papi.impl.currency.CurrencySymbolPlaceholder;
import net.tnemc.core.hook.papi.impl.currency.CurrencyTestPlaceholder;
import net.tnemc.core.hook.papi.impl.currency.CurrencyTypePlaceholder;
import net.tnemc.core.hook.papi.impl.info.InfoCreatedPlaceholder;
import net.tnemc.core.hook.papi.impl.info.InfoIDPlaceholder;
import net.tnemc.core.hook.papi.impl.info.InfoStatusPlaceholder;
import net.tnemc.core.hook.papi.impl.info.InfoTypePlaceholder;
import net.tnemc.core.hook.papi.impl.misc.MISCDebugPlaceholder;
import net.tnemc.core.hook.papi.impl.misc.MISCVersionPlaceholder;
import net.tnemc.core.hook.papi.impl.permission.PermissionBalancePlaceholder;
import net.tnemc.core.hook.papi.impl.permission.PermissionDepositPlaceholder;
import net.tnemc.core.hook.papi.impl.permission.PermissionInvitePlaceholder;
import net.tnemc.core.hook.papi.impl.permission.PermissionTransferPlaceholder;
import net.tnemc.core.hook.papi.impl.permission.PermissionWithdrawPlaceholder;
import net.tnemc.core.hook.papi.impl.top.TopPosAccountPlaceholder;
import net.tnemc.core.hook.papi.impl.top.TopPosBalancePlaceholder;
import net.tnemc.core.hook.papi.impl.top.TopPosHolderPlaceholder;
import net.tnemc.core.hook.papi.impl.top.TopPosPlaceholder;
import net.tnemc.core.hook.papi.impl.transaction.TransactionCurRegPlaceholder;
import net.tnemc.core.hook.papi.impl.transaction.TransactionCurrencyPlaceholder;
import net.tnemc.core.hook.papi.impl.transaction.TransactionPlaceholder;
import net.tnemc.core.hook.papi.impl.transaction.TransactionRegionPlaceholder;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.utils.Identifier;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import net.tnemc.plugincore.core.io.message.MessageData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

  public PlaceholderManager() {

    addPlaceholder(new AccountBalanceCurRegPlaceholder());
    addPlaceholder(new AccountBalanceCurRegStorPlaceholder());
    addPlaceholder(new AccountBalanceCurrencyPlaceholder());
    addPlaceholder(new AccountBalanceCurStorPlaceholder());
    addPlaceholder(new AccountBalancePlaceholder());
    addPlaceholder(new AccountBalanceRegionPlaceholder());
    addPlaceholder(new AccountBalanceRegStorPlaceholder());
    addPlaceholder(new AccountBalanceStoragePlaceholder());

    addPlaceholder(new BalanceCurRegPlaceholder());
    addPlaceholder(new BalanceCurRegStorPlaceholder());
    addPlaceholder(new BalanceCurrencyPlaceholder());
    addPlaceholder(new BalanceCurStorPlaceholder());
    addPlaceholder(new BalancePlaceholder());
    addPlaceholder(new BalanceRegionPlaceholder());
    addPlaceholder(new BalanceRegStorPlaceholder());
    addPlaceholder(new BalanceStoragePlaceholder());

    addPlaceholder(new CurrencyNamePlaceholder());
    addPlaceholder(new CurrencyPrecisionPlaceholder());
    addPlaceholder(new CurrencySymbolPlaceholder());
    addPlaceholder(new CurrencyTestPlaceholder());
    addPlaceholder(new CurrencyTypePlaceholder());

    addPlaceholder(new InfoCreatedPlaceholder());
    addPlaceholder(new InfoIDPlaceholder());
    addPlaceholder(new InfoStatusPlaceholder());
    addPlaceholder(new InfoTypePlaceholder());

    addPlaceholder(new MISCDebugPlaceholder());
    addPlaceholder(new MISCVersionPlaceholder());

    addPlaceholder(new PermissionBalancePlaceholder());
    addPlaceholder(new PermissionDepositPlaceholder());
    addPlaceholder(new PermissionInvitePlaceholder());
    addPlaceholder(new PermissionTransferPlaceholder());
    addPlaceholder(new PermissionWithdrawPlaceholder());

    addPlaceholder(new TopPosAccountPlaceholder());
    addPlaceholder(new TopPosBalancePlaceholder());
    addPlaceholder(new TopPosHolderPlaceholder());
    addPlaceholder(new TopPosPlaceholder());

    addPlaceholder(new TransactionCurRegPlaceholder());
    addPlaceholder(new TransactionCurrencyPlaceholder());
    addPlaceholder(new TransactionPlaceholder());
    addPlaceholder(new TransactionRegionPlaceholder());
  }

  /**
   * Parses the given ID string and returns the corresponding Identifier object.
   *
   * @param id The ID string to parse
   *
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
   * Parses the transaction type string and determines the corresponding identifier for the
   * transaction.
   *
   * @param type The transaction type string to parse
   *
   * @return The corresponding identifier for the transaction type
   */
  public static String parseTransactionType(@NotNull final String type) {

    switch(type.toLowerCase(Locale.ROOT)) {
      case "give" -> { return "give"; }
      case "take" -> { return "take"; }
      default -> { return "set"; }
    }
  }

  /**
   * Performs a transaction on the specified account.
   *
   * @param account  The account involved in the transaction
   * @param type     The type of transaction to be performed
   * @param modifier The modifier for adjusting holdings during the transaction
   * @param message  A flag indicating whether a message should be sent for the transaction
   *
   * @return An Optional containing a receipt of the transaction if successful, otherwise empty
   * Optional
   */
  public static Optional<Receipt> transact(final Account account, final String type,
                                           final HoldingsModifier modifier, final boolean message) {

    final Transaction transaction = new Transaction(type)
            .to(account, modifier)
            .processor(EconomyManager.baseProcessor())
            .source(new PluginSource("PAPI"));

    try {
      final TransactionResult result = transaction.process();

      if(!result.isSuccessful()) {
        return Optional.empty();
      }

      if(message) {
        message(account, type, modifier);
      }

      return result.getReceipt();
    } catch(final Exception ignore) {
      return Optional.empty();
    }
  }

  public static void message(final Account account, final String type, final HoldingsModifier modifier) {

    if(type.equalsIgnoreCase("set")) return;

    final String message = (type.equalsIgnoreCase("take"))? "Messages.Money.Taken" : "Messages.Money.Given";
    final MessageData msgData = new MessageData(message);
    msgData.addReplacement("$player", MainConfig.yaml().getString("Core.Server.Account.Name"));
    msgData.addReplacement("$currency", modifier.getCurrency());
    msgData.addReplacement("$amount", CurrencyFormatter.format(account, modifier.asEntry()));
    MessageHandler.send(account.getIdentifier(), msgData.grab(account.getIdentifier()));

    if(account.isPlayer() && ((PlayerAccount)account).isOnline()) {

      final Optional<PlayerProvider> provider = ((PlayerAccount)account).getPlayer();

      provider.ifPresent(playerProvider->playerProvider.message(msgData));
    }
  }

  /**
   * Parses the holdings entries for a specific account based on region, currency, ID, and
   * formatting preference.
   *
   * @param account   The account to retrieve holdings from
   * @param region    The region to get holdings for
   * @param currency  The currency UUID for the holdings
   * @param id        The ID string representing the type of holdings to retrieve
   * @param formatted Boolean indicating whether the holdings should be formatted
   *
   * @return A string representation of the parsed holdings based on the given criteria
   */
  public static final String parseHoldings(@NotNull final Account account, @NotNull final String region,
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


  //Static

  public final @Nullable String onRequest(@Nullable final String account, @NotNull final String[] params) {

    final Optional<Placeholder> placeholderOptional = placeholder(params);
    if(placeholderOptional.isPresent()) {
      return placeholderOptional.get().onRequest(account, params);
    }
    return null;
  }

  /**
   * Retrieves the placeholder that applies to the given parameters.
   *
   * @param params The parameters to match against placeholders
   *
   * @return Optional containing the matching placeholder if found, empty Optional otherwise
   */
  public final Optional<Placeholder> placeholder(final String[] params) {

    for(final Placeholder holder : placeholders.values()) {

      if(holder.applies(params)) {

        return Optional.of(holder);
      }
    }
    return Optional.empty();
  }

  /**
   * Adds the provided placeholder to the PlaceholderManager for later retrieval.
   *
   * @param placeholder The Placeholder object to add
   */
  public final void addPlaceholder(@NotNull final Placeholder placeholder) {

    placeholders.put(placeholder.identifier(), placeholder);
  }

  public final Map<String, Placeholder> placeholders() {

    return placeholders;
  }
}