package net.tnemc.bukkit.command;
/*
 * The New Economy
 * Copyright (CommandSender) 2022 Daniel "creatorfromhell" Vidmar
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
import org.bukkit.command.CommandSender;

/**
 * ConfigCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@CommandAlias("tneconfig|tnec|ecoconfig")
public class ConfigCommand extends BaseCommand {

  @Default
  @Subcommand("get|find|value")
  @Syntax("%Config.Get.Arguments")
  @Description("%Config.Get.Description")
  @CommandPermission("tne.config.get")
  public void get(CommandSender sender, String[] args) {

  }

  @Subcommand("set|=")
  @Syntax("%Config.Set.Arguments")
  @Description("%Config.Set.Description")
  @CommandPermission("tne.config.set")
  public void set(CommandSender sender, String[] args) {

  }
}
