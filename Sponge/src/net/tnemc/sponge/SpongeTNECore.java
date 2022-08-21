package net.tnemc.sponge;

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

import co.aikar.commands.SpongeCommandManager;
import com.google.inject.Inject;
import net.tnemc.core.TNECore;
import net.tnemc.core.io.storage.StorageManager;
import net.tnemc.sponge.impl.SpongeLogProvider;
import net.tnemc.sponge.impl.SpongeServerProvider;
import net.tnemc.sponge.listeners.PlayerJoinListener;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

/**
 * The Sponge main plugin class.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */

@Plugin(id="tne", name="The New Economy", version="0.1.2.0",
    description="The original feature-packed economy plugin for Minecraft.",
    authors = {"creatorfromhell"})
public class SpongeTNECore extends TNECore<CommandSource> {

  private final PluginContainer container;

  @Inject
  SpongeTNECore(final PluginContainer container, final Logger log) {
    super(new SpongeServerProvider(), new SpongeLogProvider(log), new StorageManager(),
          new SpongeCommandManager(container));
    this.container = container;
    this.logger = new SpongeLogProvider(log);
  }

  @Listener
  public void onConstructPlugin(final GamePreInitializationEvent event) {
    setInstance(this);


    //Register our event listeners
    Sponge.getEventManager().registerListeners(container, new PlayerJoinListener());
  }

  @Listener
  public void onServerStart(final GameStartedServerEvent event) {
    logger.inform("Successfully running TheNewEconomy.");
  }
}
