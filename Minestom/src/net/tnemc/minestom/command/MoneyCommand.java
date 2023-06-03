package net.tnemc.minestom.command;

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

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import net.minestom.server.command.CommandSender;
import net.tnemc.core.command.args.ArgumentsParser;
import net.tnemc.minestom.impl.MinestomCMDSource;

/**
 * MoneyCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@CommandAlias("money|eco")
public class MoneyCommand extends BaseCommand {

  @Default
  @Subcommand("balmenu|menu")
  @CommandAlias("mybal|balmenu")
  @Syntax("#{Money.MyBal.ArgumentsParser}")
  @Description("#{Money.MyBal.Description}")
  @CommandPermission("tne.money.mybal")
  public void onMyBal(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onMyBal(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Default
  @Subcommand("balance|bal|val")
  @CommandAlias("balance|bal|val")
  @Syntax("#{Money.Balance.ArgumentsParser}")
  @Description("#{Money.Balance.Description}")
  @CommandPermission("tne.money.balance")
  public void onBalance(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onBalance(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("convert")
  @Syntax("#{Money.Convert.ArgumentsParser}")
  @Description("#{Money.Convert.Description}")
  @CommandPermission("tne.money.convert")
  public void onConvert(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onConvert(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("give|+|add")
  @Syntax("#{Money.Give.ArgumentsParser}")
  @Description("#{Money.Give.Description}")
  @CommandPermission("tne.money.give")
  public void onGive(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onGive(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("note|n")
  @Syntax("#{Money.Note.ArgumentsParser}")
  @Description("#{Money.Note.Description}")
  @CommandPermission("tne.money.note")
  public void onNote(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onNote(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("other|check")
  @CommandAlias("balo|check")
  @Syntax("#{Money.Other.ArgumentsParser}")
  @Description("#{Money.Other.Description}")
  @CommandPermission("tne.money.other")
  public void onOther(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onOther(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("pay|send|transfer")
  @CommandAlias("pay|send|transfer")
  @Syntax("#{Money.Pay.ArgumentsParser}")
  @Description("#{Money.Pay.Description}")
  @CommandPermission("tne.money.pay")
  public void onPay(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onPay(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("request")
  @Syntax("#{Money.Request.ArgumentsParser}")
  @Description("#{Money.Request.Description}")
  @CommandPermission("tne.money.Request")
  public void onRequest(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onRequest(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("set|eq|=")
  @Syntax("#{Money.Set.ArgumentsParser}")
  @Description("#{Money.Set.Description}")
  @CommandPermission("tne.money.set")
  public void onSet(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onSet(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("setall")
  @Syntax("#{Money.SetAll.ArgumentsParser}")
  @Description("#{Money.SetAll.Description}")
  @CommandPermission("tne.money.setall")
  public void onSetAll(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onSetAll(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("take|minus|remove|-")
  @Syntax("#{Money.Take.ArgumentsParser}")
  @Description("#{Money.Take.Description}")
  @CommandPermission("tne.money.take")
  public void onTake(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onTake(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("top")
  @CommandAlias("baltop")
  @Syntax("#{Money.Top.ArgumentsParser}")
  @Description("#{Money.Top.Description}")
  @CommandPermission("tne.money.top")
  public void onTop(CommandSender sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onTop(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }
}