package net.tnemc.core.handlers.inventory;
/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import net.tnemc.plugincore.core.utils.HandlerResponse;

import java.util.Optional;

/**
 * PlayerCraftHandler
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class ItemCraftHandler {

  public HandlerResponse handle(final PlayerProvider provider, final AbstractItemStack<?> item) {

    final Optional<Currency> currencyOptional = TNECore.eco().currency().findCurrencyByMaterial(item.material());
    if(currencyOptional.isEmpty()) {

      return new HandlerResponse("no currency found", false);
    }

    // && provider.hasPermission("tne.money.craft." + currencyOptional.get().getIdentifier())
    if(provider == null) {

      return new HandlerResponse("provider null or has permission override", false);
    }

    if(currencyOptional.get() instanceof final ItemCurrency itemCurrency) {

      return new HandlerResponse("found, returning value", itemCurrency.blockCraft());
    }

    return new HandlerResponse("currency isn't item currency", false);
  }
}