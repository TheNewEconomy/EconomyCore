package net.tnemc.core.account.holdings;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents something that is able to have holdings information.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class WalletHolder {

  private final Map<String, RegionHoldings> holdings = new ConcurrentHashMap<>();

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
    if(holdings.containsKey(region)) {
      return holdings.get(region).getHoldingsEntry(currency, type);
    }
    return Optional.empty();
  }

  /**
   * Used to get the holdings based on specific specifications, or returns the specified default
   * value if no holdings for the specifications exists.
   *
   * @param region The region to use
   * @param currency The currency to use.
   * @param type The {@link HoldingsType type} to use.
   * @param defaultValue The default value to return if nothing exists.
   *
   * @return The holdings based on specific specifications, or the specified default value if no
   * holdings for the specifications exists.
   */
  public HoldingsEntry getHoldings(final @NotNull String region,
                                             final @NotNull String currency,
                                             final @NotNull HoldingsType type,
                                             final @NotNull HoldingsEntry defaultValue) {

    if(holdings.containsKey(region)) {
      return holdings.get(region).getHoldingsEntry(currency, type).orElse(defaultValue);
    }
    return defaultValue;
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
  public void setHoldings(final @NotNull HoldingsEntry entry, final @NotNull HoldingsType type) {

    final RegionHoldings regionHoldings =
        holdings.getOrDefault(entry.getRegion(), new RegionHoldings(entry.getRegion()));

    regionHoldings.setHoldingsEntry(entry, type);

    holdings.put(entry.getCurrency(),regionHoldings);
  }

  /**
   * Used to modify holdings within this wallet. If the wallet has no holdings for the region,
   * currency and type it will set the holdings to the modifier.
   *
   * @param modifier The modifier to use
   */
  public void modifyHoldings(final @NotNull HoldingsModifier modifier) {
    modifyHoldings(modifier, HoldingsType.NORMAL_HOLDINGS);
  }

  /**
   * Used to modify holdings within this wallet. If the wallet has no holdings for the region,
   * currency and type it will set the holdings to the modifier.
   *
   * @param modifier The modifier to use
   * @param type The type to use.
   */
  public void modifyHoldings(final @NotNull HoldingsModifier modifier,
                             final @NotNull HoldingsType type) {

    Optional<HoldingsEntry> entry = getHoldings(modifier.getRegion(), modifier.getCurrency(), type);

    if(entry.isPresent()) {
      entry.get().modify(modifier);
      setHoldings(entry.get(), type);
      return;
    }
    setHoldings(new HoldingsEntry(modifier), type);
  }

  /**
   * Used to delete specific holdings from this wallet holder.
   * @param region The region from which to delete the holdings
   */
  public void deleteHoldings(final @NotNull String region) {
    holdings.remove(region);
  }

  /**
   * Used to delete specific holdings from this wallet holder.
   * @param region The region from which to delete the holdings
   * @param currency The currency from which to delete the holdings
   */
  public void deleteHoldings(final @NotNull String region, final @NotNull String currency) {
    if(holdings.containsKey(region)) {
      holdings.get(region).getHoldings().remove(currency);
    }
  }

  /**
   * Used to delete specific holdings from this wallet holder.
   * @param region The region from which to delete the holdings
   * @param currency The currency from which to delete the holdings
   * @param type The {@link HoldingsType type} from which to delete the holdings.
   */
  public void deleteHoldings(final @NotNull String region,
                             final @NotNull String currency,
                             final @NotNull HoldingsType type) {

    if(holdings.containsKey(region)) {
      if(holdings.get(region).getHoldings().containsKey(currency)) {
        holdings.get(region).getHoldings().get(currency).getHoldings().remove(type.getIdentifier());
      }
    }
  }

  //delete all holdings
  public void deleteAllHoldings() {
    holdings.clear();
  }

  /**
   * Used to merge another {@link WalletHolder wallet} into this one. After it has been merged, the old
   * wallet will have all of its holdings cleared.
   * @param wallet The other wallet to merge.
   */
  public void merge(@NotNull WalletHolder wallet) {
    for(RegionHoldings region : wallet.getHoldings().values()) {
      for(CurrencyHoldings currency : region.getHoldings().values()) {
        for(Map.Entry<String, HoldingsEntry> entry : currency.getHoldings().entrySet()) {
          modifyHoldings(new HoldingsModifier(entry.getValue()));
        }
      }
    }

    wallet.deleteAllHoldings();
  }

  public Map<String, RegionHoldings> getHoldings() {
    return holdings;
  }
}