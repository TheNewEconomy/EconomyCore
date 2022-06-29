package net.tnemc.core.currency.item;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.currency.Denomination;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a denomination for an {@link ItemCurrency currency}.
 *
 * @see ItemCurrency
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ItemDenomination extends Denomination {

  private List<String> enchantments = new ArrayList<>();
  private List<String> flags = new ArrayList<>();
  private List<String> lore = new ArrayList<>();

  private String material;
  private short damage;
  private String name = null;
  private Integer customModel = null;

  public ItemDenomination(String material) {
    this(material, (short)0);
  }

  public ItemDenomination(String material, short damage) {
    this.material = material;
    this.damage = damage;
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

  public List<String> getLore() {
    return lore;
  }

  public void setLore(List<String> lore) {
    this.lore = lore;
  }

  public int getCustomModel() {
    return customModel;
  }

  public void setCustomModel(int customModel) {
    this.customModel = customModel;
  }
}