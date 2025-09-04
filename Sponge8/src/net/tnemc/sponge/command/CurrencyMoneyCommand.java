package net.tnemc.sponge.command;


/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.account.Account;
import net.tnemc.core.command.BaseCommand;
import net.tnemc.core.command.parameters.PercentBigDecimal;
import net.tnemc.core.command.parameters.resolver.annotation.AllSupport;
import net.tnemc.core.command.parameters.resolver.annotation.EnderSupport;
import net.tnemc.core.command.parameters.resolver.annotation.InventorySupport;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.parser.ParseMoney;
import net.tnemc.plugincore.sponge.impl.SpongeCMDSource;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.help.Help;
import revxrsal.commands.orphan.OrphanCommand;
import revxrsal.commands.sponge.actor.SpongeCommandActor;
import revxrsal.commands.sponge.annotation.CommandPermission;

/**
 * CurrencyMoneyCommand
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class CurrencyMoneyCommand implements OrphanCommand {

  private final Currency currency;

  public CurrencyMoneyCommand(final Currency currency) {

    this.currency = currency;
  }

  @Subcommand({ "balmenu", "menu", "mybal" })
  @Usage("Money.MyBal.Arguments")
  @Description("Money.MyBal.Description")
  @CommandPermission("tne.money.mybal")
  public void onMyBal(final SpongeCommandActor sender) {

    net.tnemc.core.command.MoneyCommand.onMyBal(new SpongeCMDSource(sender));
  }

  @Subcommand({ "help", "?" })
  @Usage("Help.Arguments")
  @Description("Help.Description")
  public void help(final SpongeCommandActor actor, final Help.RelatedCommands<?> commands, @Default("1") final int page) {

    BaseCommand.help(new SpongeCMDSource(actor), commands, page);
  }

  //@DefaultFor({ "bal", "money", "eco", "balance" })
  @Subcommand({ "balance", "bal", "val" })
  @Usage("Money.Balance.Arguments")
  @Description("Money.Balance.Description")
  @CommandPermission("tne.money.balance")
  public void onBalance(final SpongeCommandActor sender, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onBalance(new SpongeCMDSource(sender), currency, region);
  }

  @Subcommand({ "convert" })
  @Usage("Money.Convert.Arguments")
  @Description("Money.Convert.Description")
  @CommandPermission("tne.money.convert")
  public void onConvert(final SpongeCommandActor sender, @Named("amount") final PercentBigDecimal amount, @Named("currency") final Currency toCurrency) {

    net.tnemc.core.command.MoneyCommand.onConvert(new SpongeCMDSource(sender), amount, toCurrency, this.currency);
  }

  @Subcommand({ "deposit" })
  @Usage("Money.Deposit.Arguments")
  @Description("Money.Deposit.Description")
  @CommandPermission("tne.money.deposit")
  @InventorySupport
  @EnderSupport
  @AllSupport
  public void onDeposit(final SpongeCommandActor sender, @Named("amount") final ParseMoney amount, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onDeposit(new SpongeCMDSource(sender), amount, currency, region);
  }

  @Subcommand({ "giveall", "+a", "addall" })
  @Usage("Money.GiveAll.Arguments")
  @Description("Money.GiveAll.Description")
  @CommandPermission("tne.money.giveall")
  public void onGiveAll(final SpongeCommandActor sender, @Named("amount") final ParseMoney amount, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onGiveAll(new SpongeCMDSource(sender), amount, currency, region);
  }

  @Subcommand({ "give", "+", "add" })
  @Usage("Money.Give.Arguments")
  @Description("Money.Give.Description")
  @CommandPermission("tne.money.give")
  public void onGive(final SpongeCommandActor sender, @Named("account") final Account player, @Named("amount") final ParseMoney parseMoney, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onGive(new SpongeCMDSource(sender), player, parseMoney, currency, region);
  }

  @Subcommand({ "givenote", "+note", "addnote" })
  @Usage("Money.GiveNote.Arguments")
  @Description("Money.GiveNote.Description")
  @CommandPermission("tne.money.givenote")
  public void onGiveNote(final SpongeCommandActor sender, @Named("account") final Account player, @Named("amount") final ParseMoney amount) {

    net.tnemc.core.command.MoneyCommand.onGiveNote(new SpongeCMDSource(sender), player, amount, currency);
  }

  @Subcommand({ "note", "n" })
  @Usage("Money.Note.Arguments")
  @Description("Money.Note.Description")
  @CommandPermission("tne.money.note")
  @InventorySupport
  @EnderSupport
  @AllSupport
  public void onNote(final SpongeCommandActor sender, @Named("amount") final ParseMoney amount) {

    net.tnemc.core.command.MoneyCommand.onNote(new SpongeCMDSource(sender), amount, currency);
  }

  @Subcommand({ "other", "check", "balo" })
  @Usage("Money.Other.Arguments")
  @Description("Money.Other.Description")
  @CommandPermission("tne.money.other")
  public void onOther(final SpongeCommandActor sender, @Named("account") final Account player, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onOther(new SpongeCMDSource(sender), player, currency, region);
  }

  @Subcommand({ "pay", "send", "transfer" })
  @Usage("Money.Pay.Arguments")
  @Description("Money.Pay.Description")
  @CommandPermission("tne.money.pay")
  @InventorySupport
  @EnderSupport
  @AllSupport
  public void onPay(final SpongeCommandActor sender, @Named("account") final Account player, @Named("amount") final ParseMoney amount) {

    net.tnemc.core.command.MoneyCommand.onPay(new SpongeCMDSource(sender), player, amount, currency);
  }

  @Subcommand({ "request" })
  @Usage("Money.Request.Arguments")
  @Description("Money.Request.Description")
  @CommandPermission("tne.money.Request")
  public void onRequest(final SpongeCommandActor sender, @Named("account") final Account player, @Named("amount") final ParseMoney amount) {

    net.tnemc.core.command.MoneyCommand.onRequest(new SpongeCMDSource(sender), player, amount, currency);
  }

  @Subcommand({ "set", "eq", "=" })
  @Usage("Money.Set.Arguments")
  @Description("Money.Set.Description")
  @CommandPermission("tne.money.set")
  public void onSet(final SpongeCommandActor sender, @Named("account") final Account player, @Named("amount") final ParseMoney amount, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onSet(new SpongeCMDSource(sender), player, amount, currency, region);
  }

  @Subcommand({ "setall" })
  @Usage("Money.SetAll.Arguments")
  @Description("Money.SetAll.Description")
  @CommandPermission("tne.money.setall")
  public void onSetAll(final SpongeCommandActor sender, @Named("amount") final ParseMoney amount, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onSetAll(new SpongeCMDSource(sender), amount, currency, region);
  }

  @Subcommand({ "take", "minus", "remove", "-" })
  @Usage("Money.Take.Arguments")
  @Description("Money.Take.Description")
  @CommandPermission("tne.money.take")
  @InventorySupport
  @EnderSupport
  @AllSupport
  public void onTake(final SpongeCommandActor sender, @Named("account") final Account player, @Named("amount") final ParseMoney amount, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onTake(new SpongeCMDSource(sender), player, amount, currency, region);
  }

  @Subcommand({ "top", "baltop" })
  @Usage("Money.Top.Arguments")
  @Description("Money.Top.Description")
  @CommandPermission("tne.money.top")
  public void onTop(final SpongeCommandActor sender, @Default("1") final Integer page, @Default("false") final Boolean refresh) {

    net.tnemc.core.command.MoneyCommand.onTop(new SpongeCMDSource(sender), page, currency, refresh);
  }

  @Subcommand({ "withdraw" })
  @Usage("Money.Withdraw.Arguments")
  @Description("Money.Withdraw.Description")
  @CommandPermission("tne.money.withdraw")
  @AllSupport
  public void onWithdraw(final SpongeCommandActor sender, @Named("amount") final ParseMoney amount, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onWithdraw(new SpongeCMDSource(sender), amount, currency, region);
  }
}