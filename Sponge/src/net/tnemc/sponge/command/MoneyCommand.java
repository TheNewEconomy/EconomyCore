package net.tnemc.sponge.command;

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

import net.tnemc.core.command.args.ArgumentsParser;
import net.tnemc.sponge.impl.SpongeCMDSource;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.sponge.SpongeCommandActor;
import revxrsal.commands.sponge.annotation.CommandPermission;

/**
 * MoneyCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Command({"money", "eco"})
public class MoneyCommand {

  @Subcommand({"balmenu", "menu", "mybal", "balmenu"})
  @Usage("#{Money.MyBal.Arguments}")
  @Description("#{Money.MyBal.Description}")
  @CommandPermission("tne.money.mybal")
  public void onMyBal(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onMyBal(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @DefaultFor({"money", "eco"})
  @Subcommand({"balance", "bal", "val"})
  @Usage("#{Money.Balance.Arguments}")
  @Description("#{Money.Balance.Description}")
  @CommandPermission("tne.money.balance")
  public void onBalance(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onBalance(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"convert"})
  @Usage("#{Money.Convert.Arguments}")
  @Description("#{Money.Convert.Description}")
  @CommandPermission("tne.money.convert")
  public void onConvert(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onConvert(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"give", "+", "add"})
  @Usage("#{Money.Give.Arguments}")
  @Description("#{Money.Give.Description}")
  @CommandPermission("tne.money.give")
  public void onGive(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onGive(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"note", "n"})
  @Usage("#{Money.Note.Arguments}")
  @Description("#{Money.Note.Description}")
  @CommandPermission("tne.money.note")
  public void onNote(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onNote(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"other", "check", "balo", "check"})
  @Usage("#{Money.Other.Arguments}")
  @Description("#{Money.Other.Description}")
  @CommandPermission("tne.money.other")
  public void onOther(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onOther(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"pay", "send", "transfer"})
  @Usage("#{Money.Pay.Arguments}")
  @Description("#{Money.Pay.Description}")
  @CommandPermission("tne.money.pay")
  public void onPay(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onPay(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"request"})
  @Usage("#{Money.Request.Arguments}")
  @Description("#{Money.Request.Description}")
  @CommandPermission("tne.money.Request")
  public void onRequest(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onRequest(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"set", "eq", "="})
  @Usage("#{Money.Set.Arguments}")
  @Description("#{Money.Set.Description}")
  @CommandPermission("tne.money.set")
  public void onSet(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onSet(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"setall"})
  @Usage("#{Money.SetAll.Arguments}")
  @Description("#{Money.SetAll.Description}")
  @CommandPermission("tne.money.setall")
  public void onSetAll(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onSetAll(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"take", "minus", "remove", "-"})
  @Usage("#{Money.Take.Arguments}")
  @Description("#{Money.Take.Description}")
  @CommandPermission("tne.money.take")
  public void onTake(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onTake(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"top", "baltop"})
  @Usage("#{Money.Top.Arguments}")
  @Description("#{Money.Top.Description}")
  @CommandPermission("tne.money.top")
  public void onTop(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.MoneyCommand.onTop(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }
}