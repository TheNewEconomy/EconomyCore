
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.manager.AccountManager;
import net.tnemc.core.manager.DataManager;
import net.tnemc.core.manager.TranslationManager;

/**
 * This class manages everything for the economy plugin, from language storage to holding instances
 * to other managers.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class EconomyManager {

  private final AccountManager accountManager;
  private final TranslationManager translationManager;
  private final DataManager dataManager;

  public EconomyManager() {
    this.accountManager = new AccountManager();
    this.translationManager = new TranslationManager();
    this.dataManager = new DataManager();
  }
}