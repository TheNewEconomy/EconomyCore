package net.tnemc.core.channel.handlers;
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
import com.google.common.io.ByteStreams;
import net.tnemc.core.TNECore;
import net.tnemc.core.channel.ChannelBytesWrapper;
import net.tnemc.core.channel.ChannelMessageHandler;

import java.util.UUID;

/**
 * SyncHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SyncHandler extends ChannelMessageHandler {

  public SyncHandler() {
    super("sync");
  }

  public static void send(String account) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(TNECore.instance().getServerID().toString());
    out.writeUTF(account);

    TNECore.server().proxy().send("tne:sync", out.toByteArray());
  }

  @Override
  public void handle(ChannelBytesWrapper wrapper) {

    //This will never come into the server by design.
  }
}