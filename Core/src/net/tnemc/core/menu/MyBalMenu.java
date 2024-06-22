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
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.currency.type.VirtualType;
import net.tnemc.core.menu.icons.shared.PreviousPageIcon;
import net.tnemc.core.menu.page.mybal.MyBalAmountSelectionPage;
import net.tnemc.core.menu.page.shared.AccountSelectionPage;
import net.tnemc.core.menu.page.shared.CurrencySelectionPage;
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
  public static final int BALANCE_ACTION_CONVERT_CURRENCY_PAGE = 3;
  public static final int BALANCE_ACTION_CONVERT_AMOUNT_PAGE = 4;
  public static final int BALANCE_ACTION_DEPOSIT_AMOUNT_PAGE = 5;
  public static final int BALANCE_ACTION_WITHDRAW_AMOUNT_PAGE = 6;
  public static final int BALANCE_BREAKDOWN_PAGE = 7;
  public static final int BALANCE_PAY_PAGE = 8;
  public static final int BALANCE_PAY_AMOUNT_PAGE = 9;
  public static final int BALANCE_NOTE_AMOUNT_PAGE = 10;

  public static final String ACTION_ACCOUNT_ID = "ACTION_ACCOUNT";
  public static final String ACTION_CURRENCY = "ACTION_CURRENCY";
  public static final String ACTION_MAX_HOLDINGS = "ACTION_MAX_HOLDINGS";
  public static final String ACTION_HOLDINGS = "ACTION_HOLDINGS";

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

    final Page balanceActionsPage = new PageBuilder(BALANCE_ACTIONS_PAGE).build();
    balanceActionsPage.addIcon(new PreviousPageIcon(0, this.name, 1, ActionType.ANY));
    balanceActionsPage.setOpen(this::actionsPage);
    addPage(balanceActionsPage);

    final Page balanceConvertCurrencyPage = new PageBuilder(BALANCE_ACTION_CONVERT_CURRENCY_PAGE).build();
    balanceConvertCurrencyPage.setOpen((open->new CurrencySelectionPage("CONVERT_CURRENCY", this.name, this.name, BALANCE_ACTION_CONVERT_CURRENCY_PAGE, BALANCE_ACTION_CONVERT_AMOUNT_PAGE, "CONVERT_CURRENCY_SELECTION", this.rows).handle(open)));
    addPage(balanceConvertCurrencyPage);

    final Page balanceConvertAmountPage = new PageBuilder(BALANCE_ACTION_CONVERT_AMOUNT_PAGE).build();
    balanceConvertAmountPage.setOpen((open->new MyBalAmountSelectionPage("BALANCE_CONVERT_SELECTION", this.name, this.name, BALANCE_ACTION_CONVERT_AMOUNT_PAGE, 1).handle(open)));
    addPage(balanceConvertAmountPage);

    final Page balanceDepositAmountPage = new PageBuilder(BALANCE_ACTION_DEPOSIT_AMOUNT_PAGE).build();
    balanceDepositAmountPage.setOpen((open->new MyBalAmountSelectionPage("BALANCE_AMOUNT_DEPOSIT", this.name, this.name, BALANCE_ACTION_DEPOSIT_AMOUNT_PAGE, 1).handle(open)));
    addPage(balanceDepositAmountPage);

    final Page balanceWithdrawAmountPage = new PageBuilder(BALANCE_ACTION_WITHDRAW_AMOUNT_PAGE).build();
    balanceWithdrawAmountPage.setOpen((open->new MyBalAmountSelectionPage("BALANCE_AMOUNT_WITHDRAW", this.name, this.name, BALANCE_ACTION_WITHDRAW_AMOUNT_PAGE, 1).handle(open)));
    addPage(balanceWithdrawAmountPage);

    final Page balanceBreakdownPage = new PageBuilder(BALANCE_BREAKDOWN_PAGE).build();
    balanceBreakdownPage.addIcon(new PreviousPageIcon(0, this.name, 1, ActionType.ANY));
    balanceBreakdownPage.setOpen(this::handleBreakdownPage);
    addPage(balanceBreakdownPage);

    final Page balancePayPage = new PageBuilder(BALANCE_PAY_PAGE).build();
    balancePayPage.addIcon(new PreviousPageIcon(0, this.name, 1, ActionType.ANY));
    balancePayPage.setOpen((open->new AccountSelectionPage(ACTION_ACCOUNT_ID, this.name, this.name, BALANCE_PAY_PAGE, BALANCE_PAY_AMOUNT_PAGE, "PAY_ACCOUNT_NAME_SELECTION", this.rows).handle(open)));
    addPage(balancePayPage);

    final Page balancePayAmountPage = new PageBuilder(BALANCE_PAY_AMOUNT_PAGE).build();
    balancePayAmountPage.setOpen((open->new MyBalAmountSelectionPage("BALANCE_PAY_AMOUNT", this.name, this.name, BALANCE_PAY_AMOUNT_PAGE, 1).handle(open)));
    addPage(balancePayAmountPage);

    final Page noteAmountPage = new PageBuilder(BALANCE_NOTE_AMOUNT_PAGE).build();
    noteAmountPage.setOpen((open->new MyBalAmountSelectionPage("BALANCE_NOTE_AMOUNT", this.name, this.name, BALANCE_NOTE_AMOUNT_PAGE, 1).handle(open)));
    addPage(noteAmountPage);
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

  protected void actionsPage(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final Optional<Account> account = TNECore.eco().account().findAccount(callback.getPlayer().identifier());
      if(account.isPresent()) {

        final Optional<Object> currencyUUID = viewer.get().findData("ACTION_CURRENCY");
        if(currencyUUID.isPresent()) {

          final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency((UUID)currencyUUID.get());
          if(currencyOptional.isPresent()) {

            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                    .display("Convert Currency")
                    .lore(Collections.singletonList("Click to convert this currency to another.")))
                    .withSlot(10)
                    .withActions(new SwitchPageAction(this.name, BALANCE_ACTION_CONVERT_CURRENCY_PAGE))
                    .build());

            if(currencyOptional.get().type().supportsExchange()) {

              callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                      .display("Deposit Currency")
                      .lore(Collections.singletonList("Click to deposit some physical currency to your virtual balance.")))
                      .withSlot(12)
                      .withActions(new SwitchPageAction(this.name, BALANCE_ACTION_DEPOSIT_AMOUNT_PAGE))
                      .build());

              callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                      .display("Withdraw Currency")
                      .lore(Collections.singletonList("Click to withdraw some physical currency from your virtual balance.")))
                      .withSlot(14)
                      .withActions(new SwitchPageAction(this.name, BALANCE_ACTION_WITHDRAW_AMOUNT_PAGE))
                      .build());
            }
          }
        }
      }
    }
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
                      .display(entry.getHandler().id())
                      .lore(Collections.singletonList("Balance: " + entry.getAmount())))
                      .withSlot(i).build());
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

    actions.add(new DataAction(ACTION_CURRENCY, currency.getUid()));
    actions.add(new DataAction(ACTION_MAX_HOLDINGS, account.getHoldingsTotal(TNECore.eco().region().defaultRegion(), currency.getUid())));

    lore.add("Balance: " + CurrencyFormatter.format(account, account.getHoldingsTotal(TNECore.eco().region().defaultRegion(), currency.getUid())));

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