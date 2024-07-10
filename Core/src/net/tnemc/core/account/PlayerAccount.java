package net.tnemc.core.account;

/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
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
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.Location;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;

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

  protected final UUID uuid;
  protected long lastOnline;
  protected String language;

  public PlayerAccount(UUID identifier, String name) {
    super(identifier, name);

    this.uuid = identifier;

    //Defaults
    this.lastOnline = creationDate;
    this.language = "TNE_DEFAULT";
  }

  public String region() {
    String region = TNECore.eco().region().defaultRegion();
    final Optional<PlayerProvider> player = getPlayer();

    if(player.isPresent()) {
      region = TNECore.eco().region().getMode().region(player.get());
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
    return PluginCore.server().findPlayer(uuid);
  }

  /**
   * Used to grab the location of the account holder. This will be an empty optional for all offline
   * players accounts.
   *
   * @return The optional containing the account holder's location, or an empty optional if the
   * holder is offline.
   */
  public Optional<Location> location() {
    if(getPlayer().isEmpty()) {
      return Optional.empty();
    }
    return getPlayer().get().getLocation();
  }

  /**
   * Used to get the type of account that this is. This is for data-purposes only.
   *
   * @return The account type.
   */
  @Override
  public String type() {
    return "player";
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
