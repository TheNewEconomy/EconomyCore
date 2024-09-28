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
   * The action was unsuccessful.
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
   * The action was unsuccessful due to a callback cancellation.
   *
   * @since 0.1.2.0
   */
  FAILED_PLUGIN {
    @Override
    public boolean success() {

      return false;
    }

    @Override
    public String response() {

      return "The action was unsuccessful, because it was blocked by a plugin.";
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