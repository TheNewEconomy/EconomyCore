package net.tnemc.bukkit.listeners.mcmmo;

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

import com.gmail.nossr50.events.skills.fishing.McMMOPlayerFishingTreasureEvent;
import net.tnemc.bukkit.BukkitConfig;
import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Optional;

public class PlayerFishingTreasureListener implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onFishReward(final McMMOPlayerFishingTreasureEvent event) {

    if(BukkitConfig.yaml().getBoolean("Bukkit.McMMORewards")) {
      final Optional<Currency> currency = TNECore.eco().currency().findCurrencyByMaterial(event.getTreasure().getType().name());
      if(currency.isPresent()) event.setCancelled(true);
    }
  }
}
