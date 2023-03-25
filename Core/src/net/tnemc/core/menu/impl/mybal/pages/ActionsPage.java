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
import net.tnemc.core.menu.impl.mybal.consumer.ConvertConfirmation;
import net.tnemc.core.menu.impl.mybal.consumer.RequestConfirmation;
import net.tnemc.core.menu.impl.mybal.consumer.SendConfirmation;
import net.tnemc.core.menu.impl.shared.icons.PreviousPageIcon;
import net.tnemc.menu.core.MenuManager;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.compatibility.MenuPlayer;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.icon.action.ChatAction;
import net.tnemc.menu.core.icon.action.DataAction;
import net.tnemc.menu.core.icon.action.SwitchPageAction;
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

    final Map<Integer, Icon> icons = new HashMap<>();

    Optional<Object> curID = MenuManager.instance().getViewerData(player.identifier(), "cur_uid");
    Optional<PlayerProvider> provider = TNECore.server().findPlayer(player.identifier());

    if(provider.isPresent() && curID.isPresent()) {
      Optional<Currency> currency = TNECore.eco().currency().findCurrency(((UUID)curID.get()));

      if(currency.isPresent()) {

        MenuManager.instance().setViewerData(player.identifier(), "my_bal",
                                             "prev_page", 3);

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
                                         .lore(List.of("Send money to another account!")))
            .click((click)->{
              click.getPlayer().message("Enter the name of the account you'd like to send funds to!" +
                                            "Or type \"exit\" to quit.");
            })
            .withAction(new ChatAction((callback)->{
              System.out.println(callback.getMessage());
              if(callback.getMessage().equalsIgnoreCase("exit")) {
                player.inventory().close();
                return true;
              }

              final Optional<Account> account = TNECore.eco().account().findAccount(callback.getMessage());
              if(account.isPresent()) {

                MenuManager.instance().setViewerData(player.identifier(), "my_bal",
                                                     "confirm", new SendConfirmation());

                MenuManager.instance().setViewerData(player.identifier(), "my_bal",
                                                     "target", account.get().getIdentifier());
                callback.getPlayer().inventory().openMenu(player, "my_bal", 4);
                return true;
              } else {
                //TODO: Account doesn't exist.
              }

              return false;
            }))
            .create());


        icons.put(12, IconBuilder.of(TNECore.server()
                                         .stackBuilder()
                                         .of("EMERALD", 1)
                                         .display("Convert Funds")
                                         .lore(List.of("Convert to another currency!")))
            .withAction(new DataAction("prev_page", 3))
                .withAction(new DataAction("confirm", new ConvertConfirmation()))
            .withAction(new SwitchPageAction(5))
            .create());


        icons.put(14, IconBuilder.of(TNECore.server()
                                         .stackBuilder()
                                         .of("GOLD_INGOT", 1)
                                         .display("Request Funds")
                                         .lore(List.of("Request money from another account!")))
            .click((click)->{
              click.getPlayer().message("Enter the name of the account you'd like to request funds from!" +
                                            "Or type \"exit\" to quit.");
            })
            .withAction(new ChatAction((callback)->{
              System.out.println(callback.getMessage());
              if(callback.getMessage().equalsIgnoreCase("exit")) {
                player.inventory().close();
                return true;
              }

              final Optional<Account> account = TNECore.eco().account().findAccount(callback.getMessage());
              if(account.isPresent()) {
                System.out.println("Player : " + account.get().getName() + " exists!");

                MenuManager.instance().setViewerData(player.identifier(), "my_bal",
                                                     "confirm", new RequestConfirmation());
                callback.getPlayer().inventory().openMenu(player, "my_bal", 4);
                return true;
              } else {
                //TODO: Account doesn't exist.
              }

              return false;
            }))
            .create());
      }
    }
    return icons;
  }
}