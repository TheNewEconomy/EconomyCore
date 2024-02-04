package net.tnemc.core.channel;
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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.tnemc.core.TNECore;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.plugincore.core.channel.ChannelBytesWrapper;
import net.tnemc.plugincore.core.channel.ChannelMessageHandler;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;

/**
 * CreateHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CreateHandler extends ChannelMessageHandler {

  public CreateHandler() {
    super("create");
  }

  public static void send(final String identifier, final String name) {
    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(TNECore.core().getServerID().toString());
    out.writeUTF(identifier);
    out.writeUTF(name);

    TNECore.storage().sendProxyMessage("tne:create", out.toByteArray());
  }

  @Override
  public void handle(ChannelBytesWrapper wrapper) {

    try {

      final String accountID = wrapper.readUTF();
      final String accountName = wrapper.readUTF();

      if(accountID != null && accountName != null) {

        final AccountAPIResponse apiResponse = TNECore.eco().account().createAccount(accountID, accountName);
        if(apiResponse.getResponse().success()) {
        }
      }

    } catch(Exception e) {
      TNECore.log().error("Issue with create account plugin message handler.", e, DebugLevel.STANDARD);
    }
  }
}