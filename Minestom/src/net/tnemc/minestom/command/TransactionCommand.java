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
 * TransactionCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@CommandAlias("transaction|trans|receipt")
public class TransactionCommand extends BaseCommand {

  @Subcommand("away|gone|afk|afg")
  @Syntax("%Transaction.Away.ArgumentsParser")
  @Description("%Transaction.Away.Description")
  @CommandPermission("tne.transaction.away")
  public void away(CommandSender sender, String[] args) {
    net.tnemc.core.command.TransactionCommand.away(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Default
  @Subcommand("history|list|hist|archive")
  @Syntax("%Transaction.History.ArgumentsParser")
  @Description("%Transaction.History.Description")
  @CommandPermission("tne.transaction.history")
  public void history(CommandSender sender, String[] args) {
    net.tnemc.core.command.TransactionCommand.history(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("info|i|about|brief")
  @Syntax("%Transaction.Info.ArgumentsParser")
  @Description("%Transaction.Info.Description")
  @CommandPermission("tne.info.history")
  public void info(CommandSender sender, String[] args) {
    net.tnemc.core.command.TransactionCommand.info(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }

  @Subcommand("void|retract|undo")
  @Syntax("%Transaction.Void.ArgumentsParser")
  @Description("%Transaction.Void.Description")
  @CommandPermission("tne.void.history")
  public void voidT(CommandSender sender, String[] args) {
    net.tnemc.core.command.TransactionCommand.voidT(new ArgumentsParser(new MinestomCMDSource(sender), args));
  }
}
