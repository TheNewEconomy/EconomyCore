package net.tnemc.bukkit.depend.towny;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import com.palmergames.bukkit.towny.TownyAPI;
import net.tnemc.core.account.NonPlayerAccount;

import java.util.Objects;

/**
 * Represents an account linked to a Town in the Towny Plugin.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TownAccount extends NonPlayerAccount {

  public TownAccount(String identifier, String name) {
    super(identifier, name);

    try {
      this.identifier = Objects.requireNonNull(TownyAPI.getInstance().getTown(name)).getUUID().toString();
      owner = Objects.requireNonNull(TownyAPI.getInstance().getTown(name)).getMayor().getUUID();
    } catch(Exception ignore) {}
  }
}