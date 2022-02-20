package net.tnemc.core.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 2/13/2022.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

/**
 * A class used for storing data in a key-value pair without explicitly declaring value type.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class ConvertableData {

  private String identifier;
  private Object raw;

  public ConvertableData(String identifier, Object raw) {
    this.identifier = identifier;
    this.raw = raw;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public Object getRaw() {
    return raw;
  }

  public void setRaw(Object raw) {
    this.raw = raw;
  }

  public Integer asInt() {
    return (Integer)raw;
  }

  public Short asShort() {
    return (Short)raw;
  }

  public String asString() {
    return (String)raw;
  }

  public Double asDouble() {
    return (Double)raw;
  }

  public Long asLong() {
    return (Long)raw;
  }

  public BigDecimal asDecimal() {
    return (BigDecimal)raw;
  }

  public BigInteger asBigInt() {
    return (BigInteger)raw;
  }

  public UUID asUUID() {
    return (UUID)raw;
  }

  public Boolean asBool() {
    return (Boolean)raw;
  }
}