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

import net.kyori.adventure.text.Component;
import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.Note;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;
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
import net.tnemc.menu.core.icon.action.impl.RunnableAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.icon.impl.StateIcon;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.MessageHandler;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
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
    final Page main = new PageBuilder(1).build();
    main.setOpen((open)->{

      final UUID id = open.getPlayer().identifier();
      open.getPage().addIcon(new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
              .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Main.Currencies"), id)), this.name, CURRENCIES_PAGE, ActionType.ANY));
    });
    addPage(main);

    //Currency Page

    /*
     * Currency List Page
     */
    final Page currency = new PageBuilder(CURRENCIES_PAGE).build();
    setClose((close)->{

      final Optional<MenuViewer> viewer = close.getPlayer().viewer();
      if(viewer.isPresent()) {

        final Optional<Object> saved = viewer.get().findData("CURRENCY_SAVED_CONFIRM");
        if(saved.isEmpty()) {

          final Optional<PlayerProvider> provider = PluginCore.server().findPlayer(viewer.get().uuid());
          if(provider.isPresent()) {
            provider.get().message(new MessageData("Messages.Menu.MyEco.Main.CloseWarn"));
          }
        }
      }

    });
    currency.setOpen((open)->{

      if(open.getPlayer().viewer().isPresent()) {

        final UUID id = open.getPlayer().identifier();

        open.getPage().addIcon(new PreviousPageIcon(id, 0, this.name, 1, ActionType.ANY));

        open.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GREEN_WOOL", 1)
                                                       .customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Save"), id))
                                                       .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.SaveLore"), id))))
                                       .withActions(new RunnableAction((click)->{

                                         final File directory = new File(PluginCore.directory(), "currency");
                                         TNECore.eco().currency().getSaver().backupCurrency(directory);

                                         MISCUtils.deleteFolder(directory);

                                         TNECore.eco().currency().getSaver().saveCurrencies(directory);

                                         try {

                                           TNECore.eco().currency().getCurrencies().clear();
                                           TNECore.eco().currency().getCurIDMap().clear();

                                           TNECore.eco().currency().getLoader().loadCurrencies(directory);


                                           final Optional<MenuViewer> viewer = click.player().viewer();
                                           if(viewer.isPresent()) {
                                             viewer.get().addData("CURRENCY_SAVED_CONFIRM", true);
                                           }

                                           final Optional<PlayerProvider> provider = PluginCore.server().findPlayer(id);
                                           if(provider.isPresent()) {
                                             provider.get().message(new MessageData("Messages.Menu.MyEco.Main.Saved"));
                                           }

                                         } catch(final NoValidCurrenciesException ignore) { }
                                       }), new PageSwitchWithClose(this.name, -1))
                                       .withSlot(8)
                                       .build());

        open.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("BLACK_WOOL", 1)
                                                       .customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Reset"), id))
                                                       .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.ResetLore"), id))))
                                       .withActions(new RunnableAction((click)->{

                                         final File directory = new File(PluginCore.directory(), "currency");

                                         try {

                                           TNECore.eco().currency().getCurrencies().clear();
                                           TNECore.eco().currency().getCurIDMap().clear();

                                           TNECore.eco().currency().getLoader().loadCurrencies(directory);
                                         } catch(final NoValidCurrenciesException ignore) { }
                                       }), new PageSwitchWithClose(this.name, -1))
                                       .withSlot(6)
                                       .build());

        //add currency
        final SwitchPageIcon addCurrencyIcon = new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("ARROW", 1)
                .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.AddCurrencyDisplay"), id))
                .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.AddCurrencyLore"), id))), this.name, CURRENCY_EDIT_PAGE, ActionType.ANY);

        addCurrencyIcon.addAction(new ChatAction((message)->{

          if(message.getPlayer().viewer().isPresent()) {

            if(TNECore.eco().currency().findCurrency(message.getMessage()).isPresent()) {
              message.getPlayer().message("A currency with that identifier already exists! Enter an identifier for the currency:");
              return false;
            }

            final Currency newCurObj = new Currency(message.getMessage());
            message.getPlayer().viewer().get().addData(ACTIVE_CURRENCY, newCurObj);
            TNECore.eco().currency().addCurrency(newCurObj);
            return true;
          }
          message.getPlayer().message("Enter an identifier for the currency:");
          return false;
        }));
        addCurrencyIcon.addAction(new RunnableAction((run)->run.player().message("Enter an identifier for the currency:")));
        currency.addIcon(addCurrencyIcon);

        int i = 19;
        for(final Currency curObj : TNECore.eco().currency().currencies()) {

          currency.addIcon(new CurrencyIcon(id, i, curObj));

          i += 2;
        }
      }
    });

    addPage(currency);

    /*
     * Currency Edit Page
     */
    final Page currencyEditor = new PageBuilder(CURRENCY_EDIT_PAGE).build();

    currencyEditor.setOpen((open)->{
      if(open.getPlayer().viewer().isPresent()) {

        final UUID id = open.getPlayer().identifier();

        currencyEditor.addIcon(new PreviousPageIcon(id, 0, this.name, CURRENCIES_PAGE, ActionType.ANY));

        //denominations
        currencyEditor.addIcon(new SwitchPageIcon(10, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.EditDenominationsDisplay"), id)), this.name, DENOMINATIONS_PAGE, ActionType.ANY));

        //CURRENCY_INFO_EDIT_PAGE
        currencyEditor.addIcon(new SwitchPageIcon(11, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.BasicCurrencyInfoDisplay"), id)), this.name, CURRENCY_INFO_EDIT_PAGE, ActionType.ANY));

        //CURRENCY_FORMAT_EDIT_PAGE
        currencyEditor.addIcon(new SwitchPageIcon(12, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.CurrencyFormatOptionsDisplay"), id)), this.name, CURRENCY_FORMAT_EDIT_PAGE, ActionType.ANY));

        //CURRENCY_NOTE_EDIT_PAGE
        currencyEditor.addIcon(new SwitchPageIcon(14, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.CurrencyNoteOptionsDisplay"), id)), this.name, CURRENCY_NOTE_EDIT_PAGE, ActionType.ANY));

        final Optional<Object> currencyOpt = open.getPlayer().viewer().get().findData(ACTIVE_CURRENCY);
        if(currencyOpt.isPresent()) {

          final Currency currencyObject = (Currency)currencyOpt.get();

          final MessageData typeMessage = new MessageData("Messages.Menu.MyEco.Currency.CurrencyTypeDisplay");
          typeMessage.addReplacement("$type", currencyObject.getType());

          //CURRENCY_TYPE_EDIT_PAGE
          currencyEditor.addIcon(new SwitchPageIcon(13, PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                  .customName(MessageHandler.grab(typeMessage, id))
                  .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.SetCurrencyTypeLore"), id))), this.name, CURRENCY_TYPE_EDIT_PAGE, ActionType.ANY));
        }
      }
    });
    addPage(currencyEditor);

    /*
     * Currency Edit Info
     */
    final Page currencyInfoEditPage = new PageBuilder(CURRENCY_INFO_EDIT_PAGE).build();
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

    currencyTypeEditPage.setOpen(open->{
      if(open.getPlayer().viewer().isPresent()) {


        final UUID id = open.getPlayer().identifier();

        open.getPage().addIcon(new PreviousPageIcon(id, 0, this.name, CURRENCY_EDIT_PAGE, ActionType.ANY));
      }
    });

    int i = 19;
    for(final CurrencyType type : TNECore.eco().currency().getTypes().values()) {

      final SwitchPageIcon switchIcon = new SwitchPageIcon(i, PluginCore.server().stackBuilder().of("PAPER", 1)
              .customName(Component.text("Type: " + type.name()))
              .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.SetCurrencyTypeLore"), UUID.randomUUID()))), this.name, CURRENCY_EDIT_PAGE, ActionType.ANY, false);
      switchIcon.addAction(new RunnableAction((click)->{

        if(click.player().viewer().isPresent()) {

          final Optional<Object> currencyOpt = click.player().viewer().get().findData(ACTIVE_CURRENCY);
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
    denominationsPage.setOpen((this::handleDenominationOpen));

    addPage(denominationsPage);

    /*
     * Denominations Edit Page
     */
    final Page denominationsEditPage = new PageBuilder(DENOMINATION_EDIT_PAGE).build();
    denominationsEditPage.setOpen((this::handleDenominationEditOpen));
    addPage(denominationsEditPage);

    final Page denominationMaterialPage = new PageBuilder(DENOMINATION_MATERIAL_PAGE).build();
    denominationMaterialPage.setOpen((open->new MaterialSelectionPageCallback("DENOMINATION_MATERIAL", this.name, this.name, DENOMINATION_MATERIAL_PAGE, DENOMINATION_EDIT_PAGE, "DENOMINATION_MATERIAL_PAGE", this.rows, (selection)->{

      if(open.getPlayer().viewer().isPresent()) {

        final Optional<Object> denomOpt = open.getPlayer().viewer().get().findData(ACTIVE_DENOMINATION);
        if(denomOpt.isPresent()) {

          final Denomination denomObj = (Denomination)denomOpt.get();
          if(denomObj instanceof final ItemDenomination itemDenomination) {
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
          if(denomObj instanceof final ItemDenomination itemDenomination && !selection.getValue().isEmpty()) {
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
          if(denomObj instanceof final ItemDenomination itemDenomination && !selection.getValue().isEmpty()) {
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
   * Import Items
   */
  private void handleCurrencyEditInfoOpen(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final UUID id = viewer.get().uuid();

      callback.getPage().addIcon(new PreviousPageIcon(id, 0, this.name, CURRENCY_EDIT_PAGE, ActionType.ANY));

      final Optional<Object> currencyOpt = viewer.get().findData(ACTIVE_CURRENCY);
      if(currencyOpt.isPresent()) {

        final Currency currencyObject = (Currency)currencyOpt.get();

        //currency name icon
        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.SetIdentifierLore"), id))))
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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.SetIdentifierLore"), id)))
                                                   .customName(Component.text(currencyObject.getIdentifier())))
                                           .build());
        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                           .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.StartingBalanceDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.StartingBalanceLore"), id))))
                                           .withSlot(19)
                                           .withActions(new SwitchPageAction(this.name, CURRENCY_INFO_STARTING_SELECTION_PAGE))
                                           .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                           .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.MaximumBalanceDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.MaximumBalanceLore"), id))))
                                           .withSlot(20)
                                           .withActions(new SwitchPageAction(this.name, CURRENCY_INFO_MAX_SELECTION_PAGE))
                                           .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                           .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.MinimumBalanceDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.MinimumBalanceLore"), id))))
                                           .withSlot(21)
                                           .withActions(new SwitchPageAction(this.name, CURRENCY_INFO_MIN_SELECTION_PAGE))
                                           .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(currencyObject.getIconMaterial(), 1)
                                                           .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.SetIconMaterialDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.SetIconMaterialLore"), id))))
                                           .withSlot(22)
                                           .withActions(new SwitchPageAction(this.name, CURRENCY_ICON_MATERIAL_PAGE))
                                           .build());

        if(currencyObject instanceof final ItemCurrency itemCurrency) {

          final AbstractItemStack<?> disabledStack = PluginCore.server().stackBuilder().customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Disabled"), id)).of("RED_WOOL", 1);
          final AbstractItemStack<?> enabledStack = PluginCore.server().stackBuilder().customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Enabled"), id)).of("GREEN_WOOL", 1);

          //ender chest icon
          final String enderState = (itemCurrency.canEnderChest())? "ENABLED" : "DISABLED";
          final StateIcon enderchest = new StateIcon(disabledStack, null, "CURRENCY_ENDER", enderState, (currentState)->{
            if(currentState.toUpperCase(Locale.ROOT).equals("ENABLED")) {
              itemCurrency.setEnderChest(false);
              return "DISABLED";
            }
            itemCurrency.setEnderChest(true);
            return "ENABLED";
          });
          enderchest.setSlot(23);
          enderchest.addState("DISABLED",
                              disabledStack
                                      .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.EnderChestDisabledDisplay"), id))
                                      .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.EnderChestDisabledLore"), id))));

          enderchest.addState("ENABLED",
                              enabledStack
                                      .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.EnderChestEnabledDisplay"), id))
                                      .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.EnderChestEnabledLore"), id))));

          callback.getPage().addIcon(enderchest);

          //ender chest icon
          final String fillState = (itemCurrency.isEnderFill())? "ENABLED" : "DISABLED";
          final StateIcon enderFill = new StateIcon(disabledStack, null, "CURRENCY_ENDER_FILL", fillState, (currentState)->{
            if(currentState.toUpperCase(Locale.ROOT).equals("ENABLED")) {
              itemCurrency.setEnderChest(false);
              return "DISABLED";
            }
            itemCurrency.setEnderChest(true);
            return "ENABLED";
          });
          enderFill.setSlot(24);
          enderFill.addState("DISABLED",
                             disabledStack
                                     .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.EnderFillDisabledDisplay"), id))
                                     .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.EnderFillDisabledLore"), id))));

          enderFill.addState("ENABLED",
                             enabledStack
                                     .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.EnderFillEnabledDisplay"), id))
                                     .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.EnderFillEnabledLore"), id))));
          callback.getPage().addIcon(enderFill);

          //import item icon
          final String importState = (itemCurrency.isImportItem())? "ENABLED" : "DISABLED";
          final StateIcon importFill = new StateIcon(disabledStack, null, "CURRENCY_ENDER_FILL", importState, (currentState)->{
            if(currentState.toUpperCase(Locale.ROOT).equals("ENABLED")) {
              itemCurrency.setImportItem(false);
              return "DISABLED";
            }
            itemCurrency.setImportItem(true);
            return "ENABLED";
          });
          importFill.setSlot(25);
          importFill.addState("DISABLED",
                              disabledStack
                                      .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.ImportDisabledDisplay"), id))
                                      .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.ImportDisabledLore"), id))));

          importFill.addState("ENABLED",
                              enabledStack
                                      .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.ImportEnabledDisplay"), id))
                                      .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditInfo.ImportEnabledLore"), id))));
          callback.getPage().addIcon(importFill);
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
   * Show Balance
   */
  private void handleCurrencyEditFormatOpen(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final UUID id = viewer.get().uuid();

      callback.getPage().addIcon(new PreviousPageIcon(id, 0, this.name, CURRENCY_EDIT_PAGE, ActionType.ANY));

      final Optional<Object> currencyOpt = viewer.get().findData(ACTIVE_CURRENCY);
      if(currencyOpt.isPresent()) {

        final Currency currencyObject = (Currency)currencyOpt.get();

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                           .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.BalanceFormatDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.BalanceFormatLore"), id))))
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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SetSymbolLore"), id)))
                                                   .customName(Component.text(currencyObject.getSymbol())))
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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SetPrefixesLore"), id)))
                                                   .customName(Component.text(currencyObject.getPrefixes())))
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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SetDecimalLore"), id)))
                                                   .customName(Component.text(currencyObject.getDecimal())))
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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SetMajorSingularDisplayLore"), id)))
                                                   .customName(Component.text(currencyObject.getDisplay())))
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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SetMajorPluralDisplayLore"), id)))
                                                   .customName(Component.text(currencyObject.getDisplayPlural())))
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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SetMinorSingularDisplayLore"), id)))
                                                   .customName(Component.text(currencyObject.getDisplayMinor())))
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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SetMinorPluralDisplayLore"), id)))
                                                   .customName(Component.text(currencyObject.getDisplayMinorPlural())))
                                           .build());

        final AbstractItemStack<?> disabledStack = PluginCore.server().stackBuilder().customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Disabled"), id)).of("RED_WOOL", 1);
        final AbstractItemStack<?> enabledStack = PluginCore.server().stackBuilder().customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Enabled"), id)).of("GREEN_WOOL", 1);
        final String state = (currencyObject.isSeparateMajor())? "ENABLED" : "DISABLED";

        //ender chest icon
        final StateIcon majorSeparate = new StateIcon(disabledStack, null, "CURRENCY_SEPARATE", state, (currentState)->{
          if(currentState.toUpperCase(Locale.ROOT).equals("ENABLED")) {
            currencyObject.setSeparateMajor(false);
            return "DISABLED";
          }
          currencyObject.setSeparateMajor(true);
          return "ENABLED";
        });
        majorSeparate.setSlot(18);
        majorSeparate.addState("DISABLED",
                               disabledStack
                                       .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SeparateMajorDisabledDisplay"), id))
                                       .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SeparateMajorDisabledLore"), id))));

        majorSeparate.addState("ENABLED",
                               enabledStack
                                       .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SeparateMajorEnabledDisplay"), id))
                                       .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SeparateMajorEnabledLore"), id))));

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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.SetMajorSeparatorLore"), id)))
                                                   .customName(Component.text(currencyObject.getMajorSeparator())))
                                           .build());

        //ender chest icon
        final String balanceState = (currencyObject.isBalanceShow())? "ENABLED" : "DISABLED";
        final StateIcon showBalance = new StateIcon(disabledStack, null, "CURRENCY_SHOW_BALANCE", balanceState, (currentState)->{
          if(currentState.toUpperCase(Locale.ROOT).equals("ENABLED")) {
            currencyObject.setBalanceShow(false);
            return "DISABLED";
          }
          currencyObject.setBalanceShow(true);
          return "ENABLED";
        });
        showBalance.setSlot(20);
        showBalance.addState("DISABLED",
                             disabledStack
                                     .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.BalanceDisabledDisplay"), id))
                                     .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.BalanceDisabledLore"), id))));

        showBalance.addState("ENABLED",
                             enabledStack
                                     .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.BalanceEnabledDisplay"), id))
                                     .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditFormat.BalanceEnabledLore"), id))));

        callback.getPage().addIcon(showBalance);
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

      final UUID id = viewer.get().uuid();

      callback.getPage().addIcon(new PreviousPageIcon(id, 0, this.name, CURRENCY_EDIT_PAGE, ActionType.ANY));

      final Optional<Object> currencyOpt = viewer.get().findData(ACTIVE_CURRENCY);
      if(currencyOpt.isPresent()) {

        final Currency currency = (Currency)currencyOpt.get();

        final AbstractItemStack<?> disabledStack = PluginCore.server().stackBuilder().customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Disabled"), id)).of("RED_WOOL", 1);
        final AbstractItemStack<?> enabledStack = PluginCore.server().stackBuilder().customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.Enabled"), id)).of("GREEN_WOOL", 1);

        //ender chest icon
        final String noteState = (currency.isNotable())? "ENABLED" : "DISABLED";
        final StateIcon notableIcon = new StateIcon(disabledStack, null, "CURRENCY_NOTE", noteState, (currentState)->{
          if(currentState.toUpperCase(Locale.ROOT).equals("ENABLED")) {
            currency.setNote(null);
            return "DISABLED";
          }
          currency.setNote(new Note("PAPER", BigDecimal.ZERO, new TaxEntry("flat", 0.0)));
          return "ENABLED";
        });
        notableIcon.setSlot(23);
        notableIcon.addState("DISABLED",
                             disabledStack
                                     .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NotableDisabledDisplay"), id))
                                     .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NotableDisabledLore"), id))));

        notableIcon.addState("ENABLED",
                             enabledStack
                                     .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NotableEnabledDisplay"), id))
                                     .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NotableEnabledLore"), id))));
        callback.getPage().addIcon(notableIcon);

        if(currency.getNote().isPresent()) {

          final String material = (currency.getNote().isPresent())? currency.getNote().get().getMaterial() : "PAPER";
          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(material, 1)
                                                             .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NoteMaterialDisplay"), id))
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NoteMaterialLore"), id))))
                                             .withSlot(10)
                                             .withActions(new SwitchPageAction(this.name, CURRENCY_NOTE_MATERIAL_PAGE))
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.FlagsDisplay"), id))
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NoteFlagsLore"), id))))
                                             .withSlot(11)
                                             .withActions(new SwitchPageAction(this.name, CURRENCY_NOTE_FLAGS_PAGE))
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.EnchantsDisplay"), id))
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NoteEnchantsLore"), id))))
                                             .withSlot(12)
                                             .withActions(new SwitchPageAction(this.name, CURRENCY_NOTE_ENCHANTS_PAGE))
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NoteMinDisplay"), id))
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.MinLore"), id))))
                                             .withSlot(13)
                                             .withActions(new SwitchPageAction(this.name, CURRENCY_NOTE_MIN_SELECTION_PAGE))
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("ARROW", 1)
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.CustomModelLore"), id))))
                                             .withSlot(14)
                                             .withActions(new ChatAction((message)->{

                                               if(message.getPlayer().viewer().isPresent()) {

                                                 try {

                                                   currency.getNote().get().setCustomModelData(Integer.parseInt(message.getMessage()));

                                                   return true;
                                                 } catch(final NumberFormatException ignore) { }
                                               }
                                               message.getPlayer().message("Enter custom model of the note item:");
                                               return false;
                                             }), new RunnableAction((run)->run.player().message("Enter custom model of the note item:")))
                                             .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                                                     .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.CustomModelLore"), id)))
                                                     .customName(Component.text(String.valueOf(currency.getNote().get().getCustomModelData()))))
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NoteFeeDisplay"), id))
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.EditNote.NoteFeeLore"), id))))
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

      final UUID id = viewer.get().uuid();

      callback.getPage().addIcon(new PreviousPageIcon(id, 0, this.name, CURRENCY_EDIT_PAGE, ActionType.ANY));

      final Optional<Object> currencyOpt = viewer.get().findData(ACTIVE_CURRENCY);
      if(currencyOpt.isPresent()) {

        //add denomination
        final SwitchPageIcon addDenominationIcon = new SwitchPageIcon(2, PluginCore.server().stackBuilder().of("ARROW", 1)
                .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.AddDenominationDisplay"), id))
                .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currency.AddDenominationLore"), id))), this.name, DENOMINATIONS_PAGE, ActionType.ANY);
        addDenominationIcon.addAction(new ChatAction((message)->{

          if(message.getPlayer().viewer().isPresent()) {

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
            } catch(final NumberFormatException ignore) { }
          }
          message.getPlayer().message("Enter a valid decimal for the weight of the denomination:");
          return false;
        }));
        addDenominationIcon.addAction(new RunnableAction((run)->run.player().message("Enter a valid decimal for the weight of the denomination:")));
        callback.getPage().addIcon(addDenominationIcon);

        final Currency currency = (Currency)currencyOpt.get();
        int i = 19;
        for(final Denomination denomObj : currency.getDenominations().values()) {

          callback.getPage().addIcon(new DenominationIcon(id, i, denomObj));

          i += 2;
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

      final UUID id = viewer.get().uuid();

      callback.getPage().addIcon(new PreviousPageIcon(id, 0, this.name, DENOMINATIONS_PAGE, ActionType.ANY));

      final Optional<Object> denomOpt = viewer.get().findData(ACTIVE_DENOMINATION);
      final Optional<Object> currencyOpt = viewer.get().findData(ACTIVE_CURRENCY);
      if(denomOpt.isPresent() && currencyOpt.isPresent()) {

        final Denomination denomination = (Denomination)denomOpt.get();

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.SingularNameLore"), id))))
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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.SingularNameLore"), id)))
                                                   .customName(Component.text(denomination.singular())))
                                           .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.PluralNameLore"), id))))
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
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.PluralNameLore"), id)))
                                                   .customName(Component.text(denomination.plural())))
                                           .build());

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                           .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.SetWeightDisplay"), id))
                                                           .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.SetWeightLore"), id))))
                                           .withSlot(12)
                                           .withActions(new SwitchPageAction(this.name, DENOMINATION_WEIGHT_SELECTION_PAGE))
                                           .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                                                   .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.SetWeightLore"), id)))
                                                   .customName(Component.text(denomination.weight().toString())))
                                           .build());

        if(denomination instanceof final ItemDenomination itemDenomination) {

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(itemDenomination.getMaterial(), 1)
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.SetMaterialLore"), id))))
                                             .withSlot(13)
                                             .withActions(new SwitchPageAction(this.name, DENOMINATION_MATERIAL_PAGE))
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.DisplayNameLore"), id))))
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
                                                       .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.DisplayNameLore"), id)))
                                                       .customName(Component.text(message));
                                             })
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.CustomModelLore"), id))))
                                             .withSlot(15)
                                             .withActions(new ChatAction((message)->{

                                               if(message.getPlayer().viewer().isPresent()) {

                                                 try {

                                                   itemDenomination.setCustomModel(Integer.parseInt(message.getMessage()));
                                                   return true;
                                                 } catch(final NumberFormatException ignore) { }
                                               }
                                               message.getPlayer().message("Enter custom model of the denomination item:");
                                               return false;
                                             }), new RunnableAction((run)->run.player().message("Enter custom model of the denomination item:")))
                                             .withItemProvider((provider)->PluginCore.server().stackBuilder().of("PAPER", 1)
                                                     .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.CustomModelLore"), id)))
                                                     .customName(Component.text(String.valueOf(itemDenomination.getCustomModel()))))
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.SetBase64TextureLore"), id))))
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
                                                       .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.SetBase64TextureLore"), id)))
                                                       .customName(Component.text(message));
                                             })
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.LoreStringLore"), id))))
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
                                                       .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.LoreStringLore"), id)))
                                                       .customName(Component.text(message));
                                             })
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.FlagsDisplay"), id))
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.FlagsLore"), id))))
                                             .withSlot(18)
                                             .withActions(new SwitchPageAction(this.name, DENOMINATION_FLAGS_PAGE))
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .customName(MessageHandler.grab(new MessageData("Messages.Menu.Shared.EnchantsDisplay"), id))
                                                             .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.DenomEdit.EnchantsLore"), id))))
                                             .withSlot(19)
                                             .withActions(new SwitchPageAction(this.name, DENOMINATION_ENCHANTS_PAGE))
                                             .build());
        }
      }
    }
  }
}