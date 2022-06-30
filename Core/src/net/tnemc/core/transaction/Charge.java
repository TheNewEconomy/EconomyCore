package net.tnemc.core.transaction;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import java.math.BigDecimal;

/**
 * An object that represents a charge during any form of financial transaction.
 *
 * @see Transaction
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Charge {

  /**
   * The identifier of the commodity involved.
   */
  private String commodity;

  /**
   * The name of the world involved.
   */
  private String world;

  /**
   * The {@link BigDecimal amount} that this charge is for.
   */
  private BigDecimal amount;

  /**
   * Constructs an object that represents a charge during a financial transaction.
   * @param commodity The identifier of the commodity involved. This is usually the name of the
   *                  currency used in the charge. This could also be a material name.
   * @param world The name of the world involved.
   * @param amount The {@link BigDecimal amount} that this charge is for.
   */
  public Charge(String commodity, String world, BigDecimal amount) {
    this.commodity = commodity;
    this.world = world;
    this.amount = amount;
  }

  public String getCommodity() {
    return commodity;
  }

  public void setCommodity(String commodity) {
    this.commodity = commodity;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
