package net.tnemc.core.actions.response;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.Account;
import net.tnemc.core.actions.EconomyResponse;

/**
 * Contains some default responses that relate to {@link Account account} operations.
 *
 * @see Account
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public enum AccountResponse implements EconomyResponse {

  /**
   * The action was successfully completed, and during it an account was created.
   *
   * @since 0.1.2.0
   */
  CREATED {
    @Override
    public boolean success() {
      return true;
    }

    @Override
    public String response() {
      return "The specified account was created successfully.";
    }
  },

  /**
   * The action was successfully completed, and during it an account was created.
   *
   * @since 0.1.2.0
   */
  CREATION_FAILED {
    @Override
    public boolean success() {
      return false;
    }

    @Override
    public String response() {
      return "The specified account couldn't be created.";
    }
  },

  /**
   * The account that was attempted to be created already exists.
   *
   * @since 0.1.2.0
   */
  ALREADY_EXISTS {
    @Override
    public boolean success() {
      return false;
    }

    @Override
    public String response() {
      return "The specified account already exists.";
    }
  },

  /**
   * The account specified during a call doesn't exist.
   *
   * @since 0.1.2.0
   */
  DELETED {
    @Override
    public boolean success() {
      return true;
    }

    @Override
    public String response() {
      return "The account was deleted successfully";
    }
  },

  /**
   * The account specified during a call doesn't exist.
   *
   * @since 0.1.2.0
   */
  DOESNT_EXIST {
    @Override
    public boolean success() {
      return false;
    }

    @Override
    public String response() {
      return "The specified account doesn't exist.";
    }
  }

}