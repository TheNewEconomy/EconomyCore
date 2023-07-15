package net.tnemc.paper;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.api.callback.TNECallbacks;
import net.tnemc.paper.command.AdminCommand;
import net.tnemc.paper.command.ModuleCommand;
import net.tnemc.paper.command.MoneyCommand;
import net.tnemc.paper.command.TransactionCommand;
import net.tnemc.paper.depend.towny.TownyHandler;
import net.tnemc.paper.impl.PaperLogProvider;
import net.tnemc.paper.impl.PaperServerProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

/**
 * PaperCore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PaperCore extends TNECore {
  private final JavaPlugin plugin;

  private BukkitConfig bukkitConfig;

  public PaperCore(JavaPlugin plugin) {
    super(new PaperServerProvider(), new PaperLogProvider(plugin.getLogger()));
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
  public void registerCommandHandler() {
    command = BukkitCommandHandler.create(plugin);
  }

  @Override
  public void registerCommands() {

    //Register our commands
    command.register(new AdminCommand());
    command.register(new ModuleCommand());
    command.register(new MoneyCommand());
    command.register(new TransactionCommand());
  }

  @Override
  public void registerCallbacks() {
    this.callbackManager.addConsumer(TNECallbacks.ACCOUNT_TYPES, (callback -> {
      if(Bukkit.getPluginManager().isPluginEnabled("Towny")) {
        log().debug("Adding Towny Account Types");
        TownyHandler.addTypes();
      }

      if(Bukkit.getPluginManager().isPluginEnabled("Factions")) {
        log().debug("Adding Factions Account Types");
      }
      return false;
    }));
  }

  public static PaperCore instance() {
    return (PaperCore)TNECore.instance();
  }
}