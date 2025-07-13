package net.tnemc.core.currency;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.currency.item.ItemCurrency;
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

  private String file;

  //Balance-related configurations.
  private BigDecimal startingHoldings;
  private BigDecimal maxBalance;
  private BigDecimal minBalance;

  private boolean negativeSupport = false;
  private boolean sync = true;

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
  private String prefixesj;
  private String decimal;
  private boolean balanceShow;

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

  public Currency(final String identifier) {

    this.file = identifier + ".yml";

    //Currency Info Configurations
    this.uid = UUID.randomUUID();
    this.identifier = identifier;
    this.iconMaterial = "PAPER";
    this.display = "Dollar";
    this.displayPlural = "Dollars";
    this.displayMinor = "Cent";
    this.displayMinorPlural = "Cents";
    this.symbol = "$";

    //Currency Options Configurations
    this.decimal = ".";
    this.balanceShow = true;
    this.decimalPlaces = 2;
    this.type = "virtual";
    this.minorWeight = 100;

    //Formatting Configurations
    this.format = "<symbol><major.amount><decimal><minor.amount>";
    this.prefixes = "kMGTPEZYXWVUN₮";
    this.prefixesj = "万億兆京垓\uD855\uDF71穣溝澗正載";
    this.separateMajor = true;
    this.majorSeparator = ",";

    //Balance Configurations
    this.startingHoldings = BigDecimal.ZERO;
    this.maxBalance = CurrencyManager.largestSupported;
    this.minBalance = BigDecimal.ZERO;

    //World
    this.regions.put("global", new CurrencyRegion("global", true, false));
  }

  public static Currency clone(final Currency original, final boolean item) {

    final Currency cloned = (item)? new ItemCurrency(original.identifier) : new Currency(original.identifier);

    cloned.file = original.file;
    cloned.startingHoldings = original.startingHoldings;
    cloned.maxBalance = original.maxBalance;
    cloned.minBalance = original.minBalance;
    cloned.negativeSupport = original.negativeSupport;
    cloned.uid = original.uid;
    cloned.iconMaterial = original.iconMaterial;
    cloned.type = original.type;
    cloned.format = original.format;
    cloned.symbol = original.symbol;
    cloned.prefixes = original.prefixes;
    cloned.prefixesj = original.prefixesj;
    cloned.decimal = original.decimal;
    cloned.display = original.display;
    cloned.displayPlural = original.displayPlural;
    cloned.displayMinor = original.displayMinor;
    cloned.displayMinorPlural = original.displayMinorPlural;
    cloned.separateMajor = original.separateMajor;
    cloned.majorSeparator = original.majorSeparator;
    cloned.decimalPlaces = original.decimalPlaces;
    cloned.minorWeight = original.minorWeight;
    cloned.note = original.note;

    if(cloned instanceof final ItemCurrency clonedItem && original instanceof final ItemCurrency itemCurrency) {
      clonedItem.setEnderChest(itemCurrency.canEnderChest());
      clonedItem.setEnderFill(itemCurrency.isEnderFill());
    }

    if(original instanceof ItemCurrency && !item || !(original instanceof ItemCurrency) && item) {

      for(final Map.Entry<BigDecimal, Denomination> entry : original.getDenominations().entrySet()) {

        cloned.denominations.put(entry.getKey(), Denomination.clone(entry.getValue(), item));
      }
    } else {
      cloned.denominations.putAll(original.denominations);
    }

    cloned.conversion.putAll(original.conversion);
    original.regions.forEach((key, value)->cloned.regions.put(key, new CurrencyRegion(value.region(), value.isEnabled(), value.isDefault())));

    return cloned;
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

  public void setNote(final Note note) {

    this.note = note;
  }

  public boolean isSync() {

    return sync;
  }

  public void setSync(final boolean sync) {

    this.sync = sync;
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

  public void setStartingHoldings(final BigDecimal startingHoldings) {

    this.startingHoldings = startingHoldings;
  }

  public BigDecimal getMaxBalance() {

    return maxBalance;
  }

  public void setMaxBalance(final BigDecimal maxBalance) {

    this.maxBalance = maxBalance;
  }

  public BigDecimal getMinBalance() {

    return minBalance;
  }

  public void setMinBalance(final BigDecimal minBalance) {

    this.minBalance = minBalance;
  }

  public String getFile() {

    return file;
  }

  public void setFile(final String file) {

    this.file = file;
  }

  public UUID getUid() {

    return uid;
  }

  public void setUid(final UUID uid) {

    this.uid = uid;
  }

  public String getIdentifier() {

    return identifier;
  }

  public void setIdentifier(final String identifier) {

    this.identifier = identifier;
    this.file = identifier + ".yml";
  }

  public String getIconMaterial() {

    return iconMaterial;
  }

  public void setIconMaterial(final String iconMaterial) {

    this.iconMaterial = iconMaterial;
  }

  public String getType() {

    return type;
  }

  public void setType(final String type) {

    this.type = type;
  }

  public String getFormat() {

    return format;
  }

  public void setFormat(final String format) {

    this.format = format;
  }

  public String getSymbol() {

    return symbol;
  }

  public void setSymbol(final String symbol) {

    this.symbol = symbol;
  }

  public String getPrefixes() {

    return prefixes;
  }

  public void setPrefixes(final String prefixes) {

    this.prefixes = prefixes;
  }

  public String getPrefixesj() {

    return prefixesj;
  }

  public void setPrefixesj(final String prefixesj) {

    this.prefixesj = prefixesj;
  }

  public String getDecimal() {

    return decimal;
  }

  public void setDecimal(final String decimal) {

    this.decimal = decimal;
  }

  public boolean isBalanceShow() {

    return balanceShow;
  }

  public void setBalanceShow(final boolean balanceShow) {

    this.balanceShow = balanceShow;
  }

  public String getDisplay() {

    return display;
  }

  public void setDisplay(final String display) {

    this.display = display;
  }

  public String getDisplayPlural() {

    return displayPlural;
  }

  public void setDisplayPlural(final String displayPlural) {

    this.displayPlural = displayPlural;
  }

  public String getDisplayMinor() {

    return displayMinor;
  }

  public void setDisplayMinor(final String displayMinor) {

    this.displayMinor = displayMinor;
  }

  public String getDisplayMinorPlural() {

    return displayMinorPlural;
  }

  public void setDisplayMinorPlural(final String displayMinorPlural) {

    this.displayMinorPlural = displayMinorPlural;
  }

  public boolean isSeparateMajor() {

    return separateMajor;
  }

  public void setSeparateMajor(final boolean separateMajor) {

    this.separateMajor = separateMajor;
  }

  public String getMajorSeparator() {

    return majorSeparator;
  }

  public void setMajorSeparator(final String majorSeparator) {

    this.majorSeparator = majorSeparator;
  }

  public int getDecimalPlaces() {

    return decimalPlaces;
  }

  public void setDecimalPlaces(final int decimalPlaces) {

    this.decimalPlaces = decimalPlaces;
  }

  public int getMinorWeight() {

    return minorWeight;
  }

  public void setMinorWeight(final int minorWeight) {

    this.minorWeight = minorWeight;
  }

  public boolean negativeSupport() {

    return negativeSupport;
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

    return isGlobal() || (regions.containsKey(region) && regions.get(region).isEnabled());
  }

  public boolean isRegionDefault(final String region) {

    if(regions.containsKey(region)) {
      return regions.get(region).isDefault();
    }
    return false;
  }

  public Map<String, CurrencyRegion> getRegions() {

    return regions;
  }
}