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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.ServerInformation;
import net.tnemc.core.compatibility.LogProvider;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.core.io.StorageManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

/**
 * The core class of TNE which should be used within each implementation's class.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class TNECore {
  public static final String coreURL = "https://tnemc.net/files/module-version.xml";

  private static TNECore instance;

  private StorageManager storageManager;
  private ServerConnector connector;
  private ServerInformation information;
  private LogProvider logger;

  private EconomyManager manager;

  private File directory;

  //TODO: Constructor here.

  /**
   * The implementation's {@link ServerConnector}.
   *
   * @return The server connector for the implementation.
   */
  public static ServerConnector connector() {
    return instance.connector;
  }

  /**
   * The implementation's {@link ServerInformation}.
   *
   * @return The server information.
   */
  public static ServerInformation info() {
    return instance.information;
  }

  /**
   * The implementation's {@link LogProvider}.
   *
   * @return The log provider.
   */
  public static LogProvider log() {
    return instance.logger;
  }

  public static StorageManager storage() {
    return instance.storageManager;
  }

  public static TNECore instance() {
    return instance;
  }

  public EconomyManager manager() {
    return manager;
  }

  public Optional<PlayerProvider> findPlayer(@NotNull UUID identifier) {
    return connector().findPlayer(identifier);
  }

  public static File directory() {
    return instance.directory;
  }
}
