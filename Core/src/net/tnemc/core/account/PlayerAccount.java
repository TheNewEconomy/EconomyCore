package net.tnemc.core.account;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.PlayerProvider;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents an account that is associated with a player.
 *
 * @see Account
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerAccount extends Account {

  protected UUID uuid;
  protected long lastOnline;
  protected String language;

  public PlayerAccount(UUID identifier, String name) {
    super(identifier.toString(), name);

    this.uuid = identifier;

    //Defaults
    this.lastOnline = new Date().getTime();
    this.language = "TNE_DEFAULT";
  }

  public String world() {
    String world = TNECore.server().defaultWorld();
    final Optional<PlayerProvider> player = getPlayer();

    if(player.isPresent()) {
      world = player.get().getWorld();
    }
    return TNECore.worldProvider().resolveWorld(world);
  }

  /**
   * Attempts to find a {@link PlayerProvider player} based on an {@link UUID identifier}.
   *
   * @return An Optional containing the located {@link PlayerProvider player}, or an empty
   *         Optional if no player is located.
   */
  public Optional<PlayerProvider> getPlayer() {
    return TNECore.server().findPlayer(uuid);
  }

  public boolean isOnline() {
    return getPlayer().isPresent();
  }

  public UUID getUUID() {
    return uuid;
  }

  public long getLastOnline() {
    return lastOnline;
  }

  public void setLastOnline(long lastOnline) {
    this.lastOnline = lastOnline;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
}
