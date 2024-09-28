package net.tnemc.core.menu.page.shared;

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

import net.kyori.adventure.text.Component;
import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.MessageHandler;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * CurrencySelectionPage
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class CurrencySelectionPage {

  protected final String returnMenu;
  protected final String menuName;
  protected final int menuPage;
  protected final int returnPage;
  protected final String currencyID;
  protected final String currencyPageID;
  protected final int menuRows;

  public CurrencySelectionPage(String currencyID, String returnMenu, String menuName,
                               final int menuPage, final int returnPage, String currencyPageID, final int menuRows) {

    this.returnMenu = returnMenu;
    this.menuName = menuName;
    this.menuPage = menuPage;
    this.returnPage = returnPage;
    this.currencyID = currencyID;
    this.currencyPageID = currencyPageID;

    //we need a controller row and then at least one row for items.
    this.menuRows = (menuRows <= 1)? 2 : menuRows;
  }

  public void handle(final PageOpenCallback callback) {


    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final UUID id = viewer.get().uuid();
      final int page = (Integer)viewer.get().dataOrDefault(currencyPageID, 1);
      final int items = (menuRows - 1) * 9;
      final int start = ((page - 1) * items);

      final int maxPages = (TNECore.eco().currency().currencies().size() / items) + (((TNECore.eco().currency().currencies().size() % items) > 0)? 1 : 0);

      final int prev = (page <= 1)? maxPages : page - 1;
      final int next = (page >= maxPages)? 1 : page + 1;

      if(maxPages > 1) {

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("RED_WOOL", 1)
                                                           .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.PreviousPageDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.PreviousPage"), id))))
                                           .withActions(new DataAction(currencyPageID, prev), new SwitchPageAction(menuName, menuPage))
                                           .withSlot(0)
                                           .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GREEN_WOOL", 1)
                                                           .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.NextPageDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.NextPage"), id))))
                                           .withActions(new DataAction(currencyPageID, next), new SwitchPageAction(menuName, menuPage))
                                           .withSlot(8)
                                           .build());
      }

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("BARRIER", 1)
                                                         .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.EscapeDisplay"), id))
                                                         .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Escape"), id))))
                                         .withActions(new SwitchPageAction(returnMenu, returnPage))
                                         .withSlot(4)
                                         .build());

      for(int i = start; i < start + items; i++) {
        if(TNECore.eco().currency().currencies().size() <= i) {
          break;
        }

        final Currency currency = TNECore.eco().currency().currencies().get(i);

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(currency.getIconMaterial(), 1)
                                                           .display(Component.text(currency.getIdentifier()))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Currency.Select"), id))))
                                           .withActions(new DataAction(currencyID, currency.getUid()),
                                                        new SwitchPageAction(returnMenu, returnPage))
                                           .withSlot(9 + (i - start))
                                           .build());
      }
    }
  }
}