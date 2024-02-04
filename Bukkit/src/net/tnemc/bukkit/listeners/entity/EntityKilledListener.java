package net.tnemc.bukkit.listeners.entity;
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

import net.tnemc.core.handlers.entity.EntityDropExpHandler;
import net.tnemc.core.handlers.entity.EntityDropItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * EntityKilledListener
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class EntityKilledListener implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDeath(final EntityDeathEvent event) {

    //We don't care about players
    if(!(event.getEntity() instanceof Player)) {

      event.getDrops().removeIf(stack->new EntityDropItemHandler().handle(stack.getType().getKey().getKey()).isCancelled());

      //Remove exp from entity drop if handler is false.
      if(new EntityDropExpHandler().handle().isCancelled()) {
        event.setDroppedExp(0);
      }
    }
  }
}