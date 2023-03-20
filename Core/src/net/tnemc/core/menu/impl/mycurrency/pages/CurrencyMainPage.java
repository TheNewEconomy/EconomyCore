package net.tnemc.core.menu.impl.mycurrency.pages;

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
import net.tnemc.core.menu.impl.shared.icons.PreviousMenuIcon;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.icon.ActionType;
import net.tnemc.menu.core.icon.action.ChatAction;
import net.tnemc.menu.core.icon.action.DataAction;
import net.tnemc.menu.core.icon.action.SwitchPageAction;
import net.tnemc.menu.core.page.Page;

import java.util.Arrays;

/**
 * CurrencyMainPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyMainPage extends Page {

  public CurrencyMainPage() {
    super(1);

    int i = 10;

    for(Currency currency : TNECore.eco().currency().currencies()) {

      icons.put(0, new PreviousMenuIcon(0, "my_eco"));

      icons.put(i, IconBuilder.of(TNECore.server()
                                       .stackBuilder()
                                       .of(currency.getIconMaterial(), 1)
                                       .display(currency.getIdentifier())
                                       .lore(Arrays.asList("Left Click to View",
                                                           "Middle Click to Delete",
                                                           "Right Click to Edit"
                                       )))
          .click((click)->{
                if(click.getType().equals(ActionType.SCROLL_CLICK)) {
                  click.getPlayer().message("Attempting to delete currency: "
                                                + currency.getIdentifier() + ". Type \"confirm\" to "
                                                + "delete, or anything else to not delete.");
                }
              })
          .withAction(new DataAction("cur_uid", currency.getUid()))
          .withAction(new SwitchPageAction(2, ActionType.LEFT_CLICK)) //view menu
          .withAction(new SwitchPageAction(3, ActionType.RIGHT_CLICK)) //edit menu
          .withAction(new ChatAction((callback)->{
                if(callback.getMessage().equalsIgnoreCase("confirm")) {
                  System.out.println("Delete currency.");
                  if(TNECore.eco().currency().currencies().size() > 1) {
                    //TODO: remove from currency manager and delete files.
                  } else {
                    //TODO: Cannot delete only currency.
                  }
                }
                return true;
              }, ActionType.SCROLL_CLICK))
          .create());

      i += 2;
    }
  }
}