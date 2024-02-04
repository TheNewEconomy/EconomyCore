package net.tnemc.core.hook.treasury;
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

import me.lokka30.treasury.api.common.service.ServicePriority;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import net.tnemc.core.hook.Hook;

/**
 * TNEHook
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TreasuryHook implements Hook {

  final EconomyProvider provider = new TNETreasury();
  /**
   * @return True if this hook is enabled, otherwise false.
   */
  @Override
  public boolean enabled() {
    return true;
  }

  /**
   * Used to register this service.
   */
  @Override
  public void register() {
    ServiceRegistry.INSTANCE.registerService(
            EconomyProvider.class,
            provider,
            "TheNewEconomy",
            ServicePriority.HIGH
    );
  }
}