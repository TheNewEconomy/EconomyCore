package net.tnemc.core.menu.callbacks.icon;
/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.revampmenu.Icon;
import net.tnemc.core.revampmenu.callbacks.utils.IconClickType;

/**
 * Represents a callback, which is called when an {@link Icon} is clicked.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class IconClickCallback extends IconCallback {

  protected final IconClickType clickType;
  protected final PlayerProvider player;

  public IconClickCallback(Icon icon, IconClickType clickType, PlayerProvider player) {
    super(icon);
    this.clickType = clickType;
    this.player = player;
  }

  public IconClickType getClickType() {
    return clickType;
  }

  public PlayerProvider getPlayer() {
    return player;
  }
}
