package net.tnemc.core.io.serialization;

import org.json.simple.JSONObject;


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

/**
 * A class which represents an object that can be parsed to or from JSON.
 *
 * @since 0.1.2.0
 * @author creatorfromhell
 */
public abstract class JSONified<T> {

  protected String serialized;

  public JSONified(String serialized) {
    this.serialized = serialized;
  }

  public JSONified(T object) {
    this.serialized = serialize(object).toJSONString();
  }

  /**
   * Used to serialize this object to a JSON-valid string.
   * @param object Our object to serialize.
   * @return The {@link JSONObject} associated with the JSON-valid String.
   */
  abstract JSONObject serialize(T object);

  /**
   * Used to generate information for this object from
   * @param serialized The JSON-valid String that we are going to deserialize.
   *
   */
  abstract T deserialize(String serialized);
}