package net.tnemc.core.currency.item;

import net.tnemc.core.currency.Denomination;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a denomination for an {@link ItemCurrrency currency}.
 *
 * @see ItemCurrrency
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class ItemDenomination extends Denomination {
  private List<String> enchantments = new ArrayList<>();
  private List<String> flags = new ArrayList<>();

  private String material;
  private short damage;
  private String name;
  private String lore;
  private Integer customModel = null;

  public ItemDenomination(String material) {
    this(material, (short)0);
  }

  public ItemDenomination(String material, short damage) {
    this.material = material;
    this.damage = damage;
    this.name = null;
    this.lore = "";
  }

  public List<String> getEnchantments() {
    return enchantments;
  }

  public void setEnchantments(List<String> enchantments) {
    this.enchantments = enchantments;
  }

  public List<String> getFlags() {
    return flags;
  }

  public void setFlags(List<String> flags) {
    this.flags = flags;
  }

  public String getMaterial() {
    return material;
  }

  public void setMaterial(String material) {
    this.material = material;
  }

  public short getDamage() {
    return damage;
  }

  public void setDamage(short damage) {
    this.damage = damage;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLore() {
    return lore;
  }

  public void setLore(String lore) {
    this.lore = lore;
  }

  public int getCustomModel() {
    return customModel;
  }

  public void setCustomModel(int customModel) {
    this.customModel = customModel;
  }
}