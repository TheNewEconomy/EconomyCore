package net.tnemc.core.currency;

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

import net.tnemc.core.TNECore;
import net.tnemc.item.AbstractItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a currency note object, which is a way to make virtual balances into a physical note
 * to be traded or stored in a chest.
 *
 * @since 0.1.2.0
 * @author creatorfromhell
 */
public class Note {

  private final List<String> flags = new ArrayList<>();
  private final List<String> enchantments = new ArrayList<>();
  private final List<String> lore = new ArrayList<>();

  private String material;
  private BigDecimal minimum;
  private BigDecimal fee;

  private int customModelData;
  private String texture;

  public Note(String material, BigDecimal minimum, BigDecimal fee) {
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
    this.flags.clear();
    this.flags.addAll(flags);
  }

  public List<String> getEnchantments() {
    return enchantments;
  }

  public void setEnchantments(List<String> enchantments) {
    this.enchantments.clear();
    this.enchantments.addAll(enchantments);
  }

  public List<String> getLore() {
    return lore;
  }

  public void setLore(List<String> lore) {
    this.lore.clear();
    this.lore.addAll(lore);
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

  public AbstractItemStack<Object> stack(final String currency, final String region, final BigDecimal amount) {
    return (AbstractItemStack<Object>)TNECore.server().stackBuilder().of(material, 1)
        .display("Currency Note")
        .enchant(enchantments)
        .flags(flags)
        .modelData(customModelData)
        .lore(lore);
  }
}