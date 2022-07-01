package net.tnemc.core.account.holdings;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.holdings.modify.HoldingsModifier;

import java.math.BigDecimal;

/**
 * This class acts like a wrapper over the {@link java.math.BigDecimal} class in order to interface
 * with {@link HoldingsModifier} a little more.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class HoldingAmount {

  private BigDecimal amount;

  public HoldingAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void modify(HoldingsModifier modifier) {

  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}