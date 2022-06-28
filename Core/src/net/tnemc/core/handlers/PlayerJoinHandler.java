package net.tnemc.core.handlers;

import net.tnemc.core.compatibility.PlayerProvider;

/**
 * Represents an event where a player is joining.
 *
 * @since 0.1.2
 * @author creatorfromhell
 */
public class PlayerJoinHandler {

  /**
   * Used to handle a PlayerJoinEvent using the specified {@link PlayerProvider} class.
   * @param provider The {@link PlayerProvider} associated with the platform event.
   * @return True if the event should be cancelled, otherwise false.
   */
  public boolean handle(PlayerProvider provider) {
    //TODO: Is ready for players?

    //TODO: Check if account exists

    //TODO: Check item currency balances.

    //TODO: Check for transactions that happened while away if player has notify settings active.

    //TODO: If admin check update

    return false;
  }
}