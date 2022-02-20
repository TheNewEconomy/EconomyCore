package net.tnemc.core.currency;

import net.tnemc.core.currency.item.ItemDenomination;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Represents a denomination for an {@link Currency currency}.
 *
 * @see Currency
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class Denomination {

  private boolean major;
  private String single;
  private String plural;
  private BigInteger weight;

  public boolean isItem() {
    return (this instanceof ItemDenomination);
  }

  public void setSingle(String single) {
    this.single = single;
  }

  public void setPlural(String plural) {
    this.plural = plural;
  }

  public void setWeight(BigInteger weight) {
    this.weight = weight;
  }

  public BigInteger getTNEWeight() {
    return weight;
  }

  public void setMajor(boolean major) {
    this.major = major;
  }

  public String singular() {
    return single;
  }

  public String plural() {
    return plural;
  }

  public boolean isMajor() {
    return major;
  }

  public BigDecimal weight() {
    return new BigDecimal(weight);
  }
}