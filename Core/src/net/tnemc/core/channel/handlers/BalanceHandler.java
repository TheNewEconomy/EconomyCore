package net.tnemc.core.channel.handlers;
/*
 * The New Economy
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.channel.ChannelBytesWrapper;
import net.tnemc.core.channel.ChannelMessageHandler;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.utils.Identifier;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * BalanceHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BalanceHandler extends ChannelMessageHandler {

  public BalanceHandler() {
    super("balance");
  }

  public static void send(final String account, String region, UUID currency, Identifier handler, BigDecimal amount) {
    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(TNECore.instance().getServerID().toString());
    out.writeUTF(account);
    out.writeUTF(region);
    out.writeUTF(currency.toString());
    out.writeUTF(handler.asID());
    out.writeUTF(amount.toPlainString());

    TNECore.storage().sendMessage("tne:balance", out.toByteArray());
  }

  @Override
  public void handle(ChannelBytesWrapper wrapper) {

    try {

      final String accountID = wrapper.readUTF();
      final String region = wrapper.readUTF();
      final Optional<UUID> currency = wrapper.readUUID();
      final String handler = wrapper.readUTF();
      final Optional<BigDecimal> amountOPT = wrapper.readBigDecimal();

      if(amountOPT.isPresent() && currency.isPresent()) {

        final Optional<Account> account = TNECore.eco().account().findAccount(accountID);
        if(account.isPresent()) {
          TNECore.instance().getChannelMessageManager().addAccount(accountID);

          final Identifier type = Identifier.fromID(handler);

          final HoldingsEntry entry = new HoldingsEntry(region, currency.get(),
                                                        amountOPT.get(), type);
          account.get().setHoldings(entry, type);
        }
      }

    } catch(Exception e) {
      TNECore.log().error("Issue with balance plugin message handler.", e, DebugLevel.STANDARD);
    }
  }
}