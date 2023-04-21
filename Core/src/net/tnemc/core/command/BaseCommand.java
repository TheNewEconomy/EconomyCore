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
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.io.message.MessageData;

/**
 * BaseCommand is a class that contains various utility methods for our command classes.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BaseCommand {

  public static void help(CmdSource source, final String usage, final String node) {
    final String path = "Messages.Commands." + node;

    final MessageData descriptionData = new MessageData(path + ".Description");
    final String description = TNECore.messenger().getTranslator().translate(source.identifier(), descriptionData);

    final MessageData argData = new MessageData(path + ".Arguments");
    final String arguments = TNECore.messenger().getTranslator().translate(source.identifier(), argData);

    String helpBuilder = "Correct Usage: /" + usage + " " +
                         arguments + " - " + description;

    final MessageData helpData = new MessageData(helpBuilder);
    source.message(helpData);
  }
}