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
import net.tnemc.sponge.impl.SpongeCMDSource;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.sponge.SpongeCommandActor;
import revxrsal.commands.sponge.annotation.CommandPermission;

/**
 * ShortCommands
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ShortCommands {



  @Command({"baltop"})
  @Usage("Money.Top.Arguments")
  @Description("Money.Top.Description")
  @CommandPermission("tne.money.top")
  public void onTop(SpongeCommandActor sender, @Default("1") Integer page, @Default("") Currency currency, @Default("false") Boolean refresh) {
    net.tnemc.core.command.MoneyCommand.onTop(new SpongeCMDSource(sender), page, currency, refresh);
  }

  @Command({"pay"})
  @Usage("Money.Pay.Arguments")
  @Description("Money.Pay.Description")
  @CommandPermission("tne.money.pay")
  public void onPay(SpongeCommandActor sender, Account player, PercentBigDecimal amount, @Default("") Currency currency, @Default("") String from) {
    net.tnemc.core.command.MoneyCommand.onPay(new SpongeCMDSource(sender), player, amount, currency, from);
  }
}