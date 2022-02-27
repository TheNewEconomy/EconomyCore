package net.tnemc.core.compatibility.log;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 2/20/2022.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

/**
 * Used to outline the various debugging levels that could be utilized within the logging layer.
 *
 * @since 0.1.2.0
 * @author creatorfromhell
 */
public enum DebugLevel {
  /**
   * Standard debug level. This should include any inform calls and any error calls containing exceptions.
   */
  STANDARD("S"),

  /**
   * Detailed debug level. This should contain more detailed error calls and debug calls.
   */
  DETAILED("DE"),

  /**
   * Developer debug level. This should contain all calls.
   */
  DEVELOPER("DV");

  private String identifier;

  DebugLevel(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}