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
import net.tnemc.core.command.parameters.PercentBigDecimal;
import net.tnemc.core.currency.Currency;
import net.tnemc.plugincore.paper.impl.PaperCMDSource;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.math.BigDecimal;

/**
 * ShortCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ShortCommands {

  @Command({"givemoney", "givebal"})
  @Usage("Money.Give.Arguments")
  @Description("Money.Give.Description")
  @CommandPermission("tne.money.give")
  public void onGive(BukkitCommandActor sender, @Named("account") Account player, @Named("amount") PercentBigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onGive(new PaperCMDSource(sender), player, amount, region, currency);
  }

  @Command({"givenote", "+note", "addnote"})
  @Usage("Money.GiveNote.Arguments")
  @Description("Money.GiveNote.Description")
  @CommandPermission("tne.money.givenote")
  public void onGiveNote(BukkitCommandActor sender, Account player, @Named("amount") BigDecimal amount, @Default("") @Named("currency") Currency currency) {
    net.tnemc.core.command.MoneyCommand.onGiveNote(new PaperCMDSource(sender), player, amount, currency);
  }

  @Command({"pay"})
  @Usage("Money.Pay.Arguments")
  @Description("Money.Pay.Description")
  @CommandPermission("tne.money.pay")
  public void onPay(BukkitCommandActor sender, @Named("account") Account player, @Named("amount") PercentBigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("") String from) {
    net.tnemc.core.command.MoneyCommand.onPay(new PaperCMDSource(sender), player, amount, currency, from);
  }

  @Command({"setmoney", "setbal"})
  @Usage("Money.Set.Arguments")
  @Description("Money.Set.Description")
  @CommandPermission("tne.money.set")
  public void onSet(BukkitCommandActor sender, @Named("account") Account player, @Named("amount") BigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onSet(new PaperCMDSource(sender), player, amount, region, currency);
  }

  @Command({"takemoney", "takebal"})
  @Usage("Money.Take.Arguments")
  @Description("Money.Take.Description")
  @CommandPermission("tne.money.take")
  public void onTake(BukkitCommandActor sender, @Named("account") Account player, @Named("amount") PercentBigDecimal amount, @Default("") @Named("currency") Currency currency, @Default("world-113") @Named("region") String region) {
    net.tnemc.core.command.MoneyCommand.onTake(new PaperCMDSource(sender), player, amount, region, currency);
  }

  @Command({"baltop"})
  @Usage("Money.Top.Arguments")
  @Description("Money.Top.Description")
  @CommandPermission("tne.money.top")
  public void onTop(BukkitCommandActor sender, @Default("1") @Named("page") Integer page, @Default("") @Named("currency") Currency currency, @Default("false") Boolean refresh) {
    net.tnemc.core.command.MoneyCommand.onTop(new PaperCMDSource(sender), page, currency, refresh);
  }
}