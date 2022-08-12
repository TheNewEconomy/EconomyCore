package net.tnemc.core.menu.icon;
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

/**
 * Represents an action that is performed on an action.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface IconAction {

  /**
   * The action type that belongs to this icon action.
   * @return The {@link ActionType} for when this action should happen.
   */
  ActionType type();

  /**
   * Determines if any other icon actions should be performed after this action is performed.
   * @return True if other actions should be performed, otherwise false.
   */
  boolean continueOther();

  /**
   * This method is called when the action happens. It should return a boolean that states if the
   * action was successful or not.
   * @param menu The menu that the action happened in.
   * @param page The page of the menu that the action happened in.
   * @param player The player that performed the action.
   * @return True if the action was successful, otherwise false.
   */
  boolean onPerform(Menu menu, Page page, PlayerProvider player);
}