package net.tnemc.core.channel;

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
import net.tnemc.core.TNECore;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.channel.ChannelBytesWrapper;
import net.tnemc.plugincore.core.channel.ChannelMessageHandler;

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

  public static void send(final String identifier, final String name, final String serverAddress, final int port) {

    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(PluginCore.instance().getServerID().toString());
    out.writeUTF(identifier);
    out.writeUTF(name);
    out.writeUTF(serverAddress);
    out.writeInt(port);

    TNECore.instance().storage().sendProxyMessage("tne:sync", out.toByteArray());
  }

  @Override
  public void handle(final ChannelBytesWrapper wrapper) {

    //This will never come into the server by design.
  }
}