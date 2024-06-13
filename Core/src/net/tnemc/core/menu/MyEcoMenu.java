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
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.menu.icons.shared.SwitchPageIcon;
import net.tnemc.menu.core.Menu;
import net.tnemc.menu.core.Page;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.builder.PageBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.action.ActionType;
import net.tnemc.menu.core.icon.action.impl.ChatAction;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.RunnableAction;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;

import java.util.Collections;
import java.util.Optional;

/**
 * MyEcoMenu
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MyEcoMenu extends Menu {

  private final int CURRENCIES_PAGE = 2;
  private final int CURRENCY_EDIT_PAGE = 3;
  private final int DENOMINATIONS_PAGE = 4;
  private final int DENOMINATION_EDITOR_PAGE = 5;

  public MyEcoMenu() {

    //TODO: Add pages
    //Main page.
    //~Currencies
    final Page main = new PageBuilder(1).withIcons(
            new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                    .display("Currencies"), "my_eco", CURRENCIES_PAGE, ActionType.ANY)
    ).build();
    addPage(main);

    //Currency Page
    //~displays all currencies; right click to edit
    final Page currency = new PageBuilder(CURRENCIES_PAGE).build();

    //add currency
    currency.addIcon(new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("ARROW", 1)
            .display("Add Currency").lore(Collections.singletonList("Click to add currency")), "my_eco", CURRENCY_EDIT_PAGE, ActionType.ANY));


    int i = 19;
    for(final Currency curObj : TNECore.eco().currency().currencies()) {
      final SwitchPageIcon switchIcon = new SwitchPageIcon(i, PluginCore.server().stackBuilder().of(curObj.getIconMaterial(), 1)
              .display(curObj.getIdentifier()).lore(Collections.singletonList("Click to edit currency")), "my_eco", CURRENCY_EDIT_PAGE, ActionType.ANY);

      switchIcon.addAction(new DataAction("CURRENCY_UUID", curObj.getUid().toString()));
      currency.addIcon(switchIcon);

      i += 2;
    }
    addPage(currency);

    final Page currencyEditor = new PageBuilder(CURRENCY_EDIT_PAGE).build();

    //denominations
    currencyEditor.addIcon(new SwitchPageIcon(19, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
            .display("Edit Denominations"), "my_eco", DENOMINATIONS_PAGE, ActionType.ANY));

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

    final Page denominationsPage = new PageBuilder(DENOMINATIONS_PAGE).build();
    denominationsPage.setOpen((this::handleDenominationOpen));

    addPage(denominationsPage);

    final Page denominationsEditPage = new PageBuilder(DENOMINATION_EDITOR_PAGE).build();
    denominationsEditPage.setOpen((this::handleDenominationEditOpen));

    addPage(denominationsEditPage);
  }

  private void handleDenominationOpen(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final Optional<Object> currencyUUID = viewer.get().findData("CURRENCY_UUID");
      if(currencyUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency((String)currencyUUID.get());
        if(currencyOptional.isPresent()) {

          int i = 19;
          for(final Denomination denomObj : currencyOptional.get().getDenominations().values()) {

            final String material = (denomObj.isItem())? ((ItemDenomination)denomObj).getMaterial() : "PAPER";

            final SwitchPageIcon switchIcon = new SwitchPageIcon(i, PluginCore.server().stackBuilder().of(material, 1)
                    .display(denomObj.singular()).lore(Collections.singletonList("Click to edit denomination")), "my_eco", DENOMINATION_EDITOR_PAGE, ActionType.ANY);

            switchIcon.addAction(new DataAction("DENOMINATION_WEIGHT", denomObj.weight()));
            callback.getPage().addIcon(switchIcon);

            i+= 2;
          }
        }
      }
    }
  }

  private void handleDenominationEditOpen(final PageOpenCallback callback) {

  }
}