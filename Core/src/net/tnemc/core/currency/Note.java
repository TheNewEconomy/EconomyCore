package net.tnemc.core.currency;

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
import net.tnemc.core.config.MessageConfig;
import net.tnemc.core.transaction.tax.TaxEntry;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.plugincore.PluginCore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

  private String material;
  private BigDecimal minimum;

  private int customModelData;
  private String texture;

  final TaxEntry fee;

  public Note(String material, BigDecimal minimum, String fee) {
    this.material = material;
    this.minimum = minimum;

    if(fee.contains("%")) {
      this.fee = new TaxEntry("percent", Double.parseDouble(fee.replace("%", "")) / 100);
    } else {
      this.fee = new TaxEntry("flat", Double.parseDouble(fee));
    }
  }

  public Note(String material, BigDecimal minimum, TaxEntry fee) {
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

  public TaxEntry getFee() {
    return fee;
  }

  public List<String> getFlags() {
    return flags;
  }

  public void setFlags(List<String> flags) {
    this.flags.clear();
    for(String str : flags) {
      if(!str.isEmpty()) {
        this.flags.add(str);
      }
    }
  }

  public List<String> getEnchantments() {
    return enchantments;
  }

  public void setEnchantments(List<String> enchantments) {
    this.enchantments.clear();
    for(String str : enchantments) {
      if(!str.isEmpty()) {
        this.enchantments.add(str);
      }
    }
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
    final LinkedList<Component> lore = new LinkedList<>();
    lore.add(Component.text(MessageConfig.yaml().getString("Messages.Note.Currency").replace("$currency", currency)));
    lore.add(Component.text(MessageConfig.yaml().getString("Messages.Note.Region").replace("$region", region)));
    lore.add(Component.text(MessageConfig.yaml().getString("Messages.Note.Amount").replace("$amount", amount.toPlainString())));
    lore.add(Component.text(MessageConfig.yaml().getString("Messages.Note.Action")));

    return (AbstractItemStack<Object>)PluginCore.server().stackBuilder().of(material, 1)
        .display(Component.text(MessageConfig.yaml().getString("Messages.Note.Name")))
        .enchant(enchantments)
        .flags(flags)
        .modelData(customModelData)
        .lore(lore);
  }
}