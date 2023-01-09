package net.tnemc.core.currency.calculations;

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

import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * CurrencyData
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyData {
  private final Map<String, Integer> inventoryMaterials = new HashMap<>();

  private final TreeMap<BigDecimal, Denomination> materialValues = new TreeMap<>();

  private ItemCurrency currency;
  private boolean dropped = false;

  /**
   * A class used to hold information regarding amounts of each {@link ItemDenomination} an inventory contains. This information
   * will be in turn used for a balance calculation.
   * @param inventory The inventory associated with the data to collect.
   * @param currency The currency to use for the data collection.
   */
  public PlayerCurrencyData(Inventory inventory, ItemCurrency currency) {
    this.inventory = inventory;
    this.currency = currency;
    materialValues.putAll(currency.getDenominations());
    initializeInventoryMaterials(inventory);
  }

  /**
   * Used to calculate the amount of each tier the {@link Inventory} contains.
   * @param inventory The inventory to use for the calculation.
   */
  public void initializeInventoryMaterials(Inventory inventory) {
    for(Map.Entry<BigDecimal, Denomination> entry : materialValues.entrySet()) {

      if(entry.getValue().getItemInfo() != null) {
        inventoryMaterials.put(entry.getValue().getItemInfo().getMaterial(),
                               currency.calculation().getCount(entry.getValue().getItemInfo().toStack(), inventory));
      }
    }
  }

  public Map<String, Integer> getInventoryMaterials() {
    return inventoryMaterials;
  }

  public void removeMaterials(Denomination tier, Integer amount) {
    final int contains = inventoryMaterials.get(tier.getItemInfo().getMaterial());
    if(contains == amount) {
      inventoryMaterials.remove(tier.getItemInfo().getMaterial());
      removeAllItem(tier.getItemInfo().toStack(), inventory);
      return;
    }
    final int left = contains - amount;
    inventoryMaterials.put(tier.getItemInfo().getMaterial(), left);
    removeItemAmount(tier.getItemInfo().toStack(), inventory, amount);
  }

  public void provideMaterials(final Denomination tier, Integer amount) {
    ItemStack stack = tier.getItemInfo().toStack().clone();
    stack.setAmount(amount);
    if(giveItems(Collections.singletonList(stack), inventory)) {
      dropped = true;
    }
  }

  public void setInventoryMaterials(Map<String, Integer> inventoryMaterials) {
    this.inventoryMaterials = inventoryMaterials;
  }

  public TreeMap<BigDecimal, Denomination> getMaterialValues() {
    return materialValues;
  }

  public void setMaterialValues(TreeMap<BigDecimal, Denomination> materialValues) {
    this.materialValues = materialValues;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public ItemCurrency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public static boolean giveItems(Collection<ItemStack> items, Inventory inventory) {
    boolean dropped = false;
    for(ItemStack item : items) {
      Map<Integer, ItemStack> left = inventory.addItem(item);

      if(left.size() > 0) {
        if(inventory instanceof PlayerInventory) {
          final HumanEntity entity = ((HumanEntity)inventory.getHolder());
          drop(left, entity.getLocation());
          dropped = true;
        }
      }
    }
    return dropped;
  }

  public static void drop(Map<Integer, ItemStack> left, Location location) {

    for (Map.Entry<Integer, ItemStack> entry : left.entrySet()) {
      final ItemStack i = entry.getValue();
      Bukkit.getScheduler().runTask(TNE.instance(), () -> {
        try {
          location.getWorld().dropItemNaturally(location, i);
        } catch (Exception e) {
          //attempted to drop air/some crazy/stupid error.
        }
      });
    }
  }

  public static void giveItem(ItemStack stack, Inventory inventory, final Integer amount) {
    int remaining = amount;
    final int stacks = (amount > stack.getMaxStackSize())? (int)Math.ceil(amount / stack.getMaxStackSize()) : 1;

    Collection<ItemStack> items = new ArrayList<>();
    for(int i = 0; i < stacks; i++) {
      ItemStack clone = stack.clone();
      if(i == stacks - 1) {
        clone.setAmount(remaining);
      } else {
        clone.setAmount(stack.getMaxStackSize());
      }
      items.add(clone);
      remaining = remaining - clone.getAmount();
    }
    giveItems(items, inventory);
  }

  public static void removeAllItem(ItemStack stack, Inventory inventory) {
    for(int i = 0; i < inventory.getContents().length; i++) {
      final ItemStack item = inventory.getItem(i);

      if(stack != null && MaterialUtils.itemsEqual(stack, item)) inventory.setItem(i, null);
    }
  }

  public static Integer removeItemAmount(ItemStack stack, Inventory inventory, final Integer amount) {
    int left = amount;
    TNE.debug("Amount: " + amount);

    for(int i = 0; i < inventory.getStorageContents().length; i++) {
      if(left <= 0) break;
      ItemStack item = inventory.getItem(i);
      TNE.debug("Null?: " + (item == null));
      if(item == null || !MaterialUtils.itemsEqual(stack, item)) {
        TNE.debug("Skipping item in removeITemAmount due to not equaling.");
        continue;
      }
      TNE.debug("Items Equal, go onwards.");

      if(item.getAmount() <= left) {
        left -= item.getAmount();
        TNE.debug("remove stack from inventory");
        inventory.setItem(i, null);
      } else {
        item.setAmount(item.getAmount() - left);
        inventory.setItem(i, item);
        left = 0;
      }
    }

    if(left > 0 && inventory instanceof PlayerInventory) {
      final ItemStack helmet = ((PlayerInventory) inventory).getHelmet();
      if(helmet != null && helmet.isSimilar(stack)) {
        if(helmet.getAmount() <= left) {
          left -= helmet.getAmount();
          ((PlayerInventory) inventory).setHelmet(null);
        } else {
          helmet.setAmount(helmet.getAmount() - left);
          ((PlayerInventory) inventory).setHelmet(helmet);
          left = 0;
        }
      }

      if(left > 0 && MISCUtils.offHand()) {
        final ItemStack hand = ((PlayerInventory) inventory).getItemInOffHand();

        if(hand != null && hand.isSimilar(stack)) {
          if (hand.getAmount() <= left) {
            left -= hand.getAmount();
            ((PlayerInventory) inventory).setItemInOffHand(null);
          } else {
            hand.setAmount(hand.getAmount() - left);
            ((PlayerInventory) inventory).setItemInOffHand(hand);
            left = 0;
          }
        }
      }
    }
    return left;
  }
}