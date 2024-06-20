package net.tnemc.core.menu.page.mybal;
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

import net.tnemc.core.menu.page.shared.AmountSelectionPage;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.plugincore.PluginCore;

import java.math.BigDecimal;
import java.util.Collections;

/**
 * MyBalAmountSelectionPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MyBalAmountSelectionPage extends AmountSelectionPage {

  protected final String maxAMTID;

  public MyBalAmountSelectionPage(String amtID, String returnMenu, String menuName, int menuPage, int returnPage, String maxAMTID) {
    super(amtID, returnMenu, menuName, menuPage, returnPage);

    this.maxAMTID = maxAMTID;
  }

  @Override
  public void handle(PageOpenCallback callback) {
    super.handle(callback);

    if(callback.getPlayer().viewer().isPresent()) {

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display("Add Max")
              .lore(Collections.singletonList("Adds your entire balance.")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->formatAddClick(click, ((BigDecimal)callback.getPlayer().viewer().get().dataOrDefault(maxAMTID, BigDecimal.ZERO))))
              .withSlot(22)
              .build());
    }
  }
}
