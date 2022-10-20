package net.tnemc.bukkit;

/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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

import co.aikar.commands.PaperCommandManager;
import net.tnemc.bukkit.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * TNE
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TNE extends JavaPlugin {

  private static TNE instance;
  private BukkitTNECore core;

  public void onLoad() {
    instance = this;

    //Initialize our TNE Core Class
    this.core = new BukkitTNECore(instance);
  }

  public void onEnable() {

    this.core.command = new PaperCommandManager(this);

    //Register our event listeners
    Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);

  }

  public static TNE instance() {
    return instance;
  }
}