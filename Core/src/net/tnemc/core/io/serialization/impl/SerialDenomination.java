package net.tnemc.core.io.serialization.impl;
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

import net.tnemc.core.currency.Denomination;
import net.tnemc.core.io.serialization.JSONified;
import org.json.simple.JSONObject;

/**
 * SerialDenomination
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SerialDenomination implements JSONified<Denomination> {

  /**
   * Used to serialize this object to a JSON-valid string.
   *
   * @param denom The object to serialize.
   * @return The {@link JSONObject} associated with the JSON-valid String.
   */
  @Override
  public JSONObject serialize(Denomination denom) {
    JSONObject json = new JSONObject();
    json.put("single", denom.singular());
    json.put("plural", denom.plural());
    json.put("weight", denom.weight().toPlainString());
    json.put("item", denom.isItem());
    return json;
  }

  /**
   * Used to generate information for this object from
   * @param serialized The JSON-valid String that we are going to deserialize.
   * @return The object that was deserialized from the JSON string.
   */
  @Override
  public Denomination deserialize(String serialized) {
    return null;
  }
}