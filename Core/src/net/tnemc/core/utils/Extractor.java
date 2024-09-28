package net.tnemc.core.utils;

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
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.currency.Currency;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Extractor
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Extractor {

  public static boolean extract() {

    final File file = new File(PluginCore.directory(), "extracted.yml");

    //check to see if file exists, if it does we are going to move it to the extracts directory.
    if(file.exists()) {
      final File directory = new File(PluginCore.directory(), "extracted");
      if(!directory.exists()) {

        directory.mkdir();
      }

      final String fileName = "extracted-" + (directory.listFiles().length + 1) + ".yml";

      file.renameTo(new File(directory, fileName));
    }

    try {
      file.createNewFile();
    } catch(IOException e) {
      PluginCore.log().error("Failed to create extraction file.", e, DebugLevel.STANDARD);
      return false;
    }

    YamlDocument yaml = null;
    try {
      yaml = YamlDocument.create(file);
    } catch(IOException e) {
      PluginCore.log().error("Failed load extraction file for writing.", e, DebugLevel.STANDARD);
      return false;
    }
    final int total = TNECore.eco().account().getAccounts().values().size();
    final int frequency = (int)(total * 0.10);
    int number = 1;

    yaml.set("Version", "0.1.2.0");

    for(final Account account : TNECore.eco().account().getAccounts().values()) {

      for(final HoldingsEntry entry : account.getWallet().entryList()) {

        String username = account.getName();
        username = username.replaceAll("\\.", "!").replaceAll("\\-", "@")
                .replaceAll("\\_", "%");

        yaml.set("Accounts." + username + ".Balances." + entry.getRegion() + "."
                 + entry.getCurrency() + "." + entry.getHandler().asID(), entry.getAmount().toPlainString());

        yaml.set("Accounts." + username + ".id", account.getIdentifier());
      }
      number++;
      try {
        final boolean message = (number % frequency == 0);

        if(message) {
          final int progress = (number * 100) / total;
          PluginCore.log().inform("Extraction Progress: " + progress);
        }
      } catch(Exception ignore) { }
    }

    try {
      yaml.save();
      PluginCore.log().inform("Extraction has completed!");
    } catch(IOException e) {
      PluginCore.log().error("Failed to save extraction file.", e, DebugLevel.STANDARD);
      return false;
    }
    return true;
  }

  public static boolean restore(@Nullable final Integer extraction) {

    final File file;

    if(extraction != null && extraction > 0) {
      file = new File(PluginCore.directory(), "extracted/extracted-" + extraction + ".yml");
    } else {
      file = new File(PluginCore.directory(), "extracted.yml");
    }

    if(!file.exists()) {

      PluginCore.log().inform("The extraction file doesn't exist.");
      return false;
    }

    YamlDocument extracted = null;

    try {
      extracted = YamlDocument.create(file);
    } catch(IOException e) {
      PluginCore.log().error("Failed load extraction file for writing.", e, DebugLevel.STANDARD);
      return false;
    }

    if(extracted.contains("Accounts")) {
      final Set<Object> accounts = extracted.getSection("Accounts").getKeys();

      final int frequency = (int)(accounts.size() * 0.10);
      int number = 1;

      final boolean recode = extracted.contains("Version");

      for(final Object nameObj : accounts) {

        final String name = (String)nameObj;

        final String username = name.replaceAll("\\!", ".").replaceAll("\\@", "-").replaceAll("\\%", "_");

        final String id = extracted.getString("Accounts." + name + ".id");

        final AccountAPIResponse response = TNECore.eco().account().createAccount(id, username);
        if(!response.getResponse().success() || response.getAccount().isEmpty()) {
          PluginCore.log().inform("Couldn't create account for " + username + ". Skipping.");
        }

        final Set<Object> regions = extracted.getSection("Accounts." + name + ".Balances").getKeys();
        for(final Object regionObj : regions) {

          final String region = (String)regionObj;
          final Set<Object> currencies = extracted.getSection("Accounts." + name + ".Balances." + region).getKeys();
          for(final Object currencyObj : currencies) {

            final String currency = (String)currencyObj;
            if(!recode) {
              final String finalCurrency = (currency.equalsIgnoreCase("default"))? TNECore.eco().currency().getDefaultCurrency(region).getIdentifier() : currency;
              final Optional<Currency> cur = TNECore.eco().currency().findCurrency(finalCurrency);

              PluginCore.log().inform("Currency avail: " + cur.isPresent());
              if(cur.isPresent()) {
                final BigDecimal amount = new BigDecimal(extracted.getString("Accounts." + name + ".Balances." + region + "." + currency));

                PluginCore.log().inform("Set Balance to: " + amount.toPlainString());
                response.getAccount().get().setHoldings(new HoldingsEntry(region, cur.get().getUid(),
                                                                          amount, EconomyManager.NORMAL));
              } else {
                final BigDecimal amount = new BigDecimal(extracted.getString("Accounts." + name + ".Balances." + region + "." + currency));

                PluginCore.log().inform("Use default currency");
                PluginCore.log().inform("Set Balance to: " + amount.toPlainString());
                response.getAccount().get().setHoldings(new HoldingsEntry(region, TNECore.eco().currency().getDefaultCurrency(region).getUid(),
                                                                          amount, EconomyManager.NORMAL));
              }
            } else {

              final Set<Object> types = extracted.getSection("Accounts." + name + ".Balances." + region + "." + currency).getKeys();
              for(final Object typeObj : types) {

                final String type = (String)typeObj;
                final BigDecimal amount = new BigDecimal(extracted.getString("Accounts." + name
                                                                             + ".Balances." + region
                                                                             + "." + currency + "."
                                                                             + type));

                response.getAccount().get().setHoldings(new HoldingsEntry(region, UUID.fromString(currency),
                                                                          amount, Identifier.fromID(type)));
              }
            }
            number++;
            try {
              final boolean message = (number % frequency == 0);

              if(message) {
                final int progress = (number * 100) / accounts.size();
                PluginCore.log().inform("Restoration Progress: " + progress);
              }
            } catch(Exception ignore) { }
          }
        }
      }
      PluginCore.log().inform("Restoration has completed!");
    }

    return true;
  }
}