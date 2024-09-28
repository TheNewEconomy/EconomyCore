package net.tnemc.core.actions.response;

/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

      return "Messages.Money.ExceedsOtherPlayerMaximum";
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

      return "Messages.Money.ExceedsOtherPlayerMinimum";
    }
  },

  /**
   * The action was unsuccessful due to the amount not being a multiple of a minimum denominations
   *
   * @since 0.1.2.0
   */
  MIN_DENOM_INCAPABLE {
    @Override
    public boolean success() {

      return false;
    }

    @Override
    public String response() {

      return "Messages.Money.MinimumDenomination";
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

      return "Messages.Money.Insufficient";
    }
  },

  /**
   * The action was unsuccessful due to the account not having enough funds.
   *
   * @since 0.1.2.0
   */
  INSUFFICIENT_OTHER {
    @Override
    public boolean success() {

      return false;
    }

    @Override
    public String response() {

      return "Messages.Money.InsufficientOther";
    }
  },

  /**
   * The action was unsuccessful due to the account not having access to the currency.
   *
   * @since 0.1.2.0
   */
  RESTRICTED {
    @Override
    public boolean success() {

      return false;
    }

    @Override
    public String response() {

      return "Messages.Money.Restricted";
    }
  },

  /**
   * The action was unsuccessful due to the account not having enough funds.
   *
   * @since 0.1.2.0
   */
  RECEIVE_LOCK {
    @Override
    public boolean success() {

      return false;
    }

    @Override
    public String response() {

      return "Messages.Account.ReceiveLock";
    }
  },

  /**
   * The action was unsuccessful due to the account not having enough funds.
   *
   * @since 0.1.2.0
   */
  USE_LOCK {
    @Override
    public boolean success() {

      return false;
    }

    @Override
    public String response() {

      return "Messages.Account.UseLock";
    }
  },

  DISABLED_REGION {
    /**
     * @return True if the associated action was performed correctly.
     *
     * @since 0.1.2.0
     */
    @Override
    public boolean success() {

      return false;
    }

    @Override
    public String response() {

      return "Messages.General.Disabled";
    }
  }
}
