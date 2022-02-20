package net.tnemc.core.account.holdings;

import net.tnemc.core.currency.Currency;
import net.tnemc.core.io.cache.RefreshConcurrentMap;
import net.tnemc.core.io.cache.listeners.HoldingsMapListener;

import java.math.BigDecimal;
import java.util.UUID;

public class WorldHoldings {

  private final RefreshConcurrentMap<String, CurrencyHoldings> holdings;

  private UUID identifier;
  private String world;

  public WorldHoldings(UUID identifier, String world) {
    this. world = world;
    this.identifier = identifier;

    holdings = new RefreshConcurrentMap<>(true, 300, new HoldingsMapListener());
  }

  public RefreshConcurrentMap<String, CurrencyHoldings> getHoldings() {
    return holdings;
  }

  public BigDecimal getHoldings(Currency currency) {
    return getHoldings(currency.getIdentifier(), currency.getStartingHoldings());
  }

  public BigDecimal getHoldings(String currency, BigDecimal defaultAmount) {

    if(holdings.containsKey(currency)) {
      return (holdings.get(currency)).getHoldings();
    }
    return defaultAmount;
  }

  public BigDecimal getHoldings(String currency, BigDecimal defaultAmount, boolean database) {
    //TODO: use database
    if(holdings.containsKey(currency)) {
      return (holdings.get(currency)).getHoldings();
    }
    return defaultAmount;
  }

  public void setHoldings(Currency currency, BigDecimal amount, boolean database) {
    setHoldings(currency.getIdentifier(), amount, database);
  }

  public void setHoldings(String currency, BigDecimal amount, boolean database) {
    CurrencyHoldings currencyHoldings;
    if(holdings.containsKey(currency)) {

      currencyHoldings = (holdings.get(currency));
      currencyHoldings.setHoldings(amount);
    } else {
      currencyHoldings = new CurrencyHoldings(identifier, world, currency, amount);
    }
    holdings.put(currency, currencyHoldings);
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
}