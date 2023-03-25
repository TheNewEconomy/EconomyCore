package net.tnemc.core.manager;

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

import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyLoader;
import net.tnemc.core.currency.CurrencySaver;
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.currency.loader.DefaultCurrencyLoader;
import net.tnemc.core.currency.saver.DefaultCurrencySaver;
import net.tnemc.core.currency.type.ExperienceType;
import net.tnemc.core.currency.type.ItemType;
import net.tnemc.core.currency.type.MixedType;
import net.tnemc.core.currency.type.VirtualType;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
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
  public static final BigDecimal largestSupported= new BigDecimal("900000000000000000000000000000000000000000000");

  private final Map<UUID, Currency> currencies = new HashMap<>();
  private final Map<String, UUID> curIDMap = new HashMap<>();
  private final Map<String, CurrencyType> types = new HashMap<>();

  private CurrencyLoader loader = new DefaultCurrencyLoader();
  private CurrencySaver saver = new DefaultCurrencySaver();

  public CurrencyManager() {
    addType(new ExperienceType());
    addType(new ItemType());
    addType(new MixedType());
    addType(new VirtualType());
  }

  public void load(final File parent) {
    load(parent, true);
  }

  public void load(final File parent, boolean reset) {
    if(reset) {
      currencies.clear();
    }
    loader.loadCurrencies(new File(parent, "currency"));
  }

  public CurrencyLoader getLoader() {
    return loader;
  }

  public void setLoader(CurrencyLoader loader) {
    this.loader = loader;
  }

  public CurrencySaver getSaver() {
    return saver;
  }

  public void setSaver(CurrencySaver saver) {
    this.saver = saver;
  }

  /**
   * Used to add a currency.
   * @param currency The currency to add.
   */
  public void addCurrency(final Currency currency) {
    currencies.put(currency.getUid(), currency);
    curIDMap.put(currency.getIdentifier(), currency.getUid());
  }

  /**
   * Used to find a currency based on its unique identifier.
   * @param identifier The identifier to look for.
   * @return An Optional containing the currency if it exists, otherwise an empty Optional.
   */
  public Optional<Currency> findCurrency(final UUID identifier) {
    return Optional.ofNullable(currencies.get(identifier));
  }

  /**
   * Used to find a currency based on its user-friendly identifier.
   * @param identifier The identifier to look for.
   * @return An Optional containing the currency if it exists, otherwise an empty Optional.
   */
  public Optional<Currency> findCurrency(final String identifier) {
    return Optional.ofNullable(currencies.get(curIDMap.get(identifier)));
  }

  public Collection<Currency> currencies() {
    return currencies.values();
  }

  /**
   * Used to add a currency type.
   * @param type The currency type to add.
   */
  public void addType(final CurrencyType type) {
    types.put(type.name(), type);
  }

  /**
   * Used to find a currency type based on its identifier.
   * @param identifier The identifier to look for.
   * @return An Optional containing the currency type if it exists, otherwise an empty Optional.
   */
  public Optional<CurrencyType> findType(final String identifier) {
    return Optional.ofNullable(types.get(identifier));
  }
}