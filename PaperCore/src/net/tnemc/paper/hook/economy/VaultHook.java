package net.tnemc.paper.hook.economy;

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


import net.milkbowl.vault.economy.Economy;
import net.tnemc.core.hook.Hook;
import net.tnemc.paper.TNE;
import org.bukkit.plugin.ServicePriority;

/**
 * VaultHook represents a hook into the Vault economy API.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class VaultHook implements Hook {

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
    TNEVault vaultEconomy = new TNEVault();
    TNE.instance().getServer().getServicesManager().register(Economy.class, vaultEconomy,
                                                             TNE.instance(), ServicePriority.Highest);
    TNE.instance().getLogger().info("Hooked into Vault");
  }
}