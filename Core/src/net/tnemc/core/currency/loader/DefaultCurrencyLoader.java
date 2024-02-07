package net.tnemc.core.currency.loader;

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
import net.tnemc.core.api.callback.currency.CurrencyLoadCallback;
import net.tnemc.core.api.callback.currency.DenominationLoadCallback;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyLoader;
import net.tnemc.core.currency.CurrencyRegion;
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.currency.Denomination;
import net.tnemc.core.currency.Note;
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.utils.exceptions.NoValidCurrenciesException;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.helper.CraftingRecipe;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.utils.IOUtil;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static net.tnemc.core.manager.CurrencyManager.largestSupported;

/**
 * DefaultLoader
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class DefaultCurrencyLoader implements CurrencyLoader {


  /**
   * Loads all currencies.
   * @param directory The directory to load the currencies from.
   * @throws NoValidCurrenciesException When no valid currencies can be loaded.
   */
  @Override
  public void loadCurrencies(final File directory) throws NoValidCurrenciesException {

    if(directory.exists()) {
      final File[] currencies = IOUtil.getYAMLs(directory);
      if(currencies.length > 0) {

        for(File curFile : currencies) {

          final boolean loaded = loadCurrency(directory, curFile);

          if(!loaded) {
            PluginCore.log().error("Failed to load currency: " + curFile, DebugLevel.OFF);
          }
        }
        return;
      }
    }
    PluginCore.log().error("There are no currencies to load.", DebugLevel.OFF);
    throw new NoValidCurrenciesException();
  }

  /**
   * Loads a specific currency.
   *
   * @param directory The directory to load the currency from.
   * @param name The name of the currency to load.
   */
  @Override
  public boolean loadCurrency(final File directory, String name) {
    return loadCurrency(directory, new File(directory, name + ".yml"));
  }

  /**
   * Loads a specific currency.
   *
   * @param directory The directory to load the currency from.
   * @param curDirectory The file of the currency
   */
  @Override
  public boolean loadCurrency(final File directory, File curDirectory) {

    final YamlFile cur = new YamlFile(curDirectory);

    PluginCore.log().inform("Loading: " + curDirectory.getName());

    try {
      cur.loadWithComments();

    } catch(IOException e) {
      PluginCore.log().error("Failed to load currency: " + cur.getName(), DebugLevel.OFF);
      e.printStackTrace();
      return false;
    }

    if(cur.getBoolean("Options.Disabled", false)) {
      PluginCore.log().inform("Currency wasn't loaded as it's disabled: " + cur.getName());
      return false;
    }

    PluginCore.log().inform("Attempting to load currency: " + curDirectory.getName());

    //Currency Info configs.
    final String identifier = cur.getString("Info.Identifier", "Dollar");
    final String icon = cur.getString("Info.Icon", "PAPER");
    final String single = cur.getString("Info.Major_Single", "Dollar");
    final String plural = cur.getString("Info.Major_Plural", "Dollars");
    final String singleMinor = cur.getString("Info.Minor_Single", "Cent");
    final String pluralMinor = cur.getString("Info.Minor_Plural", "Cents");
    final String symbol = cur.getString("Info.Symbol", "$");

    //Currency Options configs.
    final String decimal = cur.getString("Options.Decimal", ".");
    final int decimalPlaces = (Math.min(cur.getInt("Options.DecimalPlaces", 2), 4));
    final String currencyType = cur.getString("Options.Type", "virtual");
    final int minorWeight = cur.getInt("Options.Minor_Weight", 100);

    //Formatting Configurations
    final String format = cur.getString("Formatting.Format", "<symbol><major.amount><decimal><minor.amount>").trim();
    final String prefixes = cur.getString("Formatting.Prefixes", "kMGTPEZYXWVUN₮").trim();
    final boolean separate = cur.getBoolean("Formatting.Major_Separate", true);
    final String separator = cur.getString("Formatting.Major_Separator", ",");

    Optional<CurrencyType> type = TNECore.eco().currency().findType(currencyType);

    if(type.isEmpty()) {
      type = TNECore.eco().currency().findType("virtual");
    }

    Currency currency = (type.get().supportsItems())? new ItemCurrency() : new Currency();

    currency.setFile(curDirectory.getName());

    final BigDecimal maxBalance = ((new BigDecimal(cur.getString("Options.MaxBalance", largestSupported.toPlainString())).compareTo(largestSupported) > 0)? largestSupported : new BigDecimal(cur.getString("MaxBalance", largestSupported.toPlainString())));
    final BigDecimal minBalance = (type.get().supportsItems())? BigDecimal.ZERO : new BigDecimal(cur.getString("Options.MinBalance", "0.00"));
    final BigDecimal balance = new BigDecimal(cur.getString("Options.Balance", "200.00"));

    //Added in build 28, needs removed by build 32.
    boolean uuidAsId = false;
    if(!cur.contains("Info.UUIDAsId")) {
      cur.set("Info.UUIDAsId", false);
      cur.setComment("Info.UUIDAsId", "#Whether to use the Identifier config as the currency's UUID. Not Recommended. Can lead to issues in the future");
    } else {
      uuidAsId = cur.getBoolean("Info.UUIDAsId");
    }


    final UUID check = UUID.nameUUIDFromBytes(identifier.getBytes(StandardCharsets.UTF_8));
    if(cur.contains("Info.UUID")) {

      final UUID id = (uuidAsId && !cur.getString("Info.UUID").equalsIgnoreCase(check.toString()))? check : UUID.fromString(cur.getString("Info.UUID"));


      final Optional<Currency> curOption = TNECore.eco().currency().findCurrency(id);
      if(curOption.isEmpty()) {
        currency.setUid(UUID.fromString(cur.getString("Info.UUID")));
      }
    } else {
      if(uuidAsId) {
        currency.setUid(check);
      }
    }

    currency.setIdentifier(identifier);
    currency.setIconMaterial(icon);
    currency.setMaxBalance(maxBalance);
    currency.setMinBalance(minBalance);
    currency.setStartingHoldings(balance);
    currency.setDecimal(decimal);
    currency.setDecimalPlaces(decimalPlaces);
    currency.setFormat(format);
    currency.setPrefixes(prefixes);
    currency.setDisplay(single);
    currency.setDisplayPlural(plural);
    currency.setDisplayMinor(singleMinor);
    currency.setDisplayPlural(pluralMinor);
    currency.setSymbol(symbol);
    currency.setType(type.get().name());
    currency.setSeparateMajor(separate);
    currency.setMajorSeparator(separator);
    currency.setMinorWeight(minorWeight);

    final boolean global = cur.getBoolean("Options.Global.Enabled", true);
    final boolean globalDefault = cur.getBoolean("Options.Global.Default", false);
    currency.getRegions().put("global", new CurrencyRegion("global", global, globalDefault));

    if(cur.contains("Options.MultiRegion.Regions")) {
      for(String region : cur.getConfigurationSection("Options.MultiRegion.Regions").getKeys(false)) {

        final boolean isDefault = cur.getBoolean("Options.MultiRegion.Regions." + region + ".Default", true);
        currency.getRegions().put(region, new CurrencyRegion(region, true, isDefault));
      }
    }

    if(cur.contains("Converting")) {
      final Set<String> converting = cur.getConfigurationSection("Converting").getKeys(false);

      for(String str : converting) {
        currency.getConversion().put(str, cur.getDouble("Converting." + str, 1.0));
      }
    }

    //Load our item-back currency configurations.
    if(currency instanceof ItemCurrency item) {
      item.setEnderChest(cur.getBoolean("Item.EnderChest", true));
    }

    //Load our note configurations.
    if(cur.getBoolean("Note.Notable", true)) {

      //Note Item configs
      final String material = cur.getString("Note.Item.Material", "PAPER");

      final BigDecimal minimum = new BigDecimal(cur.getString("Note.Minimum", "0.00"));

      final Note note = new Note(material, minimum, cur.getString("Note.Fee", "0.00"));

      note.setTexture(cur.getString("Note.Item.Texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA0NzE5YjNiOTdkMTk1YTIwNTcxOGI2ZWUyMWY1Yzk1Y2FmYTE2N2U3YWJjYTg4YTIxMDNkNTJiMzdkNzIyIn19fQ=="));
      note.setCustomModelData(cur.getInt("Note.Item.ModelData", -1));

      if(cur.contains("Note.Item.Enchantments")) {
        note.setEnchantments(cur.getStringList("Note.Item.Enchantments"));
      }

      if(cur.contains("Note.Item.Flags")) {
        note.setFlags(cur.getStringList("Note.Item.Flags"));
      }
      currency.setNote(note);
    }

    final CurrencyLoadCallback currencyLoad = new CurrencyLoadCallback(currency);
    if(PluginCore.callbacks().call(currencyLoad)) {
      PluginCore.log().error("Cancelled currency load through callback.", DebugLevel.OFF);
      return false;
    }

    if(!loadDenominations(new File(directory, identifier), currency)) {
      PluginCore.log().error("Failed to load currency. Unable to load denominations: " + currency.getIdentifier(), DebugLevel.OFF);
      return false;
    }

    TNECore.eco().currency().addCurrency(currency);

    try {
      cur.save();
    } catch(IOException e) {
      PluginCore.log().error("Failed to save currency YAML!", e, DebugLevel.OFF);
    }
    return true;
  }

  /**
   * Loads all denominations for a currency.
   * @param directory The directory to load the denominations from.
   * @param currency The currency of the denomination.
   */
  @Override
  public boolean loadDenominations(final File directory, Currency currency) {

    if(directory.exists()) {
      final File[] denominations = IOUtil.getYAMLs(directory);

      if(denominations.length > 0) {
        for (File denomination : denominations) {

          final boolean loaded = loadDenomination(currency, denomination);

          if(!loaded) {
            PluginCore.log().error("Unable to load denomination: " + denomination.getName(), DebugLevel.OFF);
          }
        }
        return true;
      }
      PluginCore.log().error("No denominations found for currency: " + currency.getIdentifier(), DebugLevel.OFF);
      return false;
    }
    PluginCore.log().error("Invalid currency directory! Must be the same name as the currency's identifier value. Currency: " + currency.getIdentifier(), DebugLevel.OFF);
    return false;
  }

  /**
   * Loads all denominations for a currency.
   *
   * @param directory The directory to load the denomination from.
   * @param currency     The currency of the denomination.
   * @param name The name of the denomination to load.
   */
  @Override
  public boolean loadDenomination(final File directory, Currency currency, String name) {
    return loadDenomination(currency, new File(directory, name + ".yml"));
  }

  /**
   * Loads all denominations for a currency.
   *
   * @param currency     The currency of the denomination.
   * @param denomFile The file of the denomination to load.
   */
  @Override
  public boolean loadDenomination(Currency currency, File denomFile) {
    final YamlFile denom = new YamlFile(denomFile);
    try {
      denom.loadWithComments();
    } catch(IOException e) {
      PluginCore.log().error("Failed to load denomination: " + denomFile.getName(), DebugLevel.OFF);
      e.printStackTrace();
      return false;
    }

    final String single = denom.getString("Info.Single", "Dollar");
    final String plural = denom.getString("Info.Plural", "Dollars");

    final BigDecimal weight = BigDecimal.valueOf(denom.getDouble("Options.Weight", 1));
    if(weight.compareTo(BigDecimal.ZERO) <= 0) {
      PluginCore.log().error("Failed to load denomination: " + denomFile.getName() + ". Invalid Options.Weight Value: " + weight.toPlainString(), DebugLevel.OFF);
      return false;
    }


    final String material = denom.getString("Options.Material", "PAPER");

    Denomination denomination = (currency instanceof ItemCurrency)?
        new ItemDenomination(weight, material) : new Denomination(weight);

    denomination.setSingle(single);
    denomination.setPlural(plural);

    if(denomination instanceof ItemDenomination item) {

      item.setName(denom.getString("Options.Name", null));
      item.setLore(denom.getStringList("Options.Lore"));
      item.setCustomModel(denom.getInt("Options.ModelData", -1));
      item.setTexture(denom.getString("Options.Texture", null));

      if(denom.contains("Options.Enchantments")) {
        item.setEnchantments(denom.getStringList("Options.Enchantments"));
      }

      if(denom.contains("Options.Flags")) {
        item.setFlags(denom.getStringList("Options.Flags"));
      }

      //Crafting
      if(denom.getBoolean("Options.Crafting.Enabled", false)) {
        final boolean shapeless = denom.getBoolean("Options.Crafting.Shapeless", false);
        final int amount = denom.getInt("Options.Crafting.Amount", 1);

        CraftingRecipe recipe = new CraftingRecipe(!shapeless, amount, TNECore.instance().denominationToStack(item));

        for(String materials : denom.getStringList("Options.Crafting.Materials")) {

          String[] split = materials.split(":");
          if(split.length >= 2) {
            recipe.getIngredients().put(split[0].charAt(0), split[1]);
          }
        }

        int i = 0;
        for(String row : denom.getStringList("Options.Crafting.Recipe")) {
          if(i > 2) break;

          recipe.getRows()[i] = row;

          i++;
        }

        PluginCore.server().registerCrafting(recipe);
      }
    }

    final DenominationLoadCallback denomCallback = new DenominationLoadCallback(currency, denomination);
    if(PluginCore.callbacks().call(denomCallback)) {
      return false;
    }

    currency.getDenominations().put(weight, denomination);
    return true;
  }
}