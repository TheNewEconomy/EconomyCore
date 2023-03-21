package net.tnemc.core.menu.impl.mybal;
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

import net.tnemc.core.menu.impl.mybal.pages.ActionsPage;
import net.tnemc.core.menu.impl.mybal.pages.BalancePage;
import net.tnemc.core.menu.impl.mybal.pages.CurrencyMainPage;
import net.tnemc.core.menu.impl.shared.pages.AmountSelectionPage;
import net.tnemc.menu.core.Menu;

/**
 * MyBalMenu
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MyBalMenu extends Menu {

  /*
   * - Currencies https://prnt.sc/WI8YCcAiFtk8
   * - - Add
   * - - Remove - simple confirmation menu
   * - - Edit
   */

  public MyBalMenu() {
    super("my_bal", "MyBal Menu", 36);

    //Add our pages.
    pages.put(1, new CurrencyMainPage());
    pages.put(2, new BalancePage());
    pages.put(3, new ActionsPage());
    pages.put(4, new AmountSelectionPage(4, "my_bal"));
  }
}