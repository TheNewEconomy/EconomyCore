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
import net.tnemc.core.command.args.ArgumentsParser;

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

  }

  public static void onCreate(ArgumentsParser parser) {

  }

  public static void onDebug(ArgumentsParser parser) {

  }

  public static void onDelete(ArgumentsParser parser) {

  }

  public static void onExtract(ArgumentsParser parser) {

  }

  public static void onPurge(ArgumentsParser parser) {

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
          //TODO: Reload data manager.
          TNECore.instance().message().load();
        }
      }
    }
  }

  public static void onReset(ArgumentsParser parser) {

  }

  public static void onRestore(ArgumentsParser parser) {

  }

  public static void onSave(ArgumentsParser parser) {

  }

  public static void onStatus(ArgumentsParser parser) {

  }

  public static void onVersion(ArgumentsParser parser) {

  }
}
