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

import net.tnemc.core.currency.Note;
import net.tnemc.core.io.serialization.JSONAble;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * SerialNote
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SerialNote implements JSONAble<Note> {


  /**
   * Used to serialize this object to a JSON-valid string.
   *
   * @param note The object to serialize.
   *
   * @return The {@link JSONObject} associated with the JSON-valid String.
   */
  @Override
  public JSONObject toJSON(Note note) {
    final JSONObject json = new JSONObject();
    json.put("material", note.getMaterial());
    json.put("minimum", note.getMinimum().toPlainString());
    json.put("customModelData", note.getCustomModelData());
    json.put("texture", note.getTexture());

    final JSONArray flagsArray = new JSONArray();
    flagsArray.addAll(note.getFlags());
    json.put("flags", flagsArray);

    final JSONArray enchantmentsArray = new JSONArray();
    enchantmentsArray.addAll(note.getEnchantments());
    json.put("enchantments", enchantmentsArray);

    json.put("fee", new SerialTaxEntry().toJSON(note.getFee()));

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
  public Note fromJSON(String serialized) {
    final JSONParser parser = new JSONParser();

    try {
      final JSONObject jsonObject = (JSONObject)parser.parse(serialized);

      final String material = (String)jsonObject.get("material");
      final BigDecimal minimum = new BigDecimal(jsonObject.get("minimum").toString());

      final JSONArray flagsArray = (JSONArray)jsonObject.get("flags");
      final JSONArray enchantmentsArray = (JSONArray)jsonObject.get("enchantments");

      final Note note = new Note(material, minimum, new SerialTaxEntry().fromJSON(((JSONObject)jsonObject.get("fee")).toJSONString()));
      note.setCustomModelData(Integer.parseInt(jsonObject.get("customModelData").toString()));
      note.setTexture((String)jsonObject.get("texture"));
      note.setFlags(new ArrayList<>(flagsArray));
      note.setEnchantments(new ArrayList<>(enchantmentsArray));

      return note;
    } catch(ParseException e) {
      e.printStackTrace();
      return null;
    }
  }
}