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

import dev.dejvokep.boostedyaml.YamlDocument;
import net.tnemc.core.TNECore;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyRegion;
import net.tnemc.core.currency.CurrencySaver;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.Note;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.utils.MISCUtils;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;

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

    YamlDocument cur = null;

    try {
      cur = YamlDocument.create(directory);

    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency: " + currency.getIdentifier(), e, DebugLevel.STANDARD);
      return;
    }

    PluginCore.log().inform("Saving Currency: " + currency.getIdentifier());

    //Currency Info configs.
    MISCUtils.setComment(cur, "Info.Server", "The server this currency belongs to, for data saving purposes.");
    cur.set("Info.Server", MainConfig.yaml().getString("Core.Server.Name"));

    MISCUtils.setComment(cur, "Info.Identifier", "The identifier of this currency used for various commands.");
    cur.set("Info.Identifier", currency.getIdentifier());

    MISCUtils.setComment(cur, "Info.UUIDAsId", "Whether to use the Identifier config as the currency's UUID. Not Recommended. Can lead to issues in the future");
    cur.set("Info.UUIDAsId", false);

    MISCUtils.setComment(cur, "Info.Icon", "The material name to use as the icon for this currency in the plugin menus.");
    cur.set("Info.Icon", currency.getIconMaterial());

    MISCUtils.setComment(cur, "Info.Major_Single", "The singular form of this currency's major denomination.");
    cur.set("Info.Major_Single", currency.getDisplay());

    MISCUtils.setComment(cur, "Info.Major_Plural", "The plural form of this currency's major denomination.");
    cur.set("Info.Major_Plural", currency.getDisplayPlural());

    MISCUtils.setComment(cur, "Info.Minor_Single", "The singular form of this currency's minor denomination.");
    cur.set("Info.Minor_Single", currency.getDisplayMinor());

    MISCUtils.setComment(cur, "Info.Minor_Plural", "The plural form of this currency's minor denomination.");
    cur.set("Info.Minor_Plural", currency.getDisplayMinorPlural());

    MISCUtils.setComment(cur, "Info.Symbol", "The character to use as the symbol for this currency.");
    cur.set("Info.Symbol", currency.getSymbol());

    MISCUtils.setComment(cur, "Info.UUID", "Used for data saving. Do NOT modify.");
    cur.set("Info.UUID", currency.getUid().toString());

    //World Configurations
    MISCUtils.setComment(cur, "Options.MultiRegion", "Configurations relating to multi region support.");
    MISCUtils.setComment(cur, "Options.MultiRegion.Regions", "Configurations relating to regions this currency is enabled in.");
    for(CurrencyRegion region : currency.getRegions().values()) {

      if(region.region().equalsIgnoreCase("global")) {

        MISCUtils.setComment(cur, "Options.Global", "Configurations relating to global configurations for this currency.");
        MISCUtils.setComment(cur, "Options.Global.Enabled", "Should this currency be global?(i.e. usable in every world)");
        cur.set("Options.Global.Enabled", region.isEnabled());

        MISCUtils.setComment(cur, "Options.Global.Default", "Should this currency be the global default?");
        cur.set("Options.Global.Default", region.isDefault());
        continue;
      }
      MISCUtils.setComment(cur, "Options.MultiRegion.Regions." + region.region() + ".Enabled", "Should this currency be enabled in this world?");
      cur.set("Options.MultiRegion.Regions." + region.region() + ".Enabled", region.isEnabled());

      MISCUtils.setComment(cur, "Options.MultiRegion.Regions." + region.region() + ".Default", "Should this currency be the default in this world?");
      cur.set("Options.MultiRegion.Regions." + region.region() + ".Default", region.isDefault());
    }

    MISCUtils.setComment(cur, "Options.Disabled", "Whether this currency is enabled.");
    cur.set("Options.Disabled", false);

    MISCUtils.setComment(cur, "Options.MaxBalance", "The maximum balance possible for this currency.");
    cur.set("Options.MaxBalance", currency.getMaxBalance().toPlainString());

    MISCUtils.setComment(cur, "Options.MinBalance", "The minimum balance possible for this currency.");
    cur.set("Options.MinBalance", currency.getMinBalance().toPlainString());

    MISCUtils.setComment(cur, "Options.Balance", "The initial balance for accounts.");
    cur.set("Options.Balance", currency.getStartingHoldings().toPlainString());

    MISCUtils.setComment(cur, "Options.Decimal", "The character to use as the decimal placeholder.");
    cur.set("Options.Decimal", currency.getDecimal());

    MISCUtils.setComment(cur, "Options.DecimalPlaces", "The amount of digits to display after the decimal character.(Max 4)");
    cur.set("Options.DecimalPlaces", currency.getDecimalPlaces());

    MISCUtils.setComment(cur, "Options.Type", "The currency type to use for this currency. Valid Types: virtual, item, experience, mixed");
    cur.set("Options.Type", currency.getType());

    MISCUtils.setComment(cur, "Options.Minor_Weight", "This is used to determine how many of minor it takes to make one major");
    cur.set("Options.Minor_Weight", currency.getMinorWeight());

    //Load our item-back currency configurations.
    if(currency instanceof ItemCurrency item) {

      MISCUtils.setComment(cur, "Item", "All configurations relating to item-backed currencies");
      MISCUtils.setComment(cur, "Item.EnderChest", "Would you like your item currency balances to also check the player's ender chest?");
      cur.set("Item.EnderChest", item.canEnderChest());

      MISCUtils.setComment(cur, "Item.EnderFill", "Would you like your item currency items to go into the ender chest before dropping on the ground if inventory is full?");
      cur.set("Item.EnderFill", item.isEnderFill());

      MISCUtils.setComment(cur, "Item.ImportItems", "Whether to import exist item currencies into a player's balance with the first balance check for this currency.");
      cur.set("Item.ImportItems", item.isImportItem());
    }

    //Formatting Configurations
    MISCUtils.setComment(cur, "Formatting", "All configurations related to formatting.");
    MISCUtils.setComment(cur, "Formatting.Format", "The format to use when outputting this currency into chat.");
    cur.set("Formatting.Format", currency.getFormat());

    MISCUtils.setComment(cur, "Formatting.Prefixes", "The SI Prefixes used when <shorten> is used for the currency's format.");
    cur.set("Formatting.Prefixes", currency.getPrefixes());

    MISCUtils.setComment(cur, "Formatting.Major_Separate", "Whether the major value should be separated every three numeric places.");
    cur.set("Formatting.Major_Separate", currency.isSeparateMajor());

    MISCUtils.setComment(cur, "Formatting.Major_Separator", "The separator to use for numeric separation.");
    cur.set("Formatting.Major_Separator", currency.getMajorSeparator());

    MISCUtils.setComment(cur, "Formatting.Balance", "Should this currency be shown in the balance commands.");
    cur.set("Formatting.Balance", currency.isBalanceShow());

    //Load our note configurations.
    final Optional<Note> note = currency.getNote();

    MISCUtils.setComment(cur, "Note", "All configurations relating to currency notes.");
    MISCUtils.setComment(cur, "Note.Notable", "Whether this currency is able to be noted using the note command");
    cur.set("Note.Notable", note.isPresent());
    if(note.isPresent()) {

      MISCUtils.setComment(cur, "Note.Fee", "The fee to note this currency.");
      cur.set("Note.Fee", note.get().getFee().asString());

      MISCUtils.setComment(cur, "Note.Minimum", "The minimum amount required to create a note for this currency.");
      cur.set("Note.Minimum", note.get().getMinimum().toPlainString());

      MISCUtils.setComment(cur, "Note.Item", "Configurations relating to the note item.");
      MISCUtils.setComment(cur, "Note.Item.Material", "The material to use.");
      cur.set("Note.Item.Material", note.get().getMaterial());

      MISCUtils.setComment(cur, "Note.Item.ModelData", "The custom model data value used for this item. Defaults to 0. Optional");
      cur.set("Note.Item.ModelData", note.get().getCustomModelData());

      MISCUtils.setComment(cur, "Note.Item.Texture", "The base64 texture to use if the material is PLAYER_HEAD");
      cur.set("Note.Item.Texture", note.get().getTexture());

      MISCUtils.setComment(cur, "Note.Item.Item.Enchantments", "All configurations relating to enchantment identification for the note item");
      cur.set("Note.Item.Item.Enchantments", note.get().getEnchantments());

      MISCUtils.setComment(cur, "Note.Item.Item.Flags", "All configurations relating to item flags identification for the note item");
      cur.set("Note.Item.Item.Flags", note.get().getFlags());
    }

    //Conversion
    if(!currency.getConversion().isEmpty()) {
      MISCUtils.setComment(cur, "Converting", "Format is currency name and decimal based rate");

      for(Map.Entry<String, Double> entry : currency.getConversion().entrySet()) {
        cur.set("Converting." + entry.getKey(), entry.getValue());
      }
    }

    try {
      cur.save();
    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency: " + currency.getIdentifier(), e, DebugLevel.STANDARD);
    }

    final File curDirectory = new File(directory.getParentFile(), currency.getIdentifier());
    if(!curDirectory.exists()) {
      try {

        if(!curDirectory.mkdirs()) {

          PluginCore.log().error("Failed to save currency: " + currency.getIdentifier(), DebugLevel.STANDARD);
          return;
        }
      } catch(Exception e) {

        PluginCore.log().error("Failed to save currency: " + currency.getIdentifier(), e, DebugLevel.STANDARD);
        return;
      }
    }

    for(final Denomination denomination : currency.getDenominations().values()) {
      saveDenomination(curDirectory, currency, denomination);
    }
  }

  public void saveID(final File directory, Currency currency) {

    YamlDocument cur = null;

    try {
      cur = YamlDocument.create(new File(directory, currency.getIdentifier() + ".yml"));

    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency: " + currency.getIdentifier(), e, DebugLevel.STANDARD);
      return;
    }

    if(!cur.contains("Info.UUID")) {
      MISCUtils.setComment(cur, "Info.UUID", "Used for data saving. Do NOT modify.");
    }

    cur.set("Info.UUID", currency.getUid().toString());

    try {
      cur.save();
    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency: " + currency.getIdentifier(), e, DebugLevel.STANDARD);
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
    YamlDocument denom = null;
    try {

      denom = YamlDocument.create(new File(directory, denomination.singular() + ".yml"));
    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency denomination: " + denomination.singular(), e, DebugLevel.STANDARD);
      return;
    }

    PluginCore.log().inform("Attempting to save currency denomination: " + denomination.singular());

    MISCUtils.setComment(denom, "Info", "Configurations relating to basic information about this currency tier.");

    MISCUtils.setComment(denom, "Info.Single", "The singular name of this tier.");
    denom.set("Info.Single", denomination.singular());

    MISCUtils.setComment(denom, "Info.Plural", "The plural name of this tier.");
    denom.set("Info.Plural", denomination.plural());

    MISCUtils.setComment(denom, "Options", "Various options for this currency tier.");

    MISCUtils.setComment(denom, "Options.Weight", "The weight of the tier. E.X. 20USD would equal 20");
    denom.set("Options.Weight", denomination.weight().doubleValue());

    if(denomination instanceof ItemDenomination itemDenomination) {

      MISCUtils.setComment(denom, "Options.Material", "The material used for this item.");
      denom.set("Options.Material", itemDenomination.getMaterial());

      if(itemDenomination.getTexture() != null) {
        MISCUtils.setComment(denom, "Options.Texture", "The base64 texture to use if the material is PLAYER_HEAD");
        denom.set("Options.Texture", itemDenomination.getTexture());
      }

      if(itemDenomination.getDamage() > 0) {
        MISCUtils.setComment(denom, "Options.Damage", "The damage value used for this item. Defaults to 0.(Optional)");
        denom.set("Options.Damage", itemDenomination.getDamage());
      }

      if(itemDenomination.getName() != null) {
        MISCUtils.setComment(denom, "Options.Name", "The custom name this item must have in order to be considered currency.(Optional)");
        denom.set("Options.Name", itemDenomination.getName());
      }

      if(!itemDenomination.getLore().isEmpty()) {
        MISCUtils.setComment(denom, "Options.Lore", "The lore string this item must have  in order to be considered currency.(Optional)");
        denom.set("Options.Lore", itemDenomination.getLoreAsString());
      }

      if(itemDenomination.getCustomModel() > -1) {
        MISCUtils.setComment(denom, "Options.ModelData", "The custom model data value used for this item. Defaults to 0.(Optional)");
        denom.set("Options.ModelData", itemDenomination.getCustomModel());
      }

      if(!itemDenomination.getEnchantments().isEmpty()) {
        MISCUtils.setComment(denom, "Options.Enchantments", "All configurations relating to enchantment identification for currency tiers.(Optional)");
        denom.set("Options.Enchantments", itemDenomination.getEnchantments());
      }

      if(!itemDenomination.getFlags().isEmpty()) {
        MISCUtils.setComment(denom, "Options.Flags", "All configurations relating to item flags identification for currency tiers.(Optional)");
        denom.set("Options.Flags", itemDenomination.getFlags());
      }

      //TODO: Crafting stuff
    }

    try {
      denom.save();
    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency denomination: " + denomination.singular(), e, DebugLevel.STANDARD);
    }
  }
}