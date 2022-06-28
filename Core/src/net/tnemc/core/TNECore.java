package net.tnemc.core;

/*
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
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.id.UUIDProvider;
import net.tnemc.core.id.impl.provider.BaseUUIDProvider;
import net.tnemc.core.storage.StorageManager;
import net.tnemc.core.world.WorldProvider;

import java.io.File;
import java.util.regex.Pattern;

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
  public static final Pattern UUID_MATCHER_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

  /* Core non-final variables utilized within TNE as settings */
  protected File directory;

  //The DebugLevel that the server is currently running in.
  private DebugLevel level = DebugLevel.STANDARD;

  /* Key Managers and Object instances utilized with TNE */

  //General Key Object Instances
  protected LogProvider logger;

  //Manager Instances
  protected StorageManager storage;
  protected UUIDProvider uuidProvider = new BaseUUIDProvider();
  protected WorldProvider worldProvider = new WorldProvider();

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

  /**
   * The implementation's {@link UUIDProvider}, which is used to manage all UUIDs within TNE.
   * @return The {@link UUIDProvider UUID Provider}.
   */
  public static UUIDProvider uuidProvider() {
    return instance.uuidProvider;
  }

  /**
   * The implementation's {@link WorldProvider}, which is used to manage everything related to world
   * groups.
   * @return The {@link WorldProvider World Provider}.
   */
  public static WorldProvider worldProvider() {
    return instance.worldProvider;
  }

  /**
   * The {@link StorageManager} we are utilizing.
   *
   * @return The {@link StorageManager}.
   */
  public static StorageManager storage() {
    return instance.storage;
  }

  public static TNECore instance() {
    return instance;
  }

  public DebugLevel getLevel() {
    return level;
  }
}
