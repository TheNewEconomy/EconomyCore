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
import net.tnemc.core.command.args.ArgumentsParser;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.core.compatibility.scheduler.ChoreTime;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.io.storage.StorageManager;
import net.tnemc.core.utils.Extractor;

import java.util.Optional;

/**
 * AdminCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AdminCommand extends BaseCommand {

  public static void onMyEco(ArgumentsParser parser) {
    if(parser.sender().player().isPresent()) {
      parser.sender().player().get().inventory().openMenu(parser.sender().player().get(), "my_eco");
    }
  }

  public static void onBackup(ArgumentsParser parser) {
    if(StorageManager.instance().backup()) {

      parser.sender().message(new MessageData("Messages.Data.Backup"));
      return;
    }
    parser.sender().message(new MessageData("Messages.Data.BackupFailed"));
  }

  //<player> [balance]
  public static void onCreate(ArgumentsParser parser) {
    if(parser.args().length < 1) {
      help(parser.sender(), "tne create", "Admin.Create");
      return;
    }

    if(TNECore.eco().account().findAccount(parser.args()[0]).isPresent()) {

      //Our account already exists, so we can't make it.
      final MessageData data = new MessageData("Messages.Admin.Exists");
      data.addReplacement("$name", parser.args()[0]);
      parser.sender().message(data);
      return;
    }

    final AccountAPIResponse response = TNECore.eco().account().createAccount(parser.args()[0]);
    if(response.getResponse().success()) {
      final MessageData data = new MessageData("Messages.Admin.Created");
      data.addReplacement("$name", parser.args()[0]);
      parser.sender().message(data);
      return;
    }
    final MessageData data = new MessageData("Messages.Admin.CreationFailed");
    data.addReplacement("$name", parser.args()[0]);
    parser.sender().message(data);
  }

  //<standard/detailed/developer>
  public static void onDebug(ArgumentsParser parser) {
    if(parser.args().length < 1) {
      help(parser.sender(), "tne debug", "Admin.Debug");
      return;
    }

    DebugLevel level = switch(parser.args()[0].toLowerCase()) {
      case "detailed" -> DebugLevel.DETAILED;
      case "developer" -> DebugLevel.DEVELOPER;
      default -> DebugLevel.STANDARD;
    };

    TNECore.instance().setLevel(level);
    final MessageData data = new MessageData("Messages.Data.Debug");
    data.addReplacement("$level", level.name());
    parser.sender().message(data);
  }

  public static void onDelete(ArgumentsParser parser) {
    if(parser.args().length < 1) {
      help(parser.sender(), "tne delete", "Admin.Delete");
      return;
    }

    if(parser.parseAccount(0).isEmpty()) {

      //Our account doesn't exist, so we can't delete it.
      final MessageData data = new MessageData("Messages.Admin.NoAccount");
      data.addReplacement("$name", parser.args()[0]);
      parser.sender().message(data);
      return;
    }

    final EconomyResponse response = TNECore.eco().account().deleteAccount(parser.args()[0]);
    if(response.success()) {

      //deleted
      final MessageData data = new MessageData("Messages.Admin.Deleted");
      data.addReplacement("$name", parser.args()[0]);
      parser.sender().message(data);
      return;
    }

    //failed deletion
    final MessageData data = new MessageData("Messages.Admin.DeletionFailed");
    data.addReplacement("$name", parser.args()[0]);
    parser.sender().message(data);
  }

  public static void onExtract(ArgumentsParser parser) {


    TNECore.server().scheduler().createDelayedTask(()->Extractor.extract(), new ChoreTime(0), ChoreExecution.SECONDARY);
    parser.sender().message(new MessageData("Messages.Admin.Extraction"));
  }

  public static void onPurge(ArgumentsParser parser) {
    //TODO: Storage Manager
  }

  public static void onReload(ArgumentsParser parser) {
    if(parser.args().length >= 1) {
      switch(parser.args()[0].toLowerCase()) {
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
  }

  public static void onReset(ArgumentsParser parser) {
    StorageManager.instance().reset();
  }

  public static void onRestore(ArgumentsParser parser) {


    TNECore.server().scheduler().createDelayedTask(()->Extractor.restore(parser.parseInt(0, 0)), new ChoreTime(0), ChoreExecution.SECONDARY);
    parser.sender().message(new MessageData("Messages.Admin.Restoration"));
  }

  public static void onSave(ArgumentsParser parser) {
    StorageManager.instance().storeAll();
    parser.sender().message(new MessageData("Messages.Data.Save"));
  }

  //<account> [status]
  public static void onStatus(ArgumentsParser parser) {
    if(parser.args().length < 1) {
      help(parser.sender(), "tne status", "Admin.Status");
      return;
    }

    final Optional<Account> account = parser.parseAccount(0);
    if(account.isEmpty()) {
      return;
    }

    if(parser.args().length < 2) {

      //We are just checking for the account's status now.
      final MessageData data = new MessageData("Messages.Admin.Status");
      data.addReplacement("$name", parser.args()[0]);
      data.addReplacement("$status", account.get().getStatus().identifier());
      parser.sender().message(data);
      return;
    }

    final AccountStatus status = TNECore.eco().account().findStatus(parser.args()[0]);

    //Set the account's status to the new one.
    account.get().setStatus(status);

    final MessageData data = new MessageData("Messages.Admin.StatusChange");
    data.addReplacement("$name", parser.args()[0]);
    data.addReplacement("$status", status.identifier());
    parser.sender().message(data);
  }

  public static void onVersion(ArgumentsParser parser) {
    final MessageData data = new MessageData("Messages.General.Version");
    data.addReplacement("$version", TNECore.version);
    data.addReplacement("$build", TNECore.build);
    parser.sender().message(data);
  }
}
