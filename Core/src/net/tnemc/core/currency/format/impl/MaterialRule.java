package net.tnemc.core.currency.format.impl;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.format.FormatRule;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.PlayerProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MaterialRule implements FormatRule {

  @Override
  public String name() {

    return "material";
  }

  /**
   * Determines whether this format rule should be included in the menu. By default, it is set to
   * true.
   *
   * @return true if this format rule should be included in the menu, false otherwise.
   */
  @Override
  public boolean includeInMenu() {

    return false;
  }

  @Override
  public String format(@Nullable final Account account, final HoldingsEntry entry, final String format) {

    String formatted = format;


    final Optional<Currency> currency = entry.currency();
    if(account != null && account.isPlayer() && currency.isPresent() && currency.get() instanceof ItemCurrency) {
      final Optional<PlayerProvider> provider = PluginCore.server().findPlayer(((PlayerAccount)account).getUUID());
      if(provider.isPresent()) {
        for(final Denomination denomination : currency.get().getDenominations().values()) {

          final ItemDenomination denom = (ItemDenomination)denomination;
          if(formatted.contains(denom.material())) {

            final AbstractItemStack<?> stack = TNECore.instance().denominationToStack(denom);
            final int count = PluginCore.server().calculations().count((AbstractItemStack<Object>)stack, provider.get().inventory().getInventory(false));
            formatted = formatted.replace("<" + denom.material() + ">", String.valueOf(count));
          }
        }
      }
    }
    return formatted;
  }
}