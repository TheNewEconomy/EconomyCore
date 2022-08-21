package net.tnemc.bukkit.command;
/*
 * The New Economy
 * Copyright (CommandSender) 2022 Daniel "creatorfromhell" Vidmar
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
import org.bukkit.command.CommandSender;

/**
 * MoneyCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@CommandAlias("money")
public class MoneyCommand extends BaseCommand {

  @Default
  @Subcommand("balance|bal|val")
  @Syntax("%Money.Balance.Arguments")
  @Description("%Money.Balance.Description")
  @CommandPermission("tne.money.balance")
  public void onBalance(CommandSender sender, String[] args) {


  }

  @Subcommand("convert")
  @Syntax("%Money.Convert.Arguments")
  @Description("%Money.Convert.Description")
  @CommandPermission("tne.money.convert")
  public void onConvert(CommandSender sender, String[] args) {

  }

  @Subcommand("give|+|add")
  @Syntax("%Money.Give.Arguments")
  @Description("%Money.Give.Description")
  @CommandPermission("tne.money.give")
  public void onGive(CommandSender sender, String[] args) {

  }

  @Subcommand("note|n")
  @Syntax("%Money.Note.Arguments")
  @Description("%Money.Note.Description")
  @CommandPermission("tne.money.note")
  public void onNote(CommandSender sender, String[] args) {

  }

  @Subcommand("pay|send|transfer")
  @Syntax("%Money.Pay.Arguments")
  @Description("%Money.Pay.Description")
  @CommandPermission("tne.money.pay")
  public void onPay(CommandSender sender, String[] args) {

  }

  @Subcommand("request")
  @Syntax("%Money.Request.Arguments")
  @Description("%Money.Request.Description")
  @CommandPermission("tne.money.Request")
  public void onRequest(CommandSender sender, String[] args) {

  }

  @Subcommand("set|eq|=")
  @Syntax("%Money.Set.Arguments")
  @Description("%Money.Set.Description")
  @CommandPermission("tne.money.set")
  public void onSet(CommandSender sender, String[] args) {

  }

  @Subcommand("setall")
  @Syntax("%Money.SetAll.Arguments")
  @Description("%Money.SetAll.Description")
  @CommandPermission("tne.money.setall")
  public void onSetAll(CommandSender sender, String[] args) {

  }

  @Subcommand("take|minus|remove|-")
  @Syntax("%Money.Take.Arguments")
  @Description("%Money.Take.Description")
  @CommandPermission("tne.money.take")
  public void onTake(CommandSender sender, String[] args) {

  }

  @Subcommand("top")
  @Syntax("%Money.Top.Arguments")
  @Description("%Money.Top.Description")
  @CommandPermission("tne.money.top")
  public void onTop(CommandSender sender, String[] args) {

  }
}