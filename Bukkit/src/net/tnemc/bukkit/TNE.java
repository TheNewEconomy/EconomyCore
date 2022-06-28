package net.tnemc.bukkit;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.bukkit.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * TNE
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TNE extends JavaPlugin {

  private static TNE instance;
  private BukkitTNECore core;

  public void onLoad() {
    instance = this;

    //Initialize our TNE Core Class
    this.core = new BukkitTNECore(instance);
  }

  public void onEnable() {

    //Register our event listeners
    Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
  }

  public static TNE instance() {
    return instance();
  }
}