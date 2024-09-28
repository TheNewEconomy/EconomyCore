package net.tnemc.core.io.storage.datables.yaml;

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
import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.CurrencyHoldings;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.RegionHoldings;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.utils.Identifier;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.io.storage.Datable;
import net.tnemc.plugincore.core.io.storage.StorageConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * YAMLHoldings
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class YAMLHoldings implements Datable<HoldingsEntry> {

  /**
   * The class that is represented by the O parameter.
   *
   * @return The class that represents the parameter.
   */
  @Override
  public Class<? extends HoldingsEntry> clazz() {

    return HoldingsEntry.class;
  }

  /**
   * USed to purge the objects of this datable.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void purge(final StorageConnector<?> connector) {
    //This isn't required, it'll be deleted with the account.
  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param object    The object to be stored.
   */
  @Override
  public void store(final StorageConnector<?> connector, @NotNull final HoldingsEntry object, @Nullable final String identifier) {

    //check if our file is in use.
    final String file = "accounts/" + identifier + ".yml";
    while(TNECore.yaml().inUse(file)) {
      try {
        Thread.sleep(1000);
      } catch(InterruptedException ignore) {
      }
    }

    TNECore.yaml().add(file);

    final File accFile = new File(PluginCore.directory(), file);
    if(!accFile.exists()) {
      try {
        accFile.createNewFile();
      } catch(IOException ignore) {

        PluginCore.log().error("Issue creating account file. Account: " + identifier);
        return;
      }
    }


    YamlDocument yaml = null;
    try {
      yaml = YamlDocument.create(accFile);
    } catch(IOException ignore) {

      PluginCore.log().error("Issue loading account file. Account: " + identifier);
      return;
    }

    yaml.set("Holdings." + MainConfig.yaml().getString("Core.Server.Name")
             + "." + object.getRegion() + "." + object.getCurrency().toString() + "."
             + object.getHandler().asID(), object.getAmount().toPlainString());

    PluginCore.log().debug("YAMLHoldings-store-Entry ID:" + identifier, DebugLevel.DEVELOPER);
    PluginCore.log().debug("YAMLHoldings-store-Entry Currency:" + object.getCurrency().toString(), DebugLevel.DEVELOPER);
    PluginCore.log().debug("YAMLHoldings-store-Entry AMT:" + object.getAmount().toPlainString(), DebugLevel.DEVELOPER);
    try {
      yaml.save();
      yaml = null;
    } catch(IOException ignore) {
      PluginCore.log().error("Issue saving account holdings to file. Account: " + identifier);
    }
    TNECore.yaml().remove(file);
  }

  /**
   * Used to store all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void storeAll(final StorageConnector<?> connector, @Nullable final String identifier) {

    final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
    if(account.isPresent()) {

      //check if our file is in use.
      final String file = "accounts/" + identifier + ".yml";
      while(TNECore.yaml().inUse(file)) {
        try {
          Thread.sleep(1000);
        } catch(InterruptedException ignore) {
        }
      }

      TNECore.yaml().add(file);

      final File accFile = new File(PluginCore.directory(), file);
      if(!accFile.exists()) {
        try {
          accFile.createNewFile();
        } catch(IOException ignore) {

          PluginCore.log().error("Issue creating account file. Account: " + identifier);
          return;
        }
      }


      YamlDocument yaml = null;
      try {
        yaml = YamlDocument.create(accFile);
      } catch(IOException ignore) {

        PluginCore.log().error("Issue loading account file. Account: " + identifier);
        return;
      }

      for(final RegionHoldings region : account.get().getWallet().getHoldings().values()) {
        for(final CurrencyHoldings currency : region.getHoldings().values()) {
          for(final HoldingsEntry entry : currency.getHoldings().values()) {

            yaml.set("Holdings." + MainConfig.yaml().getString("Core.Server.Name")
                     + "." + entry.getRegion() + "." + entry.getCurrency().toString() + "."
                     + entry.getHandler().asID(), entry.getAmount().toPlainString());
          }
        }
      }

      try {
        yaml.save();
        yaml = null;
      } catch(IOException ignore) {
        PluginCore.log().error("Issue saving account holdings to file. Account: " + identifier);
      }

      TNECore.yaml().remove(file);
    }
  }

  /**
   * Used to load this object.
   *
   * @param connector  The storage connector to use for this transaction.
   * @param identifier The identifier used to identify the object to load.
   *
   * @return The object to load.
   *
   * @throws UnsupportedOperationException as this method is not valid for holdings.
   */
  @Override
  public Optional<HoldingsEntry> load(final StorageConnector<?> connector, @NotNull final String identifier) {

    throw new UnsupportedOperationException("load for HoldingsEntry is not a supported operation.");
  }

  /**
   * Used to load all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   *
   * @return A collection containing the objects loaded.
   */
  @Override
  public Collection<HoldingsEntry> loadAll(final StorageConnector<?> connector, @Nullable final String identifier) {

    final Collection<HoldingsEntry> holdings = new ArrayList<>();

    if(identifier != null) {
      final File accFile = new File(PluginCore.directory(), "accounts/" + identifier + ".yml");
      if(!accFile.exists()) {

        PluginCore.log().error("Null account file passed to YAMLAccount.load. Account: " + identifier, DebugLevel.OFF);
        return holdings;
      }


      YamlDocument yaml = null;
      try {
        yaml = YamlDocument.create(accFile);
      } catch(IOException ignore) {

        PluginCore.log().error("Issue loading account file. Account: " + identifier, DebugLevel.OFF);
      }


      try {
        //region, currency, amount, type
        if(yaml != null) {

          //Holdings.Server.Region.Currency.Handler: Balance
          if(yaml.contains("Holdings")) {
            final Section main = yaml.getSection("Holdings");
            for(final Object serverObj : main.getKeys()) {

              final String server = (String)serverObj;
              if(!main.contains(server) || !main.isSection(server)) {
                continue;
              }

              for(final Object regionObj : main.getSection(server).getKeys()) {

                final String region = (String)regionObj;
                if(!main.contains(server + "." + region) || !main.isSection(server + "." + region)) {
                  continue;
                }

                for(final Object currencyObj : main.getSection(server + "." + region).getKeys()) {


                  final String currency = (String)currencyObj;
                  if(TNECore.eco().currency().findCurrency(currency).isEmpty()) {
                    EconomyManager.invalidCurrencies().add(currency);
                  }

                  if(!main.contains(server + "." + region + "." + currency) || !main.isSection(server + "." + region + "." + currency)) {
                    continue;
                  }

                  for(final Object handlerObj : main.getSection(server + "." + region + "." + currency).getKeys()) {

                    final String handler = (String)handlerObj;
                    final String amount = yaml.getString("Holdings." + server + "." + region + "." + currency + "." + handler, "0.0");

                    //region, currency, amount, type
                    final HoldingsEntry entry = new HoldingsEntry(region,
                                                                  UUID.fromString(currency),
                                                                  new BigDecimal(amount),
                                                                  Identifier.fromID(handler)
                    );

                    PluginCore.log().debug("YAMLHoldings-loadAll-Entry ID:" + entry.getHandler(), DebugLevel.DEVELOPER);
                    PluginCore.log().debug("YAMLHoldings-loadAll-Entry AMT:" + entry.getAmount().toPlainString(), DebugLevel.DEVELOPER);
                    holdings.add(entry);
                  }
                }
              }
            }
          }
        }
      } catch(Exception ignore) {

        PluginCore.log().error("Issue loading account file. Skipping. Account: " + identifier, DebugLevel.OFF);
      }
      yaml = null;
    }
    return holdings;
  }
}