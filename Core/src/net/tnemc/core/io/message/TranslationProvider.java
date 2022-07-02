package net.tnemc.core.io.message;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */


import java.util.UUID;

/**
 * Represents a class that provides translation services.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface TranslationProvider {

  /**
   * Used to get the language of a player with the associated identifier.
   * @param identifier The identifier of the player.
   * @return The language that should be used for this player.
   */
  String getLang(final UUID identifier);

  /**
   * Used to translate a node for the given language. This should resort to the default if the
   * specified language doesn't exist.
   *
   * @param node The node to translate.
   * @param language The language to translate the node to.
   * @return The translated message represented by the node, or the default for if the node doesn't
   * exist.
   */
  String translateNode(final String node, final String language);

  /**
   * Used to translate a node for the given language for the given player. This should resort to the
   * default if the specified language doesn't exist.
   * @param identifier The identifier of the given player.
   * @param node The node to translate.
   * @return The translated message represented by the node, or the default for if the node doesn't
   * exist.
   */
  default String translate(final UUID identifier, final String node) {
    return translateNode(node, getLang(identifier));
  }
}