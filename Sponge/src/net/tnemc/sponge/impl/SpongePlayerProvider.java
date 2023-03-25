package net.tnemc.sponge.impl;

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

import net.tnemc.core.compatibility.Location;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.menu.sponge7.SpongePlayer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.plugin.Plugin;

import java.util.Optional;

/**
 * The Sponge implementation of the {@link PlayerProvider}.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongePlayerProvider extends SpongePlayer implements PlayerProvider {
  public SpongePlayerProvider(User user, Plugin plugin) {
    super(user, plugin);
  }

  /**
   * Used to get the name of this player.
   *
   * @return The name of this player.
   */
  @Override
  public String getName() {
    return user.getName();
  }

  /**
   * Used to get the location of this player.
   *
   * @return The location of this player.
   */
  @Override
  public Optional<Location> getLocation() {
    return null;
  }

  @Override
  public String region(boolean resolve) {
    return null;
  }

  /**
   * Used to get the amount of experience this player has.
   *
   * @return The amount of levels this player has.
   */
  @Override
  public int getExp() {
    return user.getPlayer().get().get(Keys.TOTAL_EXPERIENCE).orElse(0);
  }

  /**
   * Used to set the amount of experience this player has.
   *
   * @param exp The amount of experience to set for this player.
   */
  @Override
  public void setExp(int exp) {
    user.getPlayer().get().offer(Keys.TOTAL_EXPERIENCE, exp);
  }

  /**
   * Used to get the amount of experience levels this player has.
   *
   * @return The amount of experience levels this player has.
   */
  @Override
  public int getExpLevel() {
    return user.getPlayer().get().get(Keys.EXPERIENCE_LEVEL).orElse(0);
  }

  /**
   * Used to set the amount of experience levels this player has.
   *
   * @param level The amount of experience levels to set for this player.
   */
  @Override
  public void setExpLevel(int level) {
    user.getPlayer().get().offer(Keys.EXPERIENCE_LEVEL, level);
  }

  @Override
  public SpongeInventoryProvider inventory() {
    return new SpongeInventoryProvider(identifier(), plugin);
  }

  @Override
  public void message(String message) {
    message(new MessageData(message));
  }

  @Override
  public void message(MessageData messageData) {

  }
}