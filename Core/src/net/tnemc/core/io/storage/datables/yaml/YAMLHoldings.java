package net.tnemc.core.io.storage.datables.yaml;
/*
 * The New Economy
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.CurrencyHoldings;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.RegionHoldings;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.io.storage.Datable;
import net.tnemc.core.io.storage.StorageConnector;
import net.tnemc.core.utils.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;

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
  public void purge(StorageConnector<?> connector) {
    //This isn't required, it'll be deleted with the account.
  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param object    The object to be stored.
   */
  @Override
  public void store(StorageConnector<?> connector, @NotNull HoldingsEntry object, @Nullable String identifier) {

    final File accFile = new File(TNECore.directory(), "accounts/" + identifier + ".yml");
    if(!accFile.exists()) {
      accFile.mkdirs();
    }


    YamlFile yaml = null;
    try {
      yaml = YamlFile.loadConfiguration(accFile);
    } catch(IOException ignore) {

      TNECore.log().error("Issue loading account file. Account: " + identifier);
    }

    if(yaml != null) {
      yaml.set("Holdings." + MainConfig.yaml().getString("Core.Server.Name")
                   + "." + object.getRegion() + "." + object.getCurrency().toString() + "."
                   + object.getHandler().asID(), object.getAmount().toPlainString());

      TNECore.log().debug("YAMLHoldings-store-Entry ID:" + identifier, DebugLevel.DEVELOPER);
      TNECore.log().debug("YAMLHoldings-store-Entry Currency:" + object.getCurrency().toString(), DebugLevel.DEVELOPER);
      TNECore.log().debug("YAMLHoldings-store-Entry AMT:" + object.getAmount().toPlainString(), DebugLevel.DEVELOPER);
      try {
        yaml.save();
        yaml = null;
      } catch(IOException e) {
        TNECore.log().error("Issue saving account holdings to file. Account: " + identifier);
      }
    }
  }

  /**
   * Used to store all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void storeAll(StorageConnector<?> connector, @Nullable String identifier) {

    final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
    if(account.isPresent()) {
      for(RegionHoldings region : account.get().getWallet().getHoldings().values()) {
        for(CurrencyHoldings currency : region.getHoldings().values()) {
          for(HoldingsEntry entry : currency.getHoldings().values()) {
            store(connector, entry, identifier);
          }
        }
      }
    }
  }

  /**
   * Used to load this object.
   *
   * @param connector  The storage connector to use for this transaction.
   * @param identifier The identifier used to identify the object to load.
   *
   * @return The object to load.
   * @throws UnsupportedOperationException as this method is not valid for holdings.
   */
  @Override
  public Optional<HoldingsEntry> load(StorageConnector<?> connector, @NotNull String identifier) {
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
  public Collection<HoldingsEntry> loadAll(StorageConnector<?> connector, @Nullable String identifier) {
    final Collection<HoldingsEntry> holdings = new ArrayList<>();

    if(identifier != null) {
      final File accFile = new File(TNECore.directory(), "accounts/" + identifier + ".yml");
      if(!accFile.exists()) {

        TNECore.log().error("Null account file passed to YAMLAccount.load. Account: " + identifier);
        return holdings;
      }


      YamlFile yaml = null;
      try {
        yaml = YamlFile.loadConfiguration(accFile);
      } catch(IOException ignore) {

        TNECore.log().error("Issue loading account file. Account: " + identifier);
      }


      //region, currency, amount, type
      if(yaml != null) {

        //Holdings.Server.Region.Currency.Handler: Balance
        if(yaml.contains("Holdings")) {
          final ConfigurationSection main = yaml.getConfigurationSection("Holdings");
          for(final String server : main.getKeys(false)) {

            if(!main.contains(server)) {
              continue;
            }
            for(final String region : main.getConfigurationSection(server).getKeys(false)) {

              if(!main.contains(server + "." + region)) {
                continue;
              }
              for(final String currency : main.getConfigurationSection(server + "." + region).getKeys(false)) {

                if(TNECore.eco().currency().findCurrency(currency).isEmpty()) {
                  EconomyManager.invalidCurrencies().add(currency);
                }

                if(!main.contains(server + "." + region + "." + currency)) {
                  continue;
                }
                for(final String handler : main.getConfigurationSection(server + "." + region + "." + currency).getKeys(false)) {

                  final String amount = yaml.getString("Holdings." + server + "." + region + "." + currency + "." + handler, "0.0");

                  //region, currency, amount, type
                  final HoldingsEntry entry = new HoldingsEntry(region,
                                                                UUID.fromString(currency),
                                                                new BigDecimal(amount),
                                                                Identifier.fromID(handler)
                  );

                  TNECore.log().debug("YAMLHoldings-loadAll-Entry ID:" + entry.getHandler(), DebugLevel.DEVELOPER);
                  TNECore.log().debug("YAMLHoldings-loadAll-Entry AMT:" + entry.getAmount().toPlainString(), DebugLevel.DEVELOPER);
                  holdings.add(entry);
                }
              }
            }
          }
        }
      }
      yaml = null;
    }
    return holdings;
  }
}