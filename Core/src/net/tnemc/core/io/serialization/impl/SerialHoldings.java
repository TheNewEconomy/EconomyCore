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

import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.plugincore.core.io.serialization.JSONAble;
import net.tnemc.core.utils.Identifier;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * SerialHoldings
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SerialHoldings implements JSONAble<HoldingsEntry> {

  /**
   * Used to serialize this object to a JSON-valid string.
   *
   * @param holdings The object to serialize.
   *
   * @return The {@link JSONObject} associated with the JSON-valid String.
   */
  @Override
  public JSONObject toJSON(HoldingsEntry holdings) {
    final JSONObject json = new JSONObject();

    json.put("region", holdings.getRegion());
    json.put("currency", holdings.getCurrency().toString());
    json.put("amount", holdings.getAmount().toString());
    json.put("handler", holdings.getHandler().asID());
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
  public HoldingsEntry fromJSON(String serialized) {
    try {
      final JSONParser parser = new JSONParser();
      final JSONObject json = (JSONObject)parser.parse(serialized);

      return new HoldingsEntry((String)json.get("region"),
              UUID.fromString((String)json.get("currency")),
              new BigDecimal((String)json.get("amount")),
              Identifier.fromID((String)json.get("handler")));
    } catch (ParseException | NumberFormatException | ClassCastException e) {
      e.printStackTrace();
      return null;
    }
  }
}