package net.tnemc.paper.hook.economy;

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

import net.milkbowl.vault2.economy.AccountPermission;
import net.milkbowl.vault2.economy.Economy;
import net.milkbowl.vault2.economy.EconomyResponse;
import net.milkbowl.vault2.economy.EconomyResponse.ResponseType;
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.account.shared.Member;
import net.tnemc.core.account.shared.MemberPermissions;
import net.tnemc.core.actions.source.PluginSource;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.id.UUIDPair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * TNEVaultUnlocked
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TNEVaultUnlocked implements Economy {

  /**
   * Checks if economy plugin is enabled.
   *
   * @return true if the server's economy plugin has properly enabled.
   */
  @Override
  public boolean isEnabled() {

    return true;
  }

  /**
   * Gets name of the economy plugin.
   *
   * @return Name of the active economy plugin on the server.
   */
  @Override
  public String getName() {

    return "TheNewEconomy";
  }

  @Override
  public boolean hasSharedAccountSupport() {

    return false;
  }

  /**
   * Returns true if the economy plugin supports multiple currencies.
   *
   * @return true if the economy plugin supports multiple currencies.
   */
  @Override
  public boolean hasMultiCurrencySupport() {

    return true;
  }

  @Override
  public int fractionalDigits(final String pluginName) {

    return TNECore.eco().currency().getDefaultCurrency().getDecimalPlaces();
  }

  /**
   * Plugins use this method to format a given BigDecimal amount into a human-readable amount using
   * your economy plugin's currency names/conventions.
   *
   * @param amount to format.
   *
   * @return Human-readable string describing amount, ie 5 Dollars or 5.55 Pounds.
   */
  @Override
  public @NotNull String format(final BigDecimal amount) {

    return CurrencyFormatter.format(null, new HoldingsEntry(TNECore.eco().region().defaultRegion(),
                                                            TNECore.eco().currency().getDefaultCurrency().getUid(),
                                                            amount,
                                                            EconomyManager.NORMAL
    ));
  }

  @Override
  public @NotNull String format(final String pluginName, final BigDecimal amount) {

    return CurrencyFormatter.format(null, new HoldingsEntry(TNECore.eco().region().defaultRegion(),
                                                            TNECore.eco().currency().getDefaultCurrency().getUid(),
                                                            amount,
                                                            EconomyManager.NORMAL
    ));
  }

  /**
   * Plugins use this method to format a given BigDecimal amount into a human-readable amount using
   * your economy plugin's currency names/conventions.
   *
   * @param amount   to format.
   * @param currency the currency to use for the format.
   *
   * @return Human-readable string describing amount, ie 5 Dollars or 5.55 Pounds.
   */
  @Override
  public @NotNull String format(final BigDecimal amount, final String currency) {

    return CurrencyFormatter.format(null, new HoldingsEntry(TNECore.eco().region().defaultRegion(),
                                                            TNECore.eco().currency().getDefaultCurrency().getUid(),
                                                            amount,
                                                            EconomyManager.NORMAL
    ));
  }

  @Override
  public @NotNull String format(final String pluginName, final BigDecimal amount, final String currency) {

    return CurrencyFormatter.format(null, new HoldingsEntry(TNECore.eco().region().defaultRegion(),
                                                            TNECore.eco().currency().getDefaultCurrency().getUid(),
                                                            amount,
                                                            EconomyManager.NORMAL
    ));
  }

  /**
   * Returns true if a currency with the specified name exists.
   *
   * @param currency the currency to use.
   *
   * @return true if a currency with the specified name exists.
   */
  @Override
  public boolean hasCurrency(final String currency) {

    return TNECore.eco().currency().findCurrency(currency).isPresent();
  }

  @Override
  public @NotNull String getDefaultCurrency(final String pluginName) {

    return TNECore.eco().currency().getDefaultCurrency().getIdentifier();
  }

  @Override
  public @NotNull String defaultCurrencyNamePlural(final String pluginName) {

    return TNECore.eco().currency().getDefaultCurrency().getDisplayPlural();
  }

  @Override
  public @NotNull String defaultCurrencyNameSingular(final String pluginName) {

    return TNECore.eco().currency().getDefaultCurrency().getDisplay();
  }

  /**
   * Returns a list of currencies used by the economy plugin. These are able to be used in the calls
   * in the methods of the API. May not be human-readable.
   *
   * @return list of currencies used by the economy plugin. These are able to be used in the calls
   * in the methods of the API.
   */
  @Override
  public Collection<String> currencies() {

    return TNECore.eco().currency().getCurIDMap().keySet();
  }

  /**
   * Attempts to create a account for the given UUID.
   *
   * @param uuid UUID associated with the account.
   * @param name UUID associated with the account.
   *
   * @return true if the account creation was successful.
   */
  @Override
  public boolean createAccount(final UUID uuid, final String name) {

    return TNECore.eco().account().createAccount(uuid.toString(), name).getResponse().success();
  }

  /**
   * Attempts to create an account for the given UUID on the specified world IMPLEMENTATION SPECIFIC
   * - if an economy plugin does not support this then false will always be returned.
   *
   * @param uuid      UUID associated with the account.
   * @param name      UUID associated with the account.
   * @param worldName String name of the world.
   *
   * @return if the account creation was successful
   */
  @Override
  public boolean createAccount(final UUID uuid, final String name, final String worldName) {

    return createAccount(uuid, name);
  }

  /**
   * Returns a map that represents all the UUIDs which have accounts in the plugin, as well as their
   * last-known-name. This is used for Vault's economy converter and should be given every account
   * available.
   *
   * @return a {@link Map} composed of the accounts keyed by their UUID, along with their associated
   * last-known-name.
   */
  @Override
  public Map<UUID, String> getUUIDNameMap() {

    return Map.of();
  }

  /**
   * Gets the last known name of an account owned by the given UUID. Required for messages to be
   * more human-readable than UUIDs alone can provide.
   *
   * @param uuid UUID associated with the account.
   *
   * @return name of the account owner.
   */
  @Override
  public Optional<String> getAccountName(final UUID uuid) {

    final Optional<Account> accountOpt = TNECore.eco().account().findAccount(uuid);
    return accountOpt.map(Account::getName);
  }

  /**
   * Checks if this UUID has an account yet.
   *
   * @param uuid UUID to check for an existing account.
   *
   * @return true if the UUID has an account.
   */
  @Override
  public boolean hasAccount(final UUID uuid) {

    return TNECore.eco().account().findAccount(uuid.toString()).isPresent();
  }

  /**
   * Checks if this UUID has an account yet on the given world.
   *
   * @param uuid      UUID to check for an existing account.
   * @param worldName world-specific account.
   *
   * @return if the UUID has an account.
   */
  @Override
  public boolean hasAccount(final UUID uuid, final String worldName) {

    return hasAccount(uuid);
  }

  /**
   * A method which changes the name associated with the given UUID in the Map<UUID, String>
   * received from {@link #getUUIDNameMap()}.
   *
   * @param uuid UUID whose account is having a name change.
   * @param name String name that will be associated with the UUID in the Map<UUID, String> map.
   *
   * @return true if the name change is successful.
   */
  @Override
  public boolean renameAccount(final UUID uuid, final String name) {

    return renameAccount("vault-unlocked", uuid, name);
  }

  @Override
  public boolean renameAccount(final String plugin, final UUID accountID, final String name) {

    return false;
  }

  @Override
  public boolean deleteAccount(final String plugin, final UUID accountID) {

    return TNECore.eco().account().deleteAccount(accountID).success();
  }

  @Override
  public boolean accountSupportsCurrency(final String plugin, final UUID accountID, final String currency) {

    return true;
  }

  @Override
  public boolean accountSupportsCurrency(final String plugin, final UUID accountID, final String currency, final String world) {

    return true;
  }

  /**
   * Gets balance of an account associated with a UUID.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       UUID of the account to get a balance for.
   *
   * @return Amount currently held in account associated with the given UUID.
   */
  @Override
  public BigDecimal getBalance(final String pluginName, final UUID uuid) {

    return getBalance(pluginName, uuid, TNECore.eco().region().defaultRegion(), getDefaultCurrency(pluginName));
  }

  /**
   * Gets balance of a UUID on the specified world. IMPLEMENTATION SPECIFIC - if an economy plugin
   * does not support this the global balance will be returned.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       UUID of the account to get a balance for.
   * @param world      name of the world.
   *
   * @return Amount currently held in account associated with the given UUID.
   */
  @Override
  public BigDecimal getBalance(final String pluginName, final UUID uuid, final String world) {

    return getBalance(pluginName, uuid, world, getDefaultCurrency(pluginName));
  }

  /**
   * Gets balance of a UUID on the specified world. IMPLEMENTATION SPECIFIC - if an economy plugin
   * does not support this the global balance will be returned.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       UUID of the account to get a balance for.
   * @param world      name of the world.
   * @param currency   the currency to use.
   *
   * @return Amount currently held in account associated with the given UUID.
   */
  @Override
  public BigDecimal getBalance(final String pluginName, final UUID uuid, final String world, final String currency) {

    final Optional<Account> account = TNECore.eco().account().findAccount(uuid.toString());
    final Optional<Currency> currencyOpt = TNECore.eco().currency().findCurrency(currency);

    if(account.isPresent() && currencyOpt.isPresent()) {
      PluginCore.log().debug("Vault Balance call. Account exists. Name:" + account.get().getName(), DebugLevel.STANDARD);
      return account.get().getHoldingsTotal(world, currencyOpt.get().getUid());
    }

    PluginCore.log().debug("Vault Balance call. Account doesn't exist. Name:" + uuid, DebugLevel.STANDARD);
    return BigDecimal.ZERO;
  }

  /**
   * Checks if the account associated with the given UUID has the amount - DO NOT USE NEGATIVE
   * AMOUNTS.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       the UUID associated with the account to check the balance of.
   * @param amount     the amount to check for.
   *
   * @return True if <b>UUID</b> has <b>amount</b>, False else wise.
   */
  @Override
  public boolean has(final String pluginName, final UUID uuid, final BigDecimal amount) {

    return has(pluginName, uuid, TNECore.eco().region().defaultRegion(), getDefaultCurrency(pluginName), amount);
  }

  /**
   * Checks if the account associated with the given UUID has the amount in the given world - DO NOT
   * USE NEGATIVE AMOUNTS IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the
   * global balance will be returned.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       the UUID associated with the account to check the balance of.
   * @param worldName  the name of the world to check in.
   * @param amount     the amount to check for.
   *
   * @return True if <b>UUID</b> has <b>amount</b> in the given <b>world</b>, False else wise.
   */
  @Override
  public boolean has(final String pluginName, final UUID uuid, final String worldName, final BigDecimal amount) {

    return has(pluginName, uuid, worldName, getDefaultCurrency(pluginName), amount);
  }

  /**
   * Checks if the account associated with the given UUID has the amount in the given world - DO NOT
   * USE NEGATIVE AMOUNTS IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the
   * global balance will be returned.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       the UUID associated with the account to check the balance of.
   * @param worldName  the name of the world to check in.
   * @param currency   the currency to use.
   * @param amount     the amount to check for.
   *
   * @return True if <b>UUID</b> has <b>amount</b> in the given <b>world</b>, False else wise.
   */
  @Override
  public boolean has(final String pluginName, final UUID uuid, final String worldName, final String currency, final BigDecimal amount) {

    final Optional<Account> account = TNECore.eco().account().findAccount(uuid.toString());
    final Optional<Currency> currencyOpt = TNECore.eco().currency().findCurrency(currency);

    return currencyOpt.filter(currency1->account.filter(value->value.getHoldingsTotal(worldName, currency1.getUid())
                                                                       .compareTo(amount) >= 0).isPresent()).isPresent();

  }

  /**
   * Withdraw an amount from an account associated with a UUID - DO NOT USE NEGATIVE AMOUNTS.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       the UUID associated with the account to withdraw from.
   * @param amount     Amount to withdraw.
   *
   * @return {@link EconomyResponse} which includes the Economy plugin's
   * {@link ResponseType} as to whether the transaction was a Success, Failure,
   * Unsupported.
   */
  @Override
  public EconomyResponse withdraw(final String pluginName, final UUID uuid, final BigDecimal amount) {

    return withdraw(pluginName, uuid, TNECore.eco().region().defaultRegion(), getDefaultCurrency(pluginName), amount);
  }

  /**
   * Withdraw an amount from an account associated with a UUID on a given world - DO NOT USE
   * NEGATIVE AMOUNTS IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the
   * global balance will be returned.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       the UUID associated with the account to withdraw from.
   * @param worldName  the name of the world to check in.
   * @param amount     Amount to withdraw.
   *
   * @return {@link EconomyResponse} which includes the Economy plugin's {@link ResponseType} as to
   * whether the transaction was a Success, Failure, Unsupported.
   */
  @Override
  public EconomyResponse withdraw(final String pluginName, final UUID uuid, final String worldName, final BigDecimal amount) {

    return withdraw(pluginName, uuid, worldName, getDefaultCurrency(pluginName), amount);
  }

  /**
   * Withdraw an amount from an account associated with a UUID on a given world - DO NOT USE
   * NEGATIVE AMOUNTS IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the
   * global balance will be returned.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       the UUID associated with the account to withdraw from.
   * @param worldName  the name of the world to check in.
   * @param currency   the currency to use.
   * @param amount     Amount to withdraw.
   *
   * @return {@link EconomyResponse} which includes the Economy plugin's {@link ResponseType} as to
   * whether the transaction was a Success, Failure, Unsupported.
   */
  @Override
  public EconomyResponse withdraw(final String pluginName, final UUID uuid, final String worldName, final String currency, final BigDecimal amount) {

    final Optional<Account> account = TNECore.eco().account().findAccount(uuid.toString());

    if(account.isEmpty()) {
      return new EconomyResponse(BigDecimal.ZERO, BigDecimal.ZERO,
                                 ResponseType.FAILURE, "Unable to locate associated account.");
    }

    final HoldingsModifier modifier = new HoldingsModifier(worldName,
                                                           TNECore.eco().currency().getDefaultCurrency(worldName).getUid(),
                                                           amount);

    final Transaction transaction = new Transaction("take")
            .to(account.get(), modifier.counter())
            .processor(EconomyManager.baseProcessor())
            .source(new PluginSource(pluginName));

    try {
      final TransactionResult result = transaction.process();
      return new EconomyResponse(amount, transaction.getTo().getCombinedEnding(),
                                 fromResult(result),
                                 result.getMessage());
    } catch(final InvalidTransactionException e) {

      return new EconomyResponse(amount, transaction.getTo().getCombinedEnding(),
                                 ResponseType.FAILURE, e.getMessage());
    }
  }

  /**
   * Deposit an amount to an account associated with the given UUID - DO NOT USE NEGATIVE AMOUNTS.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       the UUID associated with the account to deposit to.
   * @param amount     Amount to deposit.
   *
   * @return {@link EconomyResponse} which includes the Economy plugin's {@link ResponseType} as to
   * whether the transaction was a Success, Failure, Unsupported.
   */
  @Override
  public EconomyResponse deposit(final String pluginName, final UUID uuid, final BigDecimal amount) {

    return deposit(pluginName, uuid, TNECore.eco().region().defaultRegion(), getDefaultCurrency(pluginName), amount);
  }

  /**
   * Deposit an amount to an account associated with a UUID on a given world - DO NOT USE NEGATIVE
   * AMOUNTS IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance
   * will be returned.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       the UUID associated with the account to deposit to.
   * @param worldName  the name of the world to check in.
   * @param amount     Amount to deposit.
   *
   * @return {@link EconomyResponse} which includes the Economy plugin's {@link ResponseType} as to
   * whether the transaction was a Success, Failure, Unsupported.
   */
  @Override
  public EconomyResponse deposit(final String pluginName, final UUID uuid, final String worldName, final BigDecimal amount) {

    return deposit(pluginName, uuid, worldName, getDefaultCurrency(pluginName), amount);
  }

  /**
   * Deposit an amount to an account associated with a UUID on a given world - DO NOT USE NEGATIVE
   * AMOUNTS IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance
   * will be returned.
   *
   * @param pluginName The name of the plugin that is calling the method.
   * @param uuid       the UUID associated with the account to deposit to.
   * @param worldName  the name of the world to check in.
   * @param currency   the currency to use.
   * @param amount     Amount to deposit.
   *
   * @return {@link EconomyResponse} which includes the Economy plugin's {@link ResponseType} as to
   * whether the transaction was a Success, Failure, Unsupported.
   */
  @Override
  public EconomyResponse deposit(final String pluginName, final UUID uuid, final String worldName, final String currency, final BigDecimal amount) {

    final Optional<Account> account = TNECore.eco().account().findAccount(uuid.toString());

    if(account.isEmpty()) {
      return new EconomyResponse(BigDecimal.ZERO, BigDecimal.ZERO, ResponseType.FAILURE, "Unable to locate associated account.");
    }

    final HoldingsModifier modifier = new HoldingsModifier(worldName,
                                                           TNECore.eco().currency().getDefaultCurrency(worldName).getUid(),
                                                           amount);


    final Transaction transaction = new Transaction("give")
            .to(account.get(), modifier)
            .processor(EconomyManager.baseProcessor())
            .source(new PluginSource(pluginName));

    try {
      final TransactionResult result = transaction.process();
      return new EconomyResponse(amount, transaction.getTo().getCombinedEnding(), fromResult(result),
                                 result.getMessage());
    } catch(final InvalidTransactionException e) {

      return new EconomyResponse(amount, transaction.getTo().getCombinedEnding(),
                                 ResponseType.FAILURE, e.getMessage());
    }
  }

  @Override
  public boolean createSharedAccount(final String pluginName, final UUID accountID, final String name, final UUID owner) {

    if(TNECore.eco().account().findAccount(name).isPresent() || TNECore.eco().account().findAccount(accountID.toString()).isPresent()) {

      return false;
    }

    final SharedAccount account = new SharedAccount(accountID, name, owner);

    TNECore.eco().account().getAccounts().put(account.getIdentifier().toString(), account);
    TNECore.eco().account().uuidProvider().store(new UUIDPair(accountID, name));

    return true;
  }

  @Override
  public boolean isAccountOwner(final String pluginName, final UUID accountID, final UUID uuid) {

    final Optional<Account> account = TNECore.eco().account().findAccount(accountID.toString());
    if(account.isEmpty()) {

      return false;
    }

    if(account.get() instanceof final SharedAccount shared) {

      return shared.getOwner().equals(uuid);
    }

    return false;
  }

  @Override
  public boolean setOwner(final String pluginName, final UUID accountID, final UUID uuid) {

    final Optional<Account> account = TNECore.eco().account().findAccount(accountID.toString());
    if(account.isEmpty()) {

      return false;
    }

    if(account.get() instanceof final SharedAccount shared) {

      shared.setOwner(uuid);

      return true;
    }

    return false;
  }

  @Override
  public boolean isAccountMember(final String pluginName, final UUID accountID, final UUID uuid) {

    final Optional<Account> account = TNECore.eco().account().findAccount(accountID.toString());
    if(account.isEmpty()) {

      return false;
    }

    if(account.get() instanceof final SharedAccount shared) {

      return shared.getOwner().equals(uuid) || shared.isMember(uuid);
    }

    return false;
  }

  @Override
  public boolean addAccountMember(final String pluginName, final UUID accountID, final UUID uuid) {

    final Optional<Account> account = TNECore.eco().account().findAccount(accountID.toString());
    if(account.isEmpty()) {

      return false;
    }

    if(account.get() instanceof final SharedAccount shared) {

      final Member member = new Member(uuid);
      shared.getMembers().put(uuid, member);
      return true;
    }

    return false;
  }

  @Override
  public boolean addAccountMember(final String pluginName, final UUID accountID, final UUID uuid, final AccountPermission... initialPermissions) {

    final Optional<Account> account = TNECore.eco().account().findAccount(accountID.toString());
    if(account.isEmpty()) {

      return false;
    }

    if(account.get() instanceof final SharedAccount shared) {

      final Member member = new Member(uuid);

      for(final AccountPermission permission : initialPermissions) {

        final MemberPermissions perm = permissionConversion(permission);
        if(perm != null) {

          member.addPermission(perm, true);
        }

      }

      shared.getMembers().put(uuid, member);
      return true;
    }

    return false;
  }

  @Override
  public boolean removeAccountMember(final String pluginName, final UUID accountID, final UUID uuid) {

    final Optional<Account> account = TNECore.eco().account().findAccount(accountID.toString());
    if(account.isEmpty()) {

      return false;
    }

    if(account.get() instanceof final SharedAccount shared) {

      shared.getMembers().remove(uuid);
      return true;
    }

    return false;
  }

  @Override
  public boolean hasAccountPermission(final String pluginName, final UUID accountID, final UUID uuid, final AccountPermission permission) {

    final Optional<Account> account = TNECore.eco().account().findAccount(accountID.toString());
    final MemberPermissions perm = permissionConversion(permission);
    if(account.isEmpty() || perm == null) {

      return false;
    }

    if(account.get() instanceof final SharedAccount shared) {

      final Optional<Member> member = shared.findMember(uuid);
      if(member.isPresent()) {

        return member.get().hasPermission(perm);
      }
    }

    return false;
  }

  @Override
  public boolean updateAccountPermission(final String pluginName, final UUID accountID, final UUID uuid, final AccountPermission permission, final boolean value) {

    final Optional<Account> account = TNECore.eco().account().findAccount(accountID.toString());
    final MemberPermissions perm = permissionConversion(permission);
    if(account.isEmpty() || perm == null) {

      return false;
    }

    if(account.get() instanceof final SharedAccount shared) {

      final Optional<Member> member = shared.findMember(uuid);
      if(member.isPresent()) {

        member.get().addPermission(perm, value);
        return true;
      }
    }

    return false;
  }

  private MemberPermissions permissionConversion(final AccountPermission permission) {

    return switch(permission) {
      case DEPOSIT -> MemberPermissions.DEPOSIT;
      case WITHDRAW -> MemberPermissions.WITHDRAW;
      case BALANCE -> MemberPermissions.BALANCE;
      case TRANSFER_OWNERSHIP -> MemberPermissions.TRANSFER_OWNERSHIP;
      case INVITE_MEMBER -> MemberPermissions.ADD_MEMBER;
      case REMOVE_MEMBER -> MemberPermissions.REMOVE_MEMBER;
      case CHANGE_MEMBER_PERMISSION -> MemberPermissions.MODIFY_MEMBER;
      case OWNER -> MemberPermissions.OWNERSHIP;
      case DELETE -> MemberPermissions.DELETE_ACCOUNT;
    };
  }

  private ResponseType fromResult(@Nullable final TransactionResult result) {

    if(result != null && result.isSuccessful()) {
      return ResponseType.SUCCESS;
    }
    return ResponseType.FAILURE;
  }
}
