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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * BalanceMessageHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BalanceMessageHandler extends AccountHandler {
  public BalanceMessageHandler() {
    super("balance");
  }

  @Override
  public void handle(String account, String accountName, UUID server, DataInputStream stream) {

    try {
      final String region = stream.readUTF();
      final String currency = stream.readUTF();
      final String handler = stream.readUTF();
      final String amount = stream.readUTF();
      send(server, account, accountName, region, currency, handler, amount);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void send(UUID server, String account, String accountName, String region, String currency, String handler, String amount) {
    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(server.toString());
    out.writeUTF(account);
    out.writeUTF(accountName);
    out.writeUTF(region);
    out.writeUTF(currency);
    out.writeUTF(handler);
    out.writeUTF(amount);
    sendToAll("tne:balance", out, true);
  }
}