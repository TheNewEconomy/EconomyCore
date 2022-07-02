package net.tnemc.core.actions.response;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.actions.EconomyResponse;

/**
 * Represents general responses that may be utilized for multiple reasons.
 * 
 * @author creatorfromhell 
 * @since 0.1.2.0
 */
public enum GeneralResponse implements EconomyResponse {

  /**
   * The action was successfully performed on the account's holdings.
   *
   * @since 0.1.2.0
   */
  SUCCESS {
    @Override
    public boolean success() {
      return true;
    }

    @Override
    public String response() {
      return "The action was successful.";
    }
  },

  /**
   * The access was unsuccessful.
   *
   * @since 0.1.2.0
   */
  FAILED {
    @Override
    public boolean success() {
      return false;
    }

    @Override
    public String response() {
      return "The action was unsuccessful.";
    }
  },

  /**
   * The action was unsuccessful due to the Reserve implementation not supporting it.
   *
   * @since 0.1.2.0
   */
  UNSUPPORTED {
    @Override
    public boolean success() {
      return false;
    }

    @Override
    public String response() {
      return "This action is not supported by this version of TNE.";
    }
  }
}