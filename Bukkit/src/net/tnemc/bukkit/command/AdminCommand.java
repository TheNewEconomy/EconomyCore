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

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import net.tnemc.bukkit.impl.BukkitCMDSource;
import org.bukkit.command.CommandSender;

/**
 * AdminCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@CommandAlias("tne|ecomin|ecoadmin|ecomanage|theneweconomy")
public class AdminCommand extends BaseCommand {

  @Subcommand("backup|archive")
  @Syntax("%Admin.Backup.Arguments")
  @Description("%Admin.Backup.Description")
  @CommandPermission("tne.admin.backup")
  public void backup(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.backup(new BukkitCMDSource(sender), args);
  }

  @Subcommand("bal|balance|info")
  @Syntax("%Admin.Balance.Arguments")
  @Description("%Admin.Balance.Description")
  @CommandPermission("tne.admin.balance")
  public void balance(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.balance(new BukkitCMDSource(sender), args);
  }

  @Subcommand("create|add|new|make|+")
  @Syntax("%Admin.Create.Arguments")
  @Description("%Admin.Create.Description")
  @CommandPermission("tne.admin.create")
  public void create(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.create(new BukkitCMDSource(sender), args);
  }

  @Subcommand("debug")
  @Syntax("%Admin.Debug.Arguments")
  @Description("%Admin.Debug.Description")
  @CommandPermission("tne.admin.debug")
  public void debug(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.debug(new BukkitCMDSource(sender), args);
  }

  @Subcommand("delete|destroy|del|remove|-")
  @Syntax("%Admin.Delete.Arguments")
  @Description("%Admin.Delete.Description")
  @CommandPermission("tne.admin.delete")
  public void delete(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.delete(new BukkitCMDSource(sender), args);
  }

  @Subcommand("extract")
  @Syntax("%Admin.Extract.Arguments")
  @Description("%Admin.Extract.Description")
  @CommandPermission("tne.admin.extract")
  public void extract(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.extract(new BukkitCMDSource(sender), args);
  }

  @Subcommand("purge")
  @Syntax("%Admin.Purge.Arguments")
  @Description("%Admin.Purge.Description")
  @CommandPermission("tne.admin.purge")
  public void purge(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.purge(new BukkitCMDSource(sender), args);
  }

  @Subcommand("reload")
  @Syntax("%Admin.Reload.Arguments")
  @Description("%Admin.Reload.Description")
  @CommandPermission("tne.admin.reload")
  public void reload(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.reload(new BukkitCMDSource(sender), args);
  }

  @Subcommand("reset|nuke")
  @Syntax("%Admin.Reset.Arguments")
  @Description("%Admin.Reset.Description")
  @CommandPermission("tne.admin.reset")
  public void reset(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.reset(new BukkitCMDSource(sender), args);
  }

  @Subcommand("restore")
  @Syntax("%Admin.Restore.Arguments")
  @Description("%Admin.Restore.Description")
  @CommandPermission("tne.admin.restore")
  public void restore(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.restore(new BukkitCMDSource(sender), args);
  }

  @Subcommand("save")
  @Syntax("%Admin.Save.Arguments")
  @Description("%Admin.Save.Description")
  @CommandPermission("tne.admin.save")
  public void save(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.save(new BukkitCMDSource(sender), args);
  }

  @Subcommand("status")
  @Syntax("%Admin.Status.Arguments")
  @Description("%Admin.Status.Description")
  @CommandPermission("tne.admin.status")
  public void status(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.status(new BukkitCMDSource(sender), args);
  }

  @Subcommand("upload")
  @Syntax("%Admin.Upload.Arguments")
  @Description("%Admin.Upload.Description")
  @CommandPermission("tne.admin.upload")
  public void upload(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.upload(new BukkitCMDSource(sender), args);
  }

  @Subcommand("version|ver|build")
  @Syntax("%Admin.Version.Arguments")
  @Description("%Admin.Version.Description")
  @CommandPermission("tne.admin.version")
  public void version(CommandSender sender, String[] args) {
    net.tnemc.core.command.AdminCommand.version(new BukkitCMDSource(sender), args);
  }
}
