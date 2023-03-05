package net.tnemc.core.menu.icon.action;

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
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.Page;
import net.tnemc.core.menu.icon.ActionType;
import net.tnemc.core.menu.icon.Icon;
import net.tnemc.core.menu.icon.IconAction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DataAction
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class DataAction implements IconAction {

  protected final Map<String, Object> data = new ConcurrentHashMap<>();

  /**
   * The action type that belongs to this icon action.
   *
   * @return The {@link ActionType} for when this action should happen.
   */
  @Override
  public ActionType type() {
    return ActionType.ANY;
  }

  /**
   * Determines if any other icon actions should be performed after this action is performed.
   *
   * @return True if other actions should be performed, otherwise false.
   */
  @Override
  public boolean continueOther() {
    return true;
  }

  /**
   * This method is called when the action happens.
   *
   * @param menu   The menu that the action happened in.
   * @param page   The page of the menu that the action happened in.
   * @param player The player that performed the action.
   * @param icon   The icon clicked in the action.
   */
  @Override
  public void onPerform(Menu menu, Page page, PlayerProvider player, Icon icon) {
    TNECore.menu().appendViewerData(player.getUUID(), data);
  }
}