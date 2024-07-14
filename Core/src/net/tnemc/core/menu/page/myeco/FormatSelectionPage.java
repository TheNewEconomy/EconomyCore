package net.tnemc.core.menu.page.myeco;

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
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.currency.format.FormatRule;
import net.tnemc.core.menu.handlers.StringSelectionHandler;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.handlers.MenuClickHandler;
import net.tnemc.menu.core.icon.action.impl.ChatAction;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.RunnableAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.MessageHandler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * FormatSelectionPage
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class FormatSelectionPage {

  protected final String returnMenu;
  protected final String menuName;
  protected final int menuPage;
  protected final int returnPage;
  protected final String formatID;
  protected final String formatPageID;
  protected final int menuRows;

  protected final Consumer<StringSelectionHandler> selectionListener;

  public FormatSelectionPage(String formatID, String returnMenu, String menuName,
                             final int menuPage, final int returnPage, String formatPageID,
                             final int menuRows, Consumer<StringSelectionHandler> selectionListener) {
    this.returnMenu = returnMenu;
    this.menuName = menuName;
    this.menuPage = menuPage;
    this.returnPage = returnPage;
    this.formatID = formatID;
    this.formatPageID = formatPageID;

    //we need a controller row and then at least one row for items.
    this.menuRows = (menuRows <= 1)? 2 : menuRows;
    this.selectionListener = selectionListener;
  }

  public void handle(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final UUID id = viewer.get().uuid();

      final int page = (Integer)viewer.get().dataOrDefault(formatPageID, 1);
      final int items = (menuRows - 1) * 9;
      final int start = ((page - 1) * 9);
      final int maxPages = (CurrencyFormatter.rules().size() / items) + (((CurrencyFormatter.rules().size() % items) > 0)? 1 : 0);

      final int prev = (page <= 1)? maxPages : page - 1;
      final int next = (page >= maxPages)? 1 : page + 1;
      if(maxPages > 1) {

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("RED_WOOL", 1)
                .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.PreviousPageDisplay"), id))
                .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.PreviousPage"), id))))
                .withActions(new DataAction(formatPageID, prev), new SwitchPageAction(menuName, menuPage))
                .withSlot(0)
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GREEN_WOOL", 1)
                .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.NextPageDisplay"), id))
                .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.NextPage"), id))))
                .withActions(new DataAction(formatPageID, next), new SwitchPageAction(menuName, menuPage))
                .withSlot(8)
                .build());
      }

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
              .display(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Format.Space"), id))
              .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Format.Add"), id))))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->formatAddClick(click, " "))
              .withSlot(2)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
              .display(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Format.OwnDisplay"), id))
              .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Format.Own"), id))))
              .withActions(new ChatAction((message)->{


                if(message.getPlayer().viewer().isPresent() && !message.getMessage().isEmpty()) {

                  addToClickData(message.getPlayer().viewer().get(), message.getMessage());
                  return true;
                }

                message.getPlayer().message("Enter your own text to add to the format:");
                return false;

              }), new RunnableAction((run)->run.player().message("Enter your own text to add to the format:")),
                      new SwitchPageAction(menuName, menuPage))
              .withSlot(5)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("BLACK_WOOL", 1)
              .display(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Format.ResetDisplay"), id))
              .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Format.Reset"), id))))
              .withClick((click)->click.player().viewer().ifPresent(menuViewer->menuViewer.addData(formatID, "")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withSlot(6)
              .build());

      //TODO: Fix display
      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
              .display((Component)viewer.get().dataOrDefault(formatID, MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Format.None"), id)))
              .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Format.Format"), id))))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withSlot(4)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("BARRIER", 1)
              .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.EscapeDisplay"), id))
              .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Escape"), id))))
              .withActions(new SwitchPageAction(returnMenu, returnPage))
              .withSlot(1)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
              .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Save"), id))
              .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Format.Save"), id))))
              .withActions(new RunnableAction((click)->{

                if(selectionListener != null) {

                  selectionListener.accept(new StringSelectionHandler(click, (String)viewer.get().dataOrDefault(formatID, "<symbol><major.amount><decimal><minor.amount>")));
                }
              }), new SwitchPageAction(returnMenu, returnPage))
              .withSlot(7)
              .build());


      final String[] stringSet = CurrencyFormatter.rules().keySet().toArray(new String[CurrencyFormatter.rules().size()]);

      final LinkedList<Component> lore = new LinkedList<>();
      lore.add(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Format.Add"), id));
      lore.add(Component.text("Placeholder"));

      int slot = 9;
      for(int i = start; i < start + items; i++) {
        if(stringSet.length <= i) {
          break;
        }

        final FormatRule rule = CurrencyFormatter.rules().get(stringSet[i]);

        if(!rule.includeInMenu()) {
          continue;
        }

        //reset lore to include description for each rule.
        lore.set(1, Component.text(rule.description()));

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                .display(Component.text(rule.name()))
                .lore(lore))
                .withActions(new SwitchPageAction(menuName, menuPage))
                .withClick((click)->formatAddClick(click, rule.name()))
                .withSlot(slot)
                .build());

        slot++;
      }
    }
  }

  private void formatAddClick(final MenuClickHandler click, final String toAdd) {

    final Optional<MenuViewer> clickViewer = click.player().viewer();
    clickViewer.ifPresent(menuViewer->addToClickData(menuViewer, toAdd));
  }

  protected void addToClickData(final MenuViewer viewer, final String toAdd) {

    final String formatStr = (String)viewer.dataOrDefault(formatID, "");

    viewer.addData(formatID, formatStr + toAdd);
  }
}
