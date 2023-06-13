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
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

/**
 * ModuleCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Command({"module", "mod"})
public class ModuleCommand {

  @Subcommand({"avail", "available"})
  @Usage("#{Module.Available.Arguments}")
  @Description("#{Module.Available.Description}")
  @CommandPermission("tne.module.available")
  public void onAvailable(BukkitCommandActor sender, String[] args) {
    net.tnemc.core.command.ModuleCommand.onAvailable(new ArgumentsParser(new BukkitCMDSource(sender), args));
  }

  @Subcommand({"download", "dl"})
  @Usage("#{Module.Download.Arguments}")
  @Description("#{Module.Download.Description}")
  @CommandPermission("tne.module.download")
  public void onDownload(BukkitCommandActor sender, String[] args) {
    net.tnemc.core.command.ModuleCommand.onDownload(new ArgumentsParser(new BukkitCMDSource(sender), args));
  }

  @Subcommand({"info", "i"})
  @Usage("#{Module.Info.Arguments}")
  @Description("#{Module.Info.Description}")
  @CommandPermission("tne.module.info")
  public void onInfo(BukkitCommandActor sender, String[] args) {
    net.tnemc.core.command.ModuleCommand.onInfo(new ArgumentsParser(new BukkitCMDSource(sender), args));
  }

  @Subcommand({"list", "l"})
  @Usage("#{Module.List.Arguments}")
  @Description("#{Module.List.Description}")
  @CommandPermission("tne.list.available")
  public void onList(BukkitCommandActor sender, String[] args) {
    net.tnemc.core.command.ModuleCommand.onList(new ArgumentsParser(new BukkitCMDSource(sender), args));
  }

  @Subcommand({"load"})
  @Usage("#{Module.Load.Arguments}")
  @Description("#{Module.Load.Description}")
  @CommandPermission("tne.module.load")
  public void onLoad(BukkitCommandActor sender, String[] args) {
    net.tnemc.core.command.ModuleCommand.onLoad(new ArgumentsParser(new BukkitCMDSource(sender), args));
  }
}