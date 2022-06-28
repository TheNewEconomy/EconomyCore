package net.tnemc.bukkit;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.bukkit.impl.BukkitLogProvider;
import net.tnemc.core.TNECore;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * BukkitTNECore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BukkitTNECore extends TNECore {


  public BukkitTNECore(final JavaPlugin plugin) {
    setInstance(this);
    this.logger = new BukkitLogProvider(plugin.getLogger());
  }

  public static BukkitTNECore instance() {
    return (BukkitTNECore)TNECore.instance();
  }
}