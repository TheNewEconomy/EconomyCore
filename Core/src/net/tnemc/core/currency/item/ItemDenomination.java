package net.tnemc.core.currency.item;

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

import net.tnemc.core.currency.Denomination;
import net.tnemc.item.SerialItem;

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
  private String texture;

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

  public String getTexture() {
    return texture;
  }

  public void setTexture(String texture) {
    this.texture = texture;
  }

  public static ItemDenomination fromSerial(SerialItem<?> serial) {
    //TODO: Finish
    return null;
  }
  public SerialItem<?> toSerial() {
    //TODO: Finish
    return null;
  }
}