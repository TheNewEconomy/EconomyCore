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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.channel.ChannelBytesWrapper;
import net.tnemc.plugincore.core.channel.ChannelMessageHandler;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;

import java.util.Optional;
import java.util.UUID;

/**
 * MessageHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MessageHandler extends ChannelMessageHandler {

  public MessageHandler() {

    super("message");
  }

  public static void send(final UUID identifier, final Component component) {

    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(PluginCore.instance().getServerID().toString());
    out.writeUTF(identifier.toString());
    out.writeUTF(MiniMessage.miniMessage().serialize(component));
    PluginCore.log().debug("Sending message: " + MiniMessage.miniMessage().serialize(component));

    TNECore.instance().storage().sendProxyMessage("tne:message", out.toByteArray());
  }

  @Override
  public void handle(ChannelBytesWrapper wrapper) {

    try {

      final String uuid = wrapper.readUTF();
      final String message = wrapper.readUTF();
      PluginCore.log().debug("Received message: " + message);

      if(uuid != null && message != null) {

        final UUID identifier = UUID.fromString(uuid);

        final Optional<PlayerAccount> player = TNECore.eco().account().findPlayerAccount(identifier);
        if(player.isPresent() && player.get().getPlayer().isPresent()) {
          player.get().getPlayer().get().message(message);
        }
      }

    } catch(Exception e) {
      PluginCore.log().error("Issue with message plugin message", e, DebugLevel.STANDARD);
    }
  }
}