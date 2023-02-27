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

import net.tnemc.core.revampmenu.Icon;

/**
 * Represents a callback, which is related to an {@link net.tnemc.core.revampmenu.Icon}.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class IconCallback {

  protected Icon icon;

  public IconCallback(Icon icon) {
    this.icon = icon;
  }

  public Icon getIcon() {
    return icon;
  }

  public void setIcon(Icon icon) {
    this.icon = icon;
  }
}