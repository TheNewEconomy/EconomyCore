package net.tnemc.core.utils;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

/**
 * This represents a response from an event handler that should be returned to the implementation.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class HandlerResponse {

  private String response;
  private boolean cancelled;

  public HandlerResponse(String response, boolean cancelled) {
    this.response = response;
    this.cancelled = cancelled;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}