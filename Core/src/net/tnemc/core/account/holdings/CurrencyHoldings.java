package net.tnemc.core.account.holdings;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to keep track of regional holdings for an account.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyHoldings {

  //TODO: Storage
  private final Map<String, HoldingsEntry> holdings = new ConcurrentHashMap<>();

  private final String currency;

  public CurrencyHoldings(String currency) {
    this.currency = currency;
  }

  /**
   * Used to add {@link HoldingsEntry holdings} for a specific {@link HoldingsType}.
   * @param type The type to add the holdings to.
   * @param entry The holdings to add to the type.
   */
  public void setHoldingsEntry(final @NotNull HoldingsEntry entry, final @NotNull HoldingsType type) {
    holdings.put(type.getIdentifier(), entry);
  }

  public Optional<HoldingsEntry> getHoldingsEntry(final @NotNull HoldingsType type) {
    return Optional.ofNullable(holdings.get(type.getIdentifier()));
  }

  public Map<String, HoldingsEntry> getHoldings() {
    return holdings;
  }
}