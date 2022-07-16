package net.tnemc.core.account;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.TNECore;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.HoldingsType;
import net.tnemc.core.account.holdings.Wallet;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.io.maps.MapKey;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Optional;

/**
 * An object that is used to represent an Account within the economy plugin.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Account {

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

    //TODO: Default Status?
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
   * Used to get the holdings based on specific specifications, or returns an empty optional
   * if no holdings for the specifications exists.
   *
   * @param region The region to use
   * @param currency The currency to use.
   *
   * @return The holdings based on specific specifications, or an empty optional if no
   * holdings for the specifications exists.
   */
  public Optional<HoldingsEntry> getHoldings(final @NotNull String region,
                                             final @NotNull String currency) {
    return getHoldings(region, currency, HoldingsType.NORMAL_HOLDINGS);
  }

  /**
   * Used to get the holdings based on specific specifications, or returns an empty optional
   * if no holdings for the specifications exists.
   *
   * @param region The region to use
   * @param currency The currency to use.
   * @param type The {@link HoldingsType type} to use.
   *
   * @return The holdings based on specific specifications, or an empty optional if no
   * holdings for the specifications exists.
   */
  public Optional<HoldingsEntry> getHoldings(final @NotNull String region,
                                             final @NotNull String currency,
                                             final @NotNull HoldingsType type) {

    final Optional<Currency> currencyObject = TNECore.eco().currency().findCurrency(currency);
    return currencyObject.map(value->new HoldingsEntry(region,
                                                       currency,
                                                       value.type().getHoldings(this, region,
                                                                                value, type)));
  }

  /**
   * Sets the holdings for the specified entry in this wallet.
   * @param entry The entry to set in this wallet.
   */
  public void setHoldings(final @NotNull HoldingsEntry entry) {
    setHoldings(entry, HoldingsType.NORMAL_HOLDINGS);
  }

  /**
   * Sets the holdings for the specified entry and in this wallet.
   * @param entry The entry to set in this wallet.
   * @param type The type to use for this set operation.
   */
  public boolean setHoldings(final @NotNull HoldingsEntry entry, final @NotNull HoldingsType type) {

    final Optional<Currency> currencyObject = TNECore.eco().currency().findCurrency(entry.getCurrency());

    return currencyObject.map(currency->currency.type().setHoldings(this,
                                                                    entry.getRegion(),
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