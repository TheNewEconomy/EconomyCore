package net.tnemc.core.serialization;

import org.json.simple.JSONObject;

/**
 * A class which represents an object that can be parsed to or from JSON.
 *
 * @since 0.1.2.0
 * @author creatorfromhell
 */
public abstract class JSONified<T> {

  protected String serialized;
  protected T object;

  public JSONified(String serialized) {
    this.object = deserialize(serialized);
  }

  public JSONified(T object) {
    this.object = object;
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