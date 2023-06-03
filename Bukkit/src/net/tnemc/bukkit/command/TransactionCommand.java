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
import net.tnemc.core.command.args.ArgumentsParser;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.annotation.CommandPermission;

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
  public void away(CommandSender sender, String[] args) {
    net.tnemc.core.command.TransactionCommand.away(new ArgumentsParser(new BukkitCMDSource(sender), args));
  }

  @DefaultFor({"transaction", "trans", "receipt"})
  @Subcommand({"history", "list", "hist", "archive"})
  @Usage("#{Transaction.History.Arguments}")
  @Description("#{Transaction.History.Description}")
  @CommandPermission("tne.transaction.history")
  public void history(CommandSender sender, String[] args) {
    net.tnemc.core.command.TransactionCommand.history(new ArgumentsParser(new BukkitCMDSource(sender), args));
  }

  @Subcommand({"info", "i", "about", "brief"})
  @Usage("#{Transaction.Info.Arguments}")
  @Description("#{Transaction.Info.Description}")
  @CommandPermission("tne.info.history")
  public void info(CommandSender sender, String[] args) {
    net.tnemc.core.command.TransactionCommand.info(new ArgumentsParser(new BukkitCMDSource(sender), args));
  }

  @Subcommand({"void", "retract", "undo"})
  @Usage("#{Transaction.Void.Arguments}")
  @Description("#{Transaction.Void.Description}")
  @CommandPermission("tne.void.history")
  public void voidT(CommandSender sender, String[] args) {
    net.tnemc.core.command.TransactionCommand.voidT(new ArgumentsParser(new BukkitCMDSource(sender), args));
  }

  @Subcommand({"help"})
  public void doHelp(CommandSender sender, CommandHelp help) {
    help.showHelp();
  }
}
