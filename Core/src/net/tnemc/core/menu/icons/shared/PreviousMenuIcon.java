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

import net.kyori.adventure.text.Component;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.icon.action.ActionType;
import net.tnemc.menu.core.icon.action.impl.SwitchMenuAction;
import net.tnemc.plugincore.PluginCore;

/**
 * BackIcon
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class PreviousMenuIcon extends Icon {

  public PreviousMenuIcon(int slot, final String menu, final ActionType type) {
    super(PluginCore.server().stackBuilder().of("RED_WOOL", 1).display(Component.text("Previous Menu")), null);

    this.slot = slot;

    actions.add(new SwitchMenuAction(menu, type));
  }
}