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
import net.tnemc.plugincore.sponge.impl.SpongeCMDSource;;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.sponge.SpongeCommandActor;
import revxrsal.commands.sponge.annotation.CommandPermission;

import java.util.UUID;

/**
 * TransactionCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Command({"transaction", "trans", "receipt"})
public class TransactionCommand {

  @Subcommand({"away", "gone", "afk", "afg"})
  @Usage("#{Transaction.Away.Arguments}")
  @Description("#{Transaction.Away.Description}")
  @CommandPermission("tne.transaction.away")
  public void away(SpongeCommandActor sender, @Default("1") int page) {
    net.tnemc.core.command.TransactionCommand.away(new SpongeCMDSource(sender), page);
  }

  @DefaultFor({"transaction", "trans", "receipt"})
  @Subcommand({"history", "list", "hist", "archive"})
  @Usage("#{Transaction.History.Arguments}")
  @Description("#{Transaction.History.Description}")
  @CommandPermission("tne.transaction.history")
  public void history(SpongeCommandActor sender, @Default("1") int page, String region, @Default("SELF_ACCOUNT") Account account) {
    net.tnemc.core.command.TransactionCommand.history(new SpongeCMDSource(sender), page, region, account);
  }

  @Subcommand({"info", "i", "about", "brief"})
  @Usage("#{Transaction.Info.Arguments}")
  @Description("#{Transaction.Info.Description}")
  @CommandPermission("tne.info.history")
  public void info(SpongeCommandActor sender, UUID uuid, @Default("SELF_ACCOUNT") Account account) {
    net.tnemc.core.command.TransactionCommand.info(new SpongeCMDSource(sender), uuid, account);
  }

  @Subcommand({"void", "retract", "undo"})
  @Usage("#{Transaction.Void.Arguments}")
  @Description("#{Transaction.Void.Description}")
  @CommandPermission("tne.void.history")
  public void voidT(SpongeCommandActor sender, Account account, UUID uuid) {
    net.tnemc.core.command.TransactionCommand.voidT(new SpongeCMDSource(sender), account, uuid);
  }
}
