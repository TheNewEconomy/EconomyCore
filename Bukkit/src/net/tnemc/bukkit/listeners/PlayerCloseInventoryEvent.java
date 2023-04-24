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

import net.tnemc.bukkit.impl.BukkitPlayerProvider;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.HoldingsType;
import net.tnemc.core.currency.Currency;
import net.tnemc.menu.bukkit.BukkitPlayer;
import net.tnemc.menu.core.MenuManager;
import net.tnemc.menu.core.utils.CloseType;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * PlayerCloseInventoryEvent
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerCloseInventoryEvent implements Listener {

  private final JavaPlugin plugin;

  public PlayerCloseInventoryEvent(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onClose(final InventoryCloseEvent event) {

    if(event.getInventory().getType().equals(InventoryType.ENDER_CHEST)) {

      final Optional<Account> account = TNECore.eco().account().findAccount(event.getPlayer().getUniqueId());
      if(account.isPresent()) {

        final BukkitPlayerProvider provider = new BukkitPlayerProvider((OfflinePlayer)event.getPlayer());

        final String region = TNECore.eco().region().getMode().region(provider);

        for(Currency currency : TNECore.eco().currency().getCurrencies(region)) {

          if(currency.type().supportsItems()) {

            for(HoldingsEntry entry : account.get().getHoldings(region, currency.getUid(), HoldingsType.E_CHEST)) {

              account.get().getWallet().setHoldings(entry);
            }
          }
        }
      }
    }
  }
}
