package net.tnemc.core.menu.impl.mycurrency.pages;
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

import net.tnemc.core.menu.impl.shared.icons.PreviousPageIcon;
import net.tnemc.menu.core.compatibility.MenuPlayer;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.page.impl.PlayerPage;

import java.util.HashMap;
import java.util.Map;

/**
 * CurrencyEditorPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyEditorPage extends PlayerPage {
  public CurrencyEditorPage() {
    super(2);
  }

  @Override
  public Map<Integer, Icon> defaultIcons(MenuPlayer menuPlayer) {

    Map<Integer, Icon> icons = new HashMap<>();

    icons.put(0, new PreviousPageIcon(0, 1));


    return icons;
  }
}
