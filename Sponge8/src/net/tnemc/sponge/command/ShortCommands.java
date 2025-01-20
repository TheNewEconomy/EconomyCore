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
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.sponge.SpongeCommandActor;
import revxrsal.commands.sponge.annotation.CommandPermission;

import java.math.BigDecimal;


/**
 * ShortCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ShortCommands {

  @Command({ "givemoney", "givebal" })
  @Usage("Money.Give.Arguments")
  @Description("Money.Give.Description")
  @CommandPermission("tne.money.give")
  public void onGive(final SpongeCommandActor sender, @Named("account") final Account player, @Named("amount") final PercentBigDecimal amount, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onGive(new SpongeCMDSource(sender), player, amount, region, currency);
  }

  @Subcommand({ "giveallmoney", "giveallbal" })
  @Usage("Money.GiveAll.Arguments")
  @Description("Money.GiveAll.Description")
  @CommandPermission("tne.money.giveall")
  public void onGiveAll(final SpongeCommandActor sender, @Named("amount") final PercentBigDecimal amount, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onGiveAll(new SpongeCMDSource(sender), amount, region, currency);
  }

  @Command({ "givenote", "+note", "addnote" })
  @Usage("Money.GiveNote.Arguments")
  @Description("Money.GiveNote.Description")
  @CommandPermission("tne.money.givenote")
  public void onGiveNote(final SpongeCommandActor sender, final Account player, @Named("amount") final BigDecimal amount, @Default("") @Named("currency") final Currency currency) {

    net.tnemc.core.command.MoneyCommand.onGiveNote(new SpongeCMDSource(sender), player, amount, currency);
  }

  @Command({ "pay" })
  @Usage("Money.Pay.Arguments")
  @Description("Money.Pay.Description")
  @CommandPermission("tne.money.pay")
  public void onPay(final SpongeCommandActor sender, @Named("account") final Account player, @Named("amount") final PercentBigDecimal amount, @Default("") @Named("currency") final Currency currency, @Default("") final String from) {

    net.tnemc.core.command.MoneyCommand.onPay(new SpongeCMDSource(sender), player, amount, currency, from);
  }

  @Command({ "setmoney", "setbal" })
  @Usage("Money.Set.Arguments")
  @Description("Money.Set.Description")
  @CommandPermission("tne.money.set")
  public void onSet(final SpongeCommandActor sender, @Named("account") final Account player, @Named("amount") final BigDecimal amount, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onSet(new SpongeCMDSource(sender), player, amount, region, currency);
  }

  @Command({ "takemoney", "takebal" })
  @Usage("Money.Take.Arguments")
  @Description("Money.Take.Description")
  @CommandPermission("tne.money.take")
  public void onTake(final SpongeCommandActor sender, @Named("account") final Account player, @Named("amount") final PercentBigDecimal amount, @Default("") @Named("currency") final Currency currency, @Default("world-113") @Named("region") final String region) {

    net.tnemc.core.command.MoneyCommand.onTake(new SpongeCMDSource(sender), player, amount, region, currency);
  }

  @Command({ "baltop" })
  @Usage("Money.Top.Arguments")
  @Description("Money.Top.Description")
  @CommandPermission("tne.money.top")
  public void onTop(final SpongeCommandActor sender, @Default("1") @Named("page") final Integer page, @Default("") @Named("currency") final Currency currency, @Default("false") final Boolean refresh) {

    net.tnemc.core.command.MoneyCommand.onTop(new SpongeCMDSource(sender), page, currency, refresh);
  }

  @Command({ "transactions" })
  @Usage("Transaction.History.Arguments")
  @Description("Transaction.History.Description")
  @CommandPermission("tne.transaction.history")
  public void history(final SpongeCommandActor sender, @Default("1") final int page, @Default("world-113") @Named("region") final String region, @Default("SELF_ACCOUNT") final Account account) {

    net.tnemc.core.command.TransactionCommand.history(new SpongeCMDSource(sender), page, region, account);
  }
}