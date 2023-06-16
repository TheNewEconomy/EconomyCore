package net.tnemc.core.menu.impl.mycurrency;
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

import net.tnemc.core.menu.impl.mycurrency.pages.CurrencyEditorPage;
import net.tnemc.core.menu.impl.mycurrency.pages.CurrencyMainPage;
import net.tnemc.menu.core.Menu;

/**
 * MyCurrencyMenu
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MyCurrencyMenu extends Menu {

  /*
   * - Currencies https://prnt.sc/WI8YCcAiFtk8
   * - - Add
   * - - Remove - simple confirmation menu
   * - - Edit
   */

  public MyCurrencyMenu() {
    super("my_cur", "MyCurrencies Menu", 36);

    //2 = view
    //3 = edit
    //4 = add
    //Add our pages.
    pages.put(1, new CurrencyMainPage());
    pages.put(2, new CurrencyEditorPage());
  }
}