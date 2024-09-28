package net.tnemc.velocity;

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

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.tnemc.bungee.message.MessageManager;
import net.tnemc.bungee.message.backlog.MessageData;
import net.tnemc.velocity.event.ServerPostConnectListener;
import net.tnemc.velocity.message.MessageListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * VelocityCore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Plugin(id = "tne_velocity", name = "The New Economy Velocity", version = "0.1.3.2",
        url = "https://tnemc.net", description = "A bridge for TheNewEconomy plugin.", authors = { "creatorfromhell" })
public class VelocityCore {

  private final Map<UUID, MessageData> backlog = new HashMap<>();

  private final ProxyServer server;
  private final Logger logger;
  private MessageManager manager;


  private static VelocityCore instance;

  @Inject
  public VelocityCore(final ProxyServer server, final Logger logger) {

    this.server = server;
    this.logger = logger;
    instance = this;

    logger.info("The New Economy Velocity bridge has been started!");
  }

  @Subscribe
  public void onInitialize(final ProxyInitializeEvent event) {

    this.manager = new MessageManager(new VelocityProxy());

    server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("tne:balance"));
    server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("tne:message"));
    server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("tne:sync"));
    server.getEventManager().register(this, new MessageListener());
    server.getEventManager().register(this, new ServerPostConnectListener());
  }

  public static VelocityCore instance() {

    return instance;
  }

  public ProxyServer getServer() {

    return server;
  }

  public Map<UUID, MessageData> getBacklog() {

    return backlog;
  }

  public void remove(final UUID server) {

    backlog.remove(server);
  }
}