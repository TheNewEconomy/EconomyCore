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

import net.tnemc.core.command.args.ArgumentsParser;
import net.tnemc.sponge.impl.SpongeCMDSource;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.sponge.SpongeCommandActor;
import revxrsal.commands.sponge.annotation.CommandPermission;

/**
 * AdminCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Command({"tne", "ecomin", "ecoadmin", "ecomanage", "theneweconomy"})
@Description("#{Admin.Main.Description}")
public class AdminCommand {

  @DefaultFor({"tne", "ecomin", "ecoadmin", "ecomanage", "theneweconomy"})
  @Subcommand({"ecomenu", "menu", "myeco", "ecomenu"})
  @Usage("#{Admin.MyEco.Arguments}")
  @Description("#{Admin.MyEco.Description}")
  @CommandPermission("tne.money.myeco")
  public void onMyEco(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onMyEco(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"backup", "archive"})
  @Usage("#{Admin.Backup.Arguments}")
  @Description("#{Admin.Backup.Description}")
  @CommandPermission("tne.admin.backup")
  public void backup(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onBackup(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"create", "add", "new", "make", "+"})
  @Usage("#{Admin.Create.Arguments}")
  @Description("#{Admin.Create.Description}")
  @CommandPermission("tne.admin.create")
  public void create(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onCreate(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"debug"})
  @Usage("#{Admin.Debug.Arguments}")
  @Description("#{Admin.Debug.Description}")
  @CommandPermission("tne.admin.debug")
  public void debug(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onDebug(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"delete", "destroy", "del", "remove", "-"})
  @Usage("#{Admin.Delete.Arguments}")
  @Description("#{Admin.Delete.Description}")
  @CommandPermission("tne.admin.delete")
  public void delete(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onDelete(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"extract"})
  @Usage("#{Admin.Extract.Arguments}")
  @Description("#{Admin.Extract.Description}")
  @CommandPermission("tne.admin.extract")
  public void extract(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onExtract(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"purge"})
  @Usage("#{Admin.Purge.Arguments}")
  @Description("#{Admin.Purge.Description}")
  @CommandPermission("tne.admin.purge")
  public void purge(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onPurge(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"reload"})
  @Usage("#{Admin.Reload.Arguments}")
  @Description("#{Admin.Reload.Description}")
  @CommandPermission("tne.admin.reload")
  public void reload(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onReload(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"reset", "nuke"})
  @Usage("#{Admin.Reset.Arguments}")
  @Description("#{Admin.Reset.Description}")
  @CommandPermission("tne.admin.reset")
  public void reset(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onReset(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"restore"})
  @Usage("#{Admin.Restore.Arguments}")
  @Description("#{Admin.Restore.Description}")
  @CommandPermission("tne.admin.restore")
  public void restore(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onRestore(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"save"})
  @Usage("#{Admin.Save.Arguments}")
  @Description("#{Admin.Save.Description}")
  @CommandPermission("tne.admin.save")
  public void save(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onSave(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"status"})
  @Usage("#{Admin.Status.Arguments}")
  @Description("#{Admin.Status.Description}")
  @CommandPermission("tne.admin.status")
  public void status(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onStatus(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }

  @Subcommand({"version", "ver", "build"})
  @Usage("#{Admin.Version.Arguments}")
  @Description("#{Admin.Version.Description}")
  @CommandPermission("tne.admin.version")
  public void version(SpongeCommandActor sender, String[] args) {
    net.tnemc.core.command.AdminCommand.onVersion(new ArgumentsParser(new SpongeCMDSource(sender), args));
  }
}
