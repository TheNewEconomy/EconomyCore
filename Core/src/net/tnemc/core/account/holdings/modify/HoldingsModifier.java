package net.tnemc.core.account.holdings.modify;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.Account;

import java.math.BigDecimal;

/**
 * Represents an object that may be utilized to modify an {@link Account account's} holdings.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class HoldingsModifier {

  private final String currency;
  private final String world;
  private final BigDecimal modifier;
  private final HoldingsOperation operation;

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   * @param currency The currency to use for the modification.
   * @param world The world to use for the modification.
   * @param modifier The amount we are utilizing to modify the holdings. This may be negative to
   *                 take the holdings down.
   */
  public HoldingsModifier(String currency, String world, BigDecimal modifier) {
    this.currency = currency;
    this.world = world;
    this.modifier = modifier;
    this.operation = HoldingsOperation.ADD;
  }

  /**
   * Represents an object that may be utilized to modify an {@link Account account's} holdings. This
   * class is able to then be applied directly to the holdings of an account.
   * @param currency The currency to use for the modification.
   * @param world The world to use for the modification.
   * @param modifier The amount we are utilizing to modify the holdings. This may be negative to
   *                 take the holdings down.
   * @param operation The operation that should be performed with the modifier.
   */
  public HoldingsModifier(String currency, String world, BigDecimal modifier,
                          HoldingsOperation operation) {
    this.currency = currency;
    this.world = world;
    this.modifier = modifier;
    this.operation = operation;
  }

  public BigDecimal modify(final BigDecimal value) {
    return operation.perform(value, modifier);
  }

  public String getCurrency() {
    return currency;
  }

  public String getWorld() {
    return world;
  }

  public BigDecimal getModifier() {
    return modifier;
  }

  public HoldingsOperation getOperation() {
    return operation;
  }
}