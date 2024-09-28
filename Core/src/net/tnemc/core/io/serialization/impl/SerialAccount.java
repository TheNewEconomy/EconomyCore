package net.tnemc.core.io.serialization.impl;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.Wallet;
import net.tnemc.core.account.shared.Member;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.plugincore.core.io.serialization.JSONAble;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Optional;
import java.util.UUID;

/**
 * SerialAccount
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SerialAccount implements JSONAble<Account> {

  /**
   * Used to serialize this object to a JSON-valid string.
   *
   * @param account The object to serialize.
   *
   * @return The {@link JSONObject} associated with the JSON-valid String.
   */
  @Override
  public JSONObject toJSON(final Account account) {

    final JSONObject json = new JSONObject();

    json.put("identifier", account.getIdentifier());
    json.put("name", account.getName());
    json.put("creationDate", account.getCreationDate());
    json.put("pin", account.getPin());
    json.put("status", account.getStatus().identifier());
    json.put("type", account.type());

    final JSONArray holdingsArray = new JSONArray();
    for(final HoldingsEntry entry : account.getWallet().entryList()) {
      holdingsArray.add(new SerialHoldings().toJSON(entry));
    }
    json.put("holdings", holdingsArray);

    if(account instanceof SharedAccount shared) {
      json.put("owner", shared.getOwner());

      final JSONArray membersArray = new JSONArray();
      for(final Member member : shared.getMembers().values()) {
        membersArray.add(new SerialMember().toJSON(member));
      }
      json.put("members", membersArray);
    }

    if(account instanceof PlayerAccount player) {
      json.put("lastOnline", player.getLastOnline());
      json.put("language", player.getLanguage());
    }

    return json;
  }

  /**
   * Used to generate information for this object from
   *
   * @param serialized The JSON-valid String that we are going to deserialize.
   *
   * @return The object that was deserialized from the JSON string.
   */
  @Override
  public Account fromJSON(final String serialized) {

    try {
      final JSONParser parser = new JSONParser();
      final JSONObject json = (JSONObject)parser.parse(serialized);

      final String identifier = (String)json.get("identifier");
      final String name = (String)json.get("name");
      final String type = (String)json.get("type");

      final AccountAPIResponse response = TNECore.eco().account().createAccount(identifier,
                                                                                name,
                                                                                !(type.equalsIgnoreCase("player") ||
                                                                                  type.equalsIgnoreCase("bedrock")));
      if(response.getResponse().success()) {

        final Optional<Account> account = response.getAccount();
        if(account.isPresent()) {

          //Basic account variables
          account.get().setPin((String)json.get("pin"));
          account.get().setCreationDate((Long)json.get("creationDate"));
          account.get().setStatus(TNECore.eco().account().findStatus((String)json.get("status")));

          final JSONArray holdingsArray = (JSONArray)json.get("holdings");
          final Wallet wallet = new Wallet();
          for(final Object entryObj : holdingsArray) {
            final JSONObject entryJson = (JSONObject)entryObj;
            final HoldingsEntry entry = new SerialHoldings().fromJSON(entryJson.toJSONString());
            wallet.setHoldings(entry);
          }


          //Shared accounts.
          if(account.get() instanceof SharedAccount shared) {
            shared.setOwner((UUID)json.get("owner"));

            if(json.containsKey("members")) {

              final JSONArray membersArray = (JSONArray)json.get("members");
              for(final Object memberObj : membersArray) {

                final JSONObject memberJson = (JSONObject)memberObj;
                final Member member = new SerialMember().fromJSON(memberJson.toJSONString());
                shared.getMembers().put(member.getId(), member);
              }
            }
          }

          //Player-based accounts.
          if(account.get() instanceof PlayerAccount player) {

            player.setLastOnline((Long)json.get("lastOnline"));
            player.setLanguage((String)json.get("language"));
          }
          return account.get();
        }
      }
    } catch(ParseException | NumberFormatException | ClassCastException e) {
      e.printStackTrace();
      return null;
    }
    return null;
  }
}
