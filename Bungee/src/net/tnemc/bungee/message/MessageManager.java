package net.tnemc.bungee.message;

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

import net.tnemc.bungee.ProxyProvider;
import net.tnemc.bungee.message.handlers.BalanceMessageHandler;
import net.tnemc.bungee.message.handlers.SyncAllMessageHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all Plugin Channel messages.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MessageManager {

  private final Map<String, MessageHandler> handlers = new HashMap<>();
  private final ProxyProvider proxy;
  private static MessageManager instance;

  public MessageManager(final ProxyProvider proxy) {
    instance = this;
    this.proxy = proxy;
    handlers.put("balance", new BalanceMessageHandler());
    handlers.put("sync", new SyncAllMessageHandler());
  }

  public void onMessage(final String channel, final byte[] data) {

    final String tag = channel.replace("tne:", "");

    if(handlers.containsKey(tag)) {
      try {
        final ByteArrayInputStream stream = new ByteArrayInputStream(data);
        final DataInputStream in = new DataInputStream(stream);

        final UUID server = UUID.fromString(in.readUTF());
        final String account = in.readUTF();
        handlers.get(tag).handle(account, server, in);

        in.close();
        stream.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  public ProxyProvider proxy() {
    return proxy;
  }

  public static MessageManager instance() {
    return instance;
  }
}