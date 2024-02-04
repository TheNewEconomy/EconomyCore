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

import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.plugincore.core.io.serialization.JSONAble;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * SerialCurrency
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SerialCurrency implements JSONAble<Currency> {
  /**
   * Used to serialize this object to a JSON-valid string.
   *
   * @param currency The object to serialize.
   *
   * @return The {@link JSONObject} associated with the JSON-valid String.
   */
  @Override
  public JSONObject toJSON(Currency currency) {
    final JSONObject json = new JSONObject();

    json.put("startingHoldings", currency.getStartingHoldings().toString());
    json.put("maxBalance", currency.getMaxBalance().toString());
    json.put("minBalance", currency.getMinBalance().toString());
    json.put("uid", currency.getUid().toString());
    json.put("iconMaterial", currency.getIconMaterial());
    json.put("identifier", currency.getIdentifier());
    json.put("type", currency.getType());

    json.put("format", currency.getFormat());
    json.put("symbol", currency.getSymbol());
    json.put("prefixes", currency.getPrefixes());
    json.put("decimal", currency.getDecimal());
    json.put("display", currency.getDisplay());
    json.put("displayPlural", currency.getDisplayPlural());
    json.put("displayMinor", currency.getDisplayMinor());
    json.put("displayMinorPlural", currency.getDisplayMinorPlural());
    json.put("separateMajor", currency.isSeparateMajor());
    json.put("majorSeparator", currency.getMajorSeparator());

    json.put("decimalPlaces", currency.getDecimalPlaces());
    json.put("minorWeight", currency.getMinorWeight());

    //Item currency
    if(currency instanceof ItemCurrency item) {
      json.put("ender", item.canEnderChest());
    }

    //Note
    if(currency.getNote().isPresent()) {

      json.put("note", new SerialNote().toJSON(currency.getNote().get()));
    }

    //Denominations
    final JSONArray denominationsArray = new JSONArray();
    for(final Denomination denom : currency.getDenominations().values()) {

      denominationsArray.add(new SerialDenomination().toJSON(denom));
    }
    json.put("denominations", denominationsArray);

    // Conversion
    final JSONArray conversionArray = new JSONArray();
    for(final Map.Entry<String, Double> entry : currency.getConversion().entrySet()) {

      final JSONObject conversionObject = new JSONObject();
      conversionObject.put("identifier", entry.getKey());
      conversionObject.put("rate", entry.getValue());
      conversionArray.add(conversionObject);
    }
    json.put("conversion", conversionArray);

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
  public Currency fromJSON(String serialized) {
    final JSONParser parser = new JSONParser();

    try {
      final JSONObject jsonObject = (JSONObject) parser.parse(serialized);

      final Currency currency = (jsonObject.containsKey("ender"))? new ItemCurrency() : new Currency();

      currency.setStartingHoldings(new BigDecimal((String)jsonObject.get("startingHoldings")));
      currency.setMaxBalance(new BigDecimal((String)jsonObject.get("maxBalance")));
      currency.setMinBalance(new BigDecimal((String)jsonObject.get("minBalance")));
      currency.setUid(UUID.fromString((String)jsonObject.get("uid")));
      currency.setIconMaterial((String)jsonObject.get("iconMaterial"));
      currency.setIdentifier((String)jsonObject.get("identifier"));
      currency.setType((String)jsonObject.get("type"));

      currency.setFormat((String)jsonObject.get("format"));
      currency.setSymbol((String)jsonObject.get("symbol"));
      currency.setPrefixes((String)jsonObject.get("prefixes"));
      currency.setDecimal((String)jsonObject.get("decimal"));
      currency.setDisplay((String)jsonObject.get("display"));
      currency.setDisplayPlural((String)jsonObject.get("displayPlural"));
      currency.setDisplayMinor((String)jsonObject.get("displayMinor"));
      currency.setDisplayMinorPlural((String)jsonObject.get("displayMinorPlural"));
      currency.setSeparateMajor((Boolean)jsonObject.get("separateMajor"));
      currency.setMajorSeparator((String)jsonObject.get("majorSeparator"));

      currency.setDecimalPlaces(((Long)jsonObject.get("decimalPlaces")).intValue());
      currency.setMinorWeight(((Long)jsonObject.get("minorWeight")).intValue());

      //Item currency
      if(currency instanceof ItemCurrency item) {
        item.setEnderChest((Boolean)jsonObject.get("ender"));
      }

      //Note
      if(jsonObject.containsKey("note")) {

        final JSONObject noteJson = (JSONObject)jsonObject.get("note");
        currency.setNote(new SerialNote().fromJSON(noteJson.toJSONString()));
      }

      //Denominations
      final JSONArray denominationsArray = (JSONArray)jsonObject.get("denominations");
      for(final Object denomObj : denominationsArray) {

        final JSONObject denomJson = (JSONObject)denomObj;
        final Denomination denom = new SerialDenomination().fromJSON(denomJson.toJSONString());

        currency.getDenominations().put(denom.weight(), denom);
      }

      //Conversion
      final JSONArray conversionArray = (JSONArray)jsonObject.get("conversion");
      for(final Object entryObj : conversionArray) {

        final JSONObject entryJson = (JSONObject)entryObj;
        final String identifier = (String)entryJson.get("identifier");
        final Double rate = (Double)entryJson.get("rate");
        currency.getConversion().put(identifier, rate);
      }

      return currency;
    } catch (ParseException e) {

      e.printStackTrace();
      return null;
    }
  }
}