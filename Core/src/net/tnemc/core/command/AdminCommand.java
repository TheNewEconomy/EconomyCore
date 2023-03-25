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

import co.aikar.commands.BaseCommand;
import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.CmdSource;

/**
 * AdminCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AdminCommand extends BaseCommand {

  public static void onMyEco(CmdSource sender, String[] args) {
    if(sender.player().isPresent()) {
      sender.player().get().inventory().openMenu(sender.player().get(), "my_eco");
    }
  }

  public static void onBackup(CmdSource sender, String[] args) {

  }

  public static void onCreate(CmdSource sender, String[] args) {

  }

  public static void onDebug(CmdSource sender, String[] args) {

  }

  public static void onDelete(CmdSource sender, String[] args) {

  }

  public static void onExtract(CmdSource sender, String[] args) {

  }

  public static void onPurge(CmdSource sender, String[] args) {

  }

  public static void onReload(CmdSource sender, String[] args) {
    if(args.length >= 1) {
      switch(args[0].toLowerCase()) {
        case "config" -> {
          TNECore.instance().config().load();
          TNECore.eco().currency().load(TNECore.directory());
          return;
        }
        case "data" -> {
          TNECore.instance().data().load();
          return;
        }
        case "message" -> {
          TNECore.instance().message().load();
          return;
        }
      }
    }
    TNECore.instance().config().load();
    TNECore.eco().currency().load(TNECore.directory());
    TNECore.instance().data().load();
    //TODO: Reload data manager.
    TNECore.instance().message().load();
  }

  public static void onReset(CmdSource sender, String[] args) {

  }

  public static void onRestore(CmdSource sender, String[] args) {

  }

  public static void onSave(CmdSource sender, String[] args) {

  }

  public static void onStatus(CmdSource sender, String[] args) {

  }

  public static void onVersion(CmdSource sender, String[] args) {

  }
}
