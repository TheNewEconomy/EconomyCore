package net.tnemc.paper;
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

import net.tnemc.bukkit.BukkitConfig;
import net.tnemc.bukkit.BukkitItemCalculations;
import net.tnemc.bukkit.depend.faction.FactionHandler;
import net.tnemc.bukkit.depend.towny.TownyHandler;
import net.tnemc.core.TNECore;
import net.tnemc.core.api.callback.TNECallbacks;
import net.tnemc.menu.paper.PaperMenuHandler;
import net.tnemc.paper.command.AdminCommand;
import net.tnemc.paper.command.ModuleCommand;
import net.tnemc.paper.command.MoneyCommand;
import net.tnemc.paper.command.ShortCommands;
import net.tnemc.paper.command.TransactionCommand;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.api.CallbackManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;

/**
 * PaperCore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PaperCore extends TNECore {

  protected final JavaPlugin plugin;

  private BukkitConfig bukkitConfig;

  public PaperCore(final JavaPlugin plugin) {

    this.plugin = plugin;
  }

  public static PaperCore instance() {

    return (PaperCore)TNECore.instance();
  }

  @Override
  public void registerMenuHandler() {

    this.menuHandler = new PaperMenuHandler(plugin, true);
  }

  @Override
  public void registerCommandHandler() {

    command = BukkitLamp.builder(plugin).accept(registerParameters()).build();
  }

  @Override
  public String commandHelpWriter(final ExecutableCommand executableCommand, final CommandActor commandActor) {

    return "";
  }

  @Override
  public void registerConfigs() {

    super.registerConfigs();

    this.bukkitConfig = new BukkitConfig();
    if(!this.bukkitConfig.load()) {
      PluginCore.log().error("Failed to load bukkit configuration!");
    }
  }

  @Override
  public void registerCommands() {

    super.registerCommands();

    //Register our commands
    command.register(new AdminCommand());
    command.register(new ShortCommands());
    command.register(new ModuleCommand());
    command.register(new MoneyCommand());
    command.register(new TransactionCommand());
  }

  @Override
  public void registerCallbacks(final CallbackManager callbackManager) {

    super.registerCallbacks(callbackManager);
    callbackManager.addConsumer(TNECallbacks.ACCOUNT_TYPES.id(), (callback->{

      if(Bukkit.getPluginManager().getPlugin("Towny") != null) {

        PluginCore.log().debug("Adding Towny Account Types");
        TownyHandler.addTypes();
      }

      if(Bukkit.getPluginManager().getPlugin("Factions") != null) {

        PluginCore.log().debug("Adding Factions Account Types");
        FactionHandler.addTypes();
      }
      return false;
    }));
  }

  @Override
  public BukkitItemCalculations itemCalculations() {

    return new BukkitItemCalculations();
  }

  public JavaPlugin getPlugin() {

    return plugin;
  }
}