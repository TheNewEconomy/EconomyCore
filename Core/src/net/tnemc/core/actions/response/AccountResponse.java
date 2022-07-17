package net.tnemc.core.actions.response;


/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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