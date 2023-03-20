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
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.menu.impl.shared.icons.PreviousPageIcon;
import net.tnemc.menu.core.MenuManager;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.compatibility.MenuPlayer;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.icon.action.ChatAction;
import net.tnemc.menu.core.page.impl.PlayerPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * ActionsPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ActionsPage extends PlayerPage {

  public ActionsPage() {
    super(3);
  }

  @Override
  public Map<Integer, Icon> defaultIcons(MenuPlayer player) {

    Map<Integer, Icon> icons = new HashMap<>();

    Optional<Object> curID = MenuManager.instance().getViewerData(player.identifier(), "cur_uid");
    Optional<PlayerProvider> provider = TNECore.server().findPlayer(player.identifier());

    if(provider.isPresent() && curID.isPresent()) {
      Optional<Currency> currency = TNECore.eco().currency().findCurrency(((UUID)curID.get()));

      if(currency.isPresent()) {

        icons.put(0, new PreviousPageIcon(0, 1));

        icons.put(4, IconBuilder.of(TNECore.server()
                                        .stackBuilder()
                                        .of(currency.get().getIconMaterial(), 1)
                                        .display(currency.get().getDisplay())
                                        .lore(List.of("Chose an Action!")))
            .create());


        icons.put(10, IconBuilder.of(TNECore.server()
                                         .stackBuilder()
                                         .of("ARROW", 1)
                                         .display("Send Funds")
                                         .lore(List.of("Send money to another player!")))
            .create());


        icons.put(12, IconBuilder.of(TNECore.server()
                                         .stackBuilder()
                                         .of("EMERALD", 1)
                                         .display("Convert Funds")
                                         .lore(List.of("Convert to another currency!")))
            .create());


        icons.put(14, IconBuilder.of(TNECore.server()
                                         .stackBuilder()
                                         .of("GOLD_INGOT", 1)
                                         .display("Request Funds")
                                         .lore(List.of("Request money from another player!")))
            .create());


        icons.put(16, IconBuilder.of(TNECore.server()
                                         .stackBuilder()
                                         .of("BOOK", 1)
                                         .display("Chat Test")
                                         .lore(List.of("Test Chat")))
                .click((click)->{
                  click.getPlayer().message("Enter player name!");
                })
                .withAction(new ChatAction((callback)->{
                  System.out.println(callback.getMessage());

                  final Optional<Account> account = TNECore.eco().account().findAccount(callback.getMessage());
                  if(account.isPresent()) {
                    System.out.println("Player : " + account.get().getName() + " exists!");
                    return true;
                  }

                  return false;
                }))
            .create());
      }
    }
    return icons;
  }
}
