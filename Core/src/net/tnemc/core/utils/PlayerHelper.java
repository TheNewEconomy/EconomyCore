package net.tnemc.core.utils;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import java.util.regex.Pattern;

/**
 * PlayerHelper - Utilities relating to all things player-related.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerHelper {

  /**
   * Returns the {@link Pattern pattern} utilized to determine if a string is a valid
   * player username.
   *
   * @return The {@link Pattern pattern} to use for determining if a string is a valid
   * player username.
   *
   * @see Pattern
   */
  public static Pattern playerMatcher() {
    return Pattern.compile("^\\w*$");
  }
}