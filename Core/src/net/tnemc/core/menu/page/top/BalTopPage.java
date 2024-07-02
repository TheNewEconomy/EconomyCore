package net.tnemc.core.menu.page.top;

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
import net.tnemc.core.menu.handlers.StringSelectionHandler;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.RunnableAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.icon.impl.StateIcon;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * EnchantmentSelectionPage
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class BalTopPage {

  protected final String returnMenu;
  protected final String menuName;
  protected final int menuPage;
  protected final int returnPage;
  protected final String pageID;
  protected final int menuRows;

  public BalTopPage(String returnMenu, String menuName,
                    final int menuPage, final int returnPage, String pageID,
                    final int menuRows) {

    this.returnMenu = returnMenu;
    this.menuName = menuName;
    this.menuPage = menuPage;
    this.returnPage = returnPage;
    this.pageID = pageID;

    //we need a controller row and then at least one row for items.
    this.menuRows = (menuRows <= 1)? 2 : menuRows;
  }

  public void handle(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final int page = (Integer)viewer.get().dataOrDefault(menuName + "_PAGE", 1);
      final int items = (menuRows - 1) * 9;
      final int start = ((page - 1) * items);
      final int maxPages = (TNECore.instance().helper().enchantments().size() / items) + (((TNECore.instance().helper().enchantments().size() % items) > 0)? 1 : 0);

      final int prev = (page <= 1)? maxPages : page - 1;
      final int next = (page >= maxPages)? 1 : page + 1;

      if(maxPages > 1) {

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("RED_WOOL", 1)
                .display("Previous Page")
                .lore(Collections.singletonList("Click to go to previous page.")))
                .withActions(new DataAction(menuName + "_PAGE", prev), new SwitchPageAction(menuName, menuPage))
                .withSlot(0)
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GREEN_WOOL", 1)
                .display("Next Page")
                .lore(Collections.singletonList("Click to go to next page.")))
                .withActions(new DataAction(menuName + "_PAGE", next), new SwitchPageAction(menuName, menuPage))
                .withSlot(8)
                .build());
      }

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("BARRIER", 1)
              .display("Escape Menu")
              .lore(Collections.singletonList("Click to exit this menu.")))
              .withActions(new SwitchPageAction(returnMenu, returnPage))
              .withSlot(3)
              .build());

      for(int i = start; i < start + items; i++) {



      }
    }
  }
}