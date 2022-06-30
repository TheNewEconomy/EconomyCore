package net.tnemc.core.id.impl;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.id.UUIDAPI;

/**
 * Represents the Official Mojang API.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MojangAPI implements UUIDAPI {
  /**
   * @return The URL for this UUID API Service.
   */
  @Override
  public String url() {
    return "https://api.mojang.com/users/profiles/minecraft/";
  }
}