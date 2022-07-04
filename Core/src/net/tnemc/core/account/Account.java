package net.tnemc.core.account;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.holdings.RegionHoldings;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
}