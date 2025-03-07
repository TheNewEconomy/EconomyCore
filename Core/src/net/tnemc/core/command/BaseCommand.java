package net.tnemc.core.command;

/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.help.Help;

import java.util.Optional;
import java.util.UUID;

/**
 * BaseCommand is a class that contains various utility methods for our command classes.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BaseCommand {

  public static void help(final CmdSource<?> source, final Help.RelatedCommands<?> commands, final int page) {

    for(final ExecutableCommand<?> command : commands.paginate(page, 5)) {

      source.message(new MessageData(command.usage()));
    }
  }

  public static Optional<Account> account(final CmdSource<?> sender, final String type) {

    if(sender.identifier().isEmpty()) {
      return Optional.empty();
    }
    return TNECore.eco().account().findAccount(TNECore.eco().account().swap(type, sender.identifier().get()));
  }

  public static Optional<Account> account(final UUID sender, final String type) {

    if(sender == null) {
      return Optional.empty();
    }
    return TNECore.eco().account().findAccount(TNECore.eco().account().swap(type, sender));
  }

  /**
   * Used to get the world for this command source.
   *
   * @return The name of the world that this command source is in.
   */
  public static String region(final CmdSource<?> sender) {

    if(sender.player().isPresent()) {
      return TNECore.eco().region().getMode().region(sender.player().get());
    }
    return TNECore.eco().region().defaultRegion();
  }
}