package net.tnemc.bukkit;

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

import net.tnemc.plugincore.bukkit.BukkitPluginCore;
import net.tnemc.plugincore.core.PluginEngine;
import net.tnemc.plugincore.core.api.CallbackProvider;
import net.tnemc.plugincore.core.compatibility.ServerConnector;
import net.tnemc.plugincore.core.io.message.TranslationProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ModifiedBukkitPluginCore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ModifiedBukkitPluginCore extends BukkitPluginCore {
  public ModifiedBukkitPluginCore(JavaPlugin plugin, PluginEngine engine, ServerConnector connector, TranslationProvider provider, CallbackProvider callbackProvider) {
    super(plugin, engine, connector, provider, callbackProvider);

    this.logger = new ModifiedBukkitLogger(plugin.getLogger());
  }
}