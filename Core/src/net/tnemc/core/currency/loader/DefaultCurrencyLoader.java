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

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
import net.tnemc.core.utils.MISCUtils;
import net.tnemc.core.utils.exceptions.NoValidCurrenciesException;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.helper.CraftingRecipe;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.utils.IOUtil;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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
   *
   * @param directory The directory to load the currencies from.
   *
   * @throws NoValidCurrenciesException When no valid currencies can be loaded.
   */
  @Override
  public void loadCurrencies(final File directory) throws NoValidCurrenciesException {

    if(directory.exists()) {
      final File[] currencies = IOUtil.getYAMLs(directory);
      if(currencies.length > 0) {

        for(final File curFile : currencies) {

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
   * @param name      The name of the currency to load.
   */
  @Override
  public boolean loadCurrency(final File directory, final String name) {

    return loadCurrency(directory, new File(directory, name + ".yml"));
  }

  /**
   * Loads a specific currency.
   *
   * @param directory    The directory to load the currency from.
   * @param curDirectory The file of the currency
   */
  @Override
  public boolean loadCurrency(final File directory, final File curDirectory) {

    YamlDocument cur = null;

    PluginCore.log().inform("Loading: " + curDirectory.getName());

    try {
      cur = YamlDocument.create(curDirectory);

    } catch(final IOException e) {
      PluginCore.log().error("Failed to load currency: " + curDirectory.getName(), e, DebugLevel.OFF);
      return false;
    }

    if(cur.getBoolean("Options.Disabled", false)) {
      PluginCore.log().inform("Currency wasn't loaded as it's disabled: " + cur.getName());
      return false;
    }

    PluginCore.log().inform("Attempting to load currency: " + curDirectory.getName());

    //Currency Info configs.
    final String identifier = cur.getString("Info.Identifier", MISCUtils.randomString(3));
    final String icon = cur.getString("Info.Icon", "PAPER");
    final String single = cur.getString("Info.Major_Single", "Dollar");
    final String plural = cur.getString("Info.Major_Plural", "Dollars");
    final String singleMinor = cur.getString("Info.Minor_Single", "Cent");
    final String pluralMinor = cur.getString("Info.Minor_Plural", "Cents");
    final String symbol = cur.getString("Info.Symbol", "$");

    //Currency Options configs.
    final String decimal = cur.getString("Options.Decimal", ".");
    final boolean sync = cur.getBoolean("Options.Sync", true);
    final int decimalPlaces = (Math.min(cur.getInt("Options.DecimalPlaces", 2), 4));
    final String currencyType = cur.getString("Options.Type", "virtual");
    final int minorWeight = cur.getInt("Options.Minor_Weight", 100);

    //Formatting Configurations
    final String format = cur.getString("Formatting.Format", "<symbol><major.amount><decimal><minor.amount>").trim();
    final String prefixes = cur.getString("Formatting.Prefixes", "kMGTPEZYXWVUN₮").trim();
    final String prefixesj = cur.getString("Formatting.JPrefixes", "万億兆京垓\uD855\uDF71穣溝澗正載").trim();
    final boolean separate = cur.getBoolean("Formatting.Major_Separate", true);
    final String separator = cur.getString("Formatting.Major_Separator", ",");
    final boolean showBalance = cur.getBoolean("Formatting.Balance", true);

    final CurrencyType type = TNECore.eco().currency().findTypeOrDefault(currencyType);

    final Currency currency = (type.supportsItems())? new ItemCurrency(identifier) : new Currency(identifier);

    final BigDecimal maxBalance = ((new BigDecimal(cur.getString("Options.MaxBalance", largestSupported.toPlainString())).compareTo(largestSupported) > 0)? largestSupported : new BigDecimal(cur.getString("MaxBalance", largestSupported.toPlainString())));
    final BigDecimal minBalance = (type.supportsItems())? BigDecimal.ZERO : new BigDecimal(cur.getString("Options.MinBalance", "0.00"));
    final BigDecimal balance = new BigDecimal(cur.getString("Options.Balance", "200.00"));

    //Added in build 28, needs removed by build 32.
    boolean uuidAsId = false;
    if(!cur.contains("Info.UUIDAsId")) {
      cur.set("Info.UUIDAsId", false);
      MISCUtils.setComment(cur, "Info.UUIDAsId", "Whether to use the Identifier config as the currency's UUID. Not Recommended. Can lead to issues in the future");
    } else {
      uuidAsId = cur.getBoolean("Info.UUIDAsId");
    }


    final UUID check = UUID.nameUUIDFromBytes(identifier.getBytes(StandardCharsets.UTF_8));
    if(cur.contains("Info.UUID")) {

      final UUID id = (uuidAsId && !cur.getString("Info.UUID").equalsIgnoreCase(check.toString()))? check : UUID.fromString(cur.getString("Info.UUID"));


      final Optional<Currency> curOption = TNECore.eco().currency().find(id);
      if(curOption.isEmpty()) {
        currency.setUid(UUID.fromString(cur.getString("Info.UUID")));
      }
    } else {
      if(uuidAsId) {
        currency.setUid(check);
      }
    }

    currency.setIdentifier(identifier);
    currency.setFile(curDirectory.getName());
    currency.setIconMaterial(icon);
    currency.setSync(sync);
    currency.setMaxBalance(maxBalance);
    currency.setMinBalance(minBalance);
    currency.setStartingHoldings(balance);
    currency.setDecimal(decimal);
    currency.setDecimalPlaces(decimalPlaces);
    currency.setFormat(format);
    currency.setPrefixes(prefixes);
    currency.setPrefixesj(prefixesj);
    currency.setDisplay(single);
    currency.setDisplayPlural(plural);
    currency.setDisplayMinor(singleMinor);
    currency.setDisplayMinorPlural(pluralMinor);
    currency.setSymbol(symbol);
    currency.setType(type.name());
    currency.setSeparateMajor(separate);
    currency.setMajorSeparator(separator);
    currency.setBalanceShow(showBalance);
    currency.setMinorWeight(minorWeight);

    final boolean global = cur.getBoolean("Options.Global.Enabled", true);
    final boolean globalDefault = cur.getBoolean("Options.Global.Default", false);
    currency.getRegions().put("global", new CurrencyRegion("global", global, globalDefault));

    if(cur.contains("Options.MultiRegion.Regions")) {
      for(final Object regionObj : cur.getSection("Options.MultiRegion.Regions").getKeys()) {

        final String region = (String)regionObj;
        final boolean isDefault = cur.getBoolean("Options.MultiRegion.Regions." + region + ".Default", true);
        currency.getRegions().put(region, new CurrencyRegion(region, true, isDefault));
      }
    }

    if(cur.contains("Converting")) {
      final Set<Object> converting = cur.getSection("Converting").getKeys();

      for(final Object strObj : converting) {

        final String str = (String)strObj;
        currency.getConversion().put(str, cur.getDouble("Converting." + str, 1.0));
      }
    }

    //Load our item-back currency configurations.
    if(currency instanceof final ItemCurrency item) {
      item.setEnderChest(cur.getBoolean("Item.EnderChest", true));
      item.setEnderFill(cur.getBoolean("Item.EnderFill", true));
      item.setImportItem(cur.getBoolean("Item.ImportItems", true));
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
    } catch(final IOException e) {
      PluginCore.log().error("Failed to save currency YAML!", e, DebugLevel.OFF);
    }
    return true;
  }

  /**
   * Loads all denominations for a currency.
   *
   * @param directory The directory to load the denominations from.
   * @param currency  The currency of the denomination.
   */
  @Override
  public boolean loadDenominations(final File directory, final Currency currency) {

    if(directory.exists()) {
      final File[] denominations = IOUtil.getYAMLs(directory);

      if(denominations.length > 0) {
        for(final File denomination : denominations) {

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
   * @param currency  The currency of the denomination.
   * @param name      The name of the denomination to load.
   */
  @Override
  public boolean loadDenomination(final File directory, final Currency currency, final String name) {

    return loadDenomination(currency, new File(directory, name + ".yml"));
  }

  /**
   * Loads all denominations for a currency.
   *
   * @param currency  The currency of the denomination.
   * @param denomFile The file of the denomination to load.
   */
  @Override
  public boolean loadDenomination(final Currency currency, final File denomFile) {

    YamlDocument denom = null;
    try {
      denom = YamlDocument.create(denomFile);
    } catch(final IOException e) {
      PluginCore.log().error("Failed to load denomination: " + denomFile.getName(), e, DebugLevel.OFF);
      return false;
    }

    final String single = denom.getString("Info.Single", "Dollar");
    final String plural = denom.getString("Info.Plural", "Dollars");

    final BigDecimal weight = new BigDecimal(denom.getString("Options.Weight", "1.0"));
    PluginCore.log().debug("Loading denomination with weight of: " + weight);
    if(weight.compareTo(BigDecimal.ZERO) <= 0) {

      PluginCore.log().error("Failed to load denomination: " + denomFile.getName() + ". Invalid Options.Weight Value: " + weight.toPlainString(), DebugLevel.OFF);
      return false;
    }


    final String material = denom.getString("Options.Material", "PAPER").toLowerCase(Locale.ROOT);
    PluginCore.log().debug("Loading denomination with material of: " + material);

    final Denomination denomination = (currency instanceof ItemCurrency)?
                                      new ItemDenomination(weight, material) : new Denomination(weight);

    denomination.setSingle(single);
    denomination.setPlural(plural);

    if(denomination instanceof final ItemDenomination item) {

      item.setName(denom.getString("Options.Name", ""));

      item.maxStack(denom.getInt("Options.MaxStack", 0));

      final List<String> loreStr = denom.getStringList("Options.Lore");
      final LinkedList<Component> lore = new LinkedList<>();
      for(final String str : loreStr) {

        lore.add(MiniMessage.miniMessage().deserialize(str));
      }

      item.checks().addAll(denom.getStringList("Checks", new ArrayList<>()));

      String provider = "vanilla";
      String providerID = "";

      if(denom.getBoolean("Integrations.ItemsAdder.Enabled", false)) {

        provider = "itemsadder";
        providerID = denom.getString("Integrations.ItemsAdder.Item");
      }

      if(denom.getBoolean("Integrations.Oraxen.Enabled", false)) {

        provider = "oraxen";
        providerID = denom.getString("Integrations.Oraxen.Item");
      }

      if(denom.getBoolean("Integrations.Nexo.Enabled", false)) {

        provider = "nexo";
        providerID = denom.getString("Integrations.Nexo.Item");
      }

      if(denom.getBoolean("Integrations.Nova.Enabled", false)) {

        provider = "nova";
        providerID = denom.getString("Integrations.Nova.Item");
      }

      if(denom.getBoolean("Integrations.SlimeFun.Enabled", false)) {

        provider = "slimefun";
        providerID = denom.getString("Integrations.Slimefun.Item");
      }

      item.provider(provider);
      item.providerID(providerID);

      item.setLore(lore);
      item.setCustomModel(denom.getInt("Options.ModelData", -1));
      item.setTexture(denom.getString("Options.Texture", ""));

      if(denom.getBoolean("Options.ItemModel.Enabled", false)) {

        item.itemModel(denom.getString("Options.ItemModel.Model"));
        item.modelColours().addAll(denom.getStringList("Options.ItemModel.Colours", new ArrayList<>()));
        item.modelStrings().addAll(denom.getStringList("Options.ItemModel.Strings", new ArrayList<>()));

        for(final String str : denom.getStringList("Options.ItemModel.Booleans", new ArrayList<>())) {

          item.modelBooleans().add(Boolean.valueOf(str));
        }

        for(final String str : denom.getStringList("Options.ItemModel.Floats", new ArrayList<>())) {

          item.modelFloats().add(Float.valueOf(str));
        }
      }

      if(denom.contains("Options.Enchantments")) {
        item.enchantments(denom.getStringList("Options.Enchantments"));
      }

      if(denom.contains("Options.Flags")) {
        item.flags(denom.getStringList("Options.Flags"));
      }

      //Crafting
      if(denom.getBoolean("Options.Crafting.Enabled", false)) {
        final boolean shapeless = denom.getBoolean("Options.Crafting.Shapeless", false);
        final int amount = denom.getInt("Options.Crafting.Amount", 1);

        final AbstractItemStack<?> craftingItem = TNECore.instance().denominationToStack(item).amount(amount);

        final CraftingRecipe recipe = new CraftingRecipe(!shapeless, amount, craftingItem);

        for(final String materials : denom.getStringList("Options.Crafting.Materials")) {

          final String[] split = materials.split(":");
          if(split.length >= 2) {
            recipe.getIngredients().put(split[0].charAt(0), split[1]);
          }
        }

        int i = 0;
        for(final String row : denom.getStringList("Options.Crafting.Recipe")) {
          if(i > 2) break;

          recipe.getRows()[i] = row;

          i++;
        }

        PluginCore.server().registerCrafting(currency.getIdentifier() + ":" + single, recipe);
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