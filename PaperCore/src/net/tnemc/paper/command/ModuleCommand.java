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

import net.tnemc.core.TNECore;
import net.tnemc.core.command.BaseCommand;
import net.tnemc.plugincore.paper.impl.PaperCMDSource;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.help.CommandHelp;

/**
 * ModuleCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Command({ "module", "mod" })
public class ModuleCommand {

  @Subcommand({ "help", "?" })
  @Usage("Help.Arguments")
  @Description("Help.Description")
  public void help(final BukkitCommandActor actor, final CommandHelp<String> helpEntries, @Default("1") final int page) {

    BaseCommand.help(new PaperCMDSource(actor), helpEntries, page);
  }

  @Subcommand({ "avail", "available" })
  @Usage("Module.Available.Arguments")
  @Description("Module.Available.Description")
  @CommandPermission("tne.module.available")
  public void onAvailable(final BukkitCommandActor sender, @Default(TNECore.coreURL) final String url) {

    net.tnemc.core.command.ModuleCommand.onAvailable(new PaperCMDSource(sender), url);
  }

  @Subcommand({ "download", "dl" })
  @Usage("Module.Download.Arguments")
  @Description("Module.Download.Description")
  @CommandPermission("tne.module.download")
  public void onDownload(final BukkitCommandActor sender, final String moduleName, @Default(TNECore.coreURL) final String url) {

    net.tnemc.core.command.ModuleCommand.onDownload(new PaperCMDSource(sender), moduleName, url);
  }

  @Subcommand({ "info", "i" })
  @Usage("Module.Info.Arguments")
  @Description("Module.Info.Description")
  @CommandPermission("tne.module.info")
  public void onInfo(final BukkitCommandActor sender, final String moduleName) {

    net.tnemc.core.command.ModuleCommand.onInfo(new PaperCMDSource(sender), moduleName);
  }

  @Subcommand({ "list", "l" })
  @Usage("Module.List.Arguments")
  @DefaultFor({ "module", "mod" })
  @Description("Module.List.Description")
  @CommandPermission("tne.list.available")
  public void onList(final BukkitCommandActor sender) {

    net.tnemc.core.command.ModuleCommand.onList(new PaperCMDSource(sender));
  }

  @Subcommand({ "load" })
  @Usage("Module.Load.Arguments")
  @Description("Module.Load.Description")
  @CommandPermission("tne.module.load")
  public void onLoad(final BukkitCommandActor sender, final String moduleName) {

    net.tnemc.core.command.ModuleCommand.onLoad(new PaperCMDSource(sender), moduleName);
  }
}