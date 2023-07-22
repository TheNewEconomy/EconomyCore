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
import net.tnemc.menu.sponge8.listeners.Sponge8InventoryClickListener;
import net.tnemc.sponge.command.AdminCommand;
import net.tnemc.sponge.command.ModuleCommand;
import net.tnemc.sponge.command.MoneyCommand;
import net.tnemc.sponge.command.TransactionCommand;
import net.tnemc.sponge.impl.SpongeLogProvider;
import net.tnemc.sponge.impl.SpongeServerProvider;
import net.tnemc.sponge.listeners.player.PlayerCloseInventoryListener;
import net.tnemc.sponge.listeners.player.PlayerJoinListener;
import net.tnemc.sponge.listeners.player.PlayerLeaveListener;
import org.slf4j.Logger;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import revxrsal.commands.sponge.SpongeCommandHandler;

/**
 * The Sponge main plugin class.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */

@Plugin("tne")
public class SpongeCore extends TNECore {

  protected final PluginContainer container;

  @Inject
  SpongeCore(final PluginContainer container, final Logger log) {
    super(new SpongeServerProvider(), new SpongeLogProvider(log));
    this.container = container;
    this.logger = new SpongeLogProvider(log);
  }

  @Listener
  public void onConstructPlugin(final StartingEngineEvent<Server> event) {
    setInstance(this);

    //Register our event listeners
    Sponge.eventManager().registerListeners(container, new PlayerJoinListener(container));
    Sponge.eventManager().registerListeners(container, new PlayerLeaveListener(container));
    Sponge.eventManager().registerListeners(container, new Sponge8InventoryClickListener(container));
    Sponge.eventManager().registerListeners(container, new PlayerCloseInventoryListener(container));
  }

  @Listener
  public void onServerStart(final StartedEngineEvent<Server> event) {
    logger.inform("The New Economy has been enabled.");
  }

  public PluginContainer getContainer() {
    return container;
  }

  @Override
  public void registerCommandHandler() {
    command = SpongeCommandHandler.create(container);
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
    //nothing to see here.
  }

  public static SpongeCore instance() {
    return (SpongeCore)TNECore.instance();
  }

  public static ResourceKey key(final String key) {
    final String[] split = key.split("\\:");

    final String namespace = (split.length >= 2)? split[0] : "minecraft";
    final String value = (split.length >= 2)? split[1] : split[0];
    return ResourceKey.of(namespace, value);
  }
}
