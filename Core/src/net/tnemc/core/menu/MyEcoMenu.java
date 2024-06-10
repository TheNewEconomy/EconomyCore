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
import net.tnemc.menu.core.icon.action.impl.ChatAction;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.RunnableAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.plugincore.PluginCore;

import java.util.Collections;

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
                    .withSlot(2)
                    .build()
    ).build();
    addPage(main);

    //Currency Page
    //~displays all currencies; right click to edit
    final Page currency = new PageBuilder(2).build();

    //add currency
    currency.addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
            .lore(Collections.singletonList("Click to add currency")))
            .withActions(new SwitchPageAction("my_eco", 3))
            .withSlot(2)
            .build());

    int i = 19;
    for(final Currency curObj : TNECore.eco().currency().currencies()) {

      currency.addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(curObj.getIconMaterial(), 1)
                                           .lore(Collections.singletonList("Click to edit currency")))
              .withActions(new SwitchPageAction("my_eco", 3), new DataAction("CURRENCY_UUID", curObj.getUid().toString()))
              .withSlot(i)
              .build());

      i += 2;
    }
    addPage(currency);

    final Page currencyEditor = new PageBuilder(3).build();

    //denominations
    currencyEditor.addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
            .display("Edit Denominations"))
            .withSlot(19)
            .withActions(new SwitchPageAction("my_eco", 4))
            .build());

    //currency name icon
    currencyEditor.addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("SIGN", 1)
                    .lore(Collections.singletonList("Click to set name of currency.")))
                    .withSlot(21)
                    .withActions(new ChatAction((message)->{

                      if(message.getPlayer().viewer().isPresent()) {
                        message.getPlayer().viewer().get().addData("CURRENCY_NAME", message.getMessage());
                        return true;
                      }
                      message.getPlayer().message("Enter a name for the currency:");
                      return false;
                    }), new RunnableAction((run)->{
                      run.player().message("Enter a name for the currency:");
                    }))
                    .withItemProvider((provider)->{

                      final String message = (provider.viewer().isPresent())? (String)provider.viewer().get().dataOrDefault("CURRENCY_NAME", "Default") : "Default";

                      return PluginCore.server().stackBuilder().of("", 1)
                              .lore(Collections.singletonList("Click to set name of currency."))
                              .display(message);
                    })
            .build());

    addPage(currencyEditor);

    final Page denominationsPage = new PageBuilder(4).build();

    //add currency
    denominationsPage.addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
            .lore(Collections.singletonList("Click to add denomination")))
            .withActions(new SwitchPageAction("my_eco", 3))
            .withSlot(2)
            .build());

    i = 19;
    for(final Currency curObj : TNECore.eco().currency().currencies()) {

      denominationsPage.addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(curObj.getIconMaterial(), 1)
              .lore(Collections.singletonList("Click to edit currency")))
              .withActions(new SwitchPageAction("my_eco", 3), new DataAction("CURRENCY_UUID", curObj.getUid().toString()))
              .withSlot(i)
              .build());

      i += 2;
    }

    addPage(denominationsPage);
  }

  public void addPage(final Page page) {
    pages.put(page.number(), page);
  }
}