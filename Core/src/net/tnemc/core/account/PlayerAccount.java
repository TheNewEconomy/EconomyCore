package net.tnemc.core.account;

/*
 * The New Economy
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.Location;
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

  public String region() {
    String region = TNECore.server().defaultWorld();
    final Optional<PlayerProvider> player = getPlayer();

    if(player.isPresent()) {
      region = player.get().getRegion(true);
    }
    return region;
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

  /**
   * Used to grab the location of the account holder. This will be an empty optional for all offline
   * players accounts.
   *
   * @return The optional containing the account holder's location, or an empty optional if the
   * holder is offline.
   */
  public Optional<Location> location() {
    return getPlayer().map(PlayerProvider::getLocation);
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
