package net.tnemc.core.command;
/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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

/**
 * AdminCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@CommandAlias("tne|ecomin|ecoadmin|ecomanage|theneweconomy")
public class AdminCommand<C> extends BaseCommand {

  @Subcommand("backup|archive")
  @Syntax("%Admin.Backup.Arguments")
  @Description("%Admin.Backup.Description")
  @CommandPermission("tne.admin.backup")
  public void backup(C sender, String[] args) {

  }

  @Subcommand("bal|balance|info")
  @Syntax("%Admin.Balance.Arguments")
  @Description("%Admin.Balance.Description")
  @CommandPermission("tne.admin.balance")
  public void balance(C sender, String[] args) {

  }

  @Subcommand("create|add|new|make|+")
  @Syntax("%Admin.Create.Arguments")
  @Description("%Admin.Create.Description")
  @CommandPermission("tne.admin.create")
  public void create(C sender, String[] args) {

  }

  @Subcommand("debug")
  @Syntax("%Admin.Debug.Arguments")
  @Description("%Admin.Debug.Description")
  @CommandPermission("tne.admin.debug")
  public void debug(C sender, String[] args) {

  }

  @Subcommand("delete|destroy|del|remove|-")
  @Syntax("%Admin.Delete.Arguments")
  @Description("%Admin.Delete.Description")
  @CommandPermission("tne.admin.delete")
  public void delete(C sender, String[] args) {

  }

  @Subcommand("extract")
  @Syntax("%Admin.Extract.Arguments")
  @Description("%Admin.Extract.Description")
  @CommandPermission("tne.admin.extract")
  public void extract(C sender, String[] args) {

  }

  @Subcommand("purge")
  @Syntax("%Admin.Purge.Arguments")
  @Description("%Admin.Purge.Description")
  @CommandPermission("tne.admin.purge")
  public void purge(C sender, String[] args) {

  }

  @Subcommand("reload")
  @Syntax("%Admin.Reload.Arguments")
  @Description("%Admin.Reload.Description")
  @CommandPermission("tne.admin.reload")
  public void reload(C sender, String[] args) {

  }

  @Subcommand("reset|nuke")
  @Syntax("%Admin.Reset.Arguments")
  @Description("%Admin.Reset.Description")
  @CommandPermission("tne.admin.reset")
  public void reset(C sender, String[] args) {

  }

  @Subcommand("restore")
  @Syntax("%Admin.Restore.Arguments")
  @Description("%Admin.Restore.Description")
  @CommandPermission("tne.admin.restore")
  public void restore(C sender, String[] args) {

  }

  @Subcommand("save")
  @Syntax("%Admin.Save.Arguments")
  @Description("%Admin.Save.Description")
  @CommandPermission("tne.admin.save")
  public void save(C sender, String[] args) {

  }

  @Subcommand("status")
  @Syntax("%Admin.Status.Arguments")
  @Description("%Admin.Status.Description")
  @CommandPermission("tne.admin.status")
  public void status(C sender, String[] args) {

  }

  @Subcommand("upload")
  @Syntax("%Admin.Upload.Arguments")
  @Description("%Admin.Upload.Description")
  @CommandPermission("tne.admin.upload")
  public void upload(C sender, String[] args) {

  }

  @Subcommand("version|ver|build")
  @Syntax("%Admin.Version.Arguments")
  @Description("%Admin.Version.Description")
  @CommandPermission("tne.admin.version")
  public void version(C sender, String[] args) {

  }
}
