package net.tnemc.core.account.holdings;

/*
 * The New Economy Minecraft Server Plugin
 *
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

/**
 * Represents a holdings type. Since holdings may be stored in different aspects, this allows TNE
 * to control them all and store without needing to add new fields for each holdings option.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public enum HoldingsType {

  // all holdings
  NORMAL_HOLDINGS("Normal_Holdings"),

  //For virtual currencies, this is the balances from the database/cache
  VIRTUAL_HOLDINGS("Virtual_Holdings"),

  // for item-based currencies this is the balances in inventory only
  INVENTORY_ONLY("Inventory_Holdings"),

  // for item-based currencies this is the balances in ender chest only
  E_CHEST("Ender_Chest");

  private final String identifier;

  HoldingsType(final String identifier) {
    this.identifier = identifier;
  }

  public HoldingsType fromIdentifier(final String identifier) {
    for(HoldingsType type : values()) {
      if(type.identifier.equalsIgnoreCase(identifier)) return type;
    }
    return NORMAL_HOLDINGS;
  }

  public String getIdentifier() {
    return identifier;
  }
}