package net.tnemc.core.menu.impl.shared.pages;
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
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.menu.impl.shared.consumer.AmountConfirmation;
import net.tnemc.menu.core.MenuManager;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.compatibility.MenuPlayer;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.icon.action.ChatAction;
import net.tnemc.menu.core.icon.action.SwitchPageAction;
import net.tnemc.menu.core.page.impl.PlayerPage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * AmountSelectionPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AmountSelectionPage extends PlayerPage {

  private final String menu;

  private final int[] minorAdd = new int[] {
      42, 43, 44, 51, 52, 53
  };

  private final int[] majorAdd = new int[] {
      24, 25, 26, 33, 34, 35
  };

  public AmountSelectionPage(int id, final String menu) {
    super(id);
    this.menu = menu;
  }

  @Override
  public Map<Integer, Icon> defaultIcons(MenuPlayer player) {

    final Map<Integer, Icon> icons = new HashMap<>();

    final Optional<Object> curID = MenuManager.instance().getViewerData(player.identifier(), "cur_uid");
    final Optional<Object> target = MenuManager.instance().getViewerData(player.identifier(), "target");
    final Optional<Object> amtObj = MenuManager.instance().getViewerData(player.identifier(), "action_amt");
    final Optional<Object> confirm = MenuManager.instance().getViewerData(player.identifier(), "confirm");
    final Optional<PlayerProvider> provider = TNECore.server().findPlayer(player.identifier());

    final BigDecimal amount = amtObj.map(o->(BigDecimal)o).orElse(BigDecimal.ZERO);

    if(target.isPresent() && provider.isPresent() && curID.isPresent()) {

      final Optional<Currency> currency = TNECore.eco().currency().findCurrency(((UUID)curID.get()));

      if(currency.isPresent()) {

        icons.put(4, IconBuilder.of(TNECore.server()
                                        .stackBuilder()
                                        .of("PAPER", 1)
                                        .display("Amount: " + amount.toPlainString())).create());

        icons.put(22, IconBuilder.of(TNECore.server()
                                        .stackBuilder()
                                        .of("BOOK", 1)
                                        .display("Enter Amount"))
            .click((click)->{
              System.out.println("Custom Amount");
              click.getPlayer().message("Enter the custom amount, in decimal form." +
                                            "Or type \"exit\" to quit.");
              //updateAmount(click.getPlayer());
            })
            .withAction(new ChatAction((callback)->{
              if(callback.getMessage().equalsIgnoreCase("exit")) {
                return true;
              }

              try {
                final BigDecimal parsed = new BigDecimal(callback.getMessage());
                MenuManager.instance().setViewerData(player.identifier(), menu, "action_amt", parsed);
                updateAmountIcon(callback.getPlayer());
                callback.getPlayer().inventory().openMenu(player, "my_bal", 4);
                return true;
              } catch(Exception ignore) {
                //TODO: Invalid number entered.
                return false;
              }
            })).create());

        icons.put(40, IconBuilder.of(TNECore.server()
                                        .stackBuilder()
                                        .of("GREEN_WOOL", 1)
                                        .display("Confirm Amount"))
            .click((click)->{

              if(confirm.isPresent()) {
                AmountConfirmation confirmation = (AmountConfirmation)confirm.get();
                final Optional<Object> aObj = MenuManager.instance().getViewerData(player.identifier(), "action_amt");
                final BigDecimal amt = aObj.map(o->(BigDecimal)o).orElse(BigDecimal.ZERO);

                confirmation.confirm(player.identifier(), (UUID)target.get(),
                                     currency.get().getUid(),
                                     TNECore.eco().region().getMode().region(provider.get()), amt);
                player.inventory().close();
              }
            })
            .create());

        int minor = 0;
        int major = 0;

        for(Denomination denom : currency.get().getDenominations().values()) {

          int add = 0;
          if(denom.weight().compareTo(BigDecimal.ONE) >= 0) {
            add = majorAdd[major];
            major++;
          } else {
            add = minorAdd[minor];
            minor++;
          }

          int remove = add - 6;

          final String material = (denom.isItem())? ((ItemDenomination)denom).getMaterial() : "STONE_BUTTON";

          icons.put(add, build(player, material, "Add " + denom.weight().toPlainString(),
                              denom.weight()));

          icons.put(remove, build(player, material, "Remove " + denom.weight().toPlainString(),
                              denom.weight().multiply(new BigDecimal(-1))));
        }
      }
    }
    return icons;
  }

  private Icon build(final MenuPlayer player, final String material, final String display,
                     final BigDecimal modify) {
    return IconBuilder.of(TNECore.server()
                       .stackBuilder()
                       .of(material, 1)
                       .display(display))
        .click((click)->{

          final Optional<Object> amtObj = MenuManager.instance().getViewerData(player.identifier(), "action_amt");
          final BigDecimal amount = amtObj.map(o->(BigDecimal)o).orElse(BigDecimal.ZERO);
          MenuManager.instance().setViewerData(player.identifier(), click.getMenu().getName(),
                                               "action_amt", amount.add(modify));
          updateAmountIcon(player);
        })
        .withAction(new SwitchPageAction(4)).create();
  }

  private void updateAmountIcon(final MenuPlayer player) {
    final Optional<Object> amtObj = MenuManager.instance().getViewerData(player.identifier(), "action_amt");
    final BigDecimal amount = amtObj.map(o->(BigDecimal)o).orElse(BigDecimal.ZERO);

    playerIcons.get(player.identifier()).getIcons().put(4, IconBuilder.of(TNECore.server()
                                                                              .stackBuilder()
                                                                              .of("PAPER", 1)
                                                                              .display("Amount: " + amount.toPlainString())).create());
  }
}
