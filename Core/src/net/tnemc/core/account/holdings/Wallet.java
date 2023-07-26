package net.tnemc.core.account.holdings;

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
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.utils.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a wallet, which holds funds for something
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Wallet {

  private final Map<String, RegionHoldings> holdings = new ConcurrentHashMap<>();

  /**
   * Used to get the holdings based on specific specifications, or returns an empty optional
   * if no holdings for the specifications exists.
   *
   * @param region The region to use
   *
   * @return The holdings based on specific specifications, or an empty optional if no
   * holdings for the specifications exists.
   */
  public Optional<RegionHoldings> getHoldings(final @NotNull String region) {
    return Optional.ofNullable(holdings.get(region));
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
                                             final @NotNull UUID currency) {
    return getHoldings(region, currency, EconomyManager.NORMAL);
  }

  /**
   * Used to get the holdings based on specific specifications, or returns an empty optional
   * if no holdings for the specifications exists.
   *
   * @param region The region to use
   * @param currency The currency to use.
   * @param type The {@link Identifier type} to use.
   *
   * @return The holdings based on specific specifications, or an empty optional if no
   * holdings for the specifications exists.
   */
  public Optional<HoldingsEntry> getHoldings(final @NotNull String region,
                                             final @NotNull UUID currency,
                                             final @NotNull Identifier type) {
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
   * @param type The {@link Identifier type} to use.
   * @param defaultValue The default value to return if nothing exists.
   *
   * @return The holdings based on specific specifications, or the specified default value if no
   * holdings for the specifications exists.
   */
  public HoldingsEntry getHoldings(final @NotNull String region,
                                             final @NotNull UUID currency,
                                             final @NotNull Identifier type,
                                             final @NotNull HoldingsEntry defaultValue) {
    if(holdings.containsKey(region)) {
      return holdings.get(region).getHoldingsEntry(currency, type).orElse(defaultValue);
    }
    return defaultValue;
  }

  /**
   * Sets the holdings for the specified entry and in this wallet.
   * @param entry The entry to set in this wallet.
   */
  public void setHoldings(final @NotNull HoldingsEntry entry) {

    final RegionHoldings regionHoldings =
        holdings.getOrDefault(entry.getRegion(), new RegionHoldings());

    regionHoldings.setHoldingsEntry(entry, entry.getHandler());

    holdings.put(entry.getRegion(), regionHoldings);
  }

  /**
   * Used to modify holdings within this wallet. If the wallet has no holdings for the region,
   * currency and type it will set the holdings to the modifier.
   *
   * @param modifier The modifier to use
   */
  public void modifyHoldings(final @NotNull HoldingsModifier modifier) {
    modifyHoldings(modifier, EconomyManager.NORMAL);
  }

  /**
   * Used to modify holdings within this wallet. If the wallet has no holdings for the region,
   * currency and type it will set the holdings to the modifier.
   *
   * @param modifier The modifier to use
   * @param type The type to use.
   */
  public void modifyHoldings(final @NotNull HoldingsModifier modifier, Identifier type) {

    Optional<HoldingsEntry> entry = getHoldings(modifier.getRegion(), modifier.getCurrency(), type);

    if(entry.isPresent()) {
      entry.get().modify(modifier);
      setHoldings(entry.get());
      return;
    }
    setHoldings(new HoldingsEntry(modifier));
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
  public void deleteHoldings(final @NotNull String region, final @NotNull UUID currency) {
    if(holdings.containsKey(region)) {
      holdings.get(region).getHoldings().remove(currency);
    }
  }

  /**
   * Used to delete specific holdings from this wallet holder.
   * @param region The region from which to delete the holdings
   * @param currency The currency from which to delete the holdings
   * @param type The {@link Identifier type} from which to delete the holdings.
   */
  public void deleteHoldings(final @NotNull String region,
                             final @NotNull UUID currency,
                             final @NotNull Identifier type) {

    if(holdings.containsKey(region) && holdings.get(region).getHoldings().containsKey(currency)) {
      holdings.get(region).getHoldings().get(currency).getHoldings().remove(type.asID());
    }
  }

  //delete all holdings
  public void deleteAllHoldings() {
    holdings.clear();
  }

  /**
   * Used to merge another {@link Wallet wallet} into this one. After it has been merged, the old
   * wallet will have all of its holdings cleared.
   * @param wallet The other wallet to merge.
   */
  public void merge(@NotNull Wallet wallet) {
    for(RegionHoldings region : wallet.getHoldings().values()) {
      for(CurrencyHoldings currency : region.getHoldings().values()) {
        for(Map.Entry<String, HoldingsEntry> entry : currency.getHoldings().entrySet()) {
          modifyHoldings(new HoldingsModifier(entry.getValue()));
        }
      }
    }

    wallet.deleteAllHoldings();
  }

  public List<HoldingsEntry> entryList() {

    final List<HoldingsEntry> holdingsEntries = new ArrayList<>();

    for(RegionHoldings region : holdings.values()) {
      for(CurrencyHoldings currency : region.getHoldings().values()) {
        for(Map.Entry<String, HoldingsEntry> entry : currency.getHoldings().entrySet()) {

          holdingsEntries.add(entry.getValue());
        }
      }
    }
    return holdingsEntries;
  }

  public Map<String, RegionHoldings> getHoldings() {
    return holdings;
  }
}