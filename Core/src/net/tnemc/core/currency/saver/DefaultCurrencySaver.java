package net.tnemc.core.currency.saver;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyRegion;
import net.tnemc.core.currency.CurrencySaver;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.Note;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.plugincore.PluginCore;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * DefaultCurrencySaver
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class DefaultCurrencySaver implements CurrencySaver {

  /**
   * Saves all currencies.
   * @param directory The directory used for saving.
   */
  @Override
  public void saveCurrencies(final File directory) {

    for(final Currency currency : TNECore.eco().currency().currencies()) {
      saveCurrency(new File(directory, currency.getFile()), currency);
    }
  }

  /**
   * Saves all currency UUIDs only.
   *
   * @param directory The directory used for saving.
   */
  @Override
  public void saveCurrenciesUUID(File directory) {
    for(final Currency currency : TNECore.eco().currency().currencies()) {
      saveID(directory, currency);
    }
  }

  /**
   * Saves a specific currency
   *
   * @param directory The directory used for saving.
   * @param currency The currency to save.
   */
  @Override
  public void saveCurrency(final File directory, Currency currency) {

    final YamlFile cur = new YamlFile(directory);

    try {
      cur.createOrLoadWithComments();

    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency: " + currency.getIdentifier());
      e.printStackTrace();
      return;
    }

    PluginCore.log().inform("Saving Currency: " + currency.getIdentifier());

    //Currency Info configs.
    cur.setComment("Info.Server", "The server this currency belongs to, for data saving purposes.");
    cur.set("Info.Server", "Main Server");
    cur.setComment("Info.Identifier", "The identifier of this currency used for various commands.");
    cur.set("Info.Identifier", currency.getIdentifier());
    cur.setComment("Info.UUIDAsId", "Whether to use the Identifier config as the currency's UUID. Not Recommended. Can lead to issues in the future");
    cur.set("Info.UUIDAsId", false);
    cur.setComment("Info.Icon", "The material name to use as the icon for this currency in the plugin menus.");
    cur.set("Info.Icon", currency.getIconMaterial());
    cur.setComment("Info.Major_Single", "The singular form of this currency's major denomination.");
    cur.set("Info.Major_Single", currency.getDisplay());
    cur.set("Info.Major_Plural", currency.getDisplayPlural());
    cur.set("Info.Minor_Single", currency.getDisplayMinor());
    cur.set("Info.Minor_Plural", currency.getDisplayMinorPlural());
    cur.set("Info.Symbol", currency.getSymbol());
    cur.set("Info.UUID", currency.getUid().toString());

    //World Configurations
    cur.setComment("Options.MultiRegion", "Configurations relating to multi region support.");
    cur.setComment("Options.MultiRegion.Regions", "Configurations relating to regions this currency is enabled in.");
    for(CurrencyRegion region : currency.getRegions().values()) {
      if(region.region().equalsIgnoreCase("global")) {
        cur.setComment("Options.Global", "Configurations relating to global configurations for this currency.");
        cur.setComment("Options.Global.Enabled", "Should this currency be global?(i.e. usable in every world)");
        cur.set("Options.Global.Enabled", region.isEnabled());
        cur.setComment("Options.Global.Default", "Should this currency be the global default?");
        cur.set("Options.Global.Default", region.isDefault());
        continue;
      }
      cur.set("Options.MultiRegion.Regions." + region.region() + ".Enabled", region.isEnabled());
      cur.set("Options.MultiRegion.Regions." + region.region() + ".Default", region.isDefault());
    }
    //TODO: Finish filling out comments
    //Currency Options configs.
    cur.set("Options.Disabled", false);
    cur.set("Options.MaxBalance", currency.getMaxBalance().toPlainString());
    cur.set("Options.MinBalance", currency.getMinBalance().toPlainString());
    cur.set("Options.Balance", currency.getStartingHoldings().toPlainString());
    cur.set("Options.Decimal", currency.getDecimal());
    cur.set("Options.DecimalPlaces", currency.getDecimalPlaces());
    cur.set("Options.Type", currency.getType());
    cur.set("Options.Minor_Weight", currency.getMinorWeight());

    //Load our item-back currency configurations.
    if(currency instanceof ItemCurrency item) {
      cur.setComment("Item", "All configurations relating to item-backed currencies");
      cur.setComment("Item.EnderChest", "Would you like your item currency balances to also check the player's ender chest?");
      cur.set("Item.EnderChest", item.canEnderChest());
      cur.setComment("Item.EnderFill", "Would you like your item currency items to go into the ender chest before dropping on the ground if inventory is full?");
      cur.set("Item.EnderFill", item.isEnderFill());
    }

    //Formatting Configurations
    cur.setComment("Formatting", "All configurations related to formatting.");
    cur.setComment("Formatting.Format", "The format to use when outputting this currency into chat.");
    cur.set("Formatting.Format", currency.getFormat());
    cur.setComment("Formatting.Prefixes", "The SI Prefixes used when <shorten> is used for the currency's format.");
    cur.set("Formatting.Prefixes", currency.getPrefixes());
    cur.setComment("Formatting.Major_Separate", "Whether the major value should be separated every three numeric places.");
    cur.set("Formatting.Major_Separate", currency.isSeparateMajor());
    cur.setComment("Formatting.Major_Separator", "The separator to use for numeric separation.");
    cur.set("Formatting.Major_Separator", currency.getMajorSeparator());

    //Load our note configurations.
    final Optional<Note> note = currency.getNote();

    cur.setComment("Note", "All configurations relating to currency notes.");
    cur.setComment("Note.Notable", "Whether this currency is able to be noted using the note command");
    cur.set("Note.Notable", note.isPresent());
    if(note.isPresent()) {
      cur.setComment("Note.Fee", "The fee to note this currency.");
      cur.set("Note.Fee", note.get().getFee().asString());
      cur.setComment("Note.Minimum", "The minimum amount required to create a note for this currency.");
      cur.set("Note.Minimum", note.get().getMinimum().toPlainString());
      cur.setComment("Note.Item", "Configurations relating to the note item.");
      cur.setComment("Note.Item.Material", "The material to use.");
      cur.set("Note.Item.Material", note.get().getMaterial());
      cur.setComment("Note.Item.ModelData", "The custom model data value used for this item. Defaults to 0. Optional");
      cur.set("Note.Item.ModelData", note.get().getCustomModelData());
      cur.setComment("Note.Item.Texture", "The base64 texture to use if the material is PLAYER_HEAD");
      cur.set("Note.Item.Texture", note.get().getTexture());
      cur.setComment("Note.Item.Item.Enchantments", "All configurations relating to enchantment identification for the note item");
      cur.set("Note.Item.Item.Enchantments", note.get().getEnchantments());
      cur.setComment("Note.Item.Item.Flags", "All configurations relating to item flags identification for the note item");
      cur.set("Note.Item.Item.Flags", note.get().getFlags());
    }

    //Conversion
    if(!currency.getConversion().isEmpty()) {
      for(Map.Entry<String, Double> entry : currency.getConversion().entrySet()) {
        cur.setComment("Converting." + entry.getKey(), "Format is currency name and decimal based rate");
        cur.set("Converting." + entry.getKey(), entry.getValue());
      }
    }

    try {
      cur.save();
    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency: " + currency.getIdentifier());
      e.printStackTrace();
    }

    final File curDirectory = new File(directory.getParentFile(), currency.getIdentifier());
    if(!curDirectory.exists()) {
      try {

        curDirectory.mkdirs();
      } catch(Exception e) {

        PluginCore.log().error("Failed to save currency: " + currency.getIdentifier());
        e.printStackTrace();
        return;
      }
    }

    for(final Denomination denomination : currency.getDenominations().values()) {
      saveDenomination(curDirectory, currency, denomination);
    }
  }

  public void saveID(final File directory, Currency currency) {

    final YamlFile cur = new YamlFile(new File(directory, currency.getIdentifier() + ".yml"));

    try {
      cur.createOrLoadWithComments();

    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency: " + currency.getIdentifier());
      e.printStackTrace();
      return;
    }

    if(!cur.contains("Info.UUID")) {
      cur.setComment("Info.UUID", "Used for data saving. Do NOT modify.");
    }

    cur.set("Info.UUID", currency.getUid().toString());

    try {
      cur.save();
    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency: " + currency.getIdentifier());
      e.printStackTrace();
    }
  }

  /**
   * Saves a specific currency denomination
   *
   * @param directory The directory used for saving.
   * @param currency     The currency of the denomination.
   * @param denomination The denomination to save.
   */
  @Override
  public void saveDenomination(final File directory, Currency currency, Denomination denomination) {
    final YamlFile denom = new YamlFile(new File(directory, denomination.singular() + ".yml"));
    try {

      denom.createOrLoadWithComments();
    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency denomination: " + denomination.singular());
      e.printStackTrace();
      return;
    }

    PluginCore.log().inform("Attempting to save currency denomination: " + denomination.singular());

    denom.set("Info.Single", denomination.singular());
    denom.set("Info.Plural", denomination.plural());

    denom.set("Options.Weight", denomination.weight());

    if(denomination instanceof ItemDenomination) {

      denom.set("Options.Material", ((ItemDenomination)denomination).getMaterial());

      if(((ItemDenomination)denomination).getName() != null) {
        denom.set("Options.Name", ((ItemDenomination)denomination).getName());
      }

      if(((ItemDenomination)denomination).getLore().size() > 0) {
        denom.set("Options.Lore", ((ItemDenomination)denomination).getLore());
      }

      denom.set("Options.ModelData", ((ItemDenomination)denomination).getCustomModel());

      if(((ItemDenomination)denomination).getTexture() != null) {
        denom.set("Options.Texture", ((ItemDenomination)denomination).getTexture());
      }

      if(((ItemDenomination)denomination).getEnchantments().size() > 0) {
        denom.set("Options.Enchantments", ((ItemDenomination)denomination).getEnchantments());
      }

      if(((ItemDenomination)denomination).getFlags().size() > 0) {
        denom.set("Options.Flags", ((ItemDenomination)denomination).getFlags());
      }
    }

    try {
      denom.save();
    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency denomination: " + denomination.singular());
      e.printStackTrace();
    }
  }
}