package net.tnemc.core.compatibility.log;

/*
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
  STANDARD((byte)1, "S"),

  /**
   * Detailed debug level. This should contain more detailed error calls and debug calls.
   */
  DETAILED((byte)2, "DE"),

  /**
   * Developer debug level. This should contain all calls.
   */
  DEVELOPER((byte)3, "DV");

  private byte priority;
  private String identifier;

  DebugLevel(byte priority, String identifier) {
    this.priority = priority;
    this.identifier = identifier;
  }

  public boolean compare(DebugLevel compare) {
    return priority >= compare.priority;
  }

  public byte getPriority() {
    return priority;
  }

  public void setPriority(byte priority) {
    this.priority = priority;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}