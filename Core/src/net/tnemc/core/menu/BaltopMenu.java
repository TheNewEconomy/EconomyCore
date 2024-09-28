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

import net.kyori.adventure.text.Component;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.history.SortedHistory;
import net.tnemc.menu.core.Menu;
import net.tnemc.menu.core.Page;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.builder.PageBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.MessageHandler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * BaltopMenu
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BaltopMenu extends Menu {

  public static final String TOP_PAGE_ID = "TOP_PAGE";
  public static final int TOP_COUNT = 10;

  public BaltopMenu() {

    this.name = "baltop_menu";
    this.title = "Balance Top";
    this.rows = 3;

    /*
     * Main Page
     */
    final Page main = new PageBuilder(1).build();
    main.setOpen(this::handleMainPage);
    addPage(main);
  }

  public void handleMainPage(final PageOpenCallback callback) {

    final UUID id = callback.getPlayer().identifier();
    final Optional<Account> account = TNECore.eco().account().findAccount(callback.getPlayer().identifier());

    if(account.isPresent()) {

      final SortedHistory sorted = account.get().getSorted(account.get().getIdentifier());

      final int pagesPer = TOP_COUNT / 5;
      final int pageDiv = (sorted.maxPages() / pagesPer);
      final int maxPages = (sorted.maxPages() % pagesPer > 0)? pageDiv + 1 : pageDiv;

      final int page = (callback.getPlayer().viewer().isPresent())? (Integer)callback.getPlayer().viewer().get().dataOrDefault(TOP_PAGE_ID, 1) : 1;

      final int prev = (page <= 1)? maxPages : page - 1;
      final int next = (page >= maxPages)? 1 : page + 1;

      if(maxPages > 1) {

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("RED_WOOL", 1)
                                                           .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.PreviousPageDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.PreviousPage"), id))))
                                           .withActions(new DataAction(TOP_PAGE_ID, prev), new SwitchPageAction(this.name, 1))
                                           .withSlot(0)
                                           .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GREEN_WOOL", 1)
                                                           .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.NextPageDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.NextPage"), id))))
                                           .withActions(new DataAction(TOP_PAGE_ID, next), new SwitchPageAction(this.name, 1))
                                           .withSlot(8)
                                           .build());
      }

      int slot = 9;
      for(int i = 1; i <= 2; i++) {

        final int adjustedPage = i + (page - 1);
        if(adjustedPage > sorted.maxPages()) {
          break;
        }

        for(final Map.Entry<Long, UUID> entry : sorted.getPage(adjustedPage).entrySet()) {

          final Optional<Receipt> receipt = account.get().findReceipt(entry.getValue());
          if(receipt.isEmpty()) {
            continue;
          }
          callback.getPage().addIcon(buildTransactionIcon(slot, receipt.get()));
        }

        slot += 2;
      }
    }
  }

  protected Icon buildTransactionIcon(final int slot, final Receipt receipt) {

    String from = "None";
    final Optional<Account> fromACC = receipt.getFrom().asAccount();
    if(fromACC.isPresent()) {
      from = fromACC.get().getName();
    }

    String to = "None";
    final Optional<Account> toACC = receipt.getTo().asAccount();
    if(toACC.isPresent()) {
      to = toACC.get().getName();
    }

    final LinkedList<Component> lore = new LinkedList<>();
    lore.add(Component.text("Type: " + receipt.getType()));
    lore.add(Component.text("To: " + to));
    lore.add(Component.text("From: " + from));

    return new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                   .display(Component.text(receipt.getId().toString())).lore(lore))
            .withSlot(slot).build();
  }
}