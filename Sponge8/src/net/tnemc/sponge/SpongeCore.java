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
import net.tnemc.core.command.parameters.resolver.AccountResolver;
import net.tnemc.core.command.parameters.resolver.BigDecimalResolver;
import net.tnemc.core.command.parameters.resolver.CurrencyResolver;
import net.tnemc.core.command.parameters.resolver.DebugResolver;
import net.tnemc.core.command.parameters.resolver.StatusResolver;
import net.tnemc.core.command.parameters.suggestion.AccountSuggestion;
import net.tnemc.core.command.parameters.suggestion.CurrencySuggestion;
import net.tnemc.core.command.parameters.suggestion.DebugSuggestion;
import net.tnemc.core.command.parameters.suggestion.RegionSuggestion;
import net.tnemc.core.command.parameters.suggestion.StatusSuggestion;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.region.RegionGroup;
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
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import revxrsal.commands.sponge.SpongeCommandHandler;
import revxrsal.commands.sponge.core.SpongeHandler;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;

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
  @Inject
  @ConfigDir(sharedRoot = false)
  private Path configDir;

  @Inject
  SpongeCore(final PluginContainer container, final Logger log) {
    super(new SpongeServerProvider(), new SpongeLogProvider(log));
    this.container = container;
    this.logger = new SpongeLogProvider(log);
    this.core = this;
    command = SpongeCommandHandler.create(container);

    command.registerValueResolver(Account.class, new AccountResolver());
    command.registerValueResolver(AccountStatus.class, new StatusResolver());
    command.registerValueResolver(DebugLevel.class, new DebugResolver());
    command.registerValueResolver(Currency.class, new CurrencyResolver());
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
    command.register(new TransactionCommand());
  }

  @Listener
  public void onConstructPlugin(final StartingEngineEvent<Server> event) {
    setInstance(this);
    logger.inform("Starting up The New Economy.");

    //Register our event listeners
    Sponge.eventManager().registerListeners(container, new PlayerJoinListener(container));
    Sponge.eventManager().registerListeners(container, new PlayerLeaveListener(container));
    Sponge.eventManager().registerListeners(container, new Sponge8InventoryClickListener(container));
    Sponge.eventManager().registerListeners(container, new PlayerCloseInventoryListener(container));
  }

  @Listener
  public void onServerStart(final StartedEngineEvent<Server> event) {
    this.core.enable();
    logger.inform("The New Economy has been enabled.");
  }

  public PluginContainer getContainer() {
    return container;
  }

  @Override
  protected void onEnable() {
    this.directory = configDir.toFile();

    super.onEnable();
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
