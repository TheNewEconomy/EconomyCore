package net.tnemc.core.account.holdings;

import net.tnemc.core.io.Datable;
import net.tnemc.core.io.Queryable;

import java.math.BigDecimal;
import java.util.UUID;

public class CurrencyHoldings implements Datable<String, CurrencyHoldings> {

  private UUID identifier;
  private String world;
  private String currency;
  private BigDecimal holdings;

  public CurrencyHoldings(UUID identifier, String world, String currency, BigDecimal holdings) {
    this.identifier = identifier;
    this.world = world;
    this.currency = currency;
    this.holdings = holdings;
  }

  /**
   * Used to get the {@link Queryable data part} of this object. This is what houses all the
   * IO logic.
   *
   * @return The {@link Queryable data part} of this object. This is what houses all the
   * IO logic.
   */
  @Override
  public Queryable<String, CurrencyHoldings> getData() {
    //TODO: implement this
    return null;
  }

  /**
   * Sets the {@link Queryable data part} of this object. This is what houses all the
   * IO logic.
   *
   * @param dataObject The data object to set to.
   */
  @Override
  public void setData(Queryable<String, CurrencyHoldings> dataObject) {
    //TODO: implement this

  }

  public UUID getIdentifier() {
    return identifier;
  }

  public void setIdentifier(UUID identifier) {
    this.identifier = identifier;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public BigDecimal getHoldings() {
    return holdings;
  }

  public void setHoldings(BigDecimal holdings) {
    this.holdings = holdings;
  }
}