package net.tnemc.paper;
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
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.tnemc.item.InventoryType;
import net.tnemc.item.SerialItem;
import net.tnemc.item.data.ItemStorageData;
import net.tnemc.item.paper.PaperCalculationsProvider;
import net.tnemc.item.paper.PaperItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * PaperDebugCalculations
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PaperDebugCalculations extends PaperCalculationsProvider {

  protected final boolean debug = true;

  /**
   * Used to drop items near a player.
   *
   * @param left   A Collection containing the items to drop.
   * @param player The UUID of the player to drop the items near.
   *
   * @return True if the items were successfully dropped, otherwise false.
   */
  @Override
  public boolean drop(Collection<PaperItemStack> left, UUID player) {

    final Player playerObj = Bukkit.getPlayer(player);

    if(playerObj != null) {
      for(PaperItemStack stack : left) {
        Objects.requireNonNull(playerObj.getWorld()).dropItemNaturally(playerObj.getLocation(), stack.locale());
      }
    }
    return false;
  }

  /**
   * Removes all items that are equal to the stack from an inventory.
   *
   * @param stack     The stack to compare to for removal from the inventory.
   * @param inventory The inventory to remove the items from.
   */
  @Override
  public int removeAll(PaperItemStack stack, Inventory inventory) {

    final ItemStack compare = stack.locale().clone();
    compare.setAmount(1);

    int amount = 0;
    final PaperItemStack comp = PaperItemStack.locale(compare);

    for(int i = 0; i < inventory.getStorageContents().length; i++) {
      ItemStack item = inventory.getItem(i);
      if(item != null) {
        final PaperItemStack locale = PaperItemStack.locale(item);
        final boolean equal = itemsEqual(comp, locale);

        if(equal) {
          amount += item.getAmount();
          inventory.setItem(i, null);
        } else {
          if(locale.data().isPresent() && locale.data().get() instanceof ItemStorageData) {
            final Iterator<Map.Entry<Integer, SerialItem>> it = ((ItemStorageData)locale.data().get()).getItems().entrySet().iterator();
            while(it.hasNext()) {
              final Map.Entry<Integer, SerialItem> entry = it.next();
              if(itemsEqual(comp, new PaperItemStack().of(entry.getValue()))) {
                amount += entry.getValue().getStack().amount();
                it.remove();
                locale.markDirty();
              }
            }
            inventory.setItem(i, locale.locale());
          }
        }
      }
    }
    return amount;
  }

  /**
   * Returns a count of items equal to the specific stack in an inventory.
   *
   * @param stack     The stack to get a count of.
   * @param inventory The inventory to check.
   *
   * @return The total count of items in the inventory.
   */
  @Override
  public int count(PaperItemStack stack, Inventory inventory) {

    final ItemStack compare = stack.locale().clone();
    compare.setAmount(1);

    final PaperItemStack comp = PaperItemStack.locale(compare).debug(true);
    int amount = 0;

    for(ItemStack itemStack : inventory.getStorageContents()) {
      if(itemStack != null) {
        final PaperItemStack locale = PaperItemStack.locale(itemStack).debug(true);


        final boolean equal = itemsEqual(comp, locale);
        if(stack.debug()) {
          System.out.println("Comp Material: " + comp.material() + " Locale: " + locale.material());

          System.out.println("Displays: " + Component.EQUALS.test(comp.display(), locale.display()));

          System.out.println("Comp: " + comp.display().toString());
          System.out.println("Locale: " + locale.display().toString());
          System.out.println("Locale: " + LegacyComponentSerializer.legacySection().serialize(locale.display()));
          System.out.println("Locale: " + JSONComponentSerializer.json().serialize(locale.display()));
          System.out.println("Locale: " + PlainTextComponentSerializer.plainText().serialize(locale.display()));


          System.out.println("Lore List");
          for(Component compObj : comp.lore()) {
            System.out.println("Lore Legacy: " + LegacyComponentSerializer.legacySection().serialize(compObj));
            System.out.println("Lore JSON: " + JSONComponentSerializer.json().serialize(compObj));
            System.out.println("Lore Plain: " + PlainTextComponentSerializer.plainText().serialize(compObj));
          }

          System.out.println("Locale Lore List");
          for(Component compObj : locale.lore()) {
            System.out.println("Lore Legacy: " + LegacyComponentSerializer.legacySection().serialize(compObj));
            System.out.println("Lore JSON: " + JSONComponentSerializer.json().serialize(compObj));
            System.out.println("Lore Plain: " + PlainTextComponentSerializer.plainText().serialize(compObj));
          }

          System.out.println("Equal: " + equal);
        }

        if(locale.data().isPresent()) {
          if(locale.data().get() instanceof ItemStorageData) {
            for(Object obj : ((ItemStorageData)locale.data().get()).getItems().entrySet()) {
              final Map.Entry<Integer, SerialItem> entry = ((Map.Entry<Integer, SerialItem>)obj);
              if(itemsEqual(comp, new PaperItemStack().of(entry.getValue()))) {
                amount += entry.getValue().getStack().amount();
              }
            }
          }
        }

        if(equal) {
          amount += itemStack.getAmount();
        }
      }
    }
    return amount;
  }

  /**
   * Takes a collection of items from an inventory.
   *
   * @param items     The collection of items to remove.
   * @param inventory The inventory to remove the items from.
   */
  public void takeItems(Collection<PaperItemStack> items, Inventory inventory) {

    items.forEach(itemStack->removeItem(itemStack, inventory));
  }

  /**
   * Adds a collection of net.tnemc.item stacks to an inventory, dropping them on the ground if it's
   * a player inventory and overflow exists.
   *
   * @param items     The collection of items to add to the inventory.
   * @param inventory The inventory to add the collection of items to.
   */
  public Collection<PaperItemStack> giveItems(Collection<PaperItemStack> items, Inventory inventory) {

    final Collection<PaperItemStack> leftOver = new ArrayList<>();

    for(PaperItemStack item : items) {
      final Map<Integer, ItemStack> left = inventory.addItem(item.locale());

      if(!left.isEmpty()) {
        for(Map.Entry<Integer, ItemStack> entry : left.entrySet()) {
          final ItemStack i = entry.getValue();
          if(i == null || i.getType() == Material.AIR) {
            continue;
          }
          leftOver.add(PaperItemStack.locale(i));
        }
      }
    }
    return leftOver;
  }

  /**
   * Removes an ItemStack with a specific amount from an inventory.
   *
   * @param stack     The stack, with the correct amount, to remove.
   * @param inventory The inventory to return the net.tnemc.item stack from.
   *
   * @return The remaining amount of items to remove.
   */
  @Override
  public int removeItem(PaperItemStack stack, Inventory inventory) {

    int left = stack.locale().clone().getAmount();

    final ItemStack compare = stack.locale().clone();
    compare.setAmount(1);

    final PaperItemStack comp = PaperItemStack.locale(compare);

    for(int i = 0; i < inventory.getStorageContents().length; i++) {
      if(left <= 0) break;
      final ItemStack item = inventory.getItem(i);

      if(item == null) continue;

      final PaperItemStack itemLocale = PaperItemStack.locale(item);

      if(item.isSimilar(stack.locale())) {
        if(item.getAmount() <= left) {
          left -= item.getAmount();
          inventory.setItem(i, null);
        } else {
          item.setAmount(item.getAmount() - left);
          inventory.setItem(i, item);
          left = 0;
        }
      } else {
        if(itemLocale.data().isPresent() && itemLocale.data().get() instanceof ItemStorageData) {

          final Iterator<Map.Entry<Integer, SerialItem>> it = ((ItemStorageData)itemLocale.data().get()).getItems().entrySet().iterator();
          while(it.hasNext()) {
            if(left <= 0) break;
            final Map.Entry<Integer, SerialItem> entry = it.next();
            if(itemsEqual(comp, new PaperItemStack().of(entry.getValue()))) {
              if(entry.getValue().getStack().amount() <= left) {
                left -= entry.getValue().getStack().amount();
                it.remove();
              } else {
                entry.getValue().getStack().setAmount(entry.getValue().getStack().amount() - left);
                left = 0;
              }
              itemLocale.markDirty();
            }
          }
          inventory.setItem(i, itemLocale.locale());
        }
      }
    }
    return left;
  }

  /**
   * Used to locate an inventory for a UUID identifier.
   *
   * @param identifier The identifier to use for the search.
   * @param type       The inventory type to return.
   *
   * @return An optional containing the inventory if it works, otherwise false.
   */
  @Override
  public Optional<Inventory> getInventory(UUID identifier, InventoryType type) {

    final OfflinePlayer player = Bukkit.getOfflinePlayer(identifier);
    if(player.isOnline() && player.getPlayer() != null) {

      if(type.equals(InventoryType.ENDER_CHEST)) {
        return Optional.of(player.getPlayer().getEnderChest());
      } else {
        return Optional.of(player.getPlayer().getInventory());
      }
    }
    return Optional.empty();
  }
}
