package net.tnemc.sponge.impl;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.ProxyProvider;
import net.tnemc.sponge.SpongeCore;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.network.ChannelBinding;

/**
 * SpongeProxyProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeProxyProvider implements ProxyProvider {

  /**
   * Used to register a plugin message channel.
   *
   * @param channel The channel to register.
   */
  @Override
  public void registerChannel(String channel) {
    final ChannelBinding.RawDataChannel spongeChannel = Sponge.getGame().getChannelRegistrar().getOrCreateRaw(SpongeCore.instance(), channel);

    spongeChannel.addListener(Platform.Type.SERVER, (data, connection, side)->{

      TNECore.instance().getChannelMessageManager().handle(channel, data.array());
    });
  }

  /**
   * Used to send a message through a specific plugin message channel.
   *
   * @param channel The channel to send the message through.
   * @param bytes   The byte data to send.
   */
  @Override
  public void send(String channel, byte[] bytes) {

    final ChannelBinding.RawDataChannel spongeChannel = Sponge.getGame().getChannelRegistrar().getOrCreateRaw(SpongeCore.instance(), channel);

    spongeChannel.sendToServer(out ->out.writeByteArray(bytes));
  }
}