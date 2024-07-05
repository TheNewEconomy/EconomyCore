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
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.Note;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.menu.handlers.StringSelectionHandler;
import net.tnemc.core.menu.icons.actions.PageSwitchWithClose;
import net.tnemc.core.menu.icons.myeco.CurrencyIcon;
import net.tnemc.core.menu.icons.myeco.DenominationIcon;
import net.tnemc.core.menu.icons.shared.PreviousPageIcon;
import net.tnemc.core.menu.icons.shared.SwitchPageIcon;
import net.tnemc.core.menu.page.myeco.FormatSelectionPage;
import net.tnemc.core.menu.page.shared.AmountSelectionPage;
import net.tnemc.core.menu.page.shared.EnchantmentSelectionPage;
import net.tnemc.core.menu.page.shared.FlagSelectionPage;
import net.tnemc.core.menu.page.shared.MaterialSelectionPageCallback;
import net.tnemc.core.transaction.tax.TaxEntry;
import net.tnemc.core.utils.MISCUtils;
import net.tnemc.core.utils.exceptions.NoValidCurrenciesException;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.menu.core.Menu;
import net.tnemc.menu.core.Page;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.builder.PageBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.action.ActionType;
import net.tnemc.menu.core.icon.action.impl.ChatAction;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.RunnableAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.icon.impl.StateIcon;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * MyEcoMenu
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class MyEcoMenu extends Menu {

  public static final int CURRENCIES_PAGE = 2;
  public static final int CURRENCY_EDIT_PAGE = 3;
  public static final int CURRENCY_INFO_EDIT_PAGE = 4;
  public static final int CURRENCY_INFO_MAX_SELECTION_PAGE = 5;
  public static final int CURRENCY_INFO_MIN_SELECTION_PAGE = 6;
  public static final int CURRENCY_INFO_STARTING_SELECTION_PAGE = 7;
  public static final int CURRENCY_ICON_MATERIAL_PAGE = 8;
  public static final int CURRENCY_FORMAT_EDIT_PAGE = 9;
  public static final int CURRENCY_FORMAT_SELECTION_PAGE = 10;
  public static final int CURRENCY_TYPE_EDIT_PAGE = 11;
  public static final int CURRENCY_NOTE_EDIT_PAGE = 12;
  public static final int CURRENCY_NOTE_MATERIAL_PAGE = 13;
  public static final int CURRENCY_NOTE_FLAGS_PAGE = 14;
  public static final int CURRENCY_NOTE_ENCHANTS_PAGE = 15;
  public static final int CURRENCY_NOTE_FEE_MAIN_PAGE = 16;
  public static final int CURRENCY_NOTE_FEE_SELECTION_PAGE = 17;
  public static final int CURRENCY_NOTE_MIN_SELECTION_PAGE = 18;
  public static final int DENOMINATIONS_PAGE = 19;
  public static final int DENOMINATION_EDIT_PAGE = 20;
  public static final int DENOMINATION_WEIGHT_SELECTION_PAGE = 21;
  public static final int DENOMINATION_MATERIAL_PAGE = 22;
  public static final int DENOMINATION_ENCHANTS_PAGE = 23;
  public static final int DENOMINATION_FLAGS_PAGE = 24;

  public static final String CURRENCY_ICON_ID = "CURRENCY_ICON";
  public static final String CURRENCY_FORMAT_ID = "CURRENCY_FORMAT";
  public static final String NOTE_MATERIAL_ID = "NOTE_MATERIAL";
  public static final String NOTE_ENCHANTS_ID = "NOTE_ENCHANTS";
  public static final String NOTE_FLAGS_ID = "NOTE_FLAGS";
  public static final String DENOMINATION_MATERIAL_ID = "DENOMINATION_MATERIAL";
  public static final String DENOMINATION_ENCHANTS_ID = "DENOMINATION_ENCHANTS";
  public static final String DENOMINATION_FLAGS_ID = "DENOMINATION_FLAGS";

  public static final String ACTIVE_DENOMINATION = "ACTIVE_DENOMINATION";
  public static final String ACTIVE_CURRENCY = "ACTIVE_CURRENCY";

  public MyEcoMenu() {

    this.name = "my_eco";
    this.title = "My Eco";
    this.rows = 6;

    /*
     * Main Page
     */
    final Page main = new PageBuilder(1).withIcons(
            new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                    .display("Currencies"), this.name, CURRENCIES_PAGE, ActionType.ANY)
    ).build();
    addPage(main);

    //Currency Page

    /*
     * Currency List Page
     */
    final Page currency = new PageBuilder(CURRENCIES_PAGE).build();
    currency.addIcon(new PreviousPageIcon(0, this.name, 1, ActionType.ANY));

    currency.addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
            .display("Save")
            .lore(Collections.singletonList("Click to save the currencies.")))
            .withActions(new RunnableAction((click)->{

              final File directory = new File(PluginCore.directory(), "currency");
              TNECore.eco().currency().getSaver().backupCurrency(directory);

              MISCUtils.deleteFolder(directory);

              TNECore.eco().currency().getSaver().saveCurrencies(directory);

              try {

                TNECore.eco().currency().getCurrencies().clear();
                TNECore.eco().currency().getCurIDMap().clear();

                TNECore.eco().currency().getLoader().loadCurrencies(directory);
              } catch(NoValidCurrenciesException ignore) {}
            }), new PageSwitchWithClose(this.name, -1))
            .withSlot(6)
            .build());

    //add currency
    final SwitchPageIcon addCurrencyIcon = new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("ARROW", 1)
            .display("Add Currency").lore(Collections.singletonList("Click to add currency")), this.name, CURRENCY_EDIT_PAGE, ActionType.ANY);

    addCurrencyIcon.addAction(new ChatAction((message)->{

      if(message.getPlayer().viewer().isPresent()) {

        if(TNECore.eco().currency().findCurrency(message.getMessage()).isPresent()) {
          message.getPlayer().message("A currency with that identifier already exists! Enter an identifier for the currency:");
          return false;
        }

        message.getPlayer().viewer().get().addData(ACTIVE_CURRENCY, new Currency(message.getMessage()));
        return true;
      }
      message.getPlayer().message("Enter an identifier for the currency:");
      return false;
    }));
    addCurrencyIcon.addAction(new RunnableAction((run)->run.player().message("Enter an identifier for the currency:")));
    currency.addIcon(addCurrencyIcon);
    currency.setOpen((open)->{

      int i = 19;
      for(final Currency curObj : TNECore.eco().currency().currencies()) {
        currency.addIcon(new CurrencyIcon(i, curObj));

        i += 2;
      }
    });

    addPage(currency);

    /*
     * Currency Edit Page
     */
    final Page currencyEditor = new PageBuilder(CURRENCY_EDIT_PAGE).build();
    currencyEditor.addIcon(new PreviousPageIcon(0, this.name, CURRENCIES_PAGE, ActionType.ANY));

    //denominations
    currencyEditor.addIcon(new SwitchPageIcon(10, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
            .display("Edit Denominations"), this.name, DENOMINATIONS_PAGE, ActionType.ANY));

    //CURRENCY_INFO_EDIT_PAGE
    currencyEditor.addIcon(new SwitchPageIcon(11, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
            .display("Basic Currency Information"), this.name, CURRENCY_INFO_EDIT_PAGE, ActionType.ANY));

    //CURRENCY_FORMAT_EDIT_PAGE
    currencyEditor.addIcon(new SwitchPageIcon(12, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
            .display("Currency Format Options"), this.name, CURRENCY_FORMAT_EDIT_PAGE, ActionType.ANY));

    //CURRENCY_NOTE_EDIT_PAGE
    currencyEditor.addIcon(new SwitchPageIcon(14, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
            .display("Currency Note Options"), this.name, CURRENCY_NOTE_EDIT_PAGE, ActionType.ANY));

    currencyEditor.setOpen((open)->{
      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();

          //CURRENCY_TYPE_EDIT_PAGE
          currencyEditor.addIcon(new SwitchPageIcon(13, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                  .display("Type: " + currencyObject.getType()).lore(Collections.singletonList("Set the currency type.")), this.name, CURRENCY_TYPE_EDIT_PAGE, ActionType.ANY));
        }
      }
    });
    addPage(currencyEditor);

    /*
     * Currency Edit Info
     */
    final Page currencyInfoEditPage = new PageBuilder(CURRENCY_INFO_EDIT_PAGE).build();
    currencyInfoEditPage.addIcon(new PreviousPageIcon(0, this.name, CURRENCY_EDIT_PAGE, ActionType.ANY));
    currencyInfoEditPage.setOpen((this::handleCurrencyEditInfoOpen));

    addPage(currencyInfoEditPage);

    final Page currencyIconMaterialPage = new PageBuilder(CURRENCY_ICON_MATERIAL_PAGE).build();
    currencyIconMaterialPage.setOpen((open->new MaterialSelectionPageCallback("CURRENCY_ICON_MATERIAL", this.name, this.name, CURRENCY_ICON_MATERIAL_PAGE, CURRENCY_INFO_EDIT_PAGE, "CURRENCY_ICON_MATERIAL_PAGE", this.rows, (selection)->{

      if(selection.getClick().player().viewer().isPresent()) {

        final Optional<Object> currencyOpt = selection.getClick().player().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();
          currencyObject.setIconMaterial(selection.getValue());
        }
      }
    }).handle(open)));
    addPage(currencyIconMaterialPage);

    final Page currencyInfoMaxPage = new PageBuilder(CURRENCY_INFO_MAX_SELECTION_PAGE).build();
    currencyInfoMaxPage.setOpen((open->new AmountSelectionPage("CURRENCY_INFO_MAX", this.name, this.name, CURRENCY_INFO_MAX_SELECTION_PAGE, CURRENCY_INFO_EDIT_PAGE, (selection)->{

      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();
          currencyObject.setMaxBalance(selection.getAmount());
        }
      }
    }).handle(open)));
    addPage(currencyInfoMaxPage);

    final Page currencyInfoMinPage = new PageBuilder(CURRENCY_INFO_MIN_SELECTION_PAGE).build();
    currencyInfoMinPage.setOpen((open->new AmountSelectionPage("CURRENCY_INFO_MIN", this.name, this.name, CURRENCY_INFO_MIN_SELECTION_PAGE, CURRENCY_INFO_EDIT_PAGE, (selection)->{
      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();
          currencyObject.setMinBalance(selection.getAmount());
        }
      }
    }).handle(open)));
    addPage(currencyInfoMinPage);

    final Page currencyInfoStartingPage = new PageBuilder(CURRENCY_INFO_STARTING_SELECTION_PAGE).build();
    currencyInfoStartingPage.setOpen((open->new AmountSelectionPage("CURRENCY_INFO_STARTING", this.name, this.name, CURRENCY_INFO_STARTING_SELECTION_PAGE, CURRENCY_INFO_EDIT_PAGE, (selection)->{
      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();
          currencyObject.setStartingHoldings(selection.getAmount());
        }
      }
    }).handle(open)));
    addPage(currencyInfoStartingPage);

    /*
     * Currency Edit Format
     */
    final Page currencyFormatEditPage = new PageBuilder(CURRENCY_FORMAT_EDIT_PAGE).build();
    currencyFormatEditPage.addIcon(new PreviousPageIcon(0, this.name, CURRENCY_EDIT_PAGE, ActionType.ANY));
    currencyFormatEditPage.setOpen((this::handleCurrencyEditFormatOpen));

    addPage(currencyFormatEditPage);

    final Page currencyFormatSelectionPage = new PageBuilder(CURRENCY_FORMAT_SELECTION_PAGE).build();
    currencyFormatSelectionPage.setOpen((open->new FormatSelectionPage("CURRENCY_FORMAT_SELECTION", this.name, this.name, CURRENCY_FORMAT_SELECTION_PAGE, CURRENCY_FORMAT_EDIT_PAGE, "CURRENCY_FORMAT_SELECTION_PAGE", this.rows, (selection)->{
      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();
          currencyObject.setFormat(selection.getValue());
        }
      }
    }).handle(open)));
    addPage(currencyFormatSelectionPage);

    /*
     * Currency Edit Type
     */
    final Page currencyTypeEditPage = new PageBuilder(CURRENCY_TYPE_EDIT_PAGE).build();
    currencyTypeEditPage.addIcon(new PreviousPageIcon(0, this.name, CURRENCY_EDIT_PAGE, ActionType.ANY));

    int i = 19;
    for(final CurrencyType type : TNECore.eco().currency().getTypes().values()) {

      final SwitchPageIcon switchIcon = new SwitchPageIcon(i, PluginCore.server().stackBuilder().of("PAPER", 1)
              .display("Type: " + type.name()).lore(Collections.singletonList("Click to set currency to this type.")), this.name, CURRENCY_EDIT_PAGE, ActionType.ANY, false);
      switchIcon.addAction(new RunnableAction((click)->{

        if(click.player().viewer().isPresent()) {

          Optional<Object> currencyOpt = click.player().viewer().get().findData(ACTIVE_CURRENCY);
          if(currencyOpt.isPresent()) {

            Currency currencyObject = (Currency)currencyOpt.get();

            final CurrencyType origType = TNECore.eco().currency().findTypeOrDefault(currencyObject.getType());
            final CurrencyType newType = TNECore.eco().currency().findTypeOrDefault(type.name());
            if(origType.supportsItems() && !newType.supportsItems() || !origType.supportsItems() && newType.supportsItems()) {
              currencyObject.setType(type.name());

              currencyObject = Currency.clone(currencyObject, newType.supportsItems());
              TNECore.eco().currency().addCurrency(currencyObject);
              click.player().viewer().get().addData(ACTIVE_CURRENCY, currencyObject);

            } else {
              currencyObject.setType(type.name());
            }
          }
        }
      }));
      switchIcon.addActions();
      currencyTypeEditPage.addIcon(switchIcon);
      i += 2;
    }

    addPage(currencyTypeEditPage);

    /*
     * Currency Note Edit Info
     */
    final Page currencyFormatNotePage = new PageBuilder(CURRENCY_NOTE_EDIT_PAGE).build();
    currencyFormatNotePage.addIcon(new PreviousPageIcon(0, this.name, CURRENCY_EDIT_PAGE, ActionType.ANY));
    currencyFormatNotePage.setOpen((this::handleCurrencyEditNoteOpen));

    addPage(currencyFormatNotePage);

    final Page currencyNoteMaterialPage = new PageBuilder(CURRENCY_NOTE_MATERIAL_PAGE).build();
    currencyNoteMaterialPage.setOpen((open->new MaterialSelectionPageCallback("CURRENCY_NOTE_MATERIAL", this.name, this.name, CURRENCY_NOTE_MATERIAL_PAGE, CURRENCY_NOTE_EDIT_PAGE, "CURRENCY_NOTE_MATERIAL_PAGE", this.rows, (selection)->{

      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();
          if(currencyObject.getNote().isPresent()) {
            currencyObject.getNote().get().setMaterial(selection.getValue());
          }
        }
      }
    }).handle(open)));
    addPage(currencyNoteMaterialPage);

    final Page currencyNoteEnchantPage = new PageBuilder(CURRENCY_NOTE_ENCHANTS_PAGE).build();
    currencyNoteEnchantPage.setOpen((open->new EnchantmentSelectionPage("CURRENCY_NOTE_ENCHANTS", this.name, this.name, CURRENCY_NOTE_ENCHANTS_PAGE, CURRENCY_NOTE_EDIT_PAGE, "CURRENCY_NOTE_ENCHANTS_PAGE", this.rows, (selection)->{
      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();
          if(currencyObject.getNote().isPresent()) {
            currencyObject.getNote().get().setEnchantments(Arrays.asList(selection.getValue().split(",")));
          }
        }
      }
    }).handle(open)));
    addPage(currencyNoteEnchantPage);

    final Page currencyNoteFlagPage = new PageBuilder(CURRENCY_NOTE_FLAGS_PAGE).build();
    currencyNoteFlagPage.setOpen((open->new FlagSelectionPage("CURRENCY_NOTE_FLAGS", this.name, this.name, CURRENCY_NOTE_FLAGS_PAGE, CURRENCY_NOTE_EDIT_PAGE, "CURRENCY_NOTE_FLAGS_PAGE", this.rows, (selection)->{

      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();
          if(currencyObject.getNote().isPresent()) {
            currencyObject.getNote().get().setFlags(Arrays.asList(selection.getValue().split(",")));
          }
        }
      }
    }).handle(open)));
    addPage(currencyNoteFlagPage);

    final Page noteFeePage = new PageBuilder(CURRENCY_NOTE_FEE_SELECTION_PAGE).build();
    noteFeePage.setOpen((open->new AmountSelectionPage("CURRENCY_NOTE_FEE", this.name, this.name, CURRENCY_NOTE_FEE_SELECTION_PAGE, CURRENCY_NOTE_FEE_MAIN_PAGE, (selection)->{

      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();
          if(currencyObject.getNote().isPresent()) {
            //TODO: Note fee
          }
        }
      }
    }).handle(open)));
    addPage(noteFeePage);

    final Page noteMinPage = new PageBuilder(CURRENCY_NOTE_MIN_SELECTION_PAGE).build();
    noteMinPage.setOpen((open->new AmountSelectionPage("CURRENCY_NOTE_MIN", this.name, this.name, CURRENCY_NOTE_MIN_SELECTION_PAGE, CURRENCY_NOTE_EDIT_PAGE, (selection)->{

      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();
          if(currencyObject.getNote().isPresent()) {
            currencyObject.getNote().get().setMinimum(selection.getAmount());
          }
        }
      }
    }).handle(open)));
    addPage(noteMinPage);

    /*
     * Denominations List Page
     */
    final Page denominationsPage = new PageBuilder(DENOMINATIONS_PAGE).build();
    denominationsPage.addIcon(new PreviousPageIcon(0, this.name, CURRENCY_EDIT_PAGE, ActionType.ANY));
    denominationsPage.setOpen((this::handleDenominationOpen));
    //add denomination
    final SwitchPageIcon addDenominationIcon = new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("ARROW", 1)
            .display("Add Denomination").lore(Collections.singletonList("Click to add denomination")), this.name, DENOMINATIONS_PAGE, ActionType.ANY);
    addDenominationIcon.addAction(new ChatAction((message)->{

      if(message.getPlayer().viewer().isPresent()) {

        final Optional<Object> currencyOpt = message.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          try {

            final BigDecimal weight = new BigDecimal(message.getMessage());
            final Currency currencyObject = (Currency)currencyOpt.get();
            if(currencyObject.getDenominations().containsKey(weight)) {
              message.getPlayer().message("A denomination with that weight already exists! Enter a valid decimal for the weight of the denomination:");
              return false;
            }
            final Denomination denomObj = (currencyObject instanceof ItemCurrency)? new ItemDenomination(weight) : new Denomination(weight);
            currencyObject.getDenominations().put(weight, denomObj);

            message.getPlayer().viewer().get().addData(ACTIVE_DENOMINATION, denomObj);
            return true;
          } catch(NumberFormatException ignore) {}
        }
      }
      message.getPlayer().message("Enter a valid decimal for the weight of the denomination:");
      return false;
    }));
    addDenominationIcon.addAction(new RunnableAction((run)->run.player().message("Enter a valid decimal for the weight of the denomination:")));
    denominationsPage.addIcon(addDenominationIcon);
    addPage(denominationsPage);

    /*
     * Denominations Edit Page
     */
    final Page denominationsEditPage = new PageBuilder(DENOMINATION_EDIT_PAGE).build();
    denominationsEditPage.addIcon(new PreviousPageIcon(0, this.name, DENOMINATIONS_PAGE, ActionType.ANY));
    denominationsEditPage.setOpen((this::handleDenominationEditOpen));
    addPage(denominationsEditPage);

    final Page denominationMaterialPage = new PageBuilder(DENOMINATION_MATERIAL_PAGE).build();
    denominationMaterialPage.setOpen((open->new MaterialSelectionPageCallback("DENOMINATION_MATERIAL", this.name, this.name, DENOMINATION_MATERIAL_PAGE, DENOMINATION_EDIT_PAGE, "DENOMINATION_MATERIAL_PAGE", this.rows, (selection)->{

      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> denomOpt = open.getPlayer().viewer().get().findData(ACTIVE_DENOMINATION);
        if(denomOpt.isPresent()) {

          final Denomination denomObj = (Denomination)denomOpt.get();
          if(denomObj instanceof ItemDenomination itemDenomination) {
            itemDenomination.setMaterial(selection.getValue());
          }
        }
      }

    }).handle(open)));
    addPage(denominationMaterialPage);

    final Page denominationEnchantPage = new PageBuilder(DENOMINATION_ENCHANTS_PAGE).build();
    denominationEnchantPage.setOpen((open->new EnchantmentSelectionPage("DENOMINATION_ENCHANTS", this.name, this.name, DENOMINATION_ENCHANTS_PAGE, DENOMINATION_EDIT_PAGE, "DENOMINATION_ENCHANTS_PAGE", this.rows, (selection)->{

      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> denomOpt = open.getPlayer().viewer().get().findData(ACTIVE_DENOMINATION);
        if(denomOpt.isPresent()) {

          final Denomination denomObj = (Denomination)denomOpt.get();
          if(denomObj instanceof ItemDenomination itemDenomination && !selection.getValue().isEmpty()) {
            itemDenomination.setEnchantments(Arrays.asList(selection.getValue().split(",")));
          }
        }
      }
    }).handle(open)));
    addPage(denominationEnchantPage);

    final Page denominationFlagPage = new PageBuilder(DENOMINATION_FLAGS_PAGE).build();
    denominationFlagPage.setOpen((open->new FlagSelectionPage("DENOMINATION_FLAGS", this.name, this.name, DENOMINATION_FLAGS_PAGE, DENOMINATION_EDIT_PAGE, "DENOMINATION_FLAGS_PAGE", this.rows, (selection)->{

      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> denomOpt = open.getPlayer().viewer().get().findData(ACTIVE_DENOMINATION);
        if(denomOpt.isPresent()) {

          final Denomination denomObj = (Denomination)denomOpt.get();
          if(denomObj instanceof ItemDenomination itemDenomination && !selection.getValue().isEmpty()) {
            itemDenomination.setFlags(Arrays.asList(selection.getValue().split(",")));
          }
        }
      }
    }).handle(open)));
    addPage(denominationFlagPage);

    final Page denominationWeightPage = new PageBuilder(DENOMINATION_WEIGHT_SELECTION_PAGE).build();
    denominationWeightPage.setOpen((open->new AmountSelectionPage("DENOMINATION_WEIGHT", this.name, this.name, DENOMINATION_WEIGHT_SELECTION_PAGE, DENOMINATION_EDIT_PAGE, (selection)->{

      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> denomOpt = open.getPlayer().viewer().get().findData(ACTIVE_DENOMINATION);
        if(denomOpt.isPresent()) {

          final Denomination denomObj = (Denomination)denomOpt.get();
          denomObj.setWeight(selection.getAmount());
        }
      }
    }).handle(open)));
    addPage(denominationWeightPage);
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

      final Optional<Object> currencyOpt = viewer.get().findData(ACTIVE_CURRENCY);
      if(currencyOpt.isPresent()) {

        final Currency currencyObject = (Currency)currencyOpt.get();

        //currency name icon
        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                .lore(Collections.singletonList("Click to set the identifier of the currency.")))
                .withSlot(18)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    if(TNECore.eco().currency().findCurrency(message.getMessage()).isPresent()) {
                      message.getPlayer().message("A currency with that identifier already exists! Enter an identifier for the currency:");
                      return false;
                    }

                    currencyObject.setIdentifier(message.getMessage());
                    return true;
                  }
                  message.getPlayer().message("Enter an identifier for the currency:");
                  return false;
                }), new RunnableAction((run)->run.player().message("Enter a identifier for the currency:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set the identifier of the currency."))
                        .display(currencyObject.getIdentifier()))
                .build());
//TODO: Keep adding to messages.yml from here on.
        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                .display("Starting Balance")
                .lore(Collections.singletonList("Set the starting balance for new accounts")))
                .withSlot(19)
                .withActions(new SwitchPageAction(this.name, CURRENCY_INFO_STARTING_SELECTION_PAGE))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                .display("Maximum Balance")
                .lore(Collections.singletonList("Set the max balance for this currency")))
                .withSlot(20)
                .withActions(new SwitchPageAction(this.name, CURRENCY_INFO_MAX_SELECTION_PAGE))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                .display("Minimum Balance")
                .lore(Collections.singletonList("Set the minimum balance for this currency")))
                .withSlot(21)
                .withActions(new SwitchPageAction(this.name, CURRENCY_INFO_MIN_SELECTION_PAGE))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(currencyObject.getIconMaterial(), 1)
                .display("Set Icon Material")
                .lore(Collections.singletonList("Used as item representation in menus.")))
                .withSlot(22)
                .withActions(new SwitchPageAction(this.name, CURRENCY_ICON_MATERIAL_PAGE))
                .build());

        if(currencyObject instanceof ItemCurrency itemCurrency) {

          final AbstractItemStack<?> disabledStack = PluginCore.server().stackBuilder().display("Disabled").of("RED_WOOL", 1);
          final AbstractItemStack<?> enabledStack = PluginCore.server().stackBuilder().display("Enabled").of("GREEN_WOOL", 1);

          //ender chest icon
          final String enderState = (itemCurrency.canEnderChest())? "ENABLED" : "DISABLED";
          final StateIcon enderchest = new StateIcon(disabledStack, null, "CURRENCY_ENDER", enderState, (currentState)->{
            switch(currentState.toUpperCase(Locale.ROOT)) {

              case "ENABLED":
                itemCurrency.setEnderChest(false);
                return "DISABLED";
              default:
                itemCurrency.setEnderChest(true);
                return "ENABLED";
            }
          });
          enderchest.setSlot(23);
          enderchest.addState("DISABLED", disabledStack.display("EnderChest(Disabled)").lore(Collections.singletonList("Clicked to Enable using ender chests for item currency balances.")));
          enderchest.addState("ENABLED", enabledStack.display("EnderChest(Enabled)").lore(Collections.singletonList("Clicked to Disable using ender chests for item currency balances.")));
          callback.getPage().addIcon(enderchest);

          //ender chest icon
          final String fillState = (itemCurrency.isEnderFill())? "ENABLED" : "DISABLED";
          final StateIcon enderFill = new StateIcon(disabledStack, null, "CURRENCY_ENDER_FILL", fillState, (currentState)->{
            switch(currentState.toUpperCase(Locale.ROOT)) {

              case "ENABLED":
                itemCurrency.setEnderChest(false);
                return "DISABLED";
              default:
                itemCurrency.setEnderChest(true);
                return "ENABLED";
            }
          });
          enderFill.setSlot(24);
          enderFill.addState("DISABLED", disabledStack.display("EnderChest Fill(Disabled)").lore(Collections.singletonList("Clicked to Enable Filling the ender chest when player inventory is full.")));
          enderFill.addState("ENABLED", enabledStack.display("EnderChest Fill(Enabled)").lore(Collections.singletonList("Clicked to Disable Filling the ender chest when player inventory is full.")));
          callback.getPage().addIcon(enderFill);
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

      final Optional<Object> currencyOpt = viewer.get().findData(ACTIVE_CURRENCY);
      if(currencyOpt.isPresent()) {

        final Currency currencyObject = (Currency)currencyOpt.get();

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                .display("Click to Set Balance Format")
                .lore(Collections.singletonList("The format that is outputted from commands")))
                .withSlot(10)
                .withActions(new SwitchPageAction(this.name, CURRENCY_FORMAT_SELECTION_PAGE))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1))
                .withSlot(11)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    currencyObject.setSymbol(message.getMessage());
                    return true;
                  }
                  message.getPlayer().message("Enter the symbol for the currency:");
                  return false;
                }), new RunnableAction((run)->run.player().message("Enter the symbol for the currency:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set the symbol for the currency."))
                        .display(currencyObject.getSymbol()))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1))
                .withSlot(12)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    currencyObject.setPrefixes(message.getMessage());
                    return true;
                  }
                  message.getPlayer().message("Enter the prefixes for the currency:");
                  return false;
                }), new RunnableAction((run)->run.player().message("Enter the prefixes for the currency:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set the prefixes for the currency."))
                        .display(currencyObject.getPrefixes()))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1))
                .withSlot(13)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    currencyObject.setDecimal(message.getMessage());
                    return true;
                  }
                  message.getPlayer().message("Enter the decimal for the currency:");
                  return false;
                }), new RunnableAction((run)->run.player().message("Enter the decimal for the currency:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set the decimal for the currency."))
                        .display(currencyObject.getDecimal()))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1))
                .withSlot(14)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    currencyObject.setDisplay(message.getMessage());
                    return true;
                  }
                  message.getPlayer().message("Enter the major singular display for the currency:");
                  return false;
                }), new RunnableAction((run)->run.player().message("Enter the major singular display for the currency:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set the major singular display for the currency."))
                        .display(currencyObject.getDisplay()))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1))
                .withSlot(15)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    currencyObject.setDisplayPlural(message.getMessage());
                    return true;
                  }
                  message.getPlayer().message("Enter the major plural display for the currency:");
                  return false;
                }), new RunnableAction((run)->run.player().message("Enter the major plural display for the currency:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set the major plural display for the currency."))
                        .display(currencyObject.getDisplayPlural()))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1))
                .withSlot(16)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    currencyObject.setDisplayMinor(message.getMessage());
                    return true;
                  }

                  message.getPlayer().message("Enter the minor singular display for the currency:");
                  return false;
                }), new RunnableAction((run)->run.player().message("Enter the minor singular display for the currency:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set the minor singular display for the currency."))
                        .display(currencyObject.getDisplayMinor()))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1))
                .withSlot(17)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    currencyObject.setDisplayMinorPlural(message.getMessage());
                    return true;
                  }
                  message.getPlayer().message("Enter the minor plural display for the currency:");
                  return false;
                }), new RunnableAction((run)->run.player().message("Enter the minor plural display for the currency:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set the minor plural display for the currency."))
                        .display(currencyObject.getDisplayMinorPlural()))
                .build());

        final AbstractItemStack<?> disabledStack = PluginCore.server().stackBuilder().display("Disabled").of("RED_WOOL", 1);
        final AbstractItemStack<?> enabledStack = PluginCore.server().stackBuilder().display("Enabled").of("GREEN_WOOL", 1);
        final String state = (currencyObject.isSeparateMajor())? "ENABLED" : "DISABLED";

        //ender chest icon
        final StateIcon majorSeparate = new StateIcon(disabledStack, null, "CURRENCY_SEPARATE", state, (currentState)->{
          switch(currentState.toUpperCase(Locale.ROOT)) {

            case "ENABLED":
              currencyObject.setSeparateMajor(false);
              return "DISABLED";
            default:
              currencyObject.setSeparateMajor(true);
              return "ENABLED";
          }
        });
        majorSeparate.setSlot(18);
        majorSeparate.addState("DISABLED", disabledStack.display("Separate Major Amounts(Disabled)").lore(Collections.singletonList("Click to separate the major numeric every three numbers.")));
        majorSeparate.addState("ENABLED", enabledStack.display("Separate Major Amounts(Enabled)").lore(Collections.singletonList("Click to not separate the major numeric every three numbers.")));
        callback.getPage().addIcon(majorSeparate);

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1))
                .withSlot(19)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    currencyObject.setMajorSeparator(message.getMessage());
                    return true;
                  }
                  message.getPlayer().message("Enter the major value separator for the currency:");
                  return false;
                }), new RunnableAction((run)->run.player().message("Enter the major value separator for the currency:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set the major value separator for the currency."))
                        .display(currencyObject.getMajorSeparator()))
                .build());
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

      final Optional<Object> currencyOpt = viewer.get().findData(ACTIVE_CURRENCY);
      if(currencyOpt.isPresent()) {

        final Currency currency = (Currency)currencyOpt.get();

        final AbstractItemStack<?> disabledStack = PluginCore.server().stackBuilder().display("Disabled").of("RED_WOOL", 1);
        final AbstractItemStack<?> enabledStack = PluginCore.server().stackBuilder().display("Enabled").of("GREEN_WOOL", 1);

        //ender chest icon
        final String noteState = (currency.isNotable())? "ENABLED" : "DISABLED";
        final StateIcon notableIcon = new StateIcon(disabledStack, null, "CURRENCY_NOTE", noteState, (currentState)->{
          switch(currentState.toUpperCase(Locale.ROOT)) {

            case "ENABLED":
              currency.setNote(null);
              return "DISABLED";
            default:
              currency.setNote(new Note("PAPER", BigDecimal.ZERO, new TaxEntry("flat", 0.0)));
              return "ENABLED";
          }
        });
        notableIcon.setSlot(23);
        notableIcon.addState("DISABLED", disabledStack.display("Notable(Disabled)").lore(Collections.singletonList("Click to enable noting for this currency.")));
        notableIcon.addState("ENABLED", enabledStack.display("Notable(Enabled)").lore(Collections.singletonList("Click to disable noting for this currency.")));
        callback.getPage().addIcon(notableIcon);

        if(currency.getNote().isPresent()) {

          final String material = (currency.getNote().isPresent())? currency.getNote().get().getMaterial() : "PAPER";
          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(material, 1)
                  .display("Note Material")
                  .lore(Collections.singletonList("Click to set the material of the note item.")))
                  .withSlot(10)
                  .withActions(new SwitchPageAction(this.name, CURRENCY_NOTE_MATERIAL_PAGE))
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                  .lore(Collections.singletonList("Click to set the flags of the note item.")))
                  .withSlot(11)
                  .withActions(new SwitchPageAction(this.name, CURRENCY_NOTE_FLAGS_PAGE))
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                  .lore(Collections.singletonList("Click to set the enchantments of the note item.")))
                  .withSlot(12)
                  .withActions(new SwitchPageAction(this.name, CURRENCY_NOTE_ENCHANTS_PAGE))
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                  .display("Minimum Note Amount")
                  .lore(Collections.singletonList("Minimum Required Amount for Amount")))
                  .withSlot(13)
                  .withActions(new SwitchPageAction(this.name, CURRENCY_NOTE_MIN_SELECTION_PAGE))
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
                  .lore(Collections.singletonList("Click to set custom model of the denomination item. Optional.")))
                  .withSlot(14)
                  .withActions(new ChatAction((message)->{

                    if(message.getPlayer().viewer().isPresent()) {

                      try {

                        currency.getNote().get().setCustomModelData(Integer.parseInt(message.getMessage()));

                        return true;
                      } catch(NumberFormatException ignore) {}
                    }
                    message.getPlayer().message("Enter custom model of the note item:");
                    return false;
                  }), new RunnableAction((run)->run.player().message("Enter custom model of the note item:")))
                  .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                          .lore(Collections.singletonList("Click to set custom model of the note item. Optional."))
                          .display(String.valueOf(currency.getNote().get().getCustomModelData())))
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                  .display("Note Creation Fee")
                  .lore(Collections.singletonList("Fee for creating Note")))
                  .withSlot(19)
                  .withActions(new SwitchPageAction(this.name, CURRENCY_NOTE_FEE_MAIN_PAGE))
                  .build());
        }
      }
    }
  }

  private void handleDenominationOpen(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final Optional<Object> currencyOpt = viewer.get().findData(ACTIVE_CURRENCY);
      if(currencyOpt.isPresent()) {

        final Currency currency = (Currency)currencyOpt.get();
        int i = 19;
        for(final Denomination denomObj : currency.getDenominations().values()) {

          callback.getPage().addIcon(new DenominationIcon(i, denomObj));

          i+= 2;
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

      final Optional<Object> denomOpt = viewer.get().findData(ACTIVE_DENOMINATION);
      final Optional<Object> currencyOpt = viewer.get().findData(ACTIVE_CURRENCY);
      if(denomOpt.isPresent() && currencyOpt.isPresent()) {

        final Denomination denomination = (Denomination)denomOpt.get();

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                .lore(Collections.singletonList("Click to set singular name of denomination.")))
                .withSlot(10)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    denomination.setSingle(message.getMessage());
                    viewer.get().addData(ACTIVE_DENOMINATION, denomination);
                    return true;
                  }
                  message.getPlayer().message("Enter a singular name for the denomination:");
                  return false;

                }), new RunnableAction((run)->run.player().message("Enter a singular name for the denomination:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set singular name of denomination."))
                        .display(denomination.singular()))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                .lore(Collections.singletonList("Click to set plural name of denomination.")))
                .withSlot(11)
                .withActions(new ChatAction((message)->{

                  if(message.getPlayer().viewer().isPresent()) {

                    denomination.setPlural(message.getMessage());
                    viewer.get().addData(ACTIVE_DENOMINATION, denomination);
                    return true;
                  }
                  message.getPlayer().message("Enter a plural name for the denomination:");
                  return false;

                }), new RunnableAction((run)->run.player().message("Enter a plural name for the denomination:")))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set plural name of denomination."))
                        .display(denomination.plural()))
                .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                .display("Denomination Weight")
                .lore(Collections.singletonList("Sets weight for denomination")))
                .withSlot(12)
                .withActions(new SwitchPageAction(this.name, DENOMINATION_WEIGHT_SELECTION_PAGE))
                .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                        .lore(Collections.singletonList("Click to set weight of denomination."))
                        .display(denomination.weight().toString()))
                .build());

        if(denomination instanceof ItemDenomination itemDenomination) {

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(itemDenomination.getMaterial(), 1)
                  .lore(Collections.singletonList("Click to set material of denomination.")))
                  .withSlot(13)
                  .withActions(new SwitchPageAction(this.name, DENOMINATION_MATERIAL_PAGE))
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                  .lore(Collections.singletonList("Click to set display name of the denomination item. Optional.")))
                  .withSlot(14)
                  .withActions(new ChatAction((message)->{

                    if(message.getPlayer().viewer().isPresent()) {

                      itemDenomination.setName(message.getMessage());
                      return true;
                    }
                    message.getPlayer().message("Enter display name of the denomination item:");
                    return false;
                  }), new RunnableAction((run)->run.player().message("Enter display name of the denomination item:")))
                  .withItemProvider((provider)->{

                    final String message = (itemDenomination.getName() != null)? itemDenomination.getName() : "No Display";

                    return PluginCore.server().stackBuilder().of("PAPER", 1)
                            .lore(Collections.singletonList("Click to set display name of the denomination item. Optional."))
                            .display(message);
                  })
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                  .lore(Collections.singletonList("Click to set custom model of the denomination item. Optional.")))
                  .withSlot(15)
                  .withActions(new ChatAction((message)->{

                    if(message.getPlayer().viewer().isPresent()) {

                      try {

                        itemDenomination.setCustomModel(Integer.parseInt(message.getMessage()));
                        return true;
                      } catch(NumberFormatException ignore) {}
                    }
                    message.getPlayer().message("Enter custom model of the denomination item:");
                    return false;
                  }), new RunnableAction((run)->run.player().message("Enter custom model of the denomination item:")))
                  .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                          .lore(Collections.singletonList("Click to set custom model of the denomination item. Optional."))
                          .display(String.valueOf(itemDenomination.getCustomModel())))
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                  .lore(Collections.singletonList("Click to set the base64 texture to use if the material of the denomination item is PLAYER_HEAD. Not implemented Currently.")))
                  .withSlot(16)
                  .withActions(new ChatAction((message)->{

                    if(message.getPlayer().viewer().isPresent()) {
                      itemDenomination.setTexture(message.getMessage());
                      return true;
                    }
                    message.getPlayer().message("Enter the base64 texture to use if the material of the denomination item is PLAYER_HEAD:");
                    return false;
                  }), new RunnableAction((run)->run.player().message("Enter the base64 texture to use if the material of the denomination item is PLAYER_HEAD:")))
                  .withItemProvider((provider)->{

                    final String message = (itemDenomination.getTexture() != null)? itemDenomination.getTexture() : "No Texture";

                    return PluginCore.server().stackBuilder().of("PAPER", 1)
                            .lore(Collections.singletonList("Click to set the base64 texture to use if the material of the denomination item is PLAYER_HEAD. Not implemented Currently."))
                            .display(message);
                  })
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                  .lore(Collections.singletonList("Click to set the lore string this denomination item must have in order to be considered currency.")))
                  .withSlot(17)
                  .withActions(new ChatAction((message)->{

                    if(message.getPlayer().viewer().isPresent()) {
                      message.getPlayer().viewer().get().addData("DENOMINATION_LORE", message.getMessage());
                      return true;
                    }
                    message.getPlayer().message("Enter the lore string this denomination item must have in order to be considered currency. Use newline for a different line:");
                    return false;
                  }), new RunnableAction((run)->run.player().message("Enter the lore string this denomination item must have in order to be considered currency. Use newline for a different line:")))
                  .withItemProvider((provider)->{

                    final String message = (provider.viewer().isPresent())? (String)provider.viewer().get().dataOrDefault("DENOMINATION_LORE", "No Lore") : "No Lore";

                    return PluginCore.server().stackBuilder().of("PAPER", 1)
                            .lore(Collections.singletonList("Click to set the lore string this denomination item must have in order to be considered currency."))
                            .display(message);
                  })
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                  .lore(Collections.singletonList("Click to set flags of denomination item.")))
                  .withSlot(18)
                  .withActions(new SwitchPageAction(this.name, DENOMINATION_FLAGS_PAGE))
                  .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                  .lore(Collections.singletonList("Click to set enchantments of denomination item.")))
                  .withSlot(19)
                  .withActions(new SwitchPageAction(this.name, DENOMINATION_ENCHANTS_PAGE))
                  .build());
        }
      }
    }
  }
}