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
import net.tnemc.menu.core.Menu;
import net.tnemc.menu.core.Page;
import net.tnemc.menu.core.builder.PageBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;

import java.util.Optional;

/**
 * BaltopMenu
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BaltopMenu extends Menu {

  public BaltopMenu() {
    this.name = "bal_top";
    this.title = "Bal Top";
    this.rows = 6;

    /*
     * Main Page
     */
    final Page main = new PageBuilder(1).build();
    main.setOpen(this::handleMainPage);
    addPage(main);
  }

  public void handleMainPage(final PageOpenCallback callback) {

    final Optional<Account> account = TNECore.eco().account().findAccount(callback.getPlayer().identifier());

    if(account.isPresent()) {
      int i = 10;
      for(final Currency curObj : TNECore.eco().currency().currencies()) {

        //callback.getPage().addIcon(buildBalanceIcon(i, curObj, account.get()));

        i += 2;
      }
    }
  }
}