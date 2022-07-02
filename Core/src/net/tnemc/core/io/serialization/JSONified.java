package net.tnemc.core.io.serialization;

import org.json.simple.JSONObject;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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