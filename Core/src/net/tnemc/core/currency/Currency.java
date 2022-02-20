package net.tnemc.core.currency;

import net.tnemc.core.EconomyManager;
import net.tnemc.core.currency.item.ItemCurrrency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a currency object.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class Currency {

  private List<String> worlds = new ArrayList<>();

  private Map<String, Double> conversion = new HashMap<>();

  //World-related configurations.
  private boolean worldDefault;
  private boolean global;

  //Balance-related configurations.
  private BigDecimal startingHoldings;
  private BigDecimal maxBalance;

  //Core currency-related configurations.
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
  private CurrencyNote note;

  public boolean isNotable() {
    return !(this instanceof ItemCurrrency) && note != null;
  }

  public Optional<CurrencyNote> getNote() {
    return Optional.ofNullable(note);
  }

  public List<String> getWorlds() {
    return worlds;
  }

  public void setWorlds(List<String> worlds) {
    this.worlds = worlds;
  }

  public Map<String, Double> getConversion() {
    return conversion;
  }

  public void setConversion(Map<String, Double> conversion) {
    this.conversion = conversion;
  }

  public boolean isWorldDefault() {
    return worldDefault;
  }

  public void setWorldDefault(boolean worldDefault) {
    this.worldDefault = worldDefault;
  }

  public boolean isGlobal() {
    return global;
  }

  public void setGlobal(boolean global) {
    this.global = global;
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

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public Optional<CurrencyType> getType() {
    return EconomyManager.instance().currencyManager().findType(type);
  }

  public boolean isItem() {
    return this instanceof ItemCurrrency;
  }

  public ItemCurrrency item() {
    return (ItemCurrrency)this;
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

  public void setNote(CurrencyNote note) {
    this.note = note;
  }
}