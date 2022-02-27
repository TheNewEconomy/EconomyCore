package net.tnemc.core;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 1/2/2022.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */


import net.tnemc.core.compatibility.LogProvider;

import java.io.File;

/**
 * The core class of TNE which should be used within each implementation's class.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public abstract class TNECore {

  /*
   * Core final variables utilized within TNE.
   */
  public static final String coreURL = "https://tnemc.net/files/module-version.xml";

  /* Core non-final variables utilized within TNE as settings */
  protected File directory;

  /* Key Managers and Object instances utilized with TNE */

  //General Key Object Instances
  protected LogProvider logger;

  //Manager Instances

  /* Plugin Instance */
  private static TNECore instance;

  public static void setInstance(TNECore core) {

    if(instance == null) {
      instance = core;
    } else {
      throw new IllegalStateException("TNE has already been initiated. Please refrain from attempting" +
                                          "to modify the instance variable.");
    }
  }

  /**
   * The implementation's {@link LogProvider}.
   *
   * @return The log provider.
   */
  public static LogProvider log() {
    return instance.logger;
  }
}
