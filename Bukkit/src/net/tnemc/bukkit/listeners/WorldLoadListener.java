package net.tnemc.bukkit.listeners;

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

import net.tnemc.bukkit.TNE;
import net.tnemc.core.handlers.RegionLoadHandler;
import net.tnemc.core.region.RegionType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * WorldLoadListener
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class WorldLoadListener implements Listener {

  private final TNE plugin;

  public WorldLoadListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onLoad(final WorldLoadEvent event) {
    new RegionLoadHandler().handle(event.getWorld().getName(), RegionType.WORLD);
  }
}