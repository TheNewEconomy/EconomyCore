package net.tnemc.core.command;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.AccountStatus;
import net.tnemc.core.actions.EconomyResponse;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.core.compatibility.scheduler.ChoreTime;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.io.storage.StorageManager;
import net.tnemc.core.utils.Extractor;

/**
 * AdminCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AdminCommand extends BaseCommand {

  public static void onMyEco(CmdSource<?> sender) {
    if(sender.player().isPresent()) {
      sender.player().get().inventory().openMenu(sender.player().get(), "my_eco");
    }
  }

  public static void onBackup(CmdSource<?> sender) {
    if(StorageManager.instance().backup()) {

      sender.message(new MessageData("Messages.Data.Backup"));
      return;
    }
    sender.message(new MessageData("Messages.Data.BackupFailed"));
  }

  //<player> [balance]
  public static void onCreate(CmdSource<?> sender, String name) {

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
  public static void onDebug(CmdSource<?> sender, String level) {

    final DebugLevel debug = switch(level.toLowerCase()) {
      case "detailed" -> DebugLevel.DETAILED;
      case "developer" -> DebugLevel.DEVELOPER;
      default -> DebugLevel.STANDARD;
    };

    TNECore.instance().setLevel(debug);
    final MessageData data = new MessageData("Messages.Data.Debug");
    data.addReplacement("$level", debug.name());
    sender.message(data);
  }

  public static void onDelete(CmdSource<?> sender, String name) {

    if(TNECore.eco().account().findAccount(name).isEmpty()) {

      //Our account doesn't exist, so we can't delete it.
      final MessageData data = new MessageData("Messages.Admin.NoAccount");
      data.addReplacement("$name", name);
      sender.message(data);
      return;
    }

    final EconomyResponse response = TNECore.eco().account().deleteAccount(name);
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

  public static void onExtract(CmdSource<?> sender) {


    TNECore.server().scheduler().createDelayedTask(Extractor::extract, new ChoreTime(0), ChoreExecution.SECONDARY);
    sender.message(new MessageData("Messages.Admin.Extraction"));
  }

  public static void onPurge(CmdSource<?> sender) {
    TNECore.storage().purge();
  }

  public static void onReload(CmdSource<?> sender, String type) {
    switch(type.toLowerCase()) {
      case "config" -> {
        TNECore.instance().config().load();
        TNECore.eco().currency().load(TNECore.directory());
      }
      case "data" -> {
        TNECore.instance().data().load();
      }
      case "message" -> {
        TNECore.instance().message().load();
      }
      default -> {
        TNECore.instance().config().load();
        TNECore.eco().currency().load(TNECore.directory());
        TNECore.instance().data().load();
        TNECore.storage().loadAll(Account.class, "");
        TNECore.instance().message().load();
      }
    }
  }

  public static void onReset(CmdSource<?> sender) {
    StorageManager.instance().reset();
  }

  public static void onRestore(CmdSource<?> sender, int extraction) {


    TNECore.server().scheduler().createDelayedTask(()->Extractor.restore(extraction), new ChoreTime(0), ChoreExecution.SECONDARY);
    sender.message(new MessageData("Messages.Admin.Restoration"));
  }

  public static void onSave(CmdSource<?> sender) {
    StorageManager.instance().storeAll();
    sender.message(new MessageData("Messages.Data.Save"));
  }

  //<account> [status]
  public static void onStatus(CmdSource<?> sender, Account account, String status) {

    if(status.trim().equalsIgnoreCase("")) {

      //We are just checking for the account's status now.
      final MessageData data = new MessageData("Messages.Admin.Status");
      data.addReplacement("$name", account.getName());
      data.addReplacement("$status", account.getStatus().identifier());
      sender.message(data);
      return;
    }

    final AccountStatus accStatus = TNECore.eco().account().findStatus(status);

    //Set the account's status to the new one.
    account.setStatus(accStatus);

    final MessageData data = new MessageData("Messages.Admin.StatusChange");
    data.addReplacement("$name", account.getName());
    data.addReplacement("$status", accStatus.identifier());
    sender.message(data);
  }

  public static void onVersion(CmdSource<?> sender) {
    final MessageData data = new MessageData("Messages.General.Version");
    data.addReplacement("$version", TNECore.version);
    data.addReplacement("$build", TNECore.build);
    sender.message(data);
  }
}
