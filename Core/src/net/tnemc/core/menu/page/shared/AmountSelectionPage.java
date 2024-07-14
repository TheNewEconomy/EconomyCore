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
import net.tnemc.core.menu.handlers.AmountSelectionHandler;
import net.tnemc.core.menu.icons.actions.PageSwitchWithClose;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.handlers.MenuClickHandler;
import net.tnemc.menu.core.icon.action.impl.ChatAction;
import net.tnemc.menu.core.icon.action.impl.RunnableAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * AmountSelectionPage
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class AmountSelectionPage {

  protected final String returnMenu;
  protected final String menuName;
  protected final int menuPage;
  protected final int returnPage;
  protected final String amtID;

  protected final Consumer<AmountSelectionHandler> selectionListener;

  public AmountSelectionPage(String amtID, String returnMenu, String menuName,
                             final int menuPage, final int returnPage, Consumer<AmountSelectionHandler> selectionListener) {
    this.returnMenu = returnMenu;
    this.menuName = menuName;
    this.menuPage = menuPage;
    this.returnPage = returnPage;
    this.amtID = amtID;
    this.selectionListener = selectionListener;
  }

  public void handle(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("BARRIER", 1)
              .display(Component.text("Escape Menu"))
              .lore(Collections.singletonList(Component.text("Click to exit this menu."))))
              .withActions(new SwitchPageAction(returnMenu, returnPage))
              .withSlot(0)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GREEN_WOOL", 1)
              .display(Component.text("Save."))
              .lore(Collections.singletonList(Component.text("Click save the amount."))))
              .withActions(new RunnableAction((click)->{
                        if(selectionListener != null) {

                          selectionListener.accept(new AmountSelectionHandler(click, ((BigDecimal)viewer.get().dataOrDefault(amtID, BigDecimal.ZERO))));
                        }
                      }), new PageSwitchWithClose(returnMenu, returnPage))
              .withSlot(8)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
              .display(Component.text("Enter your own"))
              .lore(Collections.singletonList(Component.text("Click to enter a custom amount."))))
              .withActions(new ChatAction((message)->{


                        if(message.getPlayer().viewer().isPresent() && !message.getMessage().isEmpty()) {

                          try {

                            message.getPlayer().viewer().get().addData(amtID, new BigDecimal(message.getMessage()));
                            return true;
                          } catch(NumberFormatException ignore) {}
                        }

                        message.getPlayer().message("Enter a valid decimal value:");
                        return false;

                      }), new RunnableAction((run)->run.player().message("Enter a valid decimal value:")))
              .withSlot(2)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
              .display(Component.text(((BigDecimal)viewer.get().dataOrDefault(amtID, BigDecimal.ZERO)).toPlainString()))
              .lore(Collections.singletonList(Component.text("The amount"))))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withSlot(4)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("BLACK_WOOL", 1)
              .display(Component.text("Reset To Zero"))
              .lore(Collections.singletonList(Component.text("Click to reset the amount to zero."))))
              .withClick((click)->click.player().viewer().ifPresent(menuViewer->menuViewer.addData(amtID, BigDecimal.ZERO)))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withSlot(6)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Add 100")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal("100")))
              .withSlot(15)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Add 50")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal("50")))
              .withSlot(17)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Add 10")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal("10")))
              .withSlot(33)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Add 1")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal("1")))
              .withSlot(35)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Add .10")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal(".10")))
              .withSlot(51)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Add .01")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal(".01")))
              .withSlot(53)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Remove 100")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal("-100")))
              .withSlot(11)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Remove 50")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal("-50")))
              .withSlot(9)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Remove 10")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal("-10")))
              .withSlot(29)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Remove 1")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal("-1")))
              .withSlot(27)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Remove .10")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal("-.10")))
              .withSlot(47)
              .build());

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Remove .01")))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, new BigDecimal("-.01")))
              .withSlot(45)
              .build());
    }
  }

  protected void balAddClick(final MenuClickHandler click, final BigDecimal toAdd) {

    final Optional<MenuViewer> clickViewer = click.player().viewer();
    clickViewer.ifPresent(menuViewer->addToClickData(menuViewer, toAdd));
  }

  protected void addToClickData(final MenuViewer viewer, final BigDecimal toAdd) {

    final BigDecimal amt = ((BigDecimal)viewer.dataOrDefault(amtID, BigDecimal.ZERO)).add(toAdd);

    viewer.addData(amtID, amt);
  }
}