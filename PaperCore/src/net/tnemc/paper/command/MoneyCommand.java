package net.tnemc.paper.command;

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

import net.tnemc.core.account.Account;
import net.tnemc.core.command.BaseCommand;
import net.tnemc.core.command.parameters.PercentBigDecimal;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.parser.ParseMoney;
import net.tnemc.plugincore.paper.impl.PaperCMDSource;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.help.Help;

/**
 * MoneyCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Command({ "money", "eco", "mybal" })
public class MoneyCommand {

  //@DefaultFor({ "mybal" })
  @Subcommand({ "balmenu", "menu", "mybal" })
  @Usage("Money.MyBal.Arguments")
  @Description("Money.MyBal.Description")
  @CommandPermission("tne.money.mybal")
  public void onMyBal(final BukkitCommandActor sender) {

    net.tnemc.core.command.MoneyCommand.onMyBal(new PaperCMDSource(sender));
  }

  @Subcommand({ "help", "?" })
  @Usage("Help.Arguments")
  @Description("Help.Description")
  public void help(final BukkitCommandActor actor, final Help.RelatedCommands<?> commands, @Default("1") final int page) {

    BaseCommand.help(new PaperCMDSource(actor), commands, page);
  }

  //@DefaultFor({ "bal", "money", "eco", "balance" })
  @Subcommand({ "balance", "bal", "val" })
  @Usage("Money.Balance.Arguments")
  @Description("Money.Balance.Description")
  @CommandPermission("tne.money.balance")
  public void onBalance(final BukkitCommandActor sender, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onBalance(new PaperCMDSource(sender), currency, region);
  }

  @Subcommand({ "convert" })
  @Usage("Money.Convert.Arguments")
  @Description("Money.Convert.Description")
  @CommandPermission("tne.money.convert")
  public void onConvert(final BukkitCommandActor sender, @Named("amount") final PercentBigDecimal amount, @Named("currency") final Currency currency, @Named("currency") final Currency from) {

    net.tnemc.core.command.MoneyCommand.onConvert(new PaperCMDSource(sender), amount, currency, from);
  }

  @Subcommand({ "deposit" })
  @Usage("Money.Deposit.Arguments")
  @Description("Money.Deposit.Description")
  @CommandPermission("tne.money.deposit")
  public void onDeposit(final BukkitCommandActor sender, @Named("amount") final ParseMoney amount, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onDeposit(new PaperCMDSource(sender), amount, currency, region);
  }

  @Subcommand({ "give", "+", "add" })
  @Usage("Money.Give.Arguments")
  @Description("Money.Give.Description")
  @CommandPermission("tne.money.give")
  public void onGive(final BukkitCommandActor sender, final Account player, @Named("amount") final ParseMoney parseMoney, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onGive(new PaperCMDSource(sender), player, parseMoney, region, currency);
  }

  @Subcommand({ "giveall", "+a", "addall" })
  @Usage("Money.GiveAll.Arguments")
  @Description("Money.GiveAll.Description")
  @CommandPermission("tne.money.giveall")
  public void onGiveAll(final BukkitCommandActor sender, @Named("amount") final ParseMoney amount, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onGiveAll(new PaperCMDSource(sender), amount, region, currency);
  }

  @Subcommand({ "givenote", "+note", "addnote" })
  @Usage("Money.GiveNote.Arguments")
  @Description("Money.GiveNote.Description")
  @CommandPermission("tne.money.givenote")
  public void onGiveNote(final BukkitCommandActor sender, final Account player, @Named("amount") final ParseMoney amount, @Default("") @Named("currency") final Currency currency) {

    net.tnemc.core.command.MoneyCommand.onGiveNote(new PaperCMDSource(sender), player, amount, currency);
  }

  @Subcommand({ "note", "n" })
  @Usage("Money.Note.Arguments")
  @Description("Money.Note.Description")
  @CommandPermission("tne.money.note")
  public void onNote(final BukkitCommandActor sender, @Named("amount") final ParseMoney amount, @Default("") @Named("currency") final Currency currency) {

    net.tnemc.core.command.MoneyCommand.onNote(new PaperCMDSource(sender), amount, currency);
  }

  @Subcommand({ "other", "check", "balo" })
  //@DefaultFor({ "balo", "balanceother" })
  @Usage("Money.Other.Arguments")
  @Description("Money.Other.Description")
  @CommandPermission("tne.money.other")
  public void onOther(final BukkitCommandActor sender, final Account player, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onOther(new PaperCMDSource(sender), player, region, currency);
  }

  @Subcommand({ "pay", "send", "transfer" })
  @Usage("Money.Pay.Arguments")
  @Description("Money.Pay.Description")
  @CommandPermission("tne.money.pay")
  public void onPay(final BukkitCommandActor sender, final Account player, @Named("amount") final ParseMoney amount, @Default("") @Named("currency") final Currency currency, @Default("") final String from) {

    net.tnemc.core.command.MoneyCommand.onPay(new PaperCMDSource(sender), player, amount, currency, from);
  }

  @Subcommand({ "request" })
  @Usage("Money.Request.Arguments")
  @Description("Money.Request.Description")
  @CommandPermission("tne.money.Request")
  public void onRequest(final BukkitCommandActor sender, final Account player, @Named("amount") final ParseMoney amount, @Default("") @Named("currency") final Currency currency) {

    net.tnemc.core.command.MoneyCommand.onRequest(new PaperCMDSource(sender), player, amount, currency);
  }

  @Subcommand({ "set", "eq", "=" })
  @Usage("Money.Set.Arguments")
  @Description("Money.Set.Description")
  @CommandPermission("tne.money.set")
  public void onSet(final BukkitCommandActor sender, final Account player, @Named("amount") final ParseMoney amount, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onSet(new PaperCMDSource(sender), player, amount, region, currency);
  }

  @Subcommand({ "setall" })
  @Usage("Money.SetAll.Arguments")
  @Description("Money.SetAll.Description")
  @CommandPermission("tne.money.setall")
  public void onSetAll(final BukkitCommandActor sender, @Named("amount") final ParseMoney amount, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onSetAll(new PaperCMDSource(sender), amount, region, currency);
  }

  @Subcommand({ "switch", "swap" })
  @Usage("Money.Switch.Arguments")
  @Description("Money.Switch.Description")
  @CommandPermission("tne.money.switch")
  public void onSwitch(final BukkitCommandActor sender, @Named("switched") final Account account) {

    net.tnemc.core.command.MoneyCommand.onSwitch(new PaperCMDSource(sender), account);
  }

  @Subcommand({ "take", "minus", "remove", "-" })
  @Usage("Money.Take.Arguments")
  @Description("Money.Take.Description")
  @CommandPermission("tne.money.take")
  public void onTake(final BukkitCommandActor sender, final Account player, @Named("amount") final ParseMoney amount, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onTake(new PaperCMDSource(sender), player, amount, region, currency);
  }

  @Subcommand({ "top", "baltop" })
  @Usage("Money.Top.Arguments")
  @Description("Money.Top.Description")
  @CommandPermission("tne.money.top")
  public void onTop(final BukkitCommandActor sender, @Default("1") final Integer page, @Default("") @Named("currency") final Currency currency, @Default("false") final Boolean refresh) {

    net.tnemc.core.command.MoneyCommand.onTop(new PaperCMDSource(sender), page, currency, refresh);
  }

  @Subcommand({ "withdraw" })
  @Usage("Money.Withdraw.Arguments")
  @Description("Money.Withdraw.Description")
  @CommandPermission("tne.money.withdraw")
  public void onWithdraw(final BukkitCommandActor sender, @Named("amount") final ParseMoney amount, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onWithdraw(new PaperCMDSource(sender), amount, currency, region);
  }
}