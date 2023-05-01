package net.tnemc.bukkit.listeners.mcmmo;

import com.gmail.nossr50.events.skills.fishing.McMMOPlayerFishingTreasureEvent;
import net.tnemc.bukkit.BukkitConfig;
import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/8/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerFishingTreasureListener implements Listener {

  private final JavaPlugin plugin;

  public PlayerFishingTreasureListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  public void onFishReward(final McMMOPlayerFishingTreasureEvent event) {
    if(BukkitConfig.yaml().getBoolean("Bukkit.McMMORewards")) {
      Optional<Currency> currency = TNECore.eco().currency().findCurrencyByMaterial(event.getTreasure().getType().name());
      if (currency.isPresent()) event.setCancelled(true);
    }
  }
}
