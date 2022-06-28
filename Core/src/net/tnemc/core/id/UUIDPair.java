package net.tnemc.core.id;

/*
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 9/9/2020.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

import java.util.UUID;

/**
 * Class that represents a Name & UUID pair.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class UUIDPair {

  private final UUID identifier;
  private String username;

  public UUIDPair(UUID identifier, String username) {
    this.identifier = identifier;
    this.username = username;
  }

  public UUID getIdentifier() {
    return identifier;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}