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

import net.kyori.adventure.text.Component;
import net.tnemc.core.handlers.player.PlayerJoinHandler;
import net.tnemc.plugincore.core.utils.HandlerResponse;
import net.tnemc.sponge.impl.SpongePlayerProvider;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.plugin.PluginContainer;

/**
 * PlayerJoinHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerJoinListener {

  private final PluginContainer plugin;

  public PlayerJoinListener(PluginContainer plugin) {
    this.plugin = plugin;
  }

  @Listener
  public void listen(ServerSideConnectionEvent.Join event) {
    final HandlerResponse handle = new PlayerJoinHandler()
                                   .handle(new SpongePlayerProvider(event.player().user(), plugin));


    if(handle.isCancelled()) {
      event.player().kick(Component.text(handle.getResponse()));
    }
  }
}