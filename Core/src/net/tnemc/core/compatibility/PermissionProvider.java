package net.tnemc.core.compatibility;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 2/16/2022.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

/**
 * A class that acts like a bridge between various softwares and their permissions managers.
 * This is used to:
 *  a) Check for permissions.
 *  b) Register permissions.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public interface PermissionProvider {

  void register();
}