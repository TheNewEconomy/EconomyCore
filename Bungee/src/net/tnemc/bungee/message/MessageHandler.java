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

import com.google.common.io.ByteArrayDataOutput;
import net.tnemc.bungee.message.backlog.MessageData;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.util.UUID;

/**
 * MessageHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public abstract class MessageHandler {

  private final String tag;

  public MessageHandler(String tag) {
    this.tag = tag;
  }

  public static void sendToAll(final String channel, ByteArrayDataOutput out) {
    MessageManager.instance().proxy().sendToAll(channel, out.toByteArray());
  }

  public static void sendToAll(final String channel, byte[] out) {
    MessageManager.instance().proxy().sendToAll(channel, out);
  }

  public static void sendTo(final String serverName, final String channel, ByteArrayDataOutput out) {
    MessageManager.instance().proxy().sendTo(serverName, channel, out.toByteArray());
  }

  public static void sendTo(final String serverName, final String channel, byte[] out) {
    MessageManager.instance().proxy().sendTo(serverName, channel, out);
  }

  public static void sendBacklog(@NotNull final MessageData data) {
    MessageManager.instance().proxy().sendBacklog(data);
  }

  public abstract void handle(String player, String accountName, UUID server, DataInputStream stream);
}