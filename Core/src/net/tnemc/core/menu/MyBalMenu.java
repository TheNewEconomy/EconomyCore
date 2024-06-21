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
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.type.MixedType;
import net.tnemc.core.currency.type.VirtualType;
import net.tnemc.core.menu.icons.myeco.CurrencyIcon;
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
import net.tnemc.plugincore.PluginCore;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

/**
 * MyBalMenu
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MyBalMenu extends Menu {

  public static final int BALANCE_VIEW_PAGE = 2;
  public static final int BALANCE_PAY_PAGE = 3;
  public static final int BALANCE_AMOUNT_SELECTION_PAGE = 4;

  public MyBalMenu() {
    this.name = "my_bal";
    this.title = "My Bal";
    this.rows = 6;

    /*
     * Main Page
     */
    final Page main = new PageBuilder(1).withIcons(
            new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                    .display("Currencies"), this.name, BALANCE_VIEW_PAGE, ActionType.ANY)
    ).build();
    addPage(main);

    final Page balanceAmountPage = new PageBuilder(BALANCE_AMOUNT_SELECTION_PAGE).build();
    //balanceAmountPage.setOpen((open->new MyBalAmountSelectionPage("BALANCE_AMOUNT_SELECTION", name, name, CURRENCY_INFO_MAX_SELECTION_PAGE, CURRENCY_INFO_EDIT_PAGE, "BALANCE_AMOUNT_SELECTION").handle(open)));
    addPage(balanceAmountPage);
  }

  public void handleMainPage(final PageOpenCallback callback) {

    final Optional<Account> account = TNECore.eco().account().findAccount(callback.getPlayer().identifier());


    if(account.isPresent()) {
      int i = 19;
      for(final Currency curObj : TNECore.eco().currency().currencies()) {

        buildBalanceIcon(i, curObj, account.get());

        i += 2;
      }
    }
  }

  /*
   * Virtual Right click = create note
   */
  protected Icon buildBalanceIcon(final int slot, final Currency currency, final Account account) {

    final LinkedList<String> lore = new LinkedList<>();

    lore.add("Left click to pay account using currency.");

    if(currency.type() instanceof VirtualType && currency.isNotable()) {
      lore.add("Right click to create physical currency note.");
    }

    if(currency.type().supportsItems()) {
      lore.add("Right click to view balance breakdown.");
    }

    return new IconBuilder(PluginCore.server().stackBuilder().of(currency.getIconMaterial(), 1)
            .display(currency.getIdentifier()).lore(lore))
            .withSlot(slot)
            .withActions().build();
  }
}