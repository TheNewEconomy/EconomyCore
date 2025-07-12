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
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.source.PlayerSource;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Note;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.currency.type.MixedType;
import net.tnemc.core.currency.type.VirtualType;
import net.tnemc.core.menu.handlers.AmountSelectionHandler;
import net.tnemc.core.menu.icons.shared.PreviousPageIcon;
import net.tnemc.core.menu.page.mybal.MyBalAmountSelectionPage;
import net.tnemc.core.menu.page.shared.AccountSelectionPage;
import net.tnemc.core.menu.page.shared.CurrencySelectionPage;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.menu.core.Menu;
import net.tnemc.menu.core.Page;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.builder.PageBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.Icon;
import net.tnemc.menu.core.icon.action.ActionType;
import net.tnemc.menu.core.icon.action.IconAction;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.MessageHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

/**
 * MyBalMenu
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class MyBalMenu extends Menu {

  public static final int BALANCE_ACTIONS_PAGE = 2;
  public static final int BALANCE_ACTION_CONVERT_CURRENCY_PAGE = 3;
  public static final int BALANCE_ACTION_CONVERT_AMOUNT_PAGE = 4;
  public static final int BALANCE_ACTION_DEPOSIT_AMOUNT_PAGE = 5;
  public static final int BALANCE_ACTION_WITHDRAW_AMOUNT_PAGE = 6;
  public static final int BALANCE_BREAKDOWN_PAGE = 7;
  public static final int BALANCE_PAY_PAGE = 8;
  public static final int BALANCE_PAY_AMOUNT_PAGE = 9;
  public static final int BALANCE_NOTE_AMOUNT_PAGE = 10;

  public static final String ACTION_ACCOUNT_ID = "ACTION_ACCOUNT";
  public static final String ACTION_CURRENCY = "ACTION_CURRENCY";
  public static final String ACTION_CONVERT_CURRENCY = "ACTION_CURRENCY";
  public static final String ACTION_MAX_HOLDINGS = "ACTION_MAX_HOLDINGS";
  public static final String ACTION_HOLDINGS = "ACTION_HOLDINGS";

  public MyBalMenu() {

    this.name = "my_bal";
    this.title = "My Bal";
    this.rows = 6;

    /*
     * Main Page
     */
    final Page main = new PageBuilder(1).build();
    main.setOpen(this::handleMainPage);
    addPage(main);

    final Page balanceActionsPage = new PageBuilder(BALANCE_ACTIONS_PAGE).build();
    balanceActionsPage.setOpen(this::actionsPage);
    addPage(balanceActionsPage);

    final Page balanceConvertCurrencyPage = new PageBuilder(BALANCE_ACTION_CONVERT_CURRENCY_PAGE).build();
    balanceConvertCurrencyPage.setOpen((open->new CurrencySelectionPage(ACTION_CONVERT_CURRENCY, this.name, this.name, BALANCE_ACTION_CONVERT_CURRENCY_PAGE, BALANCE_ACTION_CONVERT_AMOUNT_PAGE, "CONVERT_CURRENCY_SELECTION", this.rows).handle(open)));
    addPage(balanceConvertCurrencyPage);

    final Page balanceConvertAmountPage = new PageBuilder(BALANCE_ACTION_CONVERT_AMOUNT_PAGE).build();
    balanceConvertAmountPage.setOpen((open->new MyBalAmountSelectionPage(ACTION_HOLDINGS, this.name, this.name, BALANCE_ACTION_CONVERT_AMOUNT_PAGE, -1, this::convert).handle(open)));
    addPage(balanceConvertAmountPage);

    final Page balanceDepositAmountPage = new PageBuilder(BALANCE_ACTION_DEPOSIT_AMOUNT_PAGE).build();
    balanceDepositAmountPage.setOpen((open->new MyBalAmountSelectionPage(ACTION_HOLDINGS, this.name, this.name, BALANCE_ACTION_DEPOSIT_AMOUNT_PAGE, -1, this::deposit).handle(open)));
    addPage(balanceDepositAmountPage);

    final Page balanceWithdrawAmountPage = new PageBuilder(BALANCE_ACTION_WITHDRAW_AMOUNT_PAGE).build();
    balanceWithdrawAmountPage.setOpen((open->new MyBalAmountSelectionPage(ACTION_HOLDINGS, this.name, this.name, BALANCE_ACTION_WITHDRAW_AMOUNT_PAGE, -1, this::withdraw).handle(open)));
    addPage(balanceWithdrawAmountPage);

    final Page balanceBreakdownPage = new PageBuilder(BALANCE_BREAKDOWN_PAGE).build();
    balanceBreakdownPage.setOpen(this::handleBreakdownPage);
    addPage(balanceBreakdownPage);

    final Page balancePayPage = new PageBuilder(BALANCE_PAY_PAGE).build();
    balancePayPage.setOpen((open->new AccountSelectionPage(ACTION_ACCOUNT_ID, this.name, this.name, BALANCE_PAY_PAGE, BALANCE_PAY_AMOUNT_PAGE, "PAY_ACCOUNT_NAME_SELECTION", this.rows).handle(open)));
    addPage(balancePayPage);

    final Page balancePayAmountPage = new PageBuilder(BALANCE_PAY_AMOUNT_PAGE).build();
    balancePayAmountPage.setOpen((open->new MyBalAmountSelectionPage(ACTION_HOLDINGS, this.name, this.name, BALANCE_PAY_AMOUNT_PAGE, -1, this::pay).handle(open)));
    addPage(balancePayAmountPage);

    final Page noteAmountPage = new PageBuilder(BALANCE_NOTE_AMOUNT_PAGE).build();
    noteAmountPage.setOpen((open->new MyBalAmountSelectionPage(ACTION_HOLDINGS, this.name, this.name, BALANCE_NOTE_AMOUNT_PAGE, -1, this::note).handle(open)));
    addPage(noteAmountPage);
  }

  public void handleMainPage(final PageOpenCallback callback) {

    final Optional<Account> account = TNECore.eco().account().findAccount(callback.getPlayer().identifier());

    if(account.isPresent()) {
      int i = 10;
      for(final Currency curObj : TNECore.eco().currency().currencies()) {

        callback.getPage().addIcon(buildBalanceIcon(i, curObj, account.get()));

        i += 2;
      }
    }
  }

  protected void actionsPage(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final UUID id = viewer.get().uuid();
      callback.getPage().addIcon(new PreviousPageIcon(id, 0, this.name, 1, ActionType.ANY));

      final Optional<Account> account = TNECore.eco().account().findAccount(callback.getPlayer().identifier());
      if(account.isPresent()) {

        final Optional<Object> currencyUUID = viewer.get().findData(ACTION_CURRENCY);
        if(currencyUUID.isPresent()) {

          final Optional<Currency> currencyOptional = TNECore.eco().currency().find((UUID)currencyUUID.get());
          if(currencyOptional.isPresent()) {

            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                               .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Actions.ConvertDisplay"), id))
                                                               .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Actions.Convert"), id))))
                                               .withSlot(10)
                                               .withActions(new SwitchPageAction(this.name, BALANCE_ACTION_CONVERT_CURRENCY_PAGE))
                                               .build());

            if(currencyOptional.get().type().supportsExchange()) {

              callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                                 .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Actions.DepositDisplay"), id))
                                                                 .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Actions.Deposit"), id))))
                                                 .withSlot(12)
                                                 .withActions(new SwitchPageAction(this.name, BALANCE_ACTION_DEPOSIT_AMOUNT_PAGE))
                                                 .build());

              callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                                 .customName(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Actions.WithdrawDisplay"), id))
                                                                 .lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Actions.Withdraw"), id))))
                                                 .withSlot(14)
                                                 .withActions(new SwitchPageAction(this.name, BALANCE_ACTION_WITHDRAW_AMOUNT_PAGE))
                                                 .build());
            }
          }
        }
      }
    }
  }

  protected void handleBreakdownPage(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      final UUID id = viewer.get().uuid();
      callback.getPage().addIcon(new PreviousPageIcon(id, 0, this.name, 1, ActionType.ANY));

      final Optional<Account> account = TNECore.eco().account().findAccount(callback.getPlayer().identifier());
      if(account.isPresent()) {

        final Optional<Object> currencyUUID = viewer.get().findData(ACTION_CURRENCY);
        if(currencyUUID.isPresent()) {

          final Optional<Currency> currencyOptional = TNECore.eco().currency().find((UUID)currencyUUID.get());
          if(currencyOptional.isPresent()) {

            int i = 10;
            for(final HoldingsEntry entry : account.get().getHoldings(TNECore.eco().region().defaultRegion(), (UUID)currencyUUID.get())) {

              final MessageData balMessage = new MessageData("Messages.Menu.MyBal.Breakdown.Balance");
              balMessage.addReplacement("$balance", entry.getAmount());

              callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                                 .customName(Component.text(entry.getHandler().id()))
                                                                 .lore(Collections.singletonList(MessageHandler.grab(balMessage, id))))
                                                 .withSlot(i).build());
              i += 2;
            }
          }
        }
      }
    }
  }

  /*
   * Virtual Right click = create note
   */
  protected Icon buildBalanceIcon(final int slot, final Currency currency, final Account account) {

    final UUID id = account.getIdentifier();

    final LinkedList<Component> lore = new LinkedList<>();
    final LinkedList<IconAction> actions = new LinkedList<>();

    actions.add(new DataAction(ACTION_CURRENCY, currency.getUid()));
    actions.add(new DataAction(ACTION_MAX_HOLDINGS, account.getHoldingsTotal(TNECore.eco().region().defaultRegion(), currency.getUid())));

    final MessageData balance = new MessageData("Messages.Menu.MyBal.Main.Balance");

    final HoldingsEntry entry = new HoldingsEntry(PluginCore.server().defaultWorld(), currency.getUid(),
                                                  account.getHoldingsTotal(TNECore.eco().region().defaultRegion(),
                                                                           currency.getUid()),
                                                  EconomyManager.NORMAL);

    balance.addReplacement("$balance", CurrencyFormatter.format(account, entry));

    lore.add(MessageHandler.grab(balance, id));

    lore.add(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Main.Pay"), id));
    actions.add(new SwitchPageAction(this.name, BALANCE_PAY_PAGE, ActionType.LEFT_CLICK));

    //Drop Action(Other Currency Actions)
    lore.add(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Main.Other"), id));
    actions.add(new SwitchPageAction(this.name, BALANCE_ACTIONS_PAGE, ActionType.DROP));

    if(currency.type() instanceof VirtualType && currency.isNotable()) {
      lore.add(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Main.Note"), id));
      actions.add(new SwitchPageAction(this.name, BALANCE_NOTE_AMOUNT_PAGE, ActionType.RIGHT_CLICK));
    }

    if(currency.type().supportsItems()) {
      lore.add(MessageHandler.grab(new MessageData("Messages.Menu.MyBal.Main.Breakdown"), id));
      actions.add(new SwitchPageAction(this.name, BALANCE_BREAKDOWN_PAGE, ActionType.RIGHT_CLICK));
    }

    return new IconBuilder(PluginCore.server().stackBuilder().of(currency.getIconMaterial(), 1)
                                   .customName(Component.text(currency.getIdentifier())).lore(lore))
            .withSlot(slot)
            .withActions(actions.toArray(new IconAction[actions.size()])).build();
  }

  /*
   * Our transaction methods.
   * TODO: Make this and the methods in our command handler into one somehow...
   */
  protected void convert(final AmountSelectionHandler handler) {

    final Optional<MenuViewer> viewer = handler.getClick().player().viewer();
    if(viewer.isPresent()) {
      final UUID playerUUID = viewer.get().uuid();

      final Optional<Object> currencyUUID = viewer.get().findData(ACTION_CURRENCY);
      final Optional<Object> currencyConvertUUID = viewer.get().findData(ACTION_CONVERT_CURRENCY);
      final Optional<Object> receiverUUID = viewer.get().findData(ACTION_ACCOUNT_ID + "_ID");
      if(currencyUUID.isPresent() && currencyConvertUUID.isPresent() && receiverUUID.isPresent()) {

        final Optional<Currency> fromCurrency = TNECore.eco().currency().find((UUID)currencyConvertUUID.get());
        final Optional<Currency> currencyOptional = TNECore.eco().currency().find((UUID)currencyUUID.get());
        final Optional<PlayerProvider> player = PluginCore.server().findPlayer(handler.getClick().player().identifier());
        if(currencyOptional.isPresent() && fromCurrency.isPresent() && player.isPresent()) {

          if(EconomyManager.limitCurrency()) {
            if(!player.get().hasPermission("tne.money.convert.to." + currencyOptional.get().getIdentifier())) {
              final MessageData data = new MessageData("Messages.Account.BlockedAction");
              data.addReplacement("$action", "convert to");
              data.addReplacement("$currency", currencyOptional.get().getDisplay());
              player.get().message(data);
              return;
            }

            if(!player.get().hasPermission("tne.money.convert.from." + fromCurrency.get().getIdentifier())) {
              final MessageData data = new MessageData("Messages.Account.BlockedAction");
              data.addReplacement("$action", "convert from");
              data.addReplacement("$currency", fromCurrency.get().getDisplay());
              player.get().message(data);
              return;
            }
          }

          if(handler.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            player.get().message(new MessageData("Messages.Money.Negative"));
            return;
          }

          if(currencyOptional.get().getUid().equals(fromCurrency.get().getUid())) {
            player.get().message(new MessageData("Messages.Money.ConvertSame"));
            return;
          }

          final Optional<Account> senderAccount = TNECore.eco().account().findAccount(playerUUID);
          if(senderAccount.isEmpty()) {
            final MessageData data = new MessageData("Messages.General.NoPlayer");
            data.addReplacement("$player", player.get().getName());
            player.get().message(data);
            return;
          }

          final Optional<BigDecimal> converted = fromCurrency.get().convertValue(currencyOptional.get().getIdentifier(), handler.getAmount());
          if(converted.isEmpty()) {
            final MessageData data = new MessageData("Messages.Money.NoConversion");
            data.addReplacement("$converted", currencyOptional.get().getIdentifier());
            player.get().message(data);
            return;
          }

          final String region = "";
          final HoldingsModifier modifier = new HoldingsModifier(region,
                                                                 currencyOptional.get().getUid(),
                                                                 converted.get().setScale(currencyOptional.get().getDecimalPlaces(), RoundingMode.DOWN)
          );

          final HoldingsModifier modifierFrom = new HoldingsModifier(region,
                                                                     fromCurrency.get().getUid(),
                                                                     handler.getAmount().setScale(currencyOptional.get().getDecimalPlaces(), RoundingMode.DOWN).negate()
          );

          final Transaction transaction = new Transaction("convert")
                  .from(senderAccount.get(), modifierFrom)
                  .to(senderAccount.get(), modifier)
                  .processor(EconomyManager.baseProcessor())
                  .source(new PlayerSource(playerUUID));

          final Optional<Receipt> receipt = processTransaction(player.get(), transaction, senderAccount.get().getName(), handler.getAmount());
          if(receipt.isPresent()) {
            final MessageData data = new MessageData("Messages.Money.Converted");
            data.addReplacement("$from_amount", handler.getAmount().toPlainString());
            data.addReplacement("$amount", CurrencyFormatter.format(senderAccount.get(),
                                                                    modifierFrom.asEntry()));
            player.get().message(data);
          }
        }
      }
    }
  }

  protected void deposit(final AmountSelectionHandler handler) {

    final Optional<MenuViewer> viewer = handler.getClick().player().viewer();
    if(viewer.isPresent()) {
      final UUID playerUUID = viewer.get().uuid();

      final Optional<Object> currencyUUID = viewer.get().findData(ACTION_CURRENCY);
      final Optional<Object> receiverUUID = viewer.get().findData(ACTION_ACCOUNT_ID + "_ID");
      if(currencyUUID.isPresent() && receiverUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().find((UUID)currencyUUID.get());
        final Optional<PlayerProvider> player = PluginCore.server().findPlayer(handler.getClick().player().identifier());
        if(currencyOptional.isPresent() && player.isPresent()) {

          if(EconomyManager.limitCurrency()) {
            if(!player.get().hasPermission("tne.money.deposit." + currencyOptional.get().getIdentifier())) {
              final MessageData data = new MessageData("Messages.Account.BlockedAction");
              data.addReplacement("$action", "deposit");
              data.addReplacement("$currency", currencyOptional.get().getDisplay());
              player.get().message(data);
              return;
            }
          }

          if(handler.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            player.get().message(new MessageData("Messages.Money.Negative"));
            return;
          }

          final Optional<Account> senderAccount = TNECore.eco().account().findAccount(playerUUID);
          if(senderAccount.isEmpty()) {
            final MessageData data = new MessageData("Messages.General.NoPlayer");
            data.addReplacement("$player", player.get().getName());
            player.get().message(data);
            return;
          }

          if(!(currencyOptional.get().type() instanceof MixedType)) {
            player.get().message(new MessageData("Messages.Money.NotMixed"));
            return;
          }

          final HoldingsModifier modifier = new HoldingsModifier(TNECore.eco().region().getMode().region(player.get()),
                                                                 currencyOptional.get().getUid(),
                                                                 handler.getAmount(),
                                                                 EconomyManager.VIRTUAL
          );

          final Transaction transaction = new Transaction("deposit")
                  .to(senderAccount.get(), modifier)
                  .from(senderAccount.get(), modifier.counter(EconomyManager.ITEM_ONLY))
                  .processor(EconomyManager.baseProcessor())
                  .source(new PlayerSource(playerUUID));

          final Optional<Receipt> receipt = processTransaction(player.get(), transaction, senderAccount.get().getName(), handler.getAmount());
          if(receipt.isPresent()) {
            final MessageData data = new MessageData("Messages.Money.Deposit");
            data.addReplacement("$amount", CurrencyFormatter.format(senderAccount.get(),
                                                                    modifier.asEntry()));
            player.get().message(data);
          }
        }
      }
    }
  }

  protected void note(final AmountSelectionHandler handler) {

    final Optional<MenuViewer> viewer = handler.getClick().player().viewer();
    if(viewer.isPresent()) {
      final UUID playerUUID = viewer.get().uuid();

      final Optional<Object> currencyUUID = viewer.get().findData(ACTION_CURRENCY);
      final Optional<Object> receiverUUID = viewer.get().findData(ACTION_ACCOUNT_ID + "_ID");
      if(currencyUUID.isPresent() && receiverUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().find((UUID)currencyUUID.get());
        final Optional<PlayerProvider> player = PluginCore.server().findPlayer(handler.getClick().player().identifier());
        final Optional<Account> account = TNECore.eco().account().findAccount(playerUUID);
        if(currencyOptional.isPresent() && player.isPresent() && account.isPresent()) {

          final Optional<Note> note = currencyOptional.get().getNote();
          if(note.isPresent() && account.get() instanceof PlayerAccount) {

            final String region = TNECore.eco().region().getMode().region(player.get());

            if(EconomyManager.limitCurrency() && !player.get().hasPermission("tne.money.note." + currencyOptional.get().getIdentifier())) {
              final MessageData data = new MessageData("Messages.Account.BlockedAction");
              data.addReplacement("$action", "note");
              data.addReplacement("$currency", currencyOptional.get().getDisplay());
              player.get().message(data);
              return;
            }

            if(handler.getAmount().compareTo(note.get().getMinimum()) < 0) {
              final MessageData min = new MessageData("Messages.Note.Minimum");
              min.addReplacement("$amount", note.get().getMinimum().toPlainString());
              player.get().message(min);
              return;
            }

            final BigDecimal rounded = handler.getAmount().setScale(currencyOptional.get().getDecimalPlaces(), RoundingMode.DOWN);

            final BigDecimal amt = rounded.add(note.get().getFee().calculateTax(rounded)).setScale(currencyOptional.get().getDecimalPlaces(), RoundingMode.DOWN);

            final HoldingsModifier modifier = new HoldingsModifier(region,
                                                                   currencyOptional.get().getUid(),
                                                                   amt
            );

            final Transaction transaction = new Transaction("note")
                    .from(account.get(), modifier.counter())
                    .processor(EconomyManager.baseProcessor())
                    .source(new PlayerSource(playerUUID));


            final Optional<Receipt> receipt = processTransaction(player.get(), transaction, account.get().getName(), handler.getAmount());
            if(receipt.isPresent()) {
              final Collection<AbstractItemStack<Object>> left = PluginCore.server().calculations().giveItems(Collections.singletonList(note.get().stack(currencyOptional.get().getIdentifier(), region, rounded)),
                                                                                                              player.get().inventory().getInventory(false));

              if(!left.isEmpty()) {
                PluginCore.server().calculations().drop(left, ((PlayerAccount)account.get()).getUUID(), true);
              }

              final MessageData entryMSG = new MessageData("Messages.Note.Given");
              entryMSG.addReplacement("$currency", currencyOptional.get().getIdentifier());
              entryMSG.addReplacement("$amount", CurrencyFormatter.format(account.get(), modifier.asEntry()));
              player.get().message(entryMSG);
            }
          }
        }
      }
    }
  }

  protected void pay(final AmountSelectionHandler handler) {

    final Optional<MenuViewer> viewer = handler.getClick().player().viewer();
    if(viewer.isPresent()) {
      final UUID playerUUID = viewer.get().uuid();

      final Optional<Object> currencyUUID = viewer.get().findData(ACTION_CURRENCY);
      final Optional<Object> receiverUUID = viewer.get().findData(ACTION_ACCOUNT_ID + "_ID");
      if(currencyUUID.isPresent() && receiverUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().find((UUID)currencyUUID.get());
        final Optional<PlayerProvider> player = PluginCore.server().findPlayer(handler.getClick().player().identifier());
        if(currencyOptional.isPresent() && player.isPresent()) {

          //Our receiving account
          final Optional<Account> account = TNECore.eco().account().findAccount(UUID.fromString((String)receiverUUID.get()));
          if(account.isPresent()) {

            if(EconomyManager.limitCurrency()) {
              if(!player.get().hasPermission("tne.money.pay." + currencyOptional.get().getIdentifier())) {
                final MessageData data = new MessageData("Messages.Account.BlockedAction");
                data.addReplacement("$action", "pay");
                data.addReplacement("$currency", currencyOptional.get().getDisplay());
                player.get().message(data);
                return;
              }
            }

            if(handler.getAmount().compareTo(BigDecimal.ZERO) < 0) {
              player.get().message(new MessageData("Messages.Money.Negative"));
              return;
            }

            final Optional<Account> senderAccount = TNECore.eco().account().findAccount(playerUUID);
            if(senderAccount.isEmpty()) {
              final MessageData data = new MessageData("Messages.General.NoPlayer");
              data.addReplacement("$player", player.get().getName());
              player.get().message(data);
              return;
            }

            if(senderAccount.get().getIdentifier().equals(account.get().getIdentifier())) {
              final MessageData data = new MessageData("Messages.Money.SelfPay");
              data.addReplacement("$player", player.get().getName());
              player.get().message(data);
              return;
            }

            if(!MainConfig.yaml().getBoolean("Core.Commands.Pay.Offline", true)) {
              if(!(account.get() instanceof PlayerAccount) || !((PlayerAccount)account.get()).isOnline()) {

                player.get().message(new MessageData("Messages.Money.PayFailedOnline"));
                return;
              }
            }

            if(MainConfig.yaml().getInt("Core.Commands.Pay.Radius", 0) > 0) {
              final MessageData data = new MessageData("Messages.Money.PayFailedDistance");
              data.addReplacement("$distance", String.valueOf(MainConfig.yaml().getInt("Core.Commands.Pay.Radius")));

              if(!(senderAccount.get() instanceof PlayerAccount) || !((PlayerAccount)senderAccount.get()).isOnline()
                 || !(account.get() instanceof PlayerAccount) || !((PlayerAccount)account.get()).isOnline()) {
                player.get().message(data);
                return;
              }

              final Optional<PlayerProvider> senderPlayer = ((PlayerAccount)senderAccount.get()).getPlayer();
              final Optional<PlayerProvider> playerPlayer = ((PlayerAccount)account.get()).getPlayer();
              if(senderPlayer.isEmpty() || playerPlayer.isEmpty()
                 || senderPlayer.get().getLocation().isEmpty() || playerPlayer.get().getLocation().isEmpty()) {
                player.get().message(data);
                return;
              }

              if(senderPlayer.get().getLocation().get().distance(playerPlayer.get().getLocation().get()) > MainConfig.yaml().getInt("Core.Commands.Pay.Radius")) {
                player.get().message(data);
                return;
              }
            }

            final HoldingsModifier modifier = new HoldingsModifier(TNECore.eco().region().getMode().region(player.get()),
                                                                   currencyOptional.get().getUid(),
                                                                   handler.getAmount()
            );
            final Transaction transaction = new Transaction("pay")
                    .to(account.get(), modifier)
                    .from(senderAccount.get(), modifier.counter())
                    .processor(EconomyManager.baseProcessor())
                    .source(new PlayerSource(playerUUID));

            final Optional<Receipt> receipt = processTransaction(player.get(), transaction, account.get().getName(), handler.getAmount());
            if(receipt.isPresent()) {
              final MessageData data = new MessageData("Messages.Money.Paid");
              data.addReplacement("$player", account.get().getName());
              data.addReplacement("$currency", currencyOptional.get().getIdentifier());
              data.addReplacement("$amount", CurrencyFormatter.format(account.get(),
                                                                      modifier.asEntry()));
              player.get().message(data);

              if(account.get().isPlayer() && ((PlayerAccount)account.get()).isOnline()) {

                final Optional<PlayerProvider> provider = PluginCore.server().findPlayer(((PlayerAccount)account.get()).getUUID());
                if(provider.isPresent()) {

                  final MessageData msgData = new MessageData("Messages.Money.Received");
                  msgData.addReplacement("$player", (player.get().getName() == null)? MainConfig.yaml().getString("Core.Server.Account.Name") : player.get().getName());
                  msgData.addReplacement("$amount", CurrencyFormatter.format(account.get(),
                                                                             modifier.asEntry()));
                  provider.get().message(msgData);
                }
              }
            }
          }
        }
      }
    }
  }

  protected void withdraw(final AmountSelectionHandler handler) {

    final Optional<MenuViewer> viewer = handler.getClick().player().viewer();
    if(viewer.isPresent()) {
      final UUID playerUUID = viewer.get().uuid();

      final Optional<Object> currencyUUID = viewer.get().findData(ACTION_CURRENCY);
      if(currencyUUID.isPresent()) {

        final Optional<Currency> currencyOptional = TNECore.eco().currency().find((UUID)currencyUUID.get());
        final Optional<PlayerProvider> player = PluginCore.server().findPlayer(handler.getClick().player().identifier());
        if(currencyOptional.isPresent() && player.isPresent()) {

          if(EconomyManager.limitCurrency()) {
            if(!player.get().hasPermission("tne.money.withdraw." + currencyOptional.get().getIdentifier())) {
              final MessageData data = new MessageData("Messages.Account.BlockedAction");
              data.addReplacement("$action", "withdraw funds");
              data.addReplacement("$currency", currencyOptional.get().getDisplay());
              player.get().message(data);
              return;
            }
          }

          if(handler.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            player.get().message(new MessageData("Messages.Money.Negative"));
            return;
          }

          final Optional<Account> senderAccount = TNECore.eco().account().findAccount(playerUUID);
          if(senderAccount.isEmpty()) {
            final MessageData data = new MessageData("Messages.General.NoPlayer");
            data.addReplacement("$player", player.get().getName());
            player.get().message(data);
            return;
          }

          if(!(currencyOptional.get().type() instanceof MixedType)) {
            player.get().message(new MessageData("Messages.Money.NotMixed"));
            return;
          }

          final HoldingsModifier modifier = new HoldingsModifier(TNECore.eco().region().getMode().region(player.get()),
                                                                 currencyOptional.get().getUid(),
                                                                 handler.getAmount(),
                                                                 EconomyManager.ITEM_ONLY
          );

          final Transaction transaction = new Transaction("withdraw")
                  .to(senderAccount.get(), modifier)
                  .from(senderAccount.get(), modifier.counter(EconomyManager.VIRTUAL))
                  .processor(EconomyManager.baseProcessor())
                  .source(new PlayerSource(playerUUID));

          final Optional<Receipt> receipt = processTransaction(player.get(), transaction, senderAccount.get().getName(), handler.getAmount());
          if(receipt.isPresent()) {

            final MessageData data = new MessageData("Messages.Money.Withdrawn");
            data.addReplacement("$currency", currencyOptional.get().getIdentifier());
            data.addReplacement("$amount", CurrencyFormatter.format(senderAccount.get(),
                                                                    modifier.asEntry()));
            player.get().message(data);
            return;
          }
        }
      }
    }
  }

  private static Optional<Receipt> processTransaction(final PlayerProvider player, final Transaction transaction, final String modifiedAccount, final BigDecimal modifier) {

    try {
      final TransactionResult result = transaction.process();

      if(!result.isSuccessful()) {
        final MessageData data = new MessageData(result.getMessage());
        data.addReplacement("$player", modifiedAccount);
        data.addReplacement("$amount", modifier.toPlainString());

        player.message(data);
        return Optional.empty();
      }

      return result.getReceipt();
    } catch(final InvalidTransactionException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }
}