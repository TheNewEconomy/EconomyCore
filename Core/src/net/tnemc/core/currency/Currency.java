package net.tnemc.core.currency;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.manager.CurrencyManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Represents a currency object.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Currency {

  private final Map<String, Double> conversion = new HashMap<>();
  private final TreeMap<BigDecimal, Denomination> denominations = new TreeMap<>();

  //World-related configurations.
  private final Map<String, CurrencyRegion> regions = new HashMap<>();

  //Balance-related configurations.
  private BigDecimal startingHoldings;
  private BigDecimal maxBalance;
  private BigDecimal minBalance;

  //identifier
  private UUID uid;

  //Icon used for menus.
  private String iconMaterial;

  //Utilized for backwards compat only
  private String identifier;
  private String type;

  //Format-related configurations.
  private String format;
  private String symbol;
  private String prefixes;
  private String decimal;
  private String display;
  private String displayPlural;
  private String displayMinor;
  private String displayMinorPlural;
  private boolean separateMajor;
  private String majorSeparator;

  //Weights and decimal places
  private int decimalPlaces;
  private int minorWeight;

  //MISC configurations
  private Note note;

  public Currency() {
    this.startingHoldings = BigDecimal.ZERO;
    this.maxBalance = CurrencyManager.largestSupported;
    this.minBalance = BigDecimal.ZERO;

    this.uid = UUID.randomUUID();
    this.type = "virtual";

    this.symbol = "$";
    this.decimal = ".";
    this.separateMajor = true;
    this.majorSeparator = ",";

    this.decimalPlaces = 2;
    this.minorWeight = 100;
  }

  public Denomination getDenominationByWeight(final BigDecimal weight) {
    return denominations.get(weight);
  }

  public boolean isNotable() {
    return note != null;
  }

  public Optional<Note> getNote() {
    return Optional.ofNullable(note);
  }

  public CurrencyType type() {
    final Optional<CurrencyType> type = TNECore.eco().currency().findType(this.type);
    return type.orElseGet(()->TNECore.eco().currency().findType("virtual").get());
  }

  public Optional<BigDecimal> convertValue(final String currency, final BigDecimal amount) {
    if(conversion.containsKey(currency)) {
      return Optional.of(amount.multiply(BigDecimal.valueOf(conversion.get(currency))));
    }
    return Optional.empty();
  }

  public Map<String, Double> getConversion() {
    return conversion;
  }

  public BigDecimal getStartingHoldings() {
    return startingHoldings;
  }

  public void setStartingHoldings(BigDecimal startingHoldings) {
    this.startingHoldings = startingHoldings;
  }

  public BigDecimal getMaxBalance() {
    return maxBalance;
  }

  public void setMaxBalance(BigDecimal maxBalance) {
    this.maxBalance = maxBalance;
  }

  public BigDecimal getMinBalance() {
    return minBalance;
  }

  public void setMinBalance(BigDecimal minBalance) {
    this.minBalance = minBalance;
  }

  public UUID getUid() {
    return uid;
  }

  public void setUid(UUID uid) {
    this.uid = uid;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getIconMaterial() {
    return iconMaterial;
  }

  public void setIconMaterial(String iconMaterial) {
    this.iconMaterial = iconMaterial;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getPrefixes() {
    return prefixes;
  }

  public void setPrefixes(String prefixes) {
    this.prefixes = prefixes;
  }

  public String getDecimal() {
    return decimal;
  }

  public void setDecimal(String decimal) {
    this.decimal = decimal;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public String getDisplayPlural() {
    return displayPlural;
  }

  public void setDisplayPlural(String displayPlural) {
    this.displayPlural = displayPlural;
  }

  public String getDisplayMinor() {
    return displayMinor;
  }

  public void setDisplayMinor(String displayMinor) {
    this.displayMinor = displayMinor;
  }

  public String getDisplayMinorPlural() {
    return displayMinorPlural;
  }

  public void setDisplayMinorPlural(String displayMinorPlural) {
    this.displayMinorPlural = displayMinorPlural;
  }

  public boolean isSeparateMajor() {
    return separateMajor;
  }

  public void setSeparateMajor(boolean separateMajor) {
    this.separateMajor = separateMajor;
  }

  public String getMajorSeparator() {
    return majorSeparator;
  }

  public void setMajorSeparator(String majorSeparator) {
    this.majorSeparator = majorSeparator;
  }

  public int getDecimalPlaces() {
    return decimalPlaces;
  }

  public void setDecimalPlaces(int decimalPlaces) {
    this.decimalPlaces = decimalPlaces;
  }

  public int getMinorWeight() {
    return minorWeight;
  }

  public void setMinorWeight(int minorWeight) {
    this.minorWeight = minorWeight;
  }

  public void setNote(Note note) {
    this.note = note;
  }

  public TreeMap<BigDecimal, Denomination> getDenominations() {
    return denominations;
  }

  public boolean isGlobal() {
    return (regions.containsKey("global") && regions.get("global").isEnabled());
  }

  public boolean isGlobalDefault() {
    if(isGlobal()) {
      return regions.get("global").isDefault();
    }
    return false;
  }

  public boolean regionEnabled(final String region) {
    return (regions.containsKey(region) && regions.get(region).isEnabled());
  }

  public boolean isRegionDefault(final String region) {
    if(regionEnabled(region)) {
      return regions.get(region).isDefault();
    }
    return false;
  }

  public Map<String, CurrencyRegion> getRegions() {
    return regions;
  }
}