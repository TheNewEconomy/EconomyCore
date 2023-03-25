package net.tnemc.core.menu.impl.mybal.pages;

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
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.currency.Currency;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.compatibility.MenuPlayer;
import net.tnemc.menu.core.icon.ActionType;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.icon.action.DataAction;
import net.tnemc.menu.core.icon.action.SwitchPageAction;
import net.tnemc.menu.core.page.impl.PlayerPage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

/**
 * CurrencyMainPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyMainPage extends PlayerPage {

  public CurrencyMainPage() {
    super(1);
  }

  @Override
  public Map<Integer, Icon> defaultIcons(MenuPlayer player) {

    Map<Integer, Icon> icons = new HashMap<>();

    int i = 10;

    LinkedList<String> lore = new LinkedList<>();

    for(Currency currency : TNECore.eco().currency().currencies()) {
      lore.clear();

      lore.add("Bal: " + balance(TNECore.server().findPlayer(player.identifier()).get(), currency.getIdentifier()));
      lore.add("Left Click to Perform Actions.");
      lore.add("Middle Click to convert to physical form!");
      lore.add("Right Click to View Balances.");

      icons.put(i, IconBuilder.of(TNECore.server()
                                      .stackBuilder()
                                      .of(currency.getIconMaterial(), 1)
                                      .display(currency.getIdentifier())
                                      .lore(lore))
          .withAction(new DataAction("cur_uid", currency.getUid()))
          .withAction(new SwitchPageAction(3, ActionType.LEFT_CLICK))
          .withAction(new SwitchPageAction(4, ActionType.SCROLL_CLICK))
          .withAction(new SwitchPageAction(2, ActionType.RIGHT_CLICK))
          .create());

      i += 2;
    }
    return icons;
  }

  private BigDecimal balance(PlayerProvider player, final String currency) {

    BigDecimal amount = BigDecimal.ZERO;

    final Optional<Account> account = TNECore.eco().account().findAccount(player.identifier());
    if(account.isPresent()) {
      for(HoldingsEntry entry : account.get().getHoldings(player.region(true), currency)) {
        amount = amount.add(entry.getAmount());
      }
    }
    return amount;
  }
}