package net.tnemc.core.channel;
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
import net.tnemc.core.channel.handlers.BalanceHandler;
import net.tnemc.core.channel.handlers.SyncHandler;
import net.tnemc.core.compatibility.log.DebugLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChannelMessageManager
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ChannelMessageManager {

  private final Map<String, ChannelMessageHandler> handlers = new HashMap<>();

  private final List<String> accountsMessage = new ArrayList<>();

  public ChannelMessageManager() {

    register(new BalanceHandler());
    register(new SyncHandler());
    register();
  }

  public void register(final ChannelMessageHandler handler) {
    handlers.put("tne:" + handler.tag, handler);
  }

  public void register() {
    handlers.keySet().forEach(channel->{
      TNECore.server().proxy().registerChannel(channel);
    });
  }

  public void handle(String channel, byte[] bytes) {
    TNECore.log().debug("TNE Channel Message Received.", DebugLevel.STANDARD);
    TNECore.log().debug("Channel: " + channel, DebugLevel.STANDARD);

    if(handlers.containsKey(channel)) {
      handlers.get(channel).handle(bytes);
    }
  }

  public boolean isAffected(final String account) {
    return accountsMessage.contains(account);
  }

  public void removeAccount(final String account) {
    accountsMessage.remove(account);
  }

  public void addAccount(final String account) {
    accountsMessage.add(account);
  }
}