package net.tnemc.core.menu.callbacks;
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
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.Page;

import java.util.Optional;

/**
 * ClickCallback
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ClickCallback {

  protected final Menu menu;
  protected final Page page;
  protected final PlayerProvider player;
  protected final int slot;

  public ClickCallback(Menu menu, Page page, PlayerProvider player) {
    this(menu, page, player, -1);
  }

  public ClickCallback(Menu menu, Page page, PlayerProvider player, int slot) {
    this.menu = menu;
    this.page = page;
    this.player = player;
    this.slot = slot;
  }

  public Menu getMenu() {
    return menu;
  }

  public Page getPage() {
    return page;
  }

  public PlayerProvider getPlayer() {
    return player;
  }

  public Optional<Integer> getSlot() {
    if(slot == -1) return Optional.empty();
    return Optional.of(slot);
  }
}