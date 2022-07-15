package net.tnemc.core.currency;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.TNECore;
import net.tnemc.core.currency.item.ItemCurrency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a currency object.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Currency {

  private Map<String, Double> conversion = new HashMap<>();

  //World-related configurations.

  //Balance-related configurations.
  private BigDecimal startingHoldings;
  private BigDecimal maxBalance;

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

  public boolean isNotable() {
    return !(this instanceof ItemCurrency) && note != null;
  }

  public Optional<Note> getNote() {
    return Optional.ofNullable(note);
  }

  public CurrencyType type() {
    final Optional<CurrencyType> type = TNECore.eco().currency().findType(this.type);
    return type.orElseGet(()->TNECore.eco().currency().findType("virtual").get());
  }

  public Map<String, Double> getConversion() {
    return conversion;
  }

  public void setConversion(Map<String, Double> conversion) {
    this.conversion = conversion;
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
}