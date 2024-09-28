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
 * Represents a custom response.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CustomResponse implements EconomyResponse {

  private final boolean success;
  private final String response;

  /**
   * This is a helper class for to return custom responses during API calls that return an
   * {@link EconomyResponse} object.
   *
   * @param success  Whether the action was performed successfully.
   * @param response The message to send describing the response. Example: "Action failed because
   *                 account doesn't exist"
   *
   * @since 0.1.2.0
   */
  public CustomResponse(final boolean success, final String response) {

    this.success = success;
    this.response = response;
  }

  @Override
  public boolean success() {

    return success;
  }

  @Override
  public String response() {

    return response;
  }
}