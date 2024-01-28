package net.tnemc.bukkit;

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

import net.tnemc.bukkit.command.AdminCommand;
import net.tnemc.bukkit.command.ModuleCommand;
import net.tnemc.bukkit.command.MoneyCommand;
import net.tnemc.bukkit.command.ShortCommands;
import net.tnemc.bukkit.command.TransactionCommand;
import net.tnemc.bukkit.depend.faction.FactionHandler;
import net.tnemc.bukkit.depend.towny.TownyHandler;
import net.tnemc.bukkit.impl.BukkitLogProvider;
import net.tnemc.core.TNECore;
import net.tnemc.core.api.callback.TNECallbacks;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.menu.bukkit.BukkitMenuHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

/**
 * BukkitCore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BukkitCore extends TNECore {

  private JavaPlugin plugin;

  private BukkitConfig bukkitConfig;

  public BukkitCore(JavaPlugin plugin, ServerConnector server) {
    super(server, new BukkitLogProvider(plugin.getLogger()));
    setInstance(this);
    this.plugin = plugin;
  }

  @Override
  protected void onEnable() {
    this.directory = plugin.getDataFolder();

    super.onEnable();

    this.bukkitConfig = new BukkitConfig();
    if(!this.bukkitConfig.load()) {
      TNECore.log().error("Failed to load bukkit configuration!");
    }
  }

  @Override
  public void registerMenuHandler() {
    this.menuHandler = new BukkitMenuHandler(plugin, true);
  }

  @Override
  public void registerCommandHandler() {
    command = BukkitCommandHandler.create(plugin);
  }

  @Override
  public void registerCommands() {

    //Register our commands
    command.register(new AdminCommand());
    command.register(new ShortCommands());
    command.register(new ModuleCommand());
    command.register(new MoneyCommand());
    command.register(new TransactionCommand());
  }

  @Override
  public void registerCallbacks() {
    this.callbackManager.addConsumer(TNECallbacks.ACCOUNT_TYPES, (callback->{

      if(Bukkit.getPluginManager().getPlugin("Towny") != null) {
        log().debug("Adding Towny Account Types");
        TownyHandler.addTypes();
      }

      if(Bukkit.getPluginManager().getPlugin("Factions") != null) {
        log().debug("Adding Factions Account Types");
        FactionHandler.addTypes();
      }
      return false;
    }));
  }

  public static BukkitCore instance() {
    return (BukkitCore)TNECore.instance();
  }

  public JavaPlugin getPlugin() {
    return plugin;
  }
}