package net.tnemc.sponge.command;

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
import net.tnemc.core.command.parameters.PercentBigDecimal;
import net.tnemc.core.currency.Currency;
import net.tnemc.plugincore.sponge.impl.SpongeCMDSource;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.sponge.SpongeCommandActor;
import revxrsal.commands.sponge.annotation.CommandPermission;

import java.math.BigDecimal;

/**
 * MoneyCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Command({"money", "eco", "balo", "balance", "bal", "balanceother", "mybal"})
public class MoneyCommand {

  @DefaultFor({"bal", "mybal"})
  @Subcommand({"balmenu", "menu", "mybal"})
  @Usage("Money.MyBal.Arguments")
  @Description("Money.MyBal.Description")
  @CommandPermission("tne.money.mybal")
  public void onMyBal(SpongeCommandActor sender) {
    net.tnemc.core.command.MoneyCommand.onMyBal(new SpongeCMDSource(sender));
  }

  @DefaultFor({"money", "eco", "bal", "balance"})
  @Subcommand({"balance", "bal", "val"})
  @Usage("#{Money.Balance.Arguments}")
  @Description("#{Money.Balance.Description}")
  @CommandPermission("tne.money.balance")
  public void onBalance(SpongeCommandActor sender, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onBalance(new SpongeCMDSource(sender), currency, region);
  }

  @Subcommand({"convert"})
  @Usage("#{Money.Convert.Arguments}")
  @Description("#{Money.Convert.Description}")
  @CommandPermission("tne.money.convert")
  public void onConvert(SpongeCommandActor sender, @Named("amount") PercentBigDecimal amount, @Named("currency") Currency currency, Currency fromCurrency) {
    net.tnemc.core.command.MoneyCommand.onConvert(new SpongeCMDSource(sender), amount, currency, fromCurrency);
  }

  @Subcommand({"deposit"})
  @Usage("Money.Deposit.Arguments")
  @Description("Money.Deposit.Description")
  @CommandPermission("tne.money.deposit")
  public void onDeposit(SpongeCommandActor sender, @Named("amount") PercentBigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onDeposit(new SpongeCMDSource(sender), amount, currency, region);
  }

  @Subcommand({"give", "+", "add"})
  @Usage("#{Money.Give.Arguments}")
  @Description("#{Money.Give.Description}")
  @CommandPermission("tne.money.give")
  public void onGive(SpongeCommandActor sender, Account player, @Named("amount") PercentBigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onGive(new SpongeCMDSource(sender), player, amount, region, currency);
  }

  @Subcommand({"givenote", "+note", "addnote"})
  @Usage("Money.GiveNote.Arguments")
  @Description("Money.GiveNote.Description")
  @CommandPermission("tne.money.givenote")
  public void onGiveNote(SpongeCommandActor sender, Account player, @Named("amount") BigDecimal amount, @Default("") @Named("currency") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onGiveNote(new SpongeCMDSource(sender), player, amount, currency);
  }

  @Subcommand({"note", "n"})
  @Usage("#{Money.Note.Arguments}")
  @Description("#{Money.Note.Description}")
  @CommandPermission("tne.money.note")
  public void onNote(SpongeCommandActor sender, @Named("amount") PercentBigDecimal amount, @Default("") @Named("currency") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onNote(new SpongeCMDSource(sender), amount, currency);
  }

  @Subcommand({"other", "check", "balo"})
  @DefaultFor({"balo", "balanceother"})
  @Usage("#{Money.Other.Arguments}")
  @Description("#{Money.Other.Description}")
  @CommandPermission("tne.money.other")
  public void onOther(SpongeCommandActor sender, Account player, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onOther(new SpongeCMDSource(sender), player, region, currency);
  }

  @Subcommand({"pay", "send", "transfer"})
  @Usage("#{Money.Pay.Arguments}")
  @Description("#{Money.Pay.Description}")
  @CommandPermission("tne.money.pay")
  public void onPay(SpongeCommandActor sender, Account player, @Named("amount") PercentBigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("") String from) {
    net.tnemc.core.command.MoneyCommand.onPay(new SpongeCMDSource(sender), player, amount, currency, from);
  }

  @Subcommand({"request"})
  @Usage("#{Money.Request.Arguments}")
  @Description("#{Money.Request.Description}")
  @CommandPermission("tne.money.Request")
  public void onRequest(SpongeCommandActor sender, Account player, @Named("amount") BigDecimal amount, @Default("") @Named("currency") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onRequest(new SpongeCMDSource(sender), player, amount, currency);
  }

  @Subcommand({"set", "eq", "="})
  @Usage("#{Money.Set.Arguments}")
  @Description("#{Money.Set.Description}")
  @CommandPermission("tne.money.set")
  public void onSet(SpongeCommandActor sender, Account player, @Named("amount") BigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onSet(new SpongeCMDSource(sender), player, amount, region, currency);
  }

  @Subcommand({"setall"})
  @Usage("#{Money.SetAll.Arguments}")
  @Description("#{Money.SetAll.Description}")
  @CommandPermission("tne.money.setall")
  public void onSetAll(SpongeCommandActor sender, @Named("amount") BigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onSetAll(new SpongeCMDSource(sender), amount, region, currency);
  }

  @Subcommand({"switch", "swap"})
  @Usage("Money.Switch.Arguments")
  @Description("Money.Switch.Description")
  @CommandPermission("tne.money.switch")
  public void onSwitch(SpongeCommandActor sender, @Named("switched") Account account) {
    net.tnemc.core.command.MoneyCommand.onSwitch(new SpongeCMDSource(sender), account);
  }

  @Subcommand({"take", "minus", "remove", "-"})
  @Usage("#{Money.Take.Arguments}")
  @Description("#{Money.Take.Description}")
  @CommandPermission("tne.money.take")
  public void onTake(SpongeCommandActor sender, Account player, @Named("amount") PercentBigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onTake(new SpongeCMDSource(sender), player, amount, region, currency);
  }

  @Subcommand({"top", "baltop"})
  @Usage("Money.Top.Arguments")
  @Description("Money.Top.Description")
  @CommandPermission("tne.money.top")
  public void onTop(SpongeCommandActor sender, Integer page, @Default("") @Named("currency") Currency currency, @Default("false") Boolean refresh) {
    net.tnemc.core.command.MoneyCommand.onTop(new SpongeCMDSource(sender), page, currency, refresh);
  }

  @Subcommand({"withdraw"})
  @Usage("Money.Withdraw.Arguments")
  @Description("Money.Withdraw.Description")
  @CommandPermission("tne.money.withdraw")
  public void onWithdraw(SpongeCommandActor sender, @Named("amount") PercentBigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onWithdraw(new SpongeCMDSource(sender), amount, currency, region);
  }
}