package net.tnemc.core.utils;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

/**
 * This is a class, which houses empty methods until they find their permanent logic and home.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AccountHelper {

  public static boolean exists(final String name) {
    return false;
  }

  public static boolean exists(final UUID uuid) {
    return false;
  }

  public static boolean initialize(final String uuid, final String name) {
    return true;
  }

  public static BigDecimal getHoldings(final String uuid, final String region, final String currency) {
    return BigDecimal.ZERO;
  }
}