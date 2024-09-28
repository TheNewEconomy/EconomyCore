package net.tnemc.bukkit.hook.economy;

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

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.source.PluginSource;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TNEVault
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TNEVault implements Economy {

  /**
   * Checks if economy method is enabled.
   *
   * @return Success or Failure
   */
  public boolean isEnabled() {

    return true;
  }

  /**
   * Gets name of economy method
   *
   * @return Name of Economy Method
   */
  public String getName() {

    return "TheNewEconomy";
  }

  /**
   * Returns true if the given implementation supports banks.
   *
   * @return true if the implementation supports banks
   */
  public boolean hasBankSupport() {

    return false;
  }

  /**
   * Some economy plugins round off after a certain number of digits. This function returns the
   * number of digits the plugin keeps or -1 if no rounding occurs.
   *
   * @return number of digits after the decimal point kept
   */
  public int fractionalDigits() {

    return TNECore.eco().currency().getDefaultCurrency().getDecimalPlaces();
  }

  /**
   * Format amount into a human-readable String This provides translation into economy specific
   * formatting to improve consistency between plugins.
   *
   * @param amount to format
   *
   * @return Human-readable string describing amount
   */
  public String format(double amount) {

    return CurrencyFormatter.format(null, new HoldingsEntry(TNECore.eco().region().defaultRegion(),
                                                            TNECore.eco().currency().getDefaultCurrency().getUid(),
                                                            BigDecimal.valueOf(amount),
                                                            EconomyManager.NORMAL
    ));
  }

  /**
   * Returns the name of the currency in plural form. If the economy being used does not support
   * currency names then an empty string will be returned.
   *
   * @return name of the currency (plural)
   */
  public String currencyNamePlural() {

    return TNECore.eco().currency().getDefaultCurrency().getDisplayPlural();
  }


  /**
   * Returns the name of the currency in singular form. If the economy being used does not support
   * currency names then an empty string will be returned.
   *
   * @return name of the currency (singular)
   */
  public String currencyNameSingular() {

    return TNECore.eco().currency().getDefaultCurrency().getDisplay();
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {@link #hasAccount(OfflinePlayer)} instead.
   */
  @Deprecated
  public boolean hasAccount(String name) {

    return hasAccount(name, "ahoy matey");
  }

  /**
   * Checks if this player has an account on the server yet This will always return true if the
   * player has joined the server at least once as all major economy plugins auto-generate a player
   * account when the player joins the server
   *
   * @param player to check
   *
   * @return if the player has an account
   */
  public boolean hasAccount(OfflinePlayer player) {

    return hasAccount(player, "woah");
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {@link #hasAccount(OfflinePlayer, String)} instead.
   */
  @Deprecated
  public boolean hasAccount(String name, String world) {

    return TNECore.eco().account().findAccount(name).isPresent();
  }

  /**
   * Checks if this player has an account on the server yet on the given world This will always
   * return true if the player has joined the server at least once as all major economy plugins
   * auto-generate a player account when the player joins the server
   *
   * @param player to check in the world
   * @param world  world-specific account
   *
   * @return if the player has an account
   */
  public boolean hasAccount(OfflinePlayer player, String world) {

    return hasAccount(player.getUniqueId().toString(), world);
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {@link #getBalance(OfflinePlayer)} instead.
   */
  @Deprecated
  public double getBalance(String name) {

    return getBalance(name, TNECore.eco().region().defaultRegion());
  }

  /**
   * Gets balance of a player
   *
   * @param player of the player
   *
   * @return Amount currently held in players account
   */
  public double getBalance(OfflinePlayer player) {

    return getBalance(player.getUniqueId().toString(), TNECore.eco().region().defaultRegion());
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {@link #getBalance(OfflinePlayer, String)} instead.
   */
  @Deprecated
  public double getBalance(String name, String world) {

    PluginCore.log().debug("Vault Balance call. Name: " + name + " World: " + world, DebugLevel.STANDARD);

    final Optional<Account> account = TNECore.eco().account().findAccount(name);

    if(name.contains("§")) {
      PluginCore.log().debug("==== Vault balance call with color code! ====", DebugLevel.DEVELOPER);

      final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
      for(int i = 1; i < elements.length; i++) {
        final StackTraceElement s = elements[i];
        PluginCore.log().debug("\tat " + s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + ")", DebugLevel.DEVELOPER);
      }
      PluginCore.log().debug("==== End Stack Print ====", DebugLevel.DEVELOPER);
      return 0;
    }

    if(account.isPresent()) {
      PluginCore.log().debug("Vault Balance call. Account exists. Name:" + account.get().getName(), DebugLevel.STANDARD);
      PluginCore.log().debug("Vault Balance call. Balance:" + account.get().getHoldingsTotal(world, TNECore.eco().currency().getDefaultCurrency(world).getUid()).doubleValue(), DebugLevel.STANDARD);
      return account.get().getHoldingsTotal(world, TNECore.eco().currency().getDefaultCurrency(world).getUid()).doubleValue();
    }

    PluginCore.log().debug("Vault Balance call. Account doesn't exist. Name:" + name, DebugLevel.STANDARD);
    return 0;
  }

  /**
   * Gets balance of a player on the specified world. IMPLEMENTATION SPECIFIC - if an economy plugin
   * does not support this the global balance will be returned.
   *
   * @param player to check
   * @param world  name of the world
   *
   * @return Amount currently held in players account
   */
  public double getBalance(OfflinePlayer player, String world) {

    final Optional<Account> account = TNECore.eco().account().findAccount(player.getUniqueId().toString());
    if(player.getName() != null && account.isEmpty()) {
      return getBalance(player.getName(), world);
    }
    return getBalance(player.getUniqueId().toString(), world);
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {@link #has(OfflinePlayer, double)} instead.
   */
  @Deprecated
  public boolean has(String name, double amount) {

    return has(name, TNECore.eco().region().defaultRegion(), amount);
  }

  /**
   * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
   *
   * @param player to check
   * @param amount to check for
   *
   * @return True if <b>player</b> has <b>amount</b>, False else wise
   */
  public boolean has(OfflinePlayer player, double amount) {

    final Optional<Account> account = TNECore.eco().account().findAccount(player.getUniqueId().toString());
    if(player.getName() != null && account.isEmpty()) {
      return has(player.getName(), TNECore.eco().region().defaultRegion(), amount);
    }
    return has(player.getUniqueId().toString(), TNECore.eco().region().defaultRegion(), amount);
  }

  /**
   * @deprecated As of VaultAPI 1.4 use @{link {@link #has(OfflinePlayer, String, double)} instead.
   */
  @Deprecated
  public boolean has(String name, String world, double amount) {

    final Optional<Account> account = TNECore.eco().account().findAccount(name);
    return account.filter(value->value.getHoldingsTotal(world, TNECore.eco().currency().getDefaultCurrency(world).getUid())
                                         .compareTo(BigDecimal.valueOf(amount)) >= 0).isPresent();
  }

  /**
   * Checks if the player account has the amount in a given world - DO NOT USE NEGATIVE AMOUNTS
   * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be
   * returned.
   *
   * @param player to check
   * @param world  to check with
   * @param amount to check for
   *
   * @return True if <b>player</b> has <b>amount</b>, False else wise
   */
  public boolean has(OfflinePlayer player, String world, double amount) {

    final Optional<Account> account = TNECore.eco().account().findAccount(player.getUniqueId().toString());
    if(player.getName() != null && account.isEmpty()) {
      return has(player.getName(), world, amount);
    }
    return has(player.getUniqueId().toString(), world, amount);
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {@link #withdrawPlayer(OfflinePlayer, double)} instead.
   */
  @Deprecated
  public EconomyResponse withdrawPlayer(String name, double amount) {

    return withdrawPlayer(name, TNECore.eco().region().defaultRegion(), amount);
  }

  /**
   * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS
   *
   * @param player to withdraw from
   * @param amount Amount to withdraw
   *
   * @return Detailed response of transaction
   */
  public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {

    final EconomyResponse response = withdrawPlayer(player.getUniqueId().toString(), TNECore.eco().region().defaultRegion(), amount);

    PluginCore.log().debug("Player ID: " + player.getUniqueId());
    PluginCore.log().debug("Player Name: " + player.getName());
    PluginCore.log().debug("Response" + response.errorMessage);

    if(response.transactionSuccess() || player.getName() == null) {
      return response;
    }
    return withdrawPlayer(player.getName(), TNECore.eco().region().defaultRegion(), amount);
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {@link #withdrawPlayer(OfflinePlayer, String, double)}
   * instead.
   */
  @Deprecated
  public EconomyResponse withdrawPlayer(String name, String world, double amount) {

    final Optional<Account> account = TNECore.eco().account().findAccount(name);

    if(account.isEmpty()) {
      return new EconomyResponse(0.0, 0.0,
                                 EconomyResponse.ResponseType.FAILURE, "Unable to locate associated account.");
    }

    final HoldingsModifier modifier = new HoldingsModifier(world,
                                                           TNECore.eco().currency().getDefaultCurrency(world).getUid(),
                                                           BigDecimal.valueOf(amount));

    final Transaction transaction = new Transaction("take")
            .to(account.get(), modifier.counter())
            .processor(EconomyManager.baseProcessor())
            .source(new PluginSource("Vault"));

    try {
      TransactionResult result = transaction.process();
      return new EconomyResponse(amount, transaction.getTo().getCombinedEnding().doubleValue(),
                                 fromResult(result),
                                 result.getMessage());
    } catch(InvalidTransactionException e) {

      return new EconomyResponse(amount, transaction.getTo().getCombinedEnding().doubleValue(),
                                 EconomyResponse.ResponseType.FAILURE, e.getMessage());
    }
  }

  /**
   * Withdraw an amount from a player on a given world - DO NOT USE NEGATIVE AMOUNTS IMPLEMENTATION
   * SPECIFIC - if an economy plugin does not support this the global balance will be returned.
   *
   * @param player to withdraw from
   * @param world  - name of the world
   * @param amount Amount to withdraw
   *
   * @return Detailed response of transaction
   */
  public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {

    final EconomyResponse response = withdrawPlayer(player.getUniqueId().toString(), world, amount);
    if(response.transactionSuccess() || player.getName() == null) {
      return response;
    }
    return withdrawPlayer(player.getName(), world, amount);
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {@link #depositPlayer(OfflinePlayer, double)} instead.
   */
  @Deprecated
  public EconomyResponse depositPlayer(String name, double amount) {

    return depositPlayer(name, TNECore.eco().region().defaultRegion(), amount);
  }

  /**
   * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
   *
   * @param player to deposit to
   * @param amount Amount to deposit
   *
   * @return Detailed response of transaction
   */
  public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {

    final EconomyResponse response = depositPlayer(player.getUniqueId().toString(), TNECore.eco().region().defaultRegion(), amount);
    if(response.transactionSuccess() || player.getName() == null) {
      return response;
    }
    return depositPlayer(player.getName(), TNECore.eco().region().defaultRegion(), amount);
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {@link #depositPlayer(OfflinePlayer, String, double)}
   * instead.
   */
  @Deprecated
  public EconomyResponse depositPlayer(String name, String world, double amount) {

    final Optional<Account> account = TNECore.eco().account().findAccount(name);

    if(account.isEmpty()) {
      return new EconomyResponse(0.0, 0.0,
                                 EconomyResponse.ResponseType.FAILURE, "Unable to locate associated account.");
    }

    final HoldingsModifier modifier = new HoldingsModifier(world,
                                                           TNECore.eco().currency().getDefaultCurrency(world).getUid(),
                                                           BigDecimal.valueOf(amount));


    final Transaction transaction = new Transaction("give")
            .to(account.get(), modifier)
            .processor(EconomyManager.baseProcessor())
            .source(new PluginSource("Vault"));

    try {
      TransactionResult result = transaction.process();
      return new EconomyResponse(amount, transaction.getTo().getCombinedEnding().doubleValue(),
                                 fromResult(result),
                                 result.getMessage());
    } catch(InvalidTransactionException e) {

      return new EconomyResponse(amount, transaction.getTo().getCombinedEnding().doubleValue(),
                                 EconomyResponse.ResponseType.FAILURE, e.getMessage());
    }
  }

  /**
   * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS IMPLEMENTATION SPECIFIC - if an
   * economy plugin does not support this the global balance will be returned.
   *
   * @param player to deposit to
   * @param world  name of the world
   * @param amount Amount to deposit
   *
   * @return Detailed response of transaction
   */
  public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {

    final EconomyResponse response = depositPlayer(player.getUniqueId().toString(), world, amount);
    if(response.transactionSuccess() || player.getName() == null) {
      return response;
    }
    return depositPlayer(player.getName(), world, amount);
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {{@link #createBank(String, OfflinePlayer)} instead.
   */
  @Deprecated
  public EconomyResponse createBank(String name, String player) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * Creates a bank account with the specified name and the player as the owner
   *
   * @param name   of account
   * @param player the account should be linked to
   *
   * @return EconomyResponse Object
   */
  public EconomyResponse createBank(String name, OfflinePlayer player) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * Deletes a bank account with the specified name.
   *
   * @param name of the back to delete
   *
   * @return if the operation completed successfully
   */
  public EconomyResponse deleteBank(String name) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * Returns the amount the bank has
   *
   * @param name of the account
   *
   * @return EconomyResponse Object
   */
  public EconomyResponse bankBalance(String name) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * Returns true or false whether the bank has the amount specified - DO NOT USE NEGATIVE AMOUNTS
   *
   * @param name   of the account
   * @param amount to check for
   *
   * @return EconomyResponse Object
   */
  public EconomyResponse bankHas(String name, double amount) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * Withdraw an amount from a bank account - DO NOT USE NEGATIVE AMOUNTS
   *
   * @param name   of the account
   * @param amount to withdraw
   *
   * @return EconomyResponse Object
   */
  public EconomyResponse bankWithdraw(String name, double amount) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * Deposit an amount into a bank account - DO NOT USE NEGATIVE AMOUNTS
   *
   * @param name   of the account
   * @param amount to deposit
   *
   * @return EconomyResponse Object
   */
  public EconomyResponse bankDeposit(String name, double amount) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {{@link #isBankOwner(String, OfflinePlayer)} instead.
   */
  @Deprecated
  public EconomyResponse isBankOwner(String name, String playerName) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * Check if a player is the owner of a bank account
   *
   * @param name   of the account
   * @param player to check for ownership
   *
   * @return EconomyResponse Object
   */
  public EconomyResponse isBankOwner(String name, OfflinePlayer player) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {{@link #isBankMember(String, OfflinePlayer)} instead.
   */
  @Deprecated
  public EconomyResponse isBankMember(String name, String playerName) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * Check if the player is a member of the bank account
   *
   * @param name   of the account
   * @param player to check membership
   *
   * @return EconomyResponse Object
   */
  public EconomyResponse isBankMember(String name, OfflinePlayer player) {

    return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "");
  }

  /**
   * Gets the list of banks
   *
   * @return the List of Banks
   */
  public List<String> getBanks() {

    return new ArrayList<>();
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {{@link #createPlayerAccount(OfflinePlayer)} instead.
   */
  @Deprecated
  public boolean createPlayerAccount(String name) {

    return createPlayerAccount(name, "this doesn't matter.");
  }

  /**
   * Attempts to create a player account for the given player
   *
   * @param player OfflinePlayer
   *
   * @return if the account creation was successful
   */
  public boolean createPlayerAccount(OfflinePlayer player) {

    return createPlayerAccount(player, "this doesn't matter.");
  }

  /**
   * @deprecated As of VaultAPI 1.4 use {{@link #createPlayerAccount(OfflinePlayer, String)}
   * instead.
   */
  @Deprecated
  public boolean createPlayerAccount(String name, String world) {

    PluginCore.log().debug("Vault Method: Create Player Account!", DebugLevel.STANDARD);
    return TNECore.eco().account().createAccount(name, name).getResponse().success();
  }

  /**
   * Attempts to create a player account for the given player on the specified world IMPLEMENTATION
   * SPECIFIC - if an economy plugin does not support this then false will always be returned.
   *
   * @param player OfflinePlayer
   * @param world  String name of the world
   *
   * @return if the account creation was successful
   */
  public boolean createPlayerAccount(OfflinePlayer player, String world) {

    PluginCore.log().debug("Vault Method: Create Player Account!", DebugLevel.STANDARD);
    if(player.getName() == null) {

      PluginCore.log().error("Error from plugin accessing vault createPlayerAccount! Name provided is null!(probably EssentialsX)");
      return false;
    }
    PluginCore.log().debug("Vault Method: Name: " + player.getName() + " ID: " + player.getUniqueId(), DebugLevel.STANDARD);

    if(player.getName().contains("§")) {
      PluginCore.log().debug("==== Vault create call with color code! ====", DebugLevel.DEVELOPER);

      final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
      for(int i = 1; i < elements.length; i++) {
        final StackTraceElement s = elements[i];
        PluginCore.log().debug("\tat " + s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + ")", DebugLevel.DEVELOPER);
      }
      PluginCore.log().debug("==== End Stack Print ====", DebugLevel.DEVELOPER);
      return false;
    }

    return TNECore.eco().account().createAccount(player.getUniqueId().toString(), player.getName()).getResponse().success();
  }

  private EconomyResponse.ResponseType fromResult(@Nullable TransactionResult result) {

    if(result != null && result.isSuccessful()) {
      return EconomyResponse.ResponseType.SUCCESS;
    }
    return EconomyResponse.ResponseType.FAILURE;
  }
}