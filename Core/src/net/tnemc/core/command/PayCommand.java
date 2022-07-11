package net.tnemc.core.command;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.TNECore;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.source.PlayerSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;

/**
 * PayCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PayCommand {

  public void handle(PlayerProvider sender, PlayerProvider to, HoldingsModifier payment) {

    Transaction transaction = Transaction.of("pay").from(sender.getUUID().toString(), payment)
        .to(to.getUUID().toString(), payment).source(new PlayerSource(sender.getUUID()));

    try {
      transaction.process((result->{

      }));
    } catch(InvalidTransactionException e) {
      TNECore.log().error("Error while performing transaction.", e, DebugLevel.DETAILED);
    }
  }
}