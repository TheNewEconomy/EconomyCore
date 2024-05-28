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
    cur.set("Info.Icon", currency.getIconMaterial());
    cur.set("Info.Major_Single", currency.getDisplay());
    cur.set("Info.Major_Plural", currency.getDisplayPlural());
    cur.set("Info.Minor_Single", currency.getDisplayMinor());
    cur.set("Info.Minor_Plural", currency.getDisplayMinorPlural());
    cur.set("Info.Symbol", currency.getSymbol());

    //Currency Options configs.
    cur.set("Options.MaxBalance", currency.getMaxBalance().toPlainString());
    cur.set("Options.MinBalance", currency.getMinBalance().toPlainString());
    cur.set("Options.Balance", currency.getStartingHoldings().toPlainString());
    cur.set("Options.Decimal", currency.getDecimal());
    cur.set("Options.DecimalPlaces", currency.getDecimalPlaces());
    cur.set("Options.Type", currency.getType());
    cur.set("Options.Minor_Weight", currency.getMinorWeight());

    //Formatting Configurations
    cur.set("Formatting.Format", currency.getFormat());
    cur.set("Formatting.Prefixes", currency.getPrefixes());
    cur.set("Formatting.Major_Separate", currency.isSeparateMajor());
    cur.set("Formatting.Major_Separator", currency.getMajorSeparator());

    //World Configurations
    for(CurrencyRegion region : currency.getRegions().values()) {
      if(region.region().equalsIgnoreCase("global")) {
        cur.set("Options.Global.Enabled", region.isEnabled());
        cur.set("Options.Global.Default", region.isDefault());
        continue;
      }
      cur.set("Options.MultiRegion.Regions." + region.region() + ".Enabled", region.isEnabled());
      cur.set("Options.MultiRegion.Regions." + region.region() + ".Default", region.isDefault());
    }

    //Conversion
    if(currency.getConversion().size() > 0) {
      for(Map.Entry<String, Double> entry : currency.getConversion().entrySet()) {
        cur.set("Converting." + entry.getKey(), entry.getValue());
      }
    }

    //Load our item-back currency configurations.
    if(currency instanceof ItemCurrency item) {
      cur.set("Item.EnderChest", item.canEnderChest());
      cur.set("Item.EnderFill", item.isEnderFill());
    }

    //Load our note configurations.
    final Optional<Note> note = currency.getNote();
    if(note.isPresent()) {

      cur.set("Note.Fee", note.get().getFee().asString());
      cur.set("Note.Minimum", note.get().getMinimum().toPlainString());
      cur.set("Note.Item.Material", note.get().getMaterial());
      cur.set("Note.Item.Texture", note.get().getTexture());
      cur.set("Note.Item.ModelData", note.get().getCustomModelData());
      cur.set("Note.Item.Item.Enchantments", note.get().getEnchantments());
      cur.set("Note.Item.Item.Flags", note.get().getFlags());
    }

    try {
      cur.save();
    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency: " + currency.getIdentifier());
      e.printStackTrace();
    }

    for(final Denomination denomination : currency.getDenominations().values()) {
      saveDenomination(new File(directory, currency.getIdentifier()), currency, denomination);
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
    final YamlFile denom = new YamlFile(directory);
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