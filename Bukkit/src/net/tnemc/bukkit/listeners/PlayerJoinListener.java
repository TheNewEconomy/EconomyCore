package net.tnemc.bukkit.listeners;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.bukkit.TNE;
import net.tnemc.bukkit.impl.BukkitPlayerProvider;
import net.tnemc.core.handlers.PlayerJoinHandler;
import net.tnemc.core.utils.HandlerResponse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * PlayerJoinListener
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PlayerJoinListener implements Listener {

  TNE plugin;

  public PlayerJoinListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onJoin(final PlayerJoinEvent event) {
    final HandlerResponse handle = new PlayerJoinHandler()
        .handle(new BukkitPlayerProvider(event.getPlayer()));

    if(handle.isCancelled()) {
      event.getPlayer().kickPlayer(handle.getResponse());
    }
  }
}