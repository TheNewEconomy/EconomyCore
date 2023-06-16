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

import net.tnemc.core.account.Account;
import net.tnemc.core.compatibility.CmdSource;

import java.util.UUID;

/**
 * TransactionCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TransactionCommand {

  //[page #]
  public static void away(CmdSource<?> sender, int page) {
    //TODO: This
  }

  //[page:#] [world:name/all] [player:name]
  public static void history(CmdSource<?> sender, int page, String region, Account account) {
    //TODO: This
  }

  //<uuid>
  public static void info(CmdSource<?> sender, UUID uuid) {
    //TODO: This
  }

  //<uuid>
  public static void voidT(CmdSource<?> sender, UUID uuid) {
    //TODO: This
  }
}
