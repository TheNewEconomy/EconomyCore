package net.tnemc.core.menu.page.shared;
/*
 * The New Menu Library
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
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.manager.MenuManager;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;

import java.util.Collections;
import java.util.Optional;

/**
 * MaterialSelectionMenu
 *
 * @author creatorfromhell
 * @since 1.5.0.0
 */
public class MaterialSelectionPageCallback {

  protected final String returnMenu;
  protected final String menuName;
  protected final int menuPage;
  protected final int returnPage;
  protected final String materialDataID;
  protected final String materialPageID;
  protected final int menuRows;

  public MaterialSelectionPageCallback(String materialDataID, String returnMenu, String menuName,
                                       final int menuPage, final int returnPage, String materialPageID, final int menuRows) {
    this.returnMenu = returnMenu;
    this.menuName = menuName;
    this.menuPage = menuPage;
    this.returnPage = returnPage;
    this.materialDataID = materialDataID;
    this.materialPageID = materialPageID;

    //we need a controller row and then at least one row for items.
    this.menuRows = (menuRows <= 1)? 2 : menuRows;
  }

  public void handle(final PageOpenCallback callback) {

    System.out.println("Menu Page");

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {
      System.out.println("Menu Page viewer present");

      final int page = (Integer)viewer.get().dataOrDefault(materialPageID, 1);
      final int items = (menuRows - 1) * 9;
      final int start = ((page - 1) * 9);

      final int maxPages = (TNECore.instance().helper().materials().size() / items) + (((TNECore.instance().helper().materials().size() % items) > 0)? 1 : 0);

      final int prev = (page <= 1)? maxPages : page - 1;
      final int next = (page >= maxPages)? 1 : page + 1;

      if(maxPages > 1) {

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("RED_WOOL", 1)
                .display("Previous Page")
                .lore(Collections.singletonList("Click to go to previous page.")))
                .withActions(new DataAction(materialPageID, prev), new SwitchPageAction(menuName, menuPage))
                .withSlot(0)
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GREEN_WOOL", 1)
                .display("Next Page")
                .lore(Collections.singletonList("Click to go to next page.")))
                .withActions(new DataAction(materialPageID, next), new SwitchPageAction(menuName, menuPage))
                .withSlot(8)
                .build());
      }

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("BARRIER", 1)
              .display("Escape Menu")
              .lore(Collections.singletonList("Click to exit this menu.")))
              .withActions(new SwitchPageAction(returnMenu, returnPage))
              .withSlot(4)
              .build());

      for(int i = start; i < start + items; i++) {
        if(TNECore.instance().helper().materials().size() <= i) {
          break;
        }

        final String material = TNECore.instance().helper().materials().get(i);

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(material, 1)
                .lore(Collections.singletonList("Click to select material.")))
                .withActions(new DataAction(materialDataID, material), new SwitchPageAction(returnMenu, returnPage))
                .withSlot(9 + (i - start))
                .build());
      }
    }
  }
}