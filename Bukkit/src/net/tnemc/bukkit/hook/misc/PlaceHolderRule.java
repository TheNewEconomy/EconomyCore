package net.tnemc.bukkit.hook.misc;
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

import me.clip.placeholderapi.PlaceholderAPI;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.currency.format.FormatRule;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

/**
 * PlaceHolderRule
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlaceHolderRule implements FormatRule {

  @Override
  public String name() {

    return "PAPI";
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

    if(account instanceof final PlayerAccount player) {

      return PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(player.getUUID()), format);
    }

    return PlaceholderAPI.setPlaceholders(null, format);
  }
}