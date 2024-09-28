package net.tnemc.core.command;

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
import net.tnemc.core.account.Account;
import net.tnemc.core.account.AccountStatus;
import net.tnemc.core.actions.EconomyResponse;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.utils.Extractor;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.CmdSource;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreTime;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.storage.StorageManager;

import java.util.Optional;

/**
 * AdminCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AdminCommand extends BaseCommand {

  public static void onMyEco(final CmdSource<?> sender) {

    if(sender.player().isPresent()) {

      sender.player().get().inventory().openMenu(sender.player().get(), "my_eco");
    }
  }

  public static void onBackup(final CmdSource<?> sender) {

    if(StorageManager.instance().backup()) {

      sender.message(new MessageData("Messages.Data.Backup"));
      return;
    }
    sender.message(new MessageData("Messages.Data.BackupFailed"));
  }

  //<player> [balance]
  public static void onCreate(final CmdSource<?> sender, final String name) {

    if(TNECore.eco().account().findAccount(name).isPresent()) {

      //Our account already exists, so we can't make it.
      final MessageData data = new MessageData("Messages.Admin.Exists");
      data.addReplacement("$name", name);
      sender.message(data);
      return;
    }

    final AccountAPIResponse response = TNECore.eco().account().createAccount(name);
    if(response.getResponse().success()) {
      final MessageData data = new MessageData("Messages.Admin.Created");
      data.addReplacement("$name", name);
      sender.message(data);
      return;
    }
    final MessageData data = new MessageData("Messages.Admin.CreationFailed");
    data.addReplacement("$name", name);
    sender.message(data);
  }

  //<standard/detailed/developer>
  public static void onDebug(final CmdSource<?> sender, final DebugLevel level) {

    PluginCore.instance().setLevel(level);
    final MessageData data = new MessageData("Messages.Data.Debug");
    data.addReplacement("$level", level.name());
    sender.message(data);
  }

  public static void onDelete(final CmdSource<?> sender, final String name) {

    final Optional<Account> acc = TNECore.eco().account().findAccount(name);
    if(acc.isEmpty()) {

      //Our account doesn't exist, so we can't delete it.
      final MessageData data = new MessageData("Messages.Admin.NoAccount");
      data.addReplacement("$name", name);
      sender.message(data);
      return;
    }

    final EconomyResponse response = TNECore.eco().account().deleteAccount(acc.get().getIdentifier());
    if(response.success()) {

      //deleted
      final MessageData data = new MessageData("Messages.Admin.Deleted");
      data.addReplacement("$name", name);
      sender.message(data);
      return;
    }

    //failed deletion
    final MessageData data = new MessageData("Messages.Admin.DeletionFailed");
    data.addReplacement("$name", name);
    sender.message(data);
  }

  public static void onExtract(final CmdSource<?> sender) {


    PluginCore.server().scheduler().createDelayedTask(Extractor::extract, new ChoreTime(0), ChoreExecution.SECONDARY);
    sender.message(new MessageData("Messages.Admin.Extraction"));
  }

  public static void onPurge(final CmdSource<?> sender) {

    TNECore.instance().storage().purge();
  }

  public static void onReload(final CmdSource<?> sender, final String type) {

    String formattedType = type;
    switch(type.toLowerCase()) {
      case "config" -> {
        TNECore.instance().config().load();
        TNECore.eco().currency().load(PluginCore.directory());
      }
      case "data" -> TNECore.instance().data().load();
      case "message" -> TNECore.instance().message().load();
      default -> {
        TNECore.instance().config().load();
        TNECore.eco().currency().load(PluginCore.directory());
        TNECore.instance().data().load();
        TNECore.instance().storage().loadAll(Account.class, "");
        TNECore.instance().message().load();
        formattedType = "all";
      }
    }

    final MessageData data = new MessageData("Messages.Admin.Reloaded");
    data.addReplacement("$type", formattedType);
    sender.message(data);
  }

  public static void onReset(final CmdSource<?> sender) {

    StorageManager.instance().reset();
  }

  public static void onRestore(final CmdSource<?> sender, final int extraction) {


    PluginCore.server().scheduler().createDelayedTask(()->Extractor.restore(extraction), new ChoreTime(0), ChoreExecution.SECONDARY);
    sender.message(new MessageData("Messages.Admin.Restoration"));
  }

  public static void onSave(final CmdSource<?> sender) {

    StorageManager.instance().storeAll();
    sender.message(new MessageData("Messages.Data.Save"));
  }

  //<account> [status]
  public static void onStatus(final CmdSource<?> sender, final Account account, final AccountStatus status) {

    //Set the account's status to the new one.
    account.setStatus(status);

    final MessageData data = new MessageData("Messages.Admin.StatusChange");
    data.addReplacement("$name", account.getName());
    data.addReplacement("$status", status.identifier());
    sender.message(data);
  }

  public static void onVersion(final CmdSource<?> sender) {

    final MessageData data = new MessageData("Messages.General.Version");
    data.addReplacement("$version", TNECore.version);
    data.addReplacement("$build", TNECore.build);
    sender.message(data);
  }
}
