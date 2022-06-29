package net.tnemc.core.account;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import org.jetbrains.annotations.NotNull;

/**
 * Represents the status of an {@link Account accout}. The  account
 * status affects things such as using the balance and receiving payments.
 *
 * @see Account
 * @since 0.1.2.0
 * @author creatorfromhell
 */
public interface AccountStatus {

  /**
   * @return The identifier of this account status.
   */
  @NotNull String identifier();

  /**
   * Whether the account may use money from their account.
   * @return True if the account is able to use funds from its balance, otherwise false.
   */
  boolean use();

  /**
   * Whether the account may receive money into their account.
   * @return True if the account is able to receive funds into its balance, otherwise false.
   */
  boolean receive();

}