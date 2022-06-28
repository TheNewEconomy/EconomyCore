package net.tnemc.sponge;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.TNECore;
import net.tnemc.sponge.listeners.PlayerJoinListener;
import net.tnemc.sponge.impl.SpongeLogProvider;
import org.slf4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;

import com.google.inject.Inject;
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
