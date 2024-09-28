package net.tnemc.core.api;

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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.AccountStatus;
import net.tnemc.core.account.NonPlayerAccount;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.HoldingsHandler;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.ActionSource;
import net.tnemc.core.actions.EconomyResponse;
import net.tnemc.core.actions.source.PluginSource;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.currency.format.FormatRule;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.api.CallbackManager;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * The core TNE API class.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TNEAPI {


  /**
   * The callback manager for the TNE API. This adds an event-style system that is supported across
   * every platform.
   *
   * @return The callback manager that is being used.
   */
  public CallbackManager callbacks() {

    return PluginCore.callbacks();
  }

  /**
   * Used to add a new {@link HoldingsHandler} to the {@link net.tnemc.core.EconomyManager}. These
   * handlers are able to add additional holdings sources for accounts.
   *
   * @param handler The handler to add.
   *
   * @see <a href="https://github.com/TheNewEconomy/EconomyCore/wiki/API#holdings-handlers">Handlers
   * Wiki</a>
   */
  public void addHandler(final HoldingsHandler handler) {

    EconomyManager.instance().addHandler(handler);
  }

  /**
   * Adds an account type represented by the specified class along with a validation function.
   *
   * @param type  The {@link Class} representing the account type, extending {@link SharedAccount}.
   * @param check The validation function that takes a {@code String} argument and returns a
   *              {@code Boolean}. It is used to check if an account of the specified type is valid
   *              based on some criteria. The {@code String} argument is the account's name.
   */
  public void addAccountType(final Class<? extends SharedAccount> type, final Function<String, Boolean> check) {
    //TODO: implement this.
  }

  /**
   * Adds an account status to the collection.
   *
   * @param status The {@code AccountStatus} to be added.
   */
  public void addAccountStatus(final AccountStatus status) {

    TNECore.eco().account().addAccountStatus(status);
  }

  /**
   * Used to determine if an {@link Account} exists with the specified identifier.
   * <p>
   * This method is safe to search for non-player accounts.
   *
   * @param identifier The string identifier for the account that is being looked for.
   *
   * @return True if an account with the specified identifier exists, otherwise false.
   *
   * @since 0.1.2.0
   */
  public boolean hasAccount(@NotNull final String identifier) {

    return TNECore.eco().account().findAccount(identifier).isPresent();
  }

  /**
   * Used to determine if an {@link PlayerAccount} exists with the specified identifier.
   * <p>
   * This method is not safe to search for non-player accounts.
   *
   * @param identifier The {@link UUID identifier} for the account that is being looked for.
   *
   * @return True if an account with the specified identifier exists, otherwise false.
   *
   * @since 0.1.2.0
   */
  public boolean hasPlayerAccount(@NotNull final UUID identifier) {

    return TNECore.eco().account().findAccount(identifier).isPresent();
  }

  /**
   * Looks for an account based on the provided identifier and if none is found, creates it.
   * <p>
   * This method could return any of the following account types: - {@link NonPlayerAccount} -
   * {@link SharedAccount}
   * <p>
   * This method is safe to search for non-player accounts.
   *
   * @param identifier The string identifier for the account that is being looked for.
   * @param name       The string name for the account that is being looked for.
   *
   * @return The correlating {@link Account account} object if found, otherwise the one created.
   *
   * @since 0.1.2.0
   */
  public AccountAPIResponse getOrCreateAccount(@NotNull final String identifier, @NotNull final String name) {

    final AccountAPIResponse response = TNECore.eco().account().createAccount(identifier, name);

    return response;
  }

  /**
   * Looks for an account based on the provided identifier and if none is found, creates it.
   * <p>
   * This method returns an {@link PlayerAccount}.
   * <p>
   * This method is not safe to search for non-player accounts.
   *
   * @param identifier The {@link UUID} identifier for the account that is being looked for.
   * @param name       The string name for the account that is being looked for.
   *
   * @return The correlating {@link AccountAPIResponse response}.
   *
   * @since 0.1.2.0
   */
  public AccountAPIResponse getOrCreatePlayerAccount(@NotNull final UUID identifier, @NotNull final String name) {

    return getOrCreateAccount(identifier.toString(), name);
  }

  /**
   * Attempts to create an account with the given identifier. This method returns true if the
   * account was created, otherwise false.
   * <p>
   * This method is intended for non-player accounts.
   *
   * @param identifier The String identifier for the account that is being created.
   *
   * @return If the account is created an Optional containing the {@link SharedAccount account}, or
   * an empty Optional.
   *
   * @since 0.1.2.0
   */
  public Optional<SharedAccount> createAccount(@NotNull final String identifier) {

    return TNECore.eco().account().createNonPlayerAccount(identifier);
  }

  /**
   * Attempts to create an account with the given identifier. This method returns true if the
   * account was created, otherwise false.
   * <p>
   * This method is not intended for non-player accounts.
   *
   * @param identifier The {@link UUID} identifier for the account that is being created.
   * @param name       The String representation of the name for the account being created, usually
   *                   the username of the player.
   *
   * @return The correlating {@link AccountAPIResponse response}.
   *
   * @since 0.1.2.0
   */
  public AccountAPIResponse createPlayerAccount(@NotNull final UUID identifier, @NotNull final String name) {

    return TNECore.eco().account().createAccount(identifier.toString(), name);
  }

  /**
   * Looks for an account based on the provided identifier.
   * <p>
   * This method could return any of the following account types: - {@link PlayerAccount} -
   * {@link NonPlayerAccount} - {@link SharedAccount}
   * <p>
   * This method is safe to search for non-player accounts.
   *
   * @param identifier The string identifier for the account that is being looked for.
   *
   * @return An optional containing the {@link Account} if found, otherwise an empty optional.
   *
   * @since 0.1.2.0
   */
  public Optional<Account> getAccount(@NotNull final String identifier) {

    return TNECore.eco().account().findAccount(identifier);
  }

  /**
   * Looks for an {@link PlayerAccount} based on the provided {@link UUID identifier}.
   * <p>
   * This method is not safe to search for non-player accounts.
   *
   * @param identifier The {@link UUID identifier} for the account that is being looked for.
   *
   * @return An optional containing the {@link PlayerAccount} if found, otherwise an empty optional.
   *
   * @since 0.1.2.0
   */
  public Optional<PlayerAccount> getPlayerAccount(@NotNull final UUID identifier) {

    return TNECore.eco().account().findPlayerAccount(identifier);
  }

  /**
   * Used to delete the specified account.
   * <p>
   * This method is safe to search for non-player accounts.
   *
   * @param identifier The identifier associated with the account that you wish to delete.
   * @param source     The {@link ActionSource source} response for this deletion call.
   *
   * @return The {@link EconomyResponse response} that should be returned based on the deletion
   * action.
   *
   * @since 0.1.2.0
   */
  public EconomyResponse deleteAccount(@NotNull final String identifier, @NotNull final ActionSource source) {

    return TNECore.eco().account().deleteAccount(identifier);
  }

  /**
   * Used to delete the specified account.
   * <p>
   * This method is not safe to search for non-player accounts.
   *
   * @param identifier The identifier associated with the account that you wish to delete.
   * @param source     The {@link ActionSource source} response for this deletion call.
   *
   * @return The {@link EconomyResponse response} that should be returned based on the deletion
   * action.
   *
   * @since 0.1.2.0
   **/
  public EconomyResponse deleteAccount(@NotNull final UUID identifier, @NotNull final ActionSource source) {

    return TNECore.eco().account().deleteAccount(identifier);
  }

  /**
   * Adds a {@link CurrencyType} to the collection.
   *
   * @param type The {@link CurrencyType} to be added.
   */
  public void addCurrencyType(final CurrencyType type) {

    TNECore.eco().currency().addType(type);
  }

  /**
   * Adds a {@link Currency} to the collection.
   *
   * @param currency The {@link Currency} to be added.
   */
  public void addCurrency(final Currency currency) {

    TNECore.eco().currency().addCurrency(currency);
  }

  /**
   * Adds a {@link FormatRule} for balance formatting.
   *
   * @param rule The {@link FormatRule} to be added for balance formatting.
   */
  public void addBalanceFormatRule(final FormatRule rule) {

    CurrencyFormatter.addRule(rule);
  }

  /**
   * Used to get the default currency. This could be the default currency for the server globally or
   * for the default world if the implementation supports multi-world.
   *
   * @return The currency that is the default for the server if multi-world support is not available
   * otherwise the default for the default world.
   *
   * @since 0.1.2.0
   */
  public @NotNull Currency getDefaultCurrency() {

    return TNECore.api().getDefaultCurrency();
  }

  /**
   * Used to get the default currency for the specified world if this implementation has multi-world
   * support, otherwise the default currency for the server.
   *
   * @param region The region to get the default currency for. This could be a world, biomes, or a
   *               third party based region.
   *
   * @return The default currency for the specified world if this implementation has multi-world
   * support, otherwise the default currency for the server.
   *
   * @since 0.1.2.0
   */
  public @NotNull Currency getDefaultCurrency(@NotNull final String region) {

    return TNECore.eco().currency().getDefaultCurrency(region);
  }

  /**
   * Used to get a set of every  {@link Currency} object for the server.
   *
   * @return A set of every {@link Currency} object that is available for the server.
   *
   * @since 0.1.2.0
   */
  public Collection<Currency> getCurrencies() {

    return TNECore.eco().currency().currencies();
  }

  /**
   * Used to get a set of every {@link Currency} object that is available in the specified world if
   * this implementation has multi-world support, otherwise all {@link Currency} objects for the
   * server.
   *
   * @param region The region to get the currencies for. This could be a world, biomes, or a third
   *               party based region.
   *
   * @return A set of every {@link Currency} object that is available in the specified world if this
   * implementation has multi-world support, otherwise all {@link Currency} objects for the server.
   *
   * @since 0.1.2.0
   */
  public Collection<Currency> getCurrencies(@NotNull final String region) {

    return TNECore.api().getCurrencies(region);
  }

  /**
   * Used to get the holdings of the specified identifier.
   *
   * @param identifier The identifier to get the holdings for.
   * @param world      The world to use for getting the holdings.
   * @param currency   The currency to use for getting the holdings
   *
   * @return The holdings in {@link BigDecimal} format.
   */
  public BigDecimal getHoldings(final String identifier, final String world, final String currency) {

    final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
    final Optional<Currency> currency1 = TNECore.eco().currency().findCurrency(currency);

    if(account.isPresent() && currency1.isPresent()) {
      return account.get().getHoldingsTotal(world, currency1.get().getUid());
    }
    return BigDecimal.ZERO;
  }

  /**
   * Used to check if a specific identifier has an amount of holdings.
   *
   * @param identifier The identifier to check the holdings for.
   * @param world      The world to use for the holdings check.
   * @param currency   The currency to use for the holdings check.
   * @param amount     The {@link BigDecimal} amount that we need to check if the identifier has.
   *
   * @return True if the specified identifier has the holdings, otherwise false.
   */
  public boolean hasHoldings(final String identifier, final String world, final String currency, final BigDecimal amount) {

    return getHoldings(identifier, world, currency).compareTo(amount) >= 0;
  }

  /**
   * Used to remove a certain amount of holdings from an identifier.
   *
   * @param identifier The identifier to use for the transaction.
   * @param world      The world to use for the transaction.
   * @param currency   The currency to use for the transaction.
   * @param amount     The amount to remove.
   * @param pluginName The name of the plugin performing this transaction.
   *
   * @return The associated {@link TransactionResult result} from the transaction.
   */
  public TransactionResult removeHoldings(final String identifier, final String world, final String currency, final BigDecimal amount, final String pluginName) {

    final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
    final Optional<Currency> currencyObject = TNECore.eco().currency().findCurrency(currency);

    if(account.isPresent() && currencyObject.isPresent()) {

      final HoldingsModifier modifier = new HoldingsModifier(world,
                                                             currencyObject.get().getUid(),
                                                             amount);

      final Transaction transaction = new Transaction("take")
              .to(account.get(), modifier)
              .source(new PluginSource(pluginName));
      try {

        return transaction.process();

      } catch(InvalidTransactionException e) {
        return new TransactionResult(false, e.getMessage());
      }
    }
    return new TransactionResult(false, "Invalid account or currency provided");
  }

  /**
   * Used to add a certain amount of holdings to an identifier.
   *
   * @param identifier The identifier to use for the transaction.
   * @param world      The world to use for the transaction.
   * @param currency   The currency to use for the transaction.
   * @param amount     The amount to add.
   * @param pluginName The name of the plugin performing this transaction.
   *
   * @return The associated {@link TransactionResult result} from the transaction.
   */
  public TransactionResult addHoldings(final String identifier, final String world, final String currency, final BigDecimal amount, final String pluginName) {

    final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
    final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency(currency);

    if(account.isPresent() && currencyOptional.isPresent()) {

      final HoldingsModifier modifier = new HoldingsModifier(world,
                                                             currencyOptional.get().getUid(),
                                                             amount);

      final Transaction transaction = new Transaction("give")
              .to(account.get(), modifier)
              .source(new PluginSource(pluginName));
      try {

        return transaction.process();

      } catch(InvalidTransactionException e) {
        return new TransactionResult(false, e.getMessage());
      }
    }
    return new TransactionResult(false, "Invalid account or currency provided");
  }

  /**
   * Used to set the holdings of the specified identifier.
   *
   * @param identifier The identifier to set the holdings for.
   * @param world      The world to use for setting the holdings.
   * @param currency   The currency to use for setting the holdings
   *
   * @return True if the transactions was successful, otherwise false.
   */
  public boolean setHoldings(final String identifier, final String world, final String currency, final BigDecimal amount) {

    final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
    final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency(currency);

    if(account.isPresent() && currencyOptional.isPresent()) {
      return account.get().setHoldings(new HoldingsEntry(world, currencyOptional.get().getUid(), amount, EconomyManager.NORMAL));
    }
    return false;
  }
}