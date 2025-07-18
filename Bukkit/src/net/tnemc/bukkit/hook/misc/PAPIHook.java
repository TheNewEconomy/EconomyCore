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

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.utils.Identifier;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.io.message.MessageHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PAPIHook
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PAPIHook extends PlaceholderExpansion {

  @Override
  public String getAuthor() {

    return "creatorfromhell";
  }

  @Override
  public String getIdentifier() {

    return "TNE";
  }

  @Override
  public String getVersion() {

    return "0.1.4.0";
  }

  @Override
  public boolean persist() {

    return true;
  }

  @Override
  public @Nullable String onRequest(final OfflinePlayer player, @NotNull final String paramString) {

    final String[] params = paramString.split("_");

    final String account = (player == null)? null: player.getUniqueId().toString();

    return TNECore.eco().placeholder().onRequest(account, params);
  }

  @Override
  public @Nullable String onPlaceholderRequest(final Player player, @NotNull final String paramString) {

    final String[] params = paramString.split("_");

    final String account = (player == null)? null: player.getUniqueId().toString();

    return TNECore.eco().placeholder().onRequest(account, params);
  }
}