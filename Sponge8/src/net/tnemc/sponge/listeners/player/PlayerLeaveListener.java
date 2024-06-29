package net.tnemc.sponge.listeners.player;

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

import net.tnemc.core.handlers.player.PlayerLeaveHandler;
import net.tnemc.plugincore.sponge.impl.SpongePlayerProvider;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.plugin.PluginContainer;

/**
 * PlayerLeaveListener
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerLeaveListener {

  private final PluginContainer plugin;

  public PlayerLeaveListener(PluginContainer plugin) {
    this.plugin = plugin;
  }

  @Listener
  public void listen(ServerSideConnectionEvent.Disconnect event) {
    new PlayerLeaveHandler().handle(new SpongePlayerProvider(event.player().user(), plugin));
  }
}