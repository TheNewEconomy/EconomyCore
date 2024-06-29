package net.tnemc.core.menu.icons.actions;

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

import net.tnemc.menu.core.handlers.MenuClickHandler;
import net.tnemc.menu.core.icon.action.ActionType;
import net.tnemc.menu.core.icon.action.IconAction;
import net.tnemc.menu.core.viewer.CoreStatus;

/**
 * PageSwitchWithClose
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PageSwitchWithClose extends IconAction {

  private final String menu;
  private final int page;

  public PageSwitchWithClose(String menu, int page) {
    super(ActionType.ANY);
    this.menu = menu;
    this.page = page;
  }

  public PageSwitchWithClose(String menu, int page, ActionType type) {
    super(type);
    this.menu = menu;
    this.page = page;
  }

  /**
   * Handles the click action for the icon using the provided {@link MenuClickHandler}.
   *
   * @param handler The {@link MenuClickHandler} to be executed upon the click action.
   *
   * @return {@code true} if the click action is blocked or has special behavior, otherwise
   * {@code false}.
   */
  @Override
  public boolean onClick(MenuClickHandler handler) {

    if(page == -1) {
      handler.player().inventory().close();
      return true;
    }

    handler.player().status(CoreStatus.SWITCHING);
    handler.player().inventory().openMenu(handler.player(), menu, page);
    return true;
  }
}