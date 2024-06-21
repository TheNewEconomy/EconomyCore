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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.type.MixedType;
import net.tnemc.core.currency.type.VirtualType;
import net.tnemc.core.menu.icons.myeco.CurrencyIcon;
import net.tnemc.core.menu.icons.shared.PreviousPageIcon;
import net.tnemc.core.menu.icons.shared.SwitchPageIcon;
import net.tnemc.core.menu.page.mybal.MyBalAmountSelectionPage;
import net.tnemc.core.menu.page.shared.AmountSelectionPage;
import net.tnemc.menu.core.Menu;
import net.tnemc.menu.core.Page;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.builder.PageBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.icon.action.ActionType;
import net.tnemc.menu.core.icon.action.IconAction;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

/**
 * MyBalMenu
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MyBalMenu extends Menu {

  public static final int BALANCE_ACTIONS_PAGE = 2;
  public static final int BALANCE_BREAKDOWN_PAGE = 3;
  public static final int BALANCE_PAY_PAGE = 4;
  public static final int BALANCE_AMOUNT_SELECTION_PAGE = 5;
  public static final int BALANCE_NOTE_AMOUNT_PAGE = 6;

  public MyBalMenu() {
    this.name = "my_bal";
    this.title = "My Bal";
    this.rows = 6;

    /*
     * Main Page
     */
    final Page main = new PageBuilder(1).build();
    main.setOpen(this::handleMainPage);
    addPage(main);

    final Page balanceAmountPage = new PageBuilder(BALANCE_AMOUNT_SELECTION_PAGE).build();
    balanceAmountPage.setOpen((open->new MyBalAmountSelectionPage("BALANCE_AMOUNT_SELECTION", this.name, this.name, BALANCE_AMOUNT_SELECTION_PAGE, 1, "BALANCE_AMOUNT_TOTAL_SELECTION").handle(open)));
    addPage(balanceAmountPage);

    final Page noteAmountPage = new PageBuilder(BALANCE_NOTE_AMOUNT_PAGE).build();
    noteAmountPage.setOpen((open->new MyBalAmountSelectionPage("BALANCE_NOTE_AMOUNT", this.name, this.name, BALANCE_NOTE_AMOUNT_PAGE, 1, "BALANCE_AMOUNT_TOTAL_SELECTION").handle(open)));
    addPage(noteAmountPage);

    final Page balanceActionsPage = new PageBuilder(BALANCE_ACTIONS_PAGE).build();
    balanceActionsPage.addIcon(new PreviousPageIcon(0, this.name, 1, ActionType.ANY));
    actionsPage(balanceActionsPage);
    addPage(balanceActionsPage);

    final Page balanceBreakdownPage = new PageBuilder(BALANCE_BREAKDOWN_PAGE).build();
    balanceBreakdownPage.addIcon(new PreviousPageIcon(0, this.name, 1, ActionType.ANY));
    balanceBreakdownPage.setOpen(this::handleBreakdownPage);
    addPage(balanceBreakdownPage);

    final Page balancePayPage = new PageBuilder(BALANCE_PAY_PAGE).build();
    balanceBreakdownPage.addIcon(new PreviousPageIcon(0, this.name, 1, ActionType.ANY));
    //balancePayPage.setOpen(((PageOpenCallback open)->new MyBalPayPage(this.name, this.name, BALANCE_PAY_PAGE, 1).handle(open)));
    addPage(balancePayPage);
  }

  public void handleMainPage(final PageOpenCallback callback) {

    final Optional<Account> account = TNECore.eco().account().findAccount(callback.getPlayer().identifier());

    if(account.isPresent()) {
      int i = 10;
      for(final Currency curObj : TNECore.eco().currency().currencies()) {

        callback.getPage().addIcon(buildBalanceIcon(i, curObj, account.get()));

        i += 2;
      }
    }
  }

  protected void actionsPage(final Page page) {

  }

  protected void handleBreakdownPage(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final Optional<Account> account = TNECore.eco().account().findAccount(callback.getPlayer().identifier());
      if(account.isPresent()) {

        final Optional<Object> currencyUUID = viewer.get().findData("ACTION_CURRENCY");
        if(currencyUUID.isPresent()) {

          final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency((UUID)currencyUUID.get());
          if(currencyOptional.isPresent()) {

            int i = 10;
            for(final HoldingsEntry entry : account.get().getHoldings(TNECore.eco().region().defaultRegion(), (UUID)currencyUUID.get())) {

              callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                      .display(entry.getHandler().id())).build());
              i += 2;
            }
          }
        }
      }
    }
  }

  /*
   * Virtual Right click = create note
   */
  protected Icon buildBalanceIcon(final int slot, final Currency currency, final Account account) {

    final LinkedList<String> lore = new LinkedList<>();
    final LinkedList<IconAction> actions = new LinkedList<>();

    actions.add(new DataAction("ACTION_CURRENCY", currency.getUid()));
    actions.add(new DataAction("BALANCE_AMOUNT_TOTAL_SELECTION", account.getHoldingsTotal(TNECore.eco().region().defaultRegion(), currency.getUid())));

    lore.add("Left click to pay account using currency.");
    actions.add(new SwitchPageAction(this.name, BALANCE_PAY_PAGE, ActionType.LEFT_CLICK));

    //Drop Action(Other Currency Actions)
    lore.add("Press Q for other actions using currency.");
    actions.add(new SwitchPageAction(this.name, BALANCE_ACTIONS_PAGE, ActionType.DROP));

    if(currency.type() instanceof VirtualType && currency.isNotable()) {
      lore.add("Right click to create physical currency note.");
      actions.add(new SwitchPageAction(this.name, BALANCE_NOTE_AMOUNT_PAGE, ActionType.RIGHT_CLICK));
    }

    if(currency.type().supportsItems()) {
      lore.add("Right click to view balance breakdown.");
      actions.add(new SwitchPageAction(this.name, BALANCE_BREAKDOWN_PAGE, ActionType.RIGHT_CLICK));
    }

    return new IconBuilder(PluginCore.server().stackBuilder().of(currency.getIconMaterial(), 1)
            .display(currency.getIdentifier()).lore(lore))
            .withSlot(slot)
            .withActions(actions.toArray(new IconAction[actions.size()])).build();
  }
}