package net.tnemc.core.account.holdings.modify;

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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.command.parameters.PercentBigDecimal;
import net.tnemc.core.utils.Identifier;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents an object that may be utilized to modify an {@link Account account's} holdings.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class HoldingsModifier {

  private final UUID currency;
  private final String region;
  private final HoldingsOperation operation;
  private final Identifier holdingsID;
  private BigDecimal modifier;
  private boolean percent = false;

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   *
   * @param region   The name of the region involved. This is usually a world, but could be
   *                 something else such as a world guard region name/identifier.
   * @param currency The currency to use for the modification.
   * @param modifier The amount we are utilizing to modify the holdings. This may be negative to
   *                 take the holdings down.
   */
  public HoldingsModifier(final String region, final UUID currency, final BigDecimal modifier) {

    this.region = region;
    this.currency = currency;
    this.modifier = modifier;
    this.operation = HoldingsOperation.ADD;
    this.holdingsID = EconomyManager.NORMAL;
  }

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   *
   * @param region   The name of the region involved. This is usually a world, but could be
   *                 something else such as a world guard region name/identifier.
   * @param currency The currency to use for the modification.
   * @param modifier The amount we are utilizing to modify the holdings. This may be negative to
   *                 take the holdings down.
   */
  public HoldingsModifier(final String region, final UUID currency, final PercentBigDecimal modifier) {

    this.region = region;
    this.currency = currency;
    this.modifier = modifier.value();

    if(modifier.isPercent()) {
      this.operation = HoldingsOperation.PERCENT_ADD;
      this.percent = true;
    } else {
      this.operation = HoldingsOperation.ADD;
    }
    this.holdingsID = EconomyManager.NORMAL;
  }

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   *
   * @param region   The name of the region involved. This is usually a world, but could be
   *                 something else such as a world guard region name/identifier.
   * @param currency The currency to use for the modification.
   * @param modifier The amount we are utilizing to modify the holdings. This may be negative to
   *                 take the holdings down.
   * @param id       The {@link Identifier} for the holdings type to perform this transaction on.
   */
  public HoldingsModifier(final String region, final UUID currency, final BigDecimal modifier, final Identifier id) {

    this.region = region;
    this.currency = currency;
    this.modifier = modifier;
    this.operation = HoldingsOperation.ADD;
    this.holdingsID = id;
  }

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   *
   * @param region   The name of the region involved. This is usually a world, but could be
   *                 something else such as a world guard region name/identifier.
   * @param currency The currency to use for the modification.
   * @param modifier The amount we are utilizing to modify the holdings. This may be negative to
   *                 take the holdings down.
   * @param id       The {@link Identifier} for the holdings type to perform this transaction on.
   */
  public HoldingsModifier(final String region, final UUID currency, final PercentBigDecimal modifier, final Identifier id) {

    this.region = region;
    this.currency = currency;
    this.modifier = modifier.value();

    if(modifier.isPercent()) {
      this.operation = HoldingsOperation.PERCENT_ADD;
      this.percent = true;
    } else {
      this.operation = HoldingsOperation.ADD;
    }
    this.holdingsID = id;
  }

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   *
   * @param entry The holdings entry to populate this modifier from.
   */
  public HoldingsModifier(final HoldingsEntry entry) {

    this.region = entry.getRegion();
    this.currency = entry.getCurrency();
    this.modifier = entry.getAmount();
    this.operation = HoldingsOperation.ADD;
    this.holdingsID = entry.getHandler();
  }

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   *
   * @param currency  The currency to use for the modification.
   * @param region    The name of the region involved. This is usually a world, but could be
   *                  something else such as a world guard region name/identifier.
   * @param modifier  The amount we are utilizing to modify the holdings. This may be negative to
   *                  take the holdings down.
   * @param operation The operation that should be performed with the modifier.
   */
  public HoldingsModifier(final String region, final UUID currency, final BigDecimal modifier,
                          final HoldingsOperation operation) {

    this.currency = currency;
    this.region = region;
    this.modifier = modifier;
    this.operation = operation;
    this.holdingsID = EconomyManager.NORMAL;
  }

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   *
   * @param currency  The currency to use for the modification.
   * @param region    The name of the region involved. This is usually a world, but could be
   *                  something else such as a world guard region name/identifier.
   * @param modifier  The amount we are utilizing to modify the holdings. This may be negative to
   *                  take the holdings down.
   * @param operation The operation that should be performed with the modifier.
   * @param id        The {@link Identifier} for the holdings type to perform this transaction on.
   */
  public HoldingsModifier(final String region, final UUID currency, final BigDecimal modifier,
                          final HoldingsOperation operation, final Identifier id) {

    this.currency = currency;
    this.region = region;
    this.modifier = modifier;
    this.operation = operation;
    this.holdingsID = id;
  }

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   *
   * @param entry     The holdings entry to populate this modifier from.
   * @param operation The operation that should be performed with the modifier.
   */
  public HoldingsModifier(final HoldingsEntry entry, final HoldingsOperation operation) {

    this.region = entry.getRegion();
    this.currency = entry.getCurrency();
    this.modifier = entry.getAmount();
    this.operation = operation;
    this.holdingsID = entry.getHandler();
  }

  /**
   * Returns a new {@link HoldingsModifier} object that is the opposite in terms of amount from this
   * one.
   *
   * @return The new opposite holdings modifier object.
   */
  public HoldingsModifier counter() {

    final HoldingsModifier mod = new HoldingsModifier(region, currency, modifier.negate(), operation, holdingsID);
    //System.out.println("Percent Counter: " + percent);
    mod.setPercent(percent);
    return mod;
  }

  /**
   * Returns a new {@link HoldingsModifier} object that is the opposite in terms of amount from this
   * one.
   *
   * @param type The {@link Identifier} for the holdings type to perform this transaction on.
   *
   * @return The new opposite holdings modifier object.
   */
  public HoldingsModifier counter(final Identifier type) {

    final HoldingsModifier mod = new HoldingsModifier(region, currency, modifier.negate(), operation, type);
    mod.setPercent(percent);
    return mod;
  }

  /**
   * Used to determine if this modifier is used to remove froms.
   *
   * @return True if this will remove funds, otherwise false.
   */
  public boolean isRemoval() {

    return operation.equals(HoldingsOperation.SUBTRACT)
           || operation.equals(HoldingsOperation.DIVIDE)
           || modifier.compareTo(BigDecimal.ZERO) < 0;
  }

  public BigDecimal modify(final BigDecimal value) {

    return operation.perform(value, modifier);
  }

  public void modifier(final BigDecimal value) {

    this.modifier = modifier.add(value);
  }

  public UUID getCurrency() {

    return currency;
  }

  public String getRegion() {

    return region;
  }

  public BigDecimal getModifier() {

    return modifier;
  }

  public HoldingsOperation getOperation() {

    return operation;
  }

  public Identifier getType() {

    return holdingsID;
  }

  public HoldingsEntry asEntry() {

    return new HoldingsEntry(region, currency, modifier, EconomyManager.NORMAL);
  }

  public boolean isPercent() {

    return percent;
  }

  public void setPercent(final boolean percent) {

    this.percent = percent;
  }
}