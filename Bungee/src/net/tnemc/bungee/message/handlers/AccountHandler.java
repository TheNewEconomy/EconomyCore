package net.tnemc.bungee.message.handlers;

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

import net.tnemc.bungee.message.MessageHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * AccountHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public abstract class AccountHandler extends MessageHandler {

  public AccountHandler(final String tag) {

    super(tag);
  }

  public abstract void handle(String account, String accountName, UUID server, DataInputStream stream);

  @Override
  public void handle(final UUID server, final DataInputStream in) {

    try {
      final String account = in.readUTF();
      final String accountName = in.readUTF();

      handle(account, accountName, server, in);
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}