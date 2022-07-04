package net.tnemc.core.account.holdings.modify;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;

import java.math.BigDecimal;

/**
 * Represents an object that may be utilized to modify an {@link Account account's} holdings.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class HoldingsModifier {

  private final String currency;
  private final String region;
  private final BigDecimal modifier;
  private final HoldingsOperation operation;

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   *
   * @param region The name of the region involved. This is usually a world, but could be something
   *               else such as a world guard region name/identifier.
   * @param currency The currency to use for the modification.
   * @param modifier The amount we are utilizing to modify the holdings. This may be negative to
   *                 take the holdings down.
   */
  public HoldingsModifier(final String region, final String currency, final BigDecimal modifier) {
    this.region = region;
    this.currency = currency;
    this.modifier = modifier;
    this.operation = HoldingsOperation.ADD;
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
  }

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   * @param currency The currency to use for the modification.
   * @param region The name of the region involved. This is usually a world, but could be something
   *               else such as a world guard region name/identifier.
   * @param modifier The amount we are utilizing to modify the holdings. This may be negative to
   *                 take the holdings down.
   * @param operation The operation that should be performed with the modifier.
   */
  public HoldingsModifier(final String region, final String currency, final BigDecimal modifier,
                          final HoldingsOperation operation) {
    this.currency = currency;
    this.region = region;
    this.modifier = modifier;
    this.operation = operation;
  }

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   * @param entry The holdings entry to populate this modifier from.
   * @param operation The operation that should be performed with the modifier.
   */
  public HoldingsModifier(final HoldingsEntry entry, final HoldingsOperation operation) {
    this.region = entry.getRegion();
    this.currency = entry.getCurrency();
    this.modifier = entry.getAmount();
    this.operation = operation;
  }

  public BigDecimal modify(final BigDecimal value) {
    return operation.perform(value, modifier);
  }

  public String getCurrency() {
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
}