package net.tnemc.core.currency.item;

/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tnemc.core.currency.Denomination;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a denomination for an {@link ItemCurrency currency}.
 *
 * @author creatorfromhell
 * @see ItemCurrency
 * @since 0.1.2.0
 */
public class ItemDenomination extends Denomination {

  private final List<String> enchantments = new ArrayList<>();
  private final List<String> flags = new ArrayList<>();
  private final List<Component> lore = new ArrayList<>();

  private String material = "PAPER";
  private short damage = 0;
  private String name = "";
  private Integer customModel = -1;
  private String texture = "";

  public ItemDenomination(final BigDecimal weight, final String material) {

    this(weight, material, (short)0);
  }

  public ItemDenomination(final BigDecimal weight, final String material, final short damage) {

    super(weight);
    this.material = material;
    this.damage = damage;
  }

  public ItemDenomination(final BigDecimal weight) {

    super(weight);
  }

  public List<String> getEnchantments() {

    return enchantments;
  }

  public void setEnchantments(final List<String> enchantments) {

    this.enchantments.clear();
    this.enchantments.addAll(enchantments);
  }

  public List<String> getFlags() {

    return flags;
  }

  public void setFlags(final List<String> flags) {

    this.flags.clear();
    this.flags.addAll(flags);
  }

  public String getMaterial() {

    return material;
  }

  public void setMaterial(final String material) {

    this.material = material;
  }

  public short getDamage() {

    return damage;
  }

  public void setDamage(final short damage) {

    this.damage = damage;
  }

  public String getName() {

    return name;
  }

  public void setName(final String name) {

    this.name = name;
  }

  public List<Component> getLore() {

    return lore;
  }

  public LinkedList<String> getLoreAsString() {

    final LinkedList<String> loreAsString = new LinkedList<>();
    for(final Component component : lore) {
      loreAsString.add(MiniMessage.miniMessage().serialize(component));
    }
    return loreAsString;
  }

  public void setLore(final List<Component> lore) {

    this.lore.clear();
    this.lore.addAll(lore);
  }

  public int getCustomModel() {

    return customModel;
  }

  public void setCustomModel(final int customModel) {

    this.customModel = customModel;
  }

  public String getTexture() {

    return texture;
  }

  public void setTexture(final String texture) {

    this.texture = texture;
  }
}