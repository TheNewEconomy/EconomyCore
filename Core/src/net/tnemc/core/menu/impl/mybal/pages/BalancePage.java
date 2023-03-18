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
import net.tnemc.core.account.holdings.HoldingsType;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.currency.Currency;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.compatibility.MenuPlayer;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.page.impl.PlayerPage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * BalancePage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BalancePage extends PlayerPage {

  public BalancePage() {
    super(1);
  }

  @Override
  public Map<Integer, Icon> defaultIcons(MenuPlayer player) {

    Map<Integer, Icon> icons = new HashMap<>();

    //Optional<Object> curID = MenuManager.instance().getViewerData(player.identifier(), "currency");
    Optional<PlayerProvider> provider = TNECore.server().findPlayer(player.identifier());

    if(provider.isPresent()) {
      Optional<Currency> currency = TNECore.eco().currency().findCurrency("USD");

      if(currency.isPresent()) {

        icons.put(4, IconBuilder.of(TNECore.server()
                                        .stackBuilder()
                                        .of(currency.get().getIconMaterial(), 1)
                                        .display(currency.get().getDisplay())
                                        .lore(List.of("Combined Balance: " + balance(provider.get(), currency.get().getIdentifier()).toString()))).create());

        //balance check
        int i = 10;

        final Optional<Account> account = TNECore.eco().account().findAccount(player.identifier());
        if(account.isPresent()) {
          for(HoldingsEntry entry : account.get().getHoldings(provider.get().getRegion(true), currency.get().getIdentifier())) {

            String item = "PAPER";
            if(entry.getType().equals(HoldingsType.E_CHEST)) {
              item = "ENDER_CHEST";
            } else if(entry.getType().equals(HoldingsType.INVENTORY_ONLY)) {
              item = currency.get().getIconMaterial();
            }

            icons.put(i, IconBuilder.of(TNECore.server()
                                            .stackBuilder()
                                            .of(item, 1)
                                            .display(entry.getType().getIdentifier())
                                            .lore(List.of("Balance: " + entry.getAmount().toString()))).create());

            i += 3;
          }
        }

      }
    }

    return icons;
  }

  private BigDecimal balance(PlayerProvider player, final String currency) {

    BigDecimal amount = BigDecimal.ZERO;

    final Optional<Account> account = TNECore.eco().account().findAccount(player.identifier());
    if(account.isPresent()) {
      for(HoldingsEntry entry : account.get().getHoldings(player.getRegion(true), currency)) {
        amount = amount.add(entry.getAmount());
      }
    }
    return amount;
  }
}
