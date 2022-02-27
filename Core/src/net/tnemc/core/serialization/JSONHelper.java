package net.tnemc.core.serialization;

import org.json.simple.JSONObject;

public class JSONHelper {

  private JSONObject object;

  public JSONHelper(JSONObject object) {
    this.object = object;
  }

  public boolean has(String identifier) {
    return object.containsKey(identifier);
  }

  public boolean isNull(String identifier) {
    return object.get(identifier) == null;
  }

  public JSONHelper getHelper(String identifier) {
    return new JSONHelper(getJSON(identifier));
  }

  public JSONObject getJSON(String identifier) {
    return (JSONObject)object.get(identifier);
  }

  public Short getShort(String identifier) {
    return Short.valueOf(getString(identifier));
  }

  public Double getDouble(String identifier) {
    return Double.valueOf(getString(identifier));
  }

  public Integer getInteger(String identifier) {
    return Integer.valueOf(getString(identifier));
  }

  public Boolean getBoolean(String identifier) {
    return Boolean.valueOf(getString(identifier));
  }

  public String getString(String identifier) {
    return object.get(identifier).toString();
  }

  public JSONObject getObject() {
    return object;
  }

  public void setObject(JSONObject object) {
    this.object = object;
  }
}