package net.tnemc.core.command;

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