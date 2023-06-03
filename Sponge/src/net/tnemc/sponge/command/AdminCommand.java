package net.tnemc.sponge.command;

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
import net.tnemc.core.command.args.ArgumentsParser;
import net.tnemc.sponge.impl.SpongeCMDSource;
import org.spongepowered.api.command.CommandSource;

/**
 * AdminCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@CommandAlias("tne|ecomin|ecoadmin|ecomanage|theneweconomy")
public class AdminCommand extends BaseCommand {

  @Default
  @Subcommand("ecomenu|menu")
  @CommandAlias("myeco|ecomenu")
  @Syntax("#{Money.MyEco.ArgumentsParser}")
  @Description("#{Money.MyEco.Description}")
  @CommandPermission("tne.money.myeco")
  public void onMyEco(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onMyEco(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("backup|archive")
  @Syntax("#{Admin.Backup.ArgumentsParser}")
  @Description("#{Admin.Backup.Description}")
  @CommandPermission("tne.admin.backup")
  public void backup(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onBackup(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("create|add|new|make|+")
  @Syntax("#{Admin.Create.ArgumentsParser}")
  @Description("#{Admin.Create.Description}")
  @CommandPermission("tne.admin.create")
  public void create(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onCreate(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("debug")
  @Syntax("#{Admin.Debug.ArgumentsParser}")
  @Description("#{Admin.Debug.Description}")
  @CommandPermission("tne.admin.debug")
  public void debug(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onDebug(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("delete|destroy|del|remove|-")
  @Syntax("#{Admin.Delete.ArgumentsParser}")
  @Description("#{Admin.Delete.Description}")
  @CommandPermission("tne.admin.delete")
  public void delete(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onDelete(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("extract")
  @Syntax("#{Admin.Extract.ArgumentsParser}")
  @Description("#{Admin.Extract.Description}")
  @CommandPermission("tne.admin.extract")
  public void extract(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onExtract(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("purge")
  @Syntax("#{Admin.Purge.ArgumentsParser}")
  @Description("#{Admin.Purge.Description}")
  @CommandPermission("tne.admin.purge")
  public void purge(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onPurge(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("reload")
  @Syntax("#{Admin.Reload.ArgumentsParser}")
  @Description("#{Admin.Reload.Description}")
  @CommandPermission("tne.admin.reload")
  public void reload(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onReload(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("reset|nuke")
  @Syntax("#{Admin.Reset.ArgumentsParser}")
  @Description("#{Admin.Reset.Description}")
  @CommandPermission("tne.admin.reset")
  public void reset(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onReset(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("restore")
  @Syntax("#{Admin.Restore.ArgumentsParser}")
  @Description("#{Admin.Restore.Description}")
  @CommandPermission("tne.admin.restore")
  public void restore(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onRestore(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("save")
  @Syntax("#{Admin.Save.ArgumentsParser}")
  @Description("#{Admin.Save.Description}")
  @CommandPermission("tne.admin.save")
  public void save(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onSave(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("status")
  @Syntax("#{Admin.Status.ArgumentsParser}")
  @Description("#{Admin.Status.Description}")
  @CommandPermission("tne.admin.status")
  public void status(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onStatus(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand("version|ver|build")
  @Syntax("#{Admin.Version.ArgumentsParser}")
  @Description("#{Admin.Version.Description}")
  @CommandPermission("tne.admin.version")
  public void version(CommandSource sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onVersion(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }
}
