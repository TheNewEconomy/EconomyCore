package net.tnemc.sponge;

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

import com.google.inject.Inject;
import net.tnemc.core.TNECore;
import net.tnemc.core.api.callback.TNECallbackProvider;
import net.tnemc.core.io.message.BaseTranslationProvider;
import net.tnemc.menu.sponge8.Sponge8MenuHandler;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.api.CallbackManager;
import net.tnemc.plugincore.sponge.SpongePluginCore;
import net.tnemc.sponge.command.AdminCommand;
import net.tnemc.sponge.command.ModuleCommand;
import net.tnemc.sponge.command.MoneyCommand;
import net.tnemc.sponge.command.ShortCommands;
import net.tnemc.sponge.command.TransactionCommand;
import net.tnemc.sponge.impl.SpongeEconomy;
import net.tnemc.sponge.impl.SpongeItemCalculations;
import net.tnemc.sponge.listeners.player.PlayerCloseInventoryListener;
import net.tnemc.sponge.listeners.player.PlayerJoinListener;
import net.tnemc.sponge.listeners.player.PlayerLeaveListener;
import org.apache.logging.log4j.Logger;
import org.bstats.sponge.Metrics;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.ProvideServiceEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.util.metric.MetricsConfigManager;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.sponge.SpongeLamp;

import java.nio.file.Path;

/**
 * The Sponge main plugin class.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */

@Plugin("tne")
public class SpongeCore extends TNECore {

  protected final SpongePluginCore pluginCore;
  protected final PluginContainer container;
  private final Metrics metrics;
  @Inject
  @ConfigDir(sharedRoot = false)
  private Path configDir;

  @Inject
  private MetricsConfigManager metricsConfigManager;

  @Inject
  SpongeCore(final PluginContainer container, final Logger log, final Metrics.Factory metricsFactory) {

    this.pluginCore = new SpongePluginCore(container, this, log, new BaseTranslationProvider(), new TNECallbackProvider());
    this.container = container;

    command = SpongeLamp.builder(container).accept(registerParameters()).build();

    metrics = metricsFactory.make(19246);
  }

  public static ResourceKey key(final String key) {

    final String[] split = key.split(":");

    final String namespace = (split.length >= 2)? split[0] : "minecraft";
    final String value = (split.length >= 2)? split[1] : split[0];
    return ResourceKey.of(namespace, value);
  }

  @Override
  public void registerMenuHandler() {

    this.menuHandler = new Sponge8MenuHandler(container, true);
  }

  @Listener
  public void onConstruct(final ConstructPluginEvent event) {

    if(hasConsent()) {
      metrics.startup(event);
    }
  }

  @Listener
  public void onEngineStart(final StartingEngineEvent<Server> event) {

    PluginCore.log().inform("Starting up The New Economy.");

    this.pluginCore.load();

    //Register our event listeners
    Sponge.eventManager().registerListeners(container, new PlayerJoinListener(container, event.engine().boundAddress().get()));
    Sponge.eventManager().registerListeners(container, new PlayerLeaveListener(container));
    Sponge.eventManager().registerListeners(container, new PlayerCloseInventoryListener(container));
  }

  @Listener
  public void onServerStart(final StartedEngineEvent<Server> event) {
    //this.directory = configDir.toFile();
    pluginCore.enable();
    PluginCore.log().inform("The New Economy has been enabled.");
  }

  @Listener
  public void onServerStop(final StoppingEngineEvent<Server> event) {

    pluginCore.onDisable();
    PluginCore.log().inform("The New Economy has been disabled.");
  }

  @Listener
  public void provideEconomy(final ProvideServiceEvent.EngineScoped<EconomyService> event) {

    event.suggest(SpongeEconomy::new);
  }

  public PluginContainer getContainer() {

    return container;
  }

  @Override
  public void registerCommandHandler() {

    command = SpongeLamp.builder(container).accept(registerParameters()).build();
  }

  @Override
  public String commandHelpWriter(final ExecutableCommand executableCommand, final CommandActor commandActor) {

    return "";
  }

  @Override
  public void registerCommands() {

    super.registerCommands();

    command.register(new AdminCommand());
    command.register(new ModuleCommand());
    command.register(new MoneyCommand());
    command.register(new ShortCommands());
    command.register(new TransactionCommand());
  }

  @Override
  public void registerConfigs() {

  }

  @Override
  public void registerCallbacks(final CallbackManager callbackManager) {

    super.registerCallbacks(callbackManager);
    //nothing to see here.
  }

  @Override
  public SpongeItemCalculations itemCalculations() {

    return new SpongeItemCalculations();
  }

  public boolean hasConsent() {

    return this.metricsConfigManager.collectionState(this.container).asBoolean();
  }
}
