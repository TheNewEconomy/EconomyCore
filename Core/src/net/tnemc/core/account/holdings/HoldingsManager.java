package net.tnemc.core.account.holdings;

import net.tnemc.core.account.Account;
import net.tnemc.core.currency.Currency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * An object that manages the holdings for an account.
 *
 * @see Account
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class HoldingsManager {

  private Map<String, WorldHoldings> worldHoldings = new HashMap<>();

  private UUID uuid;

  public HoldingsManager(UUID uuid) {
    this.uuid = uuid;
  }

  public Map<String, WorldHoldings> getWorldHoldings() {
    return worldHoldings;
  }

  public void setWorldHoldings(Map<String, WorldHoldings> worldBalances) {
    this.worldHoldings = worldBalances;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public BigDecimal getHoldings(String world, Currency currency) {
    if(worldHoldings.containsKey(world)) {
      return worldHoldings.get(world).getHoldings(currency.getIdentifier(), currency.getStartingHoldings());
    }
    return BigDecimal.ZERO;
  }

  public BigDecimal getHoldings(String world, String currency) {
    if(worldHoldings.containsKey(world)) {
      //TODO: Find currency object reference
      return worldHoldings.get(world).getHoldings(currency, BigDecimal.ZERO);
    }
    return BigDecimal.ZERO;
  }

  public void setHoldings(String world, String currency, BigDecimal balance) {
    setHoldings(world, currency, balance, true);
  }

  public void setHoldings(String world, String currency, BigDecimal balance, boolean update) {

    WorldHoldings holdings = worldHoldings.getOrDefault(world, new WorldHoldings(uuid, world));
    holdings.setHoldings(currency, balance, update);
    worldHoldings.put(world, holdings);
  }
}