package net.tnemc.sponge;

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

import com.google.inject.Inject;
import net.tnemc.core.TNECore;
import net.tnemc.menu.sponge7.listeners.Sponge7InventoryClickListener;
import net.tnemc.sponge.command.AdminCommand;
import net.tnemc.sponge.command.ModuleCommand;
import net.tnemc.sponge.command.MoneyCommand;
import net.tnemc.sponge.command.TransactionCommand;
import net.tnemc.sponge.impl.SpongeLogProvider;
import net.tnemc.sponge.impl.SpongeServerProvider;
import net.tnemc.sponge.listeners.PlayerCloseInventoryListener;
import net.tnemc.sponge.listeners.PlayerJoinListener;
import net.tnemc.sponge.listeners.PlayerLeaveListener;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import revxrsal.commands.sponge.SpongeCommandHandler;

/**
 * The Sponge main plugin class.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */

@Plugin(id="tne", name="The New Economy", version="0.1.2.0",
    description="The original feature-packed economy plugin for Minecraft.",
    authors = {"creatorfromhell"})
public class SpongeCore extends TNECore {

  private final PluginContainer container;
  private Plugin plugin;

  @Inject
  SpongeCore(final PluginContainer container, final Logger log) {
    super(new SpongeServerProvider(), new SpongeLogProvider(log));
    this.container = container;
    this.logger = new SpongeLogProvider(log);
  }

  @Listener
  public void onConstructPlugin(final GamePreInitializationEvent event, @Root Plugin plugin) {
    setInstance(this);

    this.plugin = plugin;

    //Register our event listeners
    Sponge.getEventManager().registerListeners(container, new PlayerJoinListener());
    Sponge.getEventManager().registerListeners(container, new PlayerLeaveListener());
    Sponge.getEventManager().registerListeners(container, new Sponge7InventoryClickListener(plugin));
    Sponge.getEventManager().registerListeners(container, new PlayerCloseInventoryListener(plugin));
  }

  @Listener
  public void onServerStart(final GameStartedServerEvent event) {
    logger.inform("The New Economy has been enabled.");
  }

  public PluginContainer getContainer() {
    return container;
  }

  public Plugin getPlugin() {
    return plugin;
  }

  @Override
  public void registerCommandHandler() {
    command = SpongeCommandHandler.create(plugin);
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

  }

  public static SpongeCore instance() {
    return (SpongeCore)TNECore.instance();
  }
}
