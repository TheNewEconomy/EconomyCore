package net.tnemc.core.manager.setup;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

/**
 * Represents a step in the setup process.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface Step {

  /**
   * The human-friendly identifier for this step.
   * @return the human-friendly identifier for this step.
   */
  String identifier();

  /**
   * Runs this step.
   * @return True if this step ran successfully, otherwise false.
   */
  boolean run();
}