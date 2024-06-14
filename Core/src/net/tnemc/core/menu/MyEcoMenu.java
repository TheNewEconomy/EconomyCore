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
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.menu.icons.myeco.CurrencyIcon;
import net.tnemc.core.menu.icons.shared.SwitchPageIcon;
import net.tnemc.menu.core.Menu;
import net.tnemc.menu.core.Page;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.builder.PageBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.action.ActionType;
import net.tnemc.menu.core.icon.action.impl.ChatAction;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.RunnableAction;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * MyEcoMenu
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MyEcoMenu extends Menu {

  public static final int CURRENCIES_PAGE = 2;
  public static final int CURRENCY_EDIT_PAGE = 3;
  public static final int CURRENCY_INFO_EDIT_PAGE = 4;
  public static final int CURRENCY_FORMAT_EDIT_PAGE = 5;
  public static final int CURRENCY_TYPE_EDIT_PAGE = 6;
  public static final int CURRENCY_NOTE_EDIT_PAGE = 7;
  public static final int DENOMINATIONS_PAGE = 8;
  public static final int DENOMINATION_EDIT_PAGE = 9;

  public MyEcoMenu() {

    this.name = "my_eco";
    this.title = "My Eco";
    this.rows = 5;
    //TODO: Add pages

    /*
     * Main Page
     */
    final Page main = new PageBuilder(1).withIcons(
            new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                    .display("Currencies"), "my_eco", CURRENCIES_PAGE, ActionType.ANY)
    ).build();
    addPage(main);

    //Currency Page

    /*
     * Currency List Page
     */
    final Page currency = new PageBuilder(CURRENCIES_PAGE).build();

    //add currency
    final SwitchPageIcon addCurrencyIcon = new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("ARROW", 1)
            .display("Add Currency").lore(Collections.singletonList("Click to add currency")), "my_eco", CURRENCY_EDIT_PAGE, ActionType.ANY);
    addCurrencyIcon.addAction(new DataAction("CURRENCY_UUID", UUID.randomUUID().toString()));
    addCurrencyIcon.addAction(new ChatAction((message)->{

      if(message.getPlayer().viewer().isPresent()) {

        if(TNECore.eco().currency().findCurrency(message.getMessage()).isPresent()) {
          message.getPlayer().message("A currency with that identifier already exists! Enter an identifier for the currency:");
          return false;
        }

        message.getPlayer().viewer().get().addData("CURRENCY_IDENTIFIER", message.getMessage());
        return true;
      }
      message.getPlayer().message("Enter an identifier for the currency:");
      return false;
    }));
    addCurrencyIcon.addAction(new RunnableAction((run)->run.player().message("Enter an identifier for the currency:")));
    currency.addIcon(addCurrencyIcon);


    int i = 19;
    for(final Currency curObj : TNECore.eco().currency().currencies()) {
      currency.addIcon(new CurrencyIcon(i, curObj));

      i += 2;
    }
    addPage(currency);

    /*
     * Currency Edit Page
     */
    final Page currencyEditor = new PageBuilder(CURRENCY_EDIT_PAGE).build();

    //denominations
    currencyEditor.addIcon(new SwitchPageIcon(19, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
            .display("Edit Denominations"), "my_eco", DENOMINATIONS_PAGE, ActionType.ANY));

    //currency name icon
    currencyEditor.addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
                    .lore(Collections.singletonList("Click to set name of currency.")))
                    .withSlot(21)
                    .withActions(new ChatAction((message)->{

                      if(message.getPlayer().viewer().isPresent()) {
                        message.getPlayer().viewer().get().addData("CURRENCY_NAME", message.getMessage());
                        return true;
                      }
                      message.getPlayer().message("Enter a name for the currency:");
                      return false;
                    }), new RunnableAction((run)->run.player().message("Enter a name for the currency:")))
                    .withItemProvider((provider)->{

                      final String message = (provider.viewer().isPresent())? (String)provider.viewer().get().dataOrDefault("CURRENCY_NAME", "Default") : "Default";

                      return PluginCore.server().stackBuilder().of("PAPER", 1)
                              .lore(Collections.singletonList("Click to set name of currency."))
                              .display(message);
                    })
            .build());

    addPage(currencyEditor);

    /*
     * Currency Edit Info
     */
    final Page currencyInfoEditPage = new PageBuilder(CURRENCY_INFO_EDIT_PAGE).build();
    currencyInfoEditPage.setOpen((this::handleCurrencyEditInfoOpen));

    addPage(currencyInfoEditPage);

    /*
     * Currency Edit Info
     */
    final Page currencyFormatEditPage = new PageBuilder(CURRENCY_FORMAT_EDIT_PAGE).build();
    currencyFormatEditPage.setOpen((this::handleCurrencyEditFormatOpen));

    addPage(currencyFormatEditPage);

    /*
     * Currency Edit Info
     */
    final Page currencyTypeEditPage = new PageBuilder(CURRENCY_TYPE_EDIT_PAGE).build();
    currencyTypeEditPage.setOpen((this::handleCurrencyEditTypeOpen));

    addPage(currencyTypeEditPage);

    /*
     * Currency Edit Info
     */
    final Page currencyFormatNotePage = new PageBuilder(CURRENCY_NOTE_EDIT_PAGE).build();
    currencyFormatNotePage.setOpen((this::handleCurrencyEditNoteOpen));

    addPage(currencyFormatNotePage);

    /*
     * Denominations List Page
     */
    final Page denominationsPage = new PageBuilder(DENOMINATIONS_PAGE).build();
    denominationsPage.setOpen((this::handleDenominationOpen));

    addPage(denominationsPage);

    /*
     * Denominations Edit Page
     */
    final Page denominationsEditPage = new PageBuilder(DENOMINATION_EDIT_PAGE).build();
    denominationsEditPage.setOpen((this::handleDenominationEditOpen));

    addPage(denominationsEditPage);
  }

  /*
   * Currency Edit Info
   *
   * Type -> new menu
   * StartingHoldings
   * MaxBalance
   * MinBalance
   * IconMaterial
   *
   * Item Type:
   * Ender Chest
   * Ender Fill
   */
  private void handleCurrencyEditInfoOpen(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final Optional<Object> currencyUUID = viewer.get().findData("CURRENCY_UUID");
      if(currencyUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency((String)currencyUUID.get());
        if(currencyOptional.isPresent()) {

        }
      }
    }
  }

  /*
   * Currency Edit Format
   *
   * Format
   * Symbol
   * Prefixes
   * Decimal Character
   * Display
   * Display Plural
   * Display Minor
   * Display Minor Plural
   * Separate Major
   * Major Separator
   */
  private void handleCurrencyEditFormatOpen(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final Optional<Object> currencyUUID = viewer.get().findData("CURRENCY_UUID");
      if(currencyUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency((String)currencyUUID.get());
        if(currencyOptional.isPresent()) {

        }
      }
    }
  }

  /*
   * Currency Types
   *
   * Name
   * Description as lore(will need to update types classes for description)
   */
  private void handleCurrencyEditTypeOpen(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final Optional<Object> currencyUUID = viewer.get().findData("CURRENCY_UUID");
      if(currencyUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency((String)currencyUUID.get());
        if(currencyOptional.isPresent()) {

        }
      }
    }
  }

  /*
   * Currency Edit Note
   *
   * Material
   * Flags
   * Enchantments
   * Minimum Amount
   * Custom Model Data
   * Fee(Can be % or amount)
   */
  private void handleCurrencyEditNoteOpen(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final Optional<Object> currencyUUID = viewer.get().findData("CURRENCY_UUID");
      if(currencyUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency((String)currencyUUID.get());
        if(currencyOptional.isPresent()) {

        }
      }
    }
  }

  private void handleDenominationOpen(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final Optional<Object> currencyUUID = viewer.get().findData("CURRENCY_UUID");
      if(currencyUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency((String)currencyUUID.get());
        if(currencyOptional.isPresent()) {

          int i = 19;
          for(final Denomination denomObj : currencyOptional.get().getDenominations().values()) {

            final String material = (denomObj.isItem())? ((ItemDenomination)denomObj).getMaterial() : "PAPER";

            final SwitchPageIcon switchIcon = new SwitchPageIcon(i, PluginCore.server().stackBuilder().of(material, 1)
                    .display(denomObj.singular()).lore(Collections.singletonList("Click to edit denomination")), "my_eco", DENOMINATION_EDIT_PAGE, ActionType.ANY);

            switchIcon.addAction(new DataAction("DENOMINATION_WEIGHT", denomObj.weight()));
            callback.getPage().addIcon(switchIcon);

            i+= 2;
          }
        }
      }
    }
  }

  /*
   * Edit Denomination
   * singular
   * plural
   * weight
   *
   * item type:
   * material
   * damage
   * name/display
   * custom model
   * texture
   * lore
   * flags
   * enchantments
   */
  private void handleDenominationEditOpen(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final Optional<Object> currencyUUID = viewer.get().findData("CURRENCY_UUID");
      if(currencyUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrency((String)currencyUUID.get());
        if(currencyOptional.isPresent()) {

          final Optional<Object> denomWeight = viewer.get().findData("DENOMINATION_WEIGHT");
          if(denomWeight.isPresent()) {

            final Optional<Denomination> denomOptional = Optional.ofNullable(currencyOptional.get().getDenominationByWeight((BigDecimal)denomWeight.get()));
            if(denomOptional.isPresent()) {


            }
          }
        }
      }
    }
  }
}