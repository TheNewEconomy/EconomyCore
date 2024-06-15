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
  protected final int returnPage;
  protected final String materialDataID;
  protected final int menuRows;

  public MaterialSelectionPageCallback(String materialDataID, String returnMenu, final int returnPage, final int menuRows) {
    this.returnMenu = returnMenu;
    this.returnPage = returnPage;
    this.materialDataID = materialDataID;

    //we need a controller row and then at least one row for items.
    this.menuRows = (menuRows <= 1)? 2 : menuRows;
  }

  public void handle(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final int page = (Integer)viewer.get().dataOrDefault("MATERIAL_SELECTION_PAGE", 1);
      final int items = (menuRows - 1) * 9;
      final int start = ((page - 1) * 9);
      final int maxPages = (MenuManager.instance().getHelper().materials().size() / items) + (((MenuManager.instance().getHelper().materials().size() % items) > 0)? 1 : 0);

      final int prev = (page <= 0)? maxPages : page - 1;
      final int next = (page >= maxPages)? 1 : page + 1;


      for(int i = start; i < start + items; i++) {

        final String material = MenuManager.instance().getHelper().materials().get(i);

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(material, 1)
                .lore(Collections.singletonList("Click to select material.")))
                .withActions(new SwitchPageAction(returnMenu, returnPage),
                        new DataAction(materialDataID, material))
                .build());
      }
    }
  }
}