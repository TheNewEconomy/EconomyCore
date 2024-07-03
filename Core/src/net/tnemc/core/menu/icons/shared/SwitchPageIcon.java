package net.tnemc.core.menu.icons.shared;

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

import net.tnemc.item.AbstractItemStack;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.icon.action.ActionType;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import org.jetbrains.annotations.NotNull;

/**
 * SwitchPageIcon
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class SwitchPageIcon extends Icon {

  private final String menu;
  private final int page;
  private final ActionType actionType;

  public SwitchPageIcon(int slot, @NotNull AbstractItemStack<?> item, final String menu, final int page, final ActionType type) {
    this(slot, item, menu, page, type, true);
  }
  public SwitchPageIcon(int slot, @NotNull AbstractItemStack<?> item, final String menu, final int page, final ActionType type, boolean registerAction) {
    super(item, null);

    this.slot = slot;
    this.menu = menu;
    this.page = page;
    this.actionType = type;

    if(registerAction) {
      actions.add(new SwitchPageAction(menu, page, type));
    }
  }

  public void addActions() {
    actions.add(new SwitchPageAction(menu, page, actionType));
  }
}