package net.tnemc.core.menu;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.menu.constraints.TNEStringConstraints;
import net.tnemc.menu.core.Menu;
import net.tnemc.menu.core.Page;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.builder.PageBuilder;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.plugincore.PluginCore;

/**
 * MyEcoMenu
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MyEcoMenu extends Menu {

  public MyEcoMenu() {

    //TODO: Add pages
    //Main page.
    //~Currencies
    final Page main = new PageBuilder(1).withIcons(
            new IconBuilder(PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                    .display("Currencies"))
                    .withActions(new SwitchPageAction("my_eco",2))
                    .build()
    ).build();
    pages.put(1, main);

    //Currency Page
    //~displays all currencies; right click to edit
    final Page currency = new PageBuilder(2).build();

    for(final Currency curObj : TNECore.eco().currency().currencies()) {

      currency.addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(curObj.getIconMaterial(), 1))
              .withActions(new SwitchPageAction("my_eco", 3))
              .withConstraint(TNEStringConstraints.CURRENCY_UUID, curObj.getUid().toString())
              .build());
    }
    pages.put(2, currency);

    final Page currencyEditor = new PageBuilder(3).build();

    pages.put(3, currencyEditor);
  }
}