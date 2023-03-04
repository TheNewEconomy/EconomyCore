package net.tnemc.bukkit.impl;
/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * IconItems
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class IconItems {

  public static final ItemStack currency;

  static {
    currency = new ItemStack(Material.GOLD_INGOT);
    ItemMeta meta = Bukkit.getItemFactory().getItemMeta(Material.GOLD_INGOT);
    meta.setDisplayName(ChatColor.GOLD + "Currency Editor");
    meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Click to open currency editor."));
    currency.setItemMeta(meta);
  }
}