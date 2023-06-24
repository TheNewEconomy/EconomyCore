package net.tnemc.bukkit.command;

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

import net.tnemc.bukkit.impl.BukkitCMDSource;
import net.tnemc.core.account.Account;
import net.tnemc.core.command.BaseCommand;
import net.tnemc.core.currency.Currency;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.help.CommandHelp;

import java.math.BigDecimal;

/**
 * MoneyCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Command({"money", "eco"})
public class MoneyCommand {

  /*@Subcommand({"balmenu", "menu", "mybal"})
  @Usage("Money.MyBal.Arguments")
  @Description("Money.MyBal.Description")
  @CommandPermission("tne.money.mybal")
  public void onMyBal(BukkitCommandActor sender, Currency currency) {
    net.tnemc.core.command.MoneyCommand.onMyBal(new BukkitCMDSource(sender), currency);
  }*/

  @Subcommand({"help", "?"})
  @Usage("Help.Arguments")
  @Description("Help.Description")
  public void help(BukkitCommandActor actor, CommandHelp<String> helpEntries, @Default("1") int page) {
    BaseCommand.help(new BukkitCMDSource(actor), helpEntries, page);
  }

  @DefaultFor({"money", "eco"})
  @Subcommand({"balance", "bal", "val"})
  @Usage("Money.Balance.Arguments")
  @Description("Money.Balance.Description")
  @CommandPermission("tne.money.balance")
  public void onBalance(BukkitCommandActor sender, @Default("") Currency currency, @Default("world-113") String region) {
    net.tnemc.core.command.MoneyCommand.onBalance(new BukkitCMDSource(sender), currency, region);
  }

  @Subcommand({"convert"})
  @Usage("Money.Convert.Arguments")
  @Description("Money.Convert.Description")
  @CommandPermission("tne.money.convert")
  public void onConvert(BukkitCommandActor sender, BigDecimal amount, Currency currency, Currency fromCurrency) {
    net.tnemc.core.command.MoneyCommand.onConvert(new BukkitCMDSource(sender), amount, currency, fromCurrency);
  }

  @Subcommand({"give", "+", "add"})
  @Usage("Money.Give.Arguments")
  @Description("Money.Give.Description")
  @CommandPermission("tne.money.give")
  public void onGive(BukkitCommandActor sender, Account player, BigDecimal amount, @Default("world-113") String region, @Default("") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onGive(new BukkitCMDSource(sender), player, amount, region, currency);
  }

  @Subcommand({"note", "n"})
  @Usage("Money.Note.Arguments")
  @Description("Money.Note.Description")
  @CommandPermission("tne.money.note")
  public void onNote(BukkitCommandActor sender, BigDecimal amount, @Default("") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onNote(new BukkitCMDSource(sender), amount, currency);
  }

  @Subcommand({"other", "check", "balo"})
  @Usage("Money.Other.Arguments")
  @Description("Money.Other.Description")
  @CommandPermission("tne.money.other")
  public void onOther(BukkitCommandActor sender, Account player, @Default("world-113") String region, @Default("") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onOther(new BukkitCMDSource(sender), player, region, currency);
  }

  @Subcommand({"pay", "send", "transfer"})
  @Usage("Money.Pay.Arguments")
  @Description("Money.Pay.Description")
  @CommandPermission("tne.money.pay")
  public void onPay(BukkitCommandActor sender, Account player, BigDecimal amount, @Default("") Currency currency, @Default("") String from) {
    net.tnemc.core.command.MoneyCommand.onPay(new BukkitCMDSource(sender), player, amount, currency, from);
  }

  @Subcommand({"request"})
  @Usage("Money.Request.Arguments")
  @Description("Money.Request.Description")
  @CommandPermission("tne.money.Request")
  public void onRequest(BukkitCommandActor sender, Account player, BigDecimal amount, @Default("") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onRequest(new BukkitCMDSource(sender), player, amount, currency);
  }

  @Subcommand({"set", "eq", "="})
  @Usage("Money.Set.Arguments")
  @Description("Money.Set.Description")
  @CommandPermission("tne.money.set")
  public void onSet(BukkitCommandActor sender, Account player, BigDecimal amount, @Default("world-113") String region, @Default("") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onSet(new BukkitCMDSource(sender), player, amount, region, currency);
  }

  @Subcommand({"setall"})
  @Usage("Money.SetAll.Arguments")
  @Description("Money.SetAll.Description")
  @CommandPermission("tne.money.setall")
  public void onSetAll(BukkitCommandActor sender, BigDecimal amount, @Default("world-113") String region, @Default("") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onSetAll(new BukkitCMDSource(sender), amount, region, currency);
  }

  @Subcommand({"take", "minus", "remove", "-"})
  @Usage("Money.Take.Arguments")
  @Description("Money.Take.Description")
  @CommandPermission("tne.money.take")
  public void onTake(BukkitCommandActor sender, Account player, BigDecimal amount, @Default("world-113") String region, @Default("") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onTake(new BukkitCMDSource(sender), player, amount, region, currency);
  }

  /*@Subcommand({"top", "baltop"})
  @Usage("Money.Top.Arguments")
  @Description("Money.Top.Description")
  @CommandPermission("tne.money.top")
  public void onTop(BukkitCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onTop(new BukkitCMDSource(sender));
  }*/
}