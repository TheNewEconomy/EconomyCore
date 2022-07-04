package net.tnemc.core.account.holdings;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to keep track of regional holdings for an account.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class RegionHoldings {

  private final Map<String, CurrencyHoldings> holdings = new ConcurrentHashMap<>();

  private final String region;

  public RegionHoldings(String region) {
    this.region = region;
  }

  public void setHoldingsEntry(final HoldingsEntry entry, final HoldingsType type) {
    final CurrencyHoldings currencyHoldings =
        holdings.getOrDefault(entry.getCurrency(), new CurrencyHoldings(entry.getCurrency()));

    currencyHoldings.setHoldingsEntry(entry, type);

    holdings.put(entry.getCurrency(), currencyHoldings);
  }

  public Optional<HoldingsEntry> getHoldingsEntry(final String currency) {
    return getHoldingsEntry(currency, HoldingsType.NORMAL_HOLDINGS);
  }

  public Optional<HoldingsEntry> getHoldingsEntry(final String currency, final HoldingsType type) {
    if(holdings.containsKey(currency)) {
      return holdings.get(currency).getHoldingsEntry(type);
    }
    return Optional.empty();
  }

  public Map<String, CurrencyHoldings> getHoldings() {
    return holdings;
  }
}