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

import net.tnemc.plugincore.sponge.impl.SpongeCMDSource;;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.sponge.SpongeCommandActor;
import revxrsal.commands.sponge.annotation.CommandPermission;

/**
 * ModuleCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Command({"module", "mod"})
public class ModuleCommand {

  @Subcommand({"info", "i"})
  @Usage("#{Module.Info.Arguments}")
  @Description("#{Module.Info.Description}")
  @CommandPermission("tne.module.info")
  public void onInfo(SpongeCommandActor sender, String moduleName) {
    net.tnemc.core.command.ModuleCommand.onInfo(new SpongeCMDSource(sender), moduleName);
  }

  @Subcommand({"list", "l"})
  @Usage("#{Module.List.Arguments}")
  @DefaultFor({"module", "mod"})
  @Description("#{Module.List.Description}")
  @CommandPermission("tne.list.available")
  public void onList(SpongeCommandActor sender) {
    net.tnemc.core.command.ModuleCommand.onList(new SpongeCMDSource(sender));
  }
}