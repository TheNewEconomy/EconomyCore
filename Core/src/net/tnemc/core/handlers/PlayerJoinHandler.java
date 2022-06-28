package net.tnemc.core.handlers;

import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.utils.AccountHelper;
import net.tnemc.core.utils.HandlerResponse;

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
  public HandlerResponse handle(PlayerProvider provider) {
    final HandlerResponse response = new HandlerResponse("", false);

    //TODO: Is ready for players? Probably not needed anymore

    if(!AccountHelper.exists(provider.getUUID())) {

      //Initialize our account.
      if(!AccountHelper.initialize(provider.getUUID(), provider.getName())) {
        response.setResponse("Messages.Account.Failed");
        response.setCancelled(true);
        return response;
      }
    }

    //TODO: Check item currency balances.

    //TODO: Check for transactions that happened while away if player has notify settings active.

    if(provider.hasPermission("tne.admin.update")) {
      //TODO: Update check.

      //TODO: Any warnings? Balance jumps?
    }

    return response;
  }
}