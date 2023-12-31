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
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.io.serialization.JSONAble;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.util.List;

/**
 * SerialDenomination
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SerialDenomination implements JSONAble<Denomination> {

  /**
   * Used to serialize this object to a JSON-valid string.
   *
   * @param denom The object to serialize.
   * @return The {@link JSONObject} associated with the JSON-valid String.
   */
  @Override
  public JSONObject toJSON(Denomination denom) {
    final JSONObject json = new JSONObject();
    json.put("single", denom.singular());
    json.put("plural", denom.plural());
    json.put("weight", denom.weight());

    if(denom instanceof ItemDenomination itemDenomination) {
      json.put("enchantments", itemDenomination.getEnchantments());
      json.put("item", true);
      json.put("flags", itemDenomination.getFlags());
      json.put("lore", itemDenomination.getLore());
      json.put("material", itemDenomination.getMaterial());
      json.put("damage", itemDenomination.getDamage());
      json.put("name", itemDenomination.getName());
      json.put("customModel", itemDenomination.getCustomModel());
      json.put("texture", itemDenomination.getTexture());
    }
    return json;
  }

  /**
   * Used to generate information for this object from
   * @param serialized The JSON-valid String that we are going to deserialize.
   * @return The object that was deserialized from the JSON string.
   */
  @Override
  public Denomination fromJSON(String serialized) {
    final JSONParser parser = new JSONParser();
    JSONObject jsonObject = null;
    try {
      jsonObject = (JSONObject) parser.parse(serialized);

      final BigDecimal weight = new BigDecimal(jsonObject.get("weight").toString());

      final Denomination denomination = (jsonObject.containsKey("item"))?
              new ItemDenomination(weight) :
              new Denomination(weight);

      denomination.setSingle((String)jsonObject.get("single"));
      denomination.setPlural((String)jsonObject.get("plural"));

      if(denomination instanceof ItemDenomination itemDenomination) {

        itemDenomination.setEnchantments((List<String>) jsonObject.get("enchantments"));
        itemDenomination.setFlags((List<String>) jsonObject.get("flags"));
        itemDenomination.setLore((List<String>) jsonObject.get("lore"));
        itemDenomination.setMaterial((String) jsonObject.get("material"));
        itemDenomination.setDamage(((Long) jsonObject.get("damage")).shortValue());
        itemDenomination.setName((String) jsonObject.get("name"));
        itemDenomination.setCustomModel((Integer) jsonObject.get("customModel"));
        itemDenomination.setTexture((String) jsonObject.get("texture"));

        return itemDenomination;
      }
      return denomination;
    } catch(ParseException e) {
      return null;
    }
  }
}