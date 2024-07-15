package net.tnemc.core.io.storage.datables.sql.standard;

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

import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.account.holdings.modify.HoldingsOperation;
import net.tnemc.core.actions.ActionSource;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.io.storage.dialect.TNEDialect;
import net.tnemc.core.manager.TransactionManager;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.TransactionParticipant;
import net.tnemc.core.utils.Identifier;
import net.tnemc.plugincore.core.io.storage.Datable;
import net.tnemc.plugincore.core.io.storage.StorageConnector;
import net.tnemc.plugincore.core.io.storage.connect.SQLConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 *
 */
public class SQLReceipt implements Datable<Receipt> {
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
    if(connector instanceof SQLConnector sql && sql.dialect() instanceof TNEDialect tne) {
      sql.executeUpdate(tne.receiptPurge(DataConfig.yaml().getInt("Data.Purge.Transaction.Days")),
                                              new Object[] {});
    }
  }

  /**
   * Used to store this object.
   *
   * @param connector The storage connector to use for this transaction.
   * @param receipt    The object to be stored.
   */
  @Override
  public void store(StorageConnector<?> connector, @NotNull Receipt receipt, @Nullable String identifier) {
    if(connector instanceof SQLConnector sql && sql.dialect() instanceof TNEDialect tne) {

      //Store the receipt info
      sql.executeUpdate(tne.saveReceipt(),
                                              new Object[]{
                                                  receipt.getId().toString(),
                                                  new java.sql.Timestamp(receipt.getTime()),
                                                  receipt.getType(),
                                                  receipt.getSource().name(),
                                                  receipt.getSource().type(),
                                                  receipt.isArchive(),
                                                  receipt.isVoided(),
                                                  receipt.isArchive(),
                                                  receipt.isVoided()
                                              });

      storeParticipant(connector, receipt.getFrom(), receipt.getModifierFrom(), "from", receipt.getId().toString());
      storeParticipant(connector, receipt.getTo(), receipt.getModifierTo(), "to", receipt.getId().toString());
    }
  }

  private void storeParticipant(StorageConnector<?> connector, @Nullable TransactionParticipant participant,
                                @Nullable HoldingsModifier modifier, final String type, @NotNull String identifier) {

    if(connector instanceof SQLConnector sql && sql.dialect() instanceof TNEDialect tne
            && participant != null && modifier != null) {

      //store participant info
      sql.executeUpdate(tne.saveParticipant(),
                                              new Object[]{
                                                  identifier,
                                                  participant.getId(),
                                                      type,
                                                  participant.getTax()
                                              });

      //store holdings
      for(HoldingsEntry entry : participant.getStartingBalances()) {
        storeReceiptHolding(connector, entry, participant.getId().toString(), identifier, false);
      }

      for(HoldingsEntry entry : participant.getEndingBalances()) {
        storeReceiptHolding(connector, entry, participant.getId().toString(), identifier, true);
      }

      //store modifier
      sql.executeUpdate(tne.saveModifier(),
                                              new Object[]{
                                                  identifier,
                                                  participant.getId(),
                                                  type,
                                                  modifier.getOperation().name(),
                                                  modifier.getRegion(),
                                                  modifier.getCurrency().toString(),
                                                  modifier.getModifier()
                                              });
    }
  }

  private void storeReceiptHolding(StorageConnector<?> connector, @NotNull HoldingsEntry entry, final String participant,
                                   final String receipt, final boolean ending) {

    if(connector instanceof SQLConnector sql && sql.dialect() instanceof TNEDialect tne) {
      sql.executeUpdate(tne.saveReceiptHolding(),
              new Object[]{
                      receipt,
                      participant,
                      ending,
                      MainConfig.yaml().getString("Core.Server.Name"),
                      entry.getRegion(),
                      entry.getCurrency().toString(),
                      entry.getHandler().asID(),
                      entry.getAmount()
              });
    }
  }

  /**
   * Used to store all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   */
                                @Override
  public void storeAll(StorageConnector<?> connector, @Nullable String identifier) {
    if(connector instanceof SQLConnector && identifier != null) {

      for(Receipt receipt : TransactionManager.receipts().getReceipts().values()) {
        store(connector, receipt, identifier);
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
   */
  @Override
  public Optional<Receipt> load(StorageConnector<?> connector, @NotNull String identifier) {

    //We shouldn't load individual Receipts, it doesn't make sense to me/no use case with caching. - creatorfromhell
    return Optional.empty();
  }

  /**
   * Loads a Receipt object from a ResultSet.
   *
   * @param result The ResultSet containing the data to load into a Receipt object.
   * @return An Optional containing the loaded Receipt object. Returns an empty Optional if the ResultSet is empty.
   * @throws SQLException if an error occurs while accessing the ResultSet data.
   */
  public Receipt load(ResultSet result, SQLConnector sql, TNEDialect dialect) throws SQLException {

    //SQL Columns: uid, performed, receipt_type, receipt_source, receipt_source_type, archive, voided
    final Receipt receipt = new Receipt(
            UUID.fromString(result.getString("uid")),
            result.getTimestamp("performed").getTime(),
            result.getString("receipt_type")
    );
    receipt.setSource(ActionSource.create(result.getString("receipt_source"), result.getString("receipt_source_type")));
    receipt.setArchive(result.getBoolean("archive"));
    receipt.setVoided(result.getBoolean("voided"));

    return receipt;
  }

  public void loadParticipants(Receipt receipt, SQLConnector sql, TNEDialect dialect) {
    try(ResultSet result = sql.executeQuery(dialect.loadParticipants(), new Object[]{
            receipt.getId().toString()})) {
      while(result.next()) {

        final String type = result.getString("participant_type").toLowerCase(Locale.ROOT);
        final UUID id = UUID.fromString(result.getString("participant"));
        final BigDecimal tax = result.getBigDecimal("tax");

        final TransactionParticipant participant = new TransactionParticipant(id, new ArrayList<>());
        participant.getStartingBalances().addAll(loadHoldings(receipt.getId(), id, false, sql, dialect));
        participant.getEndingBalances().addAll(loadHoldings(receipt.getId(), id, true, sql, dialect));

        participant.setTax(tax);
        final Optional<HoldingsModifier> modifier = loadModifier(receipt.getId(), id, type, sql, dialect);

        if(type.equals("from")) {
          receipt.setFrom(participant);
          modifier.ifPresent(receipt::setModifierFrom);
        } else {
          receipt.setTo(participant);
          modifier.ifPresent(receipt::setModifierTo);
        }
      }
    } catch(Exception ignore) {}
  }

  public Optional<HoldingsModifier> loadModifier(final UUID receiptID, final UUID participant,
                                       final String type, SQLConnector sql, TNEDialect dialect) {

    HoldingsModifier modifier = null;
    //participant, participant_type, operation, region, currency AS currency, modifier  - uid/participant
    try(ResultSet result = sql.executeQuery(dialect.loadReceiptHolding(), new Object[] {
            receiptID.toString(), participant, type })) {

      if(result.next()) {

        modifier = new HoldingsModifier(
                result.getString("region"),
                UUID.fromString(result.getString("currency")),
                result.getBigDecimal("modifier"),
                HoldingsOperation.valueOf(result.getString("operation")));
      }
    } catch(Exception ignore) {}
    return Optional.ofNullable(modifier);
  }

  public List<HoldingsEntry> loadHoldings(final UUID receiptID, final UUID participant, final boolean ending,
                                          SQLConnector sql, TNEDialect dialect) {

    final List<HoldingsEntry> holdings = new ArrayList<>();

    //participant, ending, server, region, currency AS currency, holdings_type, holdings - uid/participant
    try(ResultSet result = sql.executeQuery(dialect.loadReceiptHolding(), new Object[] {
            receiptID.toString(), participant, ending })) {

      while(result.next()) {

        final HoldingsEntry entry = new HoldingsEntry(
                result.getString("region"),
                UUID.fromString(result.getString("currency")),
                result.getBigDecimal("holdings"),
                Identifier.fromID(result.getString("holdings_type"))
        );
        holdings.add(entry);
      }
    } catch(Exception ignore) {}

    return holdings;
  }

  /**
   * Used to load all objects of this type.
   *
   * @param connector The storage connector to use for this transaction.
   *
   * @return A collection containing the objects loaded.
   */
  @Override
  public Collection<Receipt> loadAll(StorageConnector<?> connector, @Nullable String identifier) {
    final Collection<Receipt> receipts = new ArrayList<>(); // is this required? Not entirely sure it is - seems maybe a waste
    if(connector instanceof SQLConnector sql && sql.dialect() instanceof TNEDialect tne) {

      try(ResultSet result = sql.executeQuery(tne.loadReceipts(), new Object[0])) {

        while(result.next()) {

          receipts.add(load(result, sql, tne));
        }

      } catch(Exception ignore) {}
    }
    return receipts;
  }
}