package net.tnemc.core.menu.callbacks.menu;
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

import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.Page;

/**
 * MenuCloseCallback
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MenuCloseCallback extends MenuCallback {

  protected final Page page;
  protected final PlayerProvider player;

  public MenuCloseCallback(Menu menu, Page page, PlayerProvider player) {
    super(menu);
    this.page = page;
    this.player = player;
  }

  public Page getPage() {
    return page;
  }

  public PlayerProvider getPlayer() {
    return player;
  }
}