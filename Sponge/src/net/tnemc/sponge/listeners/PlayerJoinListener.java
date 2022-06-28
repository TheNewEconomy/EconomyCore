package net.tnemc.sponge.listeners;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.kyori.adventure.text.Component;
import net.tnemc.core.handlers.PlayerJoinHandler;
import net.tnemc.core.utils.HandlerResponse;
import net.tnemc.sponge.impl.SpongePlayerProvider;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

/**
 * PlayerJoinHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerJoinListener {

  @Listener
  public void listen(ServerSideConnectionEvent.Join event) {
    final HandlerResponse handle = new PlayerJoinHandler()
                                   .handle(new SpongePlayerProvider(event.player()));


    if(handle.isCancelled()) {
      event.player().kick(Component.text(handle.getResponse()));
    }
  }
}