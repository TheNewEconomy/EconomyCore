package net.tnemc.core.handlers;

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.utils.AccountHelper;
import net.tnemc.core.utils.HandlerResponse;

import java.util.Optional;

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

    Optional<Account> account = TNECore.eco().account().findAccount(provider.getUUID());

    //Our account doesn't exist, so now we need to continue from here
    if(account.isEmpty()) {

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