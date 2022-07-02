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
 * Contains some default responses that relate to holdings operations.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public enum HoldingsResponse implements EconomyResponse {

  /**
   * The action was unsuccessful due to the account going over the max supported holdings.
   *
   * @since 0.1.2.0
   */
  MAX_HOLDINGS {
    @Override
    public boolean success() {
      return false;
    }

    @Override
    public String response() {
      return "The holdings change would put this account above the max allowed holdings.";
    }
  },

  /**
   * The action was unsuccessful due to the account going below the minimum holdings.
   *
   * @since 0.1.2.0
   */
  MIN_HOLDINGS {
    @Override
    public boolean success() {
      return false;
    }

    @Override
    public String response() {
      return "The holdings change would put this account below the minimum allowed holdings.";
    }
  },

  /**
   * The action was unsuccessful due to the account not having enough funds.
   *
   * @since 0.1.2.0
   */
  INSUFFICIENT {
    @Override
    public boolean success() {
      return false;
    }

    @Override
    public String response() {
      return "The specified account has insufficient funds.";
    }
  }
}
