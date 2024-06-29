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
import net.tnemc.plugincore.core.compatibility.CmdSource;
import net.tnemc.plugincore.core.io.message.MessageData;
import revxrsal.commands.help.CommandHelp;

import java.util.Optional;

/**
 * BaseCommand is a class that contains various utility methods for our command classes.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BaseCommand {

  public static void help(CmdSource<?> source, CommandHelp<String> helpEntries, final int page) {

    for(final String entry : helpEntries.paginate(page, 5)) {
      source.message(new MessageData(entry));
    }
  }

  public static Optional<Account> account(CmdSource<?> sender) {
    if(sender.identifier().isEmpty()) {
      return Optional.empty();
    }
    return TNECore.eco().account().findAccount(sender.identifier().get());
  }

  /**
   * Used to get the world for this command source.
   * @return The name of the world that this command source is in.
   */
  public static String region(CmdSource<?> sender) {
    if(sender.player().isPresent()) {
      return TNECore.eco().region().getMode().region(sender.player().get());
    }
    return TNECore.eco().region().defaultRegion();
  }
}