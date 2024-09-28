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
import net.tnemc.core.menu.handlers.StringSelectionHandler;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.RunnableAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.icon.impl.StateIcon;
import net.tnemc.menu.core.manager.MenuManager;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.MessageHandler;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * EnchantmentSelectionPage
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class EnchantmentSelectionPage {

  protected final String returnMenu;
  protected final String menuName;
  protected final int menuPage;
  protected final int returnPage;
  protected final String enchantsID;
  protected final String enchantPageID;
  protected final int menuRows;

  protected final Consumer<StringSelectionHandler> selectionListener;

  public EnchantmentSelectionPage(String enchantsID, String returnMenu, String menuName,
                                  final int menuPage, final int returnPage, String enchantPageID,
                                  final int menuRows, Consumer<StringSelectionHandler> selectionListener) {

    this.enchantsID = enchantsID;
    this.returnMenu = returnMenu;
    this.menuName = menuName;
    this.menuPage = menuPage;
    this.returnPage = returnPage;
    this.enchantPageID = enchantPageID;

    //we need a controller row and then at least one row for items.
    this.menuRows = (menuRows <= 1)? 2 : menuRows;
    this.selectionListener = selectionListener;
  }

  public void handle(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final UUID id = viewer.get().uuid();
      final int page = (Integer)viewer.get().dataOrDefault(menuName + "_ENCHANTMENT_SELECTION_PAGE", 1);
      final int items = (menuRows - 1) * 9;
      final int start = ((page - 1) * items);
      final int maxPages = (MenuManager.instance().getHelper().enchantments().size() / items) + (((MenuManager.instance().getHelper().enchantments().size() % items) > 0)? 1 : 0);

      final int prev = (page <= 1)? maxPages : page - 1;
      final int next = (page >= maxPages)? 1 : page + 1;

      if(maxPages > 1) {

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("RED_WOOL", 1)
                                                           .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.PreviousPageDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.PreviousPage"), id))))
                                           .withActions(new DataAction(menuName + "_ENCHANTMENT_SELECTION_PAGE", prev), new SwitchPageAction(menuName, menuPage))
                                           .withSlot(0)
                                           .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GREEN_WOOL", 1)
                                                           .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.NextPageDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.NextPage"), id))))
                                           .withActions(new DataAction(menuName + "_ENCHANTMENT_SELECTION_PAGE", next), new SwitchPageAction(menuName, menuPage))
                                           .withSlot(8)
                                           .build());
      }

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("BARRIER", 1)
                                                         .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.EscapeDisplay"), id))
                                                         .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Escape"), id))))
                                         .withActions(new SwitchPageAction(returnMenu, returnPage))
                                         .withSlot(3)
                                         .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
                                                         .display(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Save"), id))
                                                         .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Enchants.Save"), id))))
                                         .withActions(new RunnableAction((click)->{

                                           if(selectionListener != null) {

                                             final StringBuilder builder = new StringBuilder();
                                             for(final String enchant : MenuManager.instance().getHelper().enchantments()) {

                                               final String value = (String)viewer.get().dataOrDefault(menuName + "_" + enchant, "Disabled");
                                               final boolean enabled = value.equalsIgnoreCase("enabled");
                                               if(enabled) {

                                                 if(!builder.isEmpty()) {
                                                   builder.append(",");
                                                 }
                                                 builder.append(enchant);
                                               }
                                             }
                                             selectionListener.accept(new StringSelectionHandler(click, builder.toString()));
                                           }
                                         }), new SwitchPageAction(returnMenu, returnPage))
                                         .withSlot(6)
                                         .build());

      for(int i = start; i < start + items; i++) {
        if(MenuManager.instance().getHelper().enchantments().size() <= i) {
          break;
        }

        final String enchantment = MenuManager.instance().getHelper().enchantments().get(i);

        final AbstractItemStack<?> disabledStack = PluginCore.server().stackBuilder().display(Component.text(enchantment + "(Disabled)")).of("RED_WOOL", 1);
        final AbstractItemStack<?> enabledStack = PluginCore.server().stackBuilder().display(Component.text(enchantment + "(Enabled)")).of("GREEN_WOOL", 1);

        //ender chest icon
        final StateIcon enchant = new StateIcon(disabledStack, null, menuName + "_" + enchantment, "DISABLED", (currentState)->{
          if(currentState.toUpperCase(Locale.ROOT).equals("ENABLED")) {
            return "DISABLED";
          }
          return "ENABLED";
        });
        enchant.setSlot(9 + (i - start));
        enchant.addState("DISABLED", disabledStack.lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Enchants.Add"), id))));
        enchant.addState("ENABLED", enabledStack.lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Enchants.Remove"), id))));
        callback.getPage().addIcon(enchant);
      }
    }
  }
}