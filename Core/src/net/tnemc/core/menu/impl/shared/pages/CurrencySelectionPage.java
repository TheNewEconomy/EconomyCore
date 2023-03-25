package net.tnemc.core.menu.impl.shared.pages;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.menu.impl.shared.icons.PreviousPageIcon;
import net.tnemc.menu.core.MenuManager;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.compatibility.MenuPlayer;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.icon.action.DataAction;
import net.tnemc.menu.core.icon.action.SwitchPageAction;
import net.tnemc.menu.core.page.impl.PlayerPage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * CurrencySelectionPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencySelectionPage extends PlayerPage {

  private final String menu;
  private final int nextPage;

  public CurrencySelectionPage(int id, final String menu, final int nextPage) {
    super(id);
    this.menu = menu;
    this.nextPage = nextPage;
  }

  @Override
  public Map<Integer, Icon> defaultIcons(MenuPlayer player) {

    final Optional<Object> curID = MenuManager.instance().getViewerData(player.identifier(), "cur_uid");

    final Map<Integer, Icon> icons = new HashMap<>();
    final Optional<Object> prevPage = MenuManager.instance().getViewerData(player.identifier(), "prev_page");

    prevPage.ifPresent(o->icons.put(0, new PreviousPageIcon(0, (Integer)o)));

    int i = 10;

    if(curID.isPresent()) {
      for(Currency currency : TNECore.eco().currency().currencies()) {

        if(currency.getUid().equals(curID.get())) {
          continue;
        }

        icons.put(i, IconBuilder.of(TNECore.server()
                                        .stackBuilder()
                                        .of(currency.getIconMaterial(), 1)
                                        .display(currency.getIdentifier()))
            .withAction(new DataAction("selected_cur", currency.getUid()))
            .withAction(new DataAction("prev_page", this.getId()))
            .withAction(new SwitchPageAction(nextPage))
            .create());
        i++;
      }
    }
    return icons;
  }
}