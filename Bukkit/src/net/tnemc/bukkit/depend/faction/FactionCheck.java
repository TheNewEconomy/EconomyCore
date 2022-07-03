package net.tnemc.bukkit.depend.faction;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.AccountTypeCheck;

import java.util.function.Function;

/**
 * FactionCheck
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class FactionCheck implements AccountTypeCheck {

  /**
   * Returns our check function that should be used to check if a given String identifier, usually name,
   * is valid for this account type.
   *
   * @return Our function that should be used to check if a given String identifier, usually name,
   * is valid for this account type.
   */
  @Override
  public Function<String, Boolean> check() {
    return value -> value.toLowerCase().contains("faction-");
  }
}
