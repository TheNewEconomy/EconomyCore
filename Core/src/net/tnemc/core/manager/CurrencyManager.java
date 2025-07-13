package net.tnemc.core.manager;

/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyLoader;
import net.tnemc.core.currency.CurrencySaver;
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.currency.loader.DefaultCurrencyLoader;
import net.tnemc.core.currency.parser.MoneyParser;
import net.tnemc.core.currency.parser.ParseRule;
import net.tnemc.core.currency.saver.DefaultCurrencySaver;
import net.tnemc.core.currency.type.ExperienceLevelType;
import net.tnemc.core.currency.type.ExperienceType;
import net.tnemc.core.currency.type.ItemType;
import net.tnemc.core.currency.type.MixedType;
import net.tnemc.core.currency.type.VirtualType;
import net.tnemc.core.utils.MISCUtils;
import net.tnemc.core.utils.exceptions.NoValidCurrenciesException;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.utils.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * CurrencyManager
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyManager {

  public static final BigDecimal largestSupported = new BigDecimal("900000000000000000000000000000000000000000000");

  private final Map<UUID, Currency> currencies = new HashMap<>();
  private final Map<String, UUID> curIDMap = new HashMap<>();
  private final Map<String, CurrencyType> types = new HashMap<>();

  private CurrencyLoader loader = new DefaultCurrencyLoader();
  private CurrencySaver saver = new DefaultCurrencySaver();

  private MoneyParser parser = new MoneyParser();

  private boolean retry = false;

  public CurrencyManager() {

    addType(new ExperienceType());
    addType(new ExperienceLevelType());
    addType(new ItemType());
    addType(new MixedType());
    addType(new VirtualType());
  }

  public boolean load(final File parent) {

    return load(parent, true);
  }

  public boolean load(final File parent, final boolean reset) {

    saver.saveCurrenciesUUID(new File(parent, "currency"));
    if(reset) {
      currencies.clear();
      curIDMap.clear();
    }

    try {
      loader.loadCurrencies(new File(parent, "currency"));
    } catch(final NoValidCurrenciesException ignore) {
      if(retry) {
        PluginCore.log().error("No valid currencies found and failed to create USD defaults! Disabling plugin. Configure your currencies properly then retry!", DebugLevel.OFF);
        return false;
      }

      PluginCore.log().error("No valid currencies found, attempting to create and load USD defaults.", DebugLevel.OFF);

      //Save our default currency file
      final File cur = new File(PluginCore.directory(), "currency");

      final int count = (cur.exists())? IOUtil.getYAMLs(cur).length : 0;
      if(!cur.exists()) {
        final boolean created = cur.mkdir();
        if(!created) {
          PluginCore.log().error("Failed to create plugin currency directory. Disabling plugin.", DebugLevel.OFF);
          return false;
        }
        if(count == 0) {
          PluginCore.server().saveResource("currency/USD.yml", false);
        }
      }

      final File usd = new File(cur, "USD");
      if(count == 0 && !usd.exists()) {
        final boolean created = usd.mkdir();
        if(!created) {
          PluginCore.log().error("Failed to create plugin USD currency directory. Disabling plugin.", DebugLevel.OFF);
          return false;
        }
        PluginCore.server().saveResource("currency/USD/one.yml", false);
        PluginCore.server().saveResource("currency/USD/penny.yml", false);
      }
      this.retry = true;
      return load(parent, reset);
    }
    return true;
  }

  /**
   * Saves all currency UUIDs only.
   *
   * @param parent The directory used for saving.
   */
  public void saveCurrenciesUUID(final File parent) {

    saver.saveCurrenciesUUID(new File(parent, "currency"));
  }

  public CurrencyLoader getLoader() {

    return loader;
  }

  public void setLoader(final CurrencyLoader loader) {

    this.loader = loader;
  }

  public CurrencySaver getSaver() {

    return saver;
  }

  public void setSaver(final CurrencySaver saver) {

    this.saver = saver;
  }

  /**
   * Used to add a currency.
   *
   * @param currency The currency to add.
   */
  public void addCurrency(final Currency currency) {

    currencies.put(currency.getUid(), currency);
    curIDMap.put(currency.getIdentifier(), currency.getUid());
  }

  /**
   * Used to get the default currency. This could be the default currency for the server globally or
   * for the default world if the implementation supports multi-world.
   *
   * @return The currency that is the default for the server if multi-world support is not available
   * otherwise the default for the default world.
   *
   * @since 0.1.2.0
   */
  @NotNull
  public Currency defaultCurrency() {

    for(final Currency currency : currencies.values()) {
      if(currency.isGlobalDefault()) {
        return currency;
      }
    }
    return currencies.values().iterator().next();
  }

  /**
   * Used to get the default currency for the specified world if this implementation has multi-world
   * support, otherwise the default currency for the server.
   *
   * @param region The region to get the default currency for. This could be a world, biomes, or a
   *               third party based region.
   *
   * @return The default currency for the specified world if this implementation has multi-world
   * support, otherwise the default currency for the server.
   *
   * @since 0.1.2.0
   */
  @NotNull
  public Currency defaultCurrency(@NotNull final String region) {

    for(final Currency currency : currencies.values()) {
      if(currency.isRegionDefault(region)) {
        return currency;
      }
    }
    return defaultCurrency();
  }

  /**
   * Used to get a set of every {@link Currency} object that is available in the specified world if
   * this implementation has multi-world support, otherwise all {@link Currency} objects for the
   * server.
   *
   * @param region The region to get the currencies for. This could be a world, biomes, or a third
   *               party based region.
   *
   * @return A set of every {@link Currency} object that is available in the specified world if this
   * implementation has multi-world support, otherwise all {@link Currency} objects for the server.
   *
   * @since 0.1.2.0
   */
  public Collection<Currency> getCurrencies(@NotNull final String region) {

    final Collection<Currency> regionCurrencies = new ArrayList<>();

    for(final Currency currency : currencies.values()) {
      if(currency.regionEnabled(region)) {
        regionCurrencies.add(currency);
      }
    }
    return regionCurrencies;
  }

  /**
   * Used to find a {@link Currency currency} based on its unique identifier.
   *
   * @param identifier The identifier to look for.
   *
   * @return An Optional containing the currency if it exists, otherwise an empty Optional.
   */
  public Optional<Currency> find(final UUID identifier) {

    return Optional.ofNullable(currencies.get(identifier));
  }

  /**
   * Used to find a {@link Currency currency} based on an item.
   *
   * @param item The item to use for this search.
   *
   * @return An Optional containing the currency if this item is a valid currency item, otherwise an
   * empty Optional.
   */
  public Optional<Currency> findByItem(final AbstractItemStack<?> item) {

    for(final Currency currency : currencies.values()) {

      if(currency instanceof ItemCurrency) {

        final Optional<ItemDenomination> denom = ((ItemCurrency)currency).getDenominationByMaterial(item.material());
        if(denom.isPresent()) {
          return Optional.of(currency);
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Used to find a {@link Currency currency} based on a material.
   *
   * @param material The material to use for this search.
   *
   * @return An Optional containing the currency if this item is a valid currency item, otherwise an
   * empty Optional.
   */
  public Optional<Currency> findCurrencyByMaterial(final String material) {

    for(final Currency currency : currencies.values()) {

      if(currency instanceof ItemCurrency) {

        final Optional<ItemDenomination> denom = ((ItemCurrency)currency).getDenominationByMaterial(material);
        if(denom.isPresent()) {
          return Optional.of(currency);
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Finds a Currency object based on the symbol provided.
   *
   * @param symbol The symbol of the currency to search for.
   *
   * @return An Optional containing the Currency object if found, otherwise an empty Optional.
   */
  public Optional<Currency> findCurrencyBySymbol(@Nullable final String symbol) {

    if(symbol == null) {

      return Optional.empty();
    }

    for(final Currency currency : currencies.values()) {

      if(currency.getSymbol().equalsIgnoreCase(symbol)) {

        return Optional.of(currency);
      }
    }
    return Optional.empty();
  }

  /**
   * Used to find a {@link Currency currency} based on its user-friendly identifier.
   *
   * @param identifier The identifier to look for.
   *
   * @return An Optional containing the currency if it exists, otherwise an empty Optional.
   */
  public Optional<Currency> find(final String identifier) {

    try {

      return Optional.ofNullable(currencies.get(UUID.fromString(identifier)));
    } catch(final Exception ignore) {

      for(final Map.Entry<String, UUID> entry : curIDMap.entrySet()) {

        if(entry.getKey().equalsIgnoreCase(identifier)) {

          return Optional.ofNullable(currencies.get(entry.getValue()));
        }
      }
      return Optional.empty();
    }
  }

  /**
   * Used to find a {@link Currency currency} based on its {@link UUID identifier}, or a new
   * Currency object if the specified identifier doesn't exist.
   *
   * @param identifier The identifier to look for.
   *
   * @return The currency object if found; Otherwise a new currency object.
   */
  public Currency findOrDefault(final UUID identifier) {

    return findOrDefault(identifier, false);
  }

  /**
   * Used to find a {@link Currency currency} based on its {@link UUID identifier}, or a new
   * Currency object if the specified identifier doesn't exist.
   *
   * @param identifier The identifier to look for.
   * @param item       True if this should return an {@link ItemCurrency} object if the specified
   *                   identifier doesn't exist.
   *
   * @return The currency object if found; Otherwise a new currency object.
   */
  public Currency findOrDefault(final UUID identifier, final boolean item) {

    if(identifier != null && currencies.containsKey(identifier)) {
      return currencies.get(identifier);
    }

    final String curID = MISCUtils.randomString(3);
    if(item) return new ItemCurrency(curID);
    return new Currency(curID);
  }

  public LinkedList<Currency> currencies() {

    return new LinkedList<>(currencies.values());
  }

  public Map<String, CurrencyType> getTypes() {

    return types;
  }

  /**
   * Used to add a currency type.
   *
   * @param type The currency type to add.
   */
  public void addType(final CurrencyType type) {

    types.put(type.name(), type);
  }

  /**
   * Used to find a currency type based on its identifier.
   *
   * @param identifier The identifier to look for.
   *
   * @return An Optional containing the currency type if it exists, otherwise an empty Optional.
   */
  public Optional<CurrencyType> findType(final String identifier) {

    return Optional.ofNullable(types.get(identifier));
  }

  public CurrencyType findTypeOrDefault(final String identifier) {

    return types.getOrDefault(identifier, types.get("virtual"));
  }

  public MoneyParser parser() {

    return parser;
  }

  /**
   * Adds a ParseRule to the parser for applying rules during parsing.
   *
   * @param rule The ParseRule object to add for parsing.
   */
  public void addParseRule(final ParseRule rule) {

    parser.addRule(rule);
  }

  public Map<String, UUID> getCurIDMap() {

    return curIDMap;
  }

  public Map<UUID, Currency> getCurrencies() {

    return currencies;
  }

  public void delete(final UUID uid) {

    if(currencies.containsKey(uid)) {
      //TODO: Delete files.
    }
    currencies.remove(uid);
  }
}