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

import net.tnemc.core.account.Account;
import net.tnemc.core.account.AccountStatus;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.sponge.impl.SpongeCMDSource;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
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
  public void onMyEco(SpongeCommandActor sender) {
    net.tnemc.core.command.AdminCommand.onMyEco(new SpongeCMDSource(sender));
  }

  @Subcommand({"backup", "archive"})
  @Usage("#{Admin.Backup.Arguments}")
  @Description("#{Admin.Backup.Description}")
  @CommandPermission("tne.admin.backup")
  public void backup(SpongeCommandActor sender) {
    net.tnemc.core.command.AdminCommand.onBackup(new SpongeCMDSource(sender));
  }

  @Subcommand({"create", "add", "new", "make", "+"})
  @Usage("#{Admin.Create.Arguments}")
  @Description("#{Admin.Create.Description}")
  @CommandPermission("tne.admin.create")
  public void create(SpongeCommandActor sender, String name) {
    net.tnemc.core.command.AdminCommand.onCreate(new SpongeCMDSource(sender), name);
  }

  @Subcommand({"debug"})
  @Usage("#{Admin.Debug.Arguments}")
  @Description("#{Admin.Debug.Description}")
  @CommandPermission("tne.admin.debug")
  public void debug(SpongeCommandActor sender, DebugLevel level) {
    net.tnemc.core.command.AdminCommand.onDebug(new SpongeCMDSource(sender), level);
  }

  @Subcommand({"delete", "destroy", "del", "remove", "-"})
  @Usage("#{Admin.Delete.Arguments}")
  @Description("#{Admin.Delete.Description}")
  @CommandPermission("tne.admin.delete")
  public void delete(SpongeCommandActor sender, String name) {
    net.tnemc.core.command.AdminCommand.onDelete(new SpongeCMDSource(sender), name);
  }

  @Subcommand({"extract"})
  @Usage("#{Admin.Extract.Arguments}")
  @Description("#{Admin.Extract.Description}")
  @CommandPermission("tne.admin.extract")
  public void extract(SpongeCommandActor sender) {
    net.tnemc.core.command.AdminCommand.onExtract(new SpongeCMDSource(sender));
  }

  @Subcommand({"purge"})
  @Usage("#{Admin.Purge.Arguments}")
  @Description("#{Admin.Purge.Description}")
  @CommandPermission("tne.admin.purge")
  public void purge(SpongeCommandActor sender) {
    net.tnemc.core.command.AdminCommand.onPurge(new SpongeCMDSource(sender));
  }

  @Subcommand({"reload"})
  @Usage("#{Admin.Reload.Arguments}")
  @Description("#{Admin.Reload.Description}")
  @CommandPermission("tne.admin.reload")
  public void reload(SpongeCommandActor sender, @Default("all") String type) {
    net.tnemc.core.command.AdminCommand.onReload(new SpongeCMDSource(sender), type);
  }

  @Subcommand({"reset", "nuke"})
  @Usage("#{Admin.Reset.Arguments}")
  @Description("#{Admin.Reset.Description}")
  @CommandPermission("tne.admin.reset")
  public void reset(SpongeCommandActor sender) {
    net.tnemc.core.command.AdminCommand.onReset(new SpongeCMDSource(sender));
  }

  @Subcommand({"restore"})
  @Usage("#{Admin.Restore.Arguments}")
  @Description("#{Admin.Restore.Description}")
  @CommandPermission("tne.admin.restore")
  public void restore(SpongeCommandActor sender, @Default("0") int extraction) {
    net.tnemc.core.command.AdminCommand.onRestore(new SpongeCMDSource(sender), extraction);
  }

  @Subcommand({"save"})
  @Usage("#{Admin.Save.Arguments}")
  @Description("#{Admin.Save.Description}")
  @CommandPermission("tne.admin.save")
  public void save(SpongeCommandActor sender) {
    net.tnemc.core.command.AdminCommand.onSave(new SpongeCMDSource(sender));
  }

  @Subcommand({"status"})
  @Usage("#{Admin.Status.Arguments}")
  @Description("#{Admin.Status.Description}")
  @CommandPermission("tne.admin.status")
  public void status(SpongeCommandActor sender, Account account, @Default("normal") AccountStatus status) {
    net.tnemc.core.command.AdminCommand.onStatus(new SpongeCMDSource(sender), account, status);
  }

  @Subcommand({"version", "ver", "build"})
  @Usage("#{Admin.Version.Arguments}")
  @Description("#{Admin.Version.Description}")
  @CommandPermission("tne.admin.version")
  public void version(SpongeCommandActor sender) {
    net.tnemc.core.command.AdminCommand.onVersion(new SpongeCMDSource(sender));
  }
}
