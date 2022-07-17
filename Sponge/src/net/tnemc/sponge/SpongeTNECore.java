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

import com.google.inject.Inject;
import net.tnemc.core.TNECore;
import net.tnemc.sponge.impl.SpongeLogProvider;
import net.tnemc.sponge.listeners.PlayerJoinListener;
import org.slf4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

/**
 * The Sponge main plugin class.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */

@Plugin("tne")
public class SpongeTNECore extends TNECore {

  private final PluginContainer container;

  @Inject
  SpongeTNECore(final PluginContainer container, final Logger log) {
    this.container = container;
    this.logger = new SpongeLogProvider(log);
  }

  @Listener
  public void onConstructPlugin(final ConstructPluginEvent event) {
    setInstance(this);


    //Register our event listeners
    Sponge.eventManager().registerListeners(container, new PlayerJoinListener());
  }

  @Listener
  public void onServerStart(final StartedEngineEvent<Server> event) {
    logger.inform("Successfully running TheNewEconomy.");
  }
}
