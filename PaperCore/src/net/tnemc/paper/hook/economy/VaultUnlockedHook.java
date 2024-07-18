package net.tnemc.paper.hook.economy;

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

import net.milkbowl.vault2.economy.Economy;
import net.tnemc.bukkit.BukkitCore;
import net.tnemc.core.hook.Hook;
import net.tnemc.paper.PaperCore;
import net.tnemc.plugincore.PluginCore;
import org.bukkit.plugin.ServicePriority;

/**
 * VaultUnlockedHook
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class VaultUnlockedHook implements Hook {

  /**
   * @return True if this hook is enabled, otherwise false.
   */
  @Override
  public boolean enabled() {
    return false;
  }

  /**
   * Used to register this service.
   */
  @Override
  public void register() {
    PaperCore.instance().getPlugin().getServer().getServicesManager().register(Economy.class, new TNEVaultUnlocked(),
            PaperCore.instance().getPlugin(), ServicePriority.Highest);

    PluginCore.log().inform("Hooked into VaultUnlocked");
  }
}