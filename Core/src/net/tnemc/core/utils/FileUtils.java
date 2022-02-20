package net.tnemc.core.utils;

import java.io.File;

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
public class FileUtils {

  /**
   * Used to get an array of all files in a directory with a specific extension.
   * @param directory The directory to search.
   * @param extension The extension we are looking for.
   * @return The array of {@link File} objects that have the extension matching the one we are searching for.
   */
  public static File[] getByEXT(final File directory, final String extension) {
    return directory.listFiles((dir, name) -> name.endsWith(extension));
  }
}