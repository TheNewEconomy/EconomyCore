package net.tnemc.core.currency;

import net.tnemc.item.SerialItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/9/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

/**
 * Represents a currency note object, which is a way to make virtual balances into a physical note
 * to be traded or stored in a chest.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class CurrencyNote {

  private List<String> flags = new ArrayList<>();
  private List<String> enchantments = new ArrayList<>();

  private String material;
  private BigDecimal minimum;
  private BigDecimal fee;

  private int customModelData;
  private String texture;

  public CurrencyNote(String material, BigDecimal minimum, BigDecimal fee) {
    this.material = material;
    this.minimum = minimum;
    this.fee = fee;
  }

  public BigDecimal getMinimum() {
    return minimum;
  }

  public void setMinimum(BigDecimal minimum) {
    this.minimum = minimum;
  }

  public BigDecimal getFee() {
    return fee;
  }

  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

  public List<String> getFlags() {
    return flags;
  }

  public void setFlags(List<String> flags) {
    this.flags = flags;
  }

  public List<String> getEnchantments() {
    return enchantments;
  }

  public void setEnchantments(List<String> enchantments) {
    this.enchantments = enchantments;
  }

  public String getMaterial() {
    return material;
  }

  public void setMaterial(String material) {
    this.material = material;
  }

  public int getCustomModelData() {
    return customModelData;
  }

  public void setCustomModelData(int customModelData) {
    this.customModelData = customModelData;
  }

  public String getTexture() {
    return texture;
  }

  public void setTexture(String texture) {
    this.texture = texture;
  }

  public SerialItem build() {
    return new SerialItem();
  }
}