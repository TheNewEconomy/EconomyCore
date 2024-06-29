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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.account.holdings.modify.HoldingsOperation;
import net.tnemc.core.actions.ActionSource;
import net.tnemc.core.api.callback.account.AccountSaveCallback;
import net.tnemc.core.manager.TransactionManager;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.TransactionParticipant;
import net.tnemc.core.utils.Identifier;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.id.UUIDPair;
import net.tnemc.plugincore.core.io.storage.Datable;
import net.tnemc.plugincore.core.io.storage.StorageConnector;
import net.tnemc.plugincore.core.io.storage.connect.SQLConnector;
import net.tnemc.plugincore.core.utils.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * YAMLLog
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class YAMLReceipt implements Datable<Receipt> {
  /**
   * The class that is represented by the O parameter.
   *
   * @return The class that represents the parameter.
   */
  @Override
  public Class<? extends Receipt> clazz() {
    return Receipt.class;
  }

  /**
   * USed to purge the objects of this datable.
   *
   * @param connector The storage connector to use for this transaction.
   */
  @Override
  public void purge(StorageConnector<?> connector) {

  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param receipt The object to be stored.
   * @param identifier An optional identifier for loading this object. Note: some Datables may
   * require this identifier.
   */
  @Override
  public void store(StorageConnector<?> connector, @NotNull Receipt receipt, @Nullable String identifier) {


    final String fileSrc = "transactions/" + receipt.getId().toString() + ".yml";
    TNECore.yaml().add(fileSrc);

    final File file = new File(PluginCore.directory(), "transactions/" + receipt.getId().toString() + ".yml");
    if(!file.exists()) {
      try {
        file.createNewFile();
      } catch(IOException ignore) {

        PluginCore.log().error("Issue creating transaction file. Transaction: " + receipt.getId().toString());
        return;
      }
    }


    YamlFile yaml = null;
    try {
      yaml = YamlFile.loadConfiguration(file);
    } catch(IOException ignore) {

      PluginCore.log().error("Issue loading transaction file. Transaction: " + receipt.getId().toString());
      return;
    }

    yaml.set("id", receipt.getId().toString());
    yaml.set("time", receipt.getTime());
    yaml.set("type", receipt.getType());
    yaml.set("source.type", receipt.getSource().type());
    yaml.set("source.name", receipt.getSource().name());
    yaml.set("archive", receipt.isArchive());
    yaml.set("voided", receipt.isVoided());
    yaml.set("from", receipt.getFrom());
    yaml.set("to", receipt.getTo());
    yaml.set("modifierTo", receipt.getModifierTo());
    yaml.set("modifierFrom", receipt.getModifierFrom());

    if(receipt.getFrom() != null && receipt.getModifierFrom() != null) {
      yaml.set("from.id", receipt.getFrom().getId().toString());
      yaml.set("from.tax", receipt.getFrom().getTax().toPlainString());

      for(final HoldingsEntry entry : receipt.getFrom().getStartingBalances()) {
        yaml.set("from.starting." + entry.getRegion() + "." + entry.getCurrency().toString() + "." + entry.getHandler().asID(), entry.getAmount().toPlainString());
      }

      for(final HoldingsEntry entry : receipt.getFrom().getEndingBalances()) {
        yaml.set("from.ending." + entry.getRegion() + "." + entry.getCurrency().toString() + "." + entry.getHandler().asID(), entry.getAmount().toPlainString());
      }

      yaml.set("from.modifier.region", receipt.getModifierFrom().getRegion());
      yaml.set("from.modifier.currency", receipt.getModifierFrom().getCurrency().toString());
      yaml.set("from.modifier.modifier", receipt.getModifierFrom().getModifier().toPlainString());
      yaml.set("from.modifier.operation", receipt.getModifierFrom().getOperation().name());
    }

    if(receipt.getTo() != null && receipt.getModifierTo() != null) {

      yaml.set("to.id", receipt.getTo().getId().toString());
      yaml.set("to.tax", receipt.getTo().getTax().toPlainString());

      for(final HoldingsEntry entry : receipt.getTo().getStartingBalances()) {
        yaml.set("to.starting." + entry.getRegion() + "." + entry.getCurrency().toString() + "." + entry.getHandler().asID(), entry.getAmount().toPlainString());
      }

      for(final HoldingsEntry entry : receipt.getTo().getEndingBalances()) {
        yaml.set("to.ending." + entry.getRegion() + "." + entry.getCurrency().toString() + "." + entry.getHandler().asID(), entry.getAmount().toPlainString());
      }

      yaml.set("to.modifier.region", receipt.getModifierTo().getRegion());
      yaml.set("to.modifier.currency", receipt.getModifierTo().getCurrency().toString());
      yaml.set("to.modifier.modifier", receipt.getModifierTo().getModifier().toPlainString());
      yaml.set("to.modifier.operation", receipt.getModifierTo().getOperation().name());
    }

    try {
      yaml.save();

      yaml = null;
    } catch(IOException ignore) {
      PluginCore.log().error("Issue saving transaction file. Transaction: " + receipt.getId().toString());
      return;
    }
    TNECore.yaml().remove(fileSrc);
  }

  /**
   * Used to store all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   * @param identifier The identifier used to load objects, if they relate to a specific identifier,
   * otherwise this will be null.
   */
  @Override
  public void storeAll(StorageConnector<?> connector, @Nullable String identifier) {

    for(Receipt receipt : TransactionManager.receipts().getReceipts().values()) {
      store(connector, receipt, identifier);
    }
  }

  /**
   * Used to load this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param identifier The identifier used to identify the object to load.
   *
   * @return The object to load.
   */
  @Override
  public Optional<Receipt> load(StorageConnector<?> connector, @NotNull String identifier) {

    final File file = new File(PluginCore.directory(), "transactions/" + identifier + ".yml");
    if(!file.exists()) {

      PluginCore.log().error("Null receipt file passed to YAMLReceipt.load. Receipt: " + identifier);
      return Optional.empty();
    }
    return load(connector, file, identifier);
  }

  public Optional<Receipt> load(StorageConnector<?> connector, File file, final String identifier) {
    if(file == null) {

      PluginCore.log().error("Null account file passed to YAMLReceipt.load. Receipt: " + identifier);
      return Optional.empty();
    }

    YamlFile yaml = null;
    try {
      yaml = YamlFile.loadConfiguration(file);
    } catch(IOException ignore) {

      PluginCore.log().error("Issue loading account file. Receipt: " + identifier);
    }

    if(yaml != null) {

      final UUID id = UUID.fromString(yaml.getString("id"));
      final long time = yaml.getLong("time");
      final String type = yaml.getString("type");
      final String sourceType = yaml.getString("source.type");
      final String sourceName = yaml.getString("source.name");

      final Receipt receipt = new Receipt(id, time, type);
      receipt.setSource(ActionSource.create(sourceName, sourceType));

      loadParticipant(yaml, receipt, "from");
      loadParticipant(yaml, receipt, "to");

      receipt.setArchive(yaml.getBoolean("archive"));
      receipt.setVoided(yaml.getBoolean("voided"));

      return Optional.of(receipt);
    }
    return Optional.empty();
  }

  public void loadParticipant(YamlFile yaml, Receipt receipt, String type) {
    final BigDecimal tax = new BigDecimal(yaml.getString(type + ".tax"));

    final TransactionParticipant participant = new TransactionParticipant(UUID.fromString(yaml.getString(type + ".id")), new ArrayList<>());
    participant.setTax(tax);

    participant.getStartingBalances().addAll(loadHoldings(yaml, type, "starting"));
    participant.getEndingBalances().addAll(loadHoldings(yaml, type, "ending"));

    switch(type.toLowerCase(Locale.ROOT)) {

      case "from":
        receipt.setFrom(participant);
        receipt.setModifierFrom(loadModifier(yaml, type));
        break;
      default:
        receipt.setTo(participant);
        receipt.setModifierTo(loadModifier(yaml, type));
        break;
    }
  }

  public HoldingsModifier loadModifier(YamlFile yaml, String type) {

    return new HoldingsModifier(yaml.getString(type + ".modifier.region"),
            UUID.fromString(yaml.getString(type + ".modifier.currency")),
            new BigDecimal(yaml.getString(type + ".modifier.modifier")),
            HoldingsOperation.valueOf(yaml.getString(type + ".modifier.operation")));

  }

  public List<HoldingsEntry> loadHoldings(YamlFile yaml, String type, String holdingsType) {
    final List<HoldingsEntry> holdings = new ArrayList<>();

    final ConfigurationSection startingSection = yaml.getConfigurationSection(type + "." + holdingsType);
    if(startingSection != null) {

      for(final String region : startingSection.getKeys(false)) {

        final ConfigurationSection currencySection = startingSection.getConfigurationSection(region);
        if(currencySection != null) {

          for(final String currency : currencySection.getKeys(false)) {

            final ConfigurationSection handlerSection = currencySection.getConfigurationSection(currency);
            if(handlerSection != null) {

              for(final String handler : handlerSection.getKeys(false)) {

                final BigDecimal amount = new BigDecimal(handlerSection.getString(handler));
                final HoldingsEntry entry = new HoldingsEntry(region, UUID.fromString(currency), amount, Identifier.fromID(handler));
                holdings.add(entry);
              }
            }
          }
        }
      }
    }
    return holdings;
  }

  /**
   * Used to load all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   * @param identifier The identifier used to load objects, if they relate to a specific identifier,
   * otherwise this will be null.
   *
   * @return A collection containing the objects loaded.
   */
  @Override
  public Collection<Receipt> loadAll(StorageConnector<?> connector, @Nullable String identifier) {
    final Collection<Receipt> receipts = new ArrayList<>();

    for(File file : IOUtil.getYAMLs(new File(PluginCore.directory(), "transactions"))) {

      final Optional<Receipt> loaded = load(connector, file, file.getName().replace(".yml", ""));
      if(loaded.isPresent()) {
        receipts.add(loaded.get());
        TransactionManager.receipts().log(loaded.get());
      }
    }
    return receipts;
  }
}