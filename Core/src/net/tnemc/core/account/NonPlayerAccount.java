package net.tnemc.core.account;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import java.util.UUID;

/**
 * Represents an account that is not associated with a player.
 *
 * @see Account
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class NonPlayerAccount extends SharedAccount {

  public NonPlayerAccount(String identifier, String name, UUID owner) {
    super(identifier, name, owner);
  }
}
