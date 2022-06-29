package net.tnemc.core.currency;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

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
  private Note note;

  public boolean isNotable() {
    return !(this instanceof ItemCurrency) && note != null;
  }

  public Optional<Note> getNote() {
    return Optional.ofNullable(note);
  }
}