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
import net.tnemc.core.account.Account;
import net.tnemc.core.account.AccountStatus;
import net.tnemc.core.command.parameters.PercentBigDecimal;
import net.tnemc.core.command.parameters.resolver.AccountResolver;
import net.tnemc.core.command.parameters.resolver.BigDecimalResolver;
import net.tnemc.core.command.parameters.resolver.CurrencyResolver;
import net.tnemc.core.command.parameters.resolver.DebugResolver;
import net.tnemc.core.command.parameters.resolver.PercentDecimalResolver;
import net.tnemc.core.command.parameters.resolver.StatusResolver;
import net.tnemc.core.command.parameters.suggestion.AccountSuggestion;
import net.tnemc.core.command.parameters.suggestion.CurrencySuggestion;
import net.tnemc.core.command.parameters.suggestion.DebugSuggestion;
import net.tnemc.core.command.parameters.suggestion.RegionSuggestion;
import net.tnemc.core.command.parameters.suggestion.StatusSuggestion;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.region.RegionGroup;
import net.tnemc.sponge.command.AdminCommand;
import net.tnemc.sponge.command.ModuleCommand;
import net.tnemc.sponge.command.MoneyCommand;
import net.tnemc.sponge.command.ShortCommands;
import net.tnemc.sponge.command.TransactionCommand;
import net.tnemc.sponge.hook.misc.LuckPermsHook;
import net.tnemc.sponge.impl.SpongeLogProvider;
import net.tnemc.sponge.impl.SpongeServerProvider;
import net.tnemc.sponge.impl.eco.SpongeEconomy;
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
import revxrsal.commands.sponge.SpongeCommandHandler;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Optional;

/**
 * The Sponge main plugin class.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */

@Plugin("tne")
public class SpongeCore extends TNECore {

  protected final PluginContainer container;

  private SpongeCore core;
  private final Metrics metrics;
  @Inject
  @ConfigDir(sharedRoot = false)
  private Path configDir;

  @Inject
  private MetricsConfigManager metricsConfigManager;

  @Inject
  SpongeCore(final PluginContainer container, final Logger log, final Metrics.Factory metricsFactory) {
    super(new SpongeServerProvider(), new SpongeLogProvider(log));
    this.container = container;
    this.logger = new SpongeLogProvider(log);
    this.core = this;
    command = SpongeCommandHandler.create(container);

    command.registerValueResolver(Account.class, new AccountResolver());
    command.registerValueResolver(AccountStatus.class, new StatusResolver());
    command.registerValueResolver(DebugLevel.class, new DebugResolver());
    command.registerValueResolver(Currency.class, new CurrencyResolver());
    command.registerValueResolver(PercentBigDecimal.class, new PercentDecimalResolver());
    command.registerValueResolver(BigDecimal.class, new BigDecimalResolver());

    //Annotation
    command.getAutoCompleter().registerParameterSuggestions(AccountStatus.class, new StatusSuggestion());
    command.getAutoCompleter().registerParameterSuggestions(DebugLevel.class, new DebugSuggestion());
    command.getAutoCompleter().registerParameterSuggestions(RegionGroup.class, new RegionSuggestion());
    command.getAutoCompleter().registerParameterSuggestions(Account.class, new AccountSuggestion());
    command.getAutoCompleter().registerParameterSuggestions(Currency.class, new CurrencySuggestion());

    command.register(new AdminCommand());
    command.register(new ModuleCommand());
    command.register(new MoneyCommand());
    command.register(new ShortCommands());
    command.register(new TransactionCommand());

    final Optional<PluginContainer> luckContainer = Sponge.pluginManager().plugin("luckperms");
    if(luckContainer.isPresent()) {
      LuckPermsHook.register();
    }

    metrics = metricsFactory.make(19246);
  }

  @Override
  public void registerMenuHandler() {
    //TODO: Menu Handler for Sponge
  }


  @Listener
  public void onConstruct(final ConstructPluginEvent event) {
    if(hasConsent()) {
      metrics.startup(event);
    }
  }

  @Listener
  public void onEngineStart(final StartingEngineEvent<Server> event) {
    setInstance(this);
    logger.inform("Starting up The New Economy.");

    //Register our event listeners
    Sponge.eventManager().registerListeners(container, new PlayerJoinListener(container));
    Sponge.eventManager().registerListeners(container, new PlayerLeaveListener(container));
    Sponge.eventManager().registerListeners(container, new PlayerCloseInventoryListener(container));
  }

  @Listener
  public void onServerStart(final StartedEngineEvent<Server> event) {
    this.directory = configDir.toFile();
    this.core.enable();
    logger.inform("The New Economy has been enabled.");
  }

  @Listener
  public void onServerStop(final StoppingEngineEvent<Server> event) {
    super.onDisable();
    logger.inform("The New Economy has been disabled.");
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
    //command = SpongeCommandHandler.create(container);
  }

  @Override
  public void registerCommands() {

    //Register our commands
  }
  /*@Listener
  public void handleRegistrationEvent(RegisterCommandEvent<Command> event) {
    System.out.println("Register TNE Commands.");

    if(command == null) return;

    ((SpongeHandler)command).getRegistered().forEach((name, command)-> {
      event.register(container, command, name);
      System.out.println("Register: " + name);
    });
  }*/

  @Override
  public void registerCallbacks() {
    //nothing to see here.
  }

  public boolean hasConsent() {
    return this.metricsConfigManager.collectionState(this.container).asBoolean();
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
