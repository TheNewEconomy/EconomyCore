package net.tnemc.core.account;

/*
 * The New Economy
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
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
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.Wallet;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.io.maps.MapKey;
import net.tnemc.core.transaction.receipt.ReceiptBox;
import net.tnemc.core.utils.Identifier;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * An object that is used to represent an Account within the economy plugin.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 * @see ReceiptBox
 */
public class Account extends ReceiptBox {

  protected String identifier;
  protected String name;
  protected long creationDate;
  protected String pin;

  protected Wallet wallet;

  protected AccountStatus status;

  public Account(String identifier, String name) {
    this.identifier = identifier;
    this.name = name;

    //Defaults
    this.creationDate = new Date().getTime();
    this.pin = "";

    this.status = TNECore.eco().account().findStatus("normal");

    //Set our default wallet
    this.wallet = new Wallet();
  }

  /**
   * Used to get the type of account that this is. This is for data-purposes only.
   * @return The account type.
   */
  public String type() {
    return "account";
  }

  /**
   * Used to determine if this {@link Account account} represents a Player.
   *
   * @return True if this {@link Account account} represents a Player, otherwise false.
   */
  public boolean isPlayer() {
    return (this instanceof PlayerAccount);
  }

  /**
   * Used to determine if this {@link Account account} supports balances based on inventories.
   * This is usually item-based currencies, or experience-based currencies. This should only return
   * true if the account is a {@link PlayerAccount player}.
   *
   * @return True if this {@link Account account} supports balances based on inventories.
   */
  public boolean supportsInventoryBalances() {
    return isPlayer();
  }

  /**
   * Used to get the BigDecimal value of this account's holding based on the specifications.
   *
   * @param region The region to use
   * @param currency The currency to use.
   *
   * @return The total of every {@link HoldingsEntry} for the specifications.
   */
  public BigDecimal getHoldingsTotal(final @NotNull String region,
                                     final @NotNull UUID currency) {

    BigDecimal amount = BigDecimal.ZERO;

    for(HoldingsEntry entry : getHoldings(region, currency)) {
      amount = amount.add(entry.getAmount());
    }
    return amount;
  }

  /**
   * Used to get the holdings based on specific specifications, or returns an empty optional
   * if no holdings for the specifications exists.
   *
   * @param region The region to use
   * @param currency The currency to use.
   *
   * @return The holdings based on specific specifications, or an empty optional if no
   * holdings for the specifications exists.
   */
  public List<HoldingsEntry> getHoldings(final @NotNull String region,
                                             final @NotNull UUID currency) {
    return getHoldings(region, currency, EconomyManager.NORMAL);
  }

  /**
   * Used to get the holdings based on specific specifications, or returns an empty optional
   * if no holdings for the specifications exists.
   *
   * @param region The region to use
   * @param currency The currency to use.
   * @param type The {@link Identifier identifier} of the holdings handler to use.
   *
   * @return The holdings based on specific specifications, or an empty optional if no
   * holdings for the specifications exists.
   */
  public List<HoldingsEntry> getHoldings(final @NotNull String region,
                                             final @NotNull UUID currency,
                                             final @NotNull Identifier type) {

    final Optional<Currency> currencyObject = TNECore.eco().currency().findCurrency(currency);

    if(currencyObject.isPresent()) {
      final String resolve = TNECore.eco().region().resolve(region);
      return currencyObject.get().type().getHoldings(this, resolve, currencyObject.get(), type);
    }
    return new ArrayList<>();
  }

  /**
   * Used to get the holdings based on specific specifications, or returns an empty optional
   * if no holdings for the specifications exists.
   *
   * @param region The region to use
   * @param type The {@link Identifier identifier} of the holdings handler to use.
   *
   * @return The holdings based on specific specifications, or an empty optional if no
   * holdings for the specifications exists.
   */
  public List<HoldingsEntry> getAllHoldings(final @NotNull String region,
                                            final @NotNull Identifier type) {

    final List<HoldingsEntry> holdings = new ArrayList<>();
    final String resolve = TNECore.eco().region().resolve(region);

    TNECore.eco().currency().currencies().forEach((currency)->{
      BigDecimal amount = BigDecimal.ZERO;
      for(HoldingsEntry entry : currency.type().getHoldings(this, resolve, currency, type)) {
        amount = amount.add(entry.getAmount());
      }
      holdings.add(new HoldingsEntry(resolve, currency.getUid(), amount, type));
    });
    return holdings;
  }

  /**
   * Sets the holdings for the specified entry in this wallet.
   * @param entry The entry to set in this wallet.
   */
  public boolean setHoldings(final @NotNull HoldingsEntry entry) {
    return setHoldings(entry, EconomyManager.NORMAL);
  }

  /**
   * Sets the holdings for the specified entry and in this wallet.
   * @param entry The entry to set in this wallet.
   * @param type The {@link Identifier identifier} of the holdings handler to use.
   */
  public boolean setHoldings(final @NotNull HoldingsEntry entry, final @NotNull Identifier type) {

    final Optional<Currency> currencyObject = TNECore.eco().currency().findCurrency(entry.getCurrency());

    final String region = TNECore.eco().region().resolve(entry.getRegion());

    return currencyObject.map(currency->currency.type().setHoldings(this,
                                                                    region,
                                                                    currency,
                                                                    type,
                                                                    entry.getAmount()
    )).orElse(false);
  }

  @MapKey
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(long creationDate) {
    this.creationDate = creationDate;
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public AccountStatus getStatus() {
    return status;
  }

  public void setStatus(AccountStatus status) {
    this.status = status;
  }

  public Wallet getWallet() {
    return wallet;
  }

  public void setWallet(Wallet wallet) {
    this.wallet = wallet;
  }
}