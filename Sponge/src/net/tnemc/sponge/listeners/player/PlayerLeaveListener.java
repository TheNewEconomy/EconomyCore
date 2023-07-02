package net.tnemc.sponge.listeners.player;
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

import net.tnemc.core.handlers.player.PlayerLeaveHandler;
import net.tnemc.sponge.impl.SpongePlayerProvider;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

/**
 * PlayerLeaveListener
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerLeaveListener {

  @Listener
  public void listen(ClientConnectionEvent.Disconnect event, @Root Plugin plugin) {
    new PlayerLeaveHandler().handle(new SpongePlayerProvider(event.getTargetEntity(), plugin));
  }
}