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
 * Represents a custom response.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CustomResponse implements EconomyResponse {

  private final boolean success;
  private final String response;

  /**
   * This is a helper class for to return custom responses during API calls
   * that return an {@link EconomyResponse} object.
   *
   * @param success  Whether the action was performed successfully.
   * @param response The message to send describing the response. Example: "Action failed because account
   *                 doesn't exist"
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