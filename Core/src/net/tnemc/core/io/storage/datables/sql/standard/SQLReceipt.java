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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.io.storage.dialect.TNEDialect;
import net.tnemc.core.manager.TransactionManager;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.TransactionParticipant;
import net.tnemc.plugincore.core.io.storage.Datable;
import net.tnemc.plugincore.core.io.storage.StorageConnector;
import net.tnemc.plugincore.core.io.storage.connect.SQLConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
   * @param object    The object to be stored.
   */
  @Override
  public void store(StorageConnector<?> connector, @NotNull Receipt object, @Nullable String identifier) {
    if(connector instanceof SQLConnector sql && sql.dialect() instanceof TNEDialect tne) {

      //Store the receipt info
      sql.executeUpdate(tne.saveReceipt(),
                                              new Object[]{
                                                  object.getId().toString(),
                                                  new java.sql.Timestamp(object.getTime()),
                                                  object.getType(),
                                                  object.getSource().name(),
                                                  object.getSource().type(),
                                                  object.isArchive(),
                                                  object.isVoided(),
                                                  object.isArchive(),
                                                  object.isVoided()
                                              });

      storeParticipant(connector, object.getFrom(), object.getModifierFrom(), object.getId().toString());
      storeParticipant(connector, object.getTo(), object.getModifierTo(), object.getId().toString());
    }
  }

  private void storeParticipant(StorageConnector<?> connector, @Nullable TransactionParticipant participant,
                                @Nullable HoldingsModifier modifier, @NotNull String identifier) {

    if(connector instanceof SQLConnector sql && sql.dialect() instanceof TNEDialect tne
            && participant != null && modifier != null) {

      //store participant info
      sql.executeUpdate(tne.saveParticipant(),
                                              new Object[]{
                                                  identifier,
                                                  participant.getId(),
                                                  "account",
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
                                                  "account",
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

      final Optional<Account> account = TNECore.eco().account().findAccount(identifier);
      if(account.isPresent()) {
        for(Receipt receipt : TransactionManager.receipts().getReceiptsByParticipant(account.get().getIdentifier())) {
          store(connector, receipt, identifier);
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
  public Optional<Receipt> load(ResultSet result) throws SQLException {

    //SQL Columns: uid, performed, receipt_type, receipt_source, receipt_source_type, archive, voided
    Receipt receipt = new Receipt(
            UUID.fromString(result.getString("uid")),
            result.getTimestamp("performed").getTime(),
            null
    );
    receipt.setArchive(result.getBoolean("archive"));
    receipt.setVoided(result.getBoolean("voided"));

    return Optional.ofNullable(receipt);
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

        if(result.next()) {

          final Optional<Receipt> receipt = load(result);
          if(receipt.isPresent()) {

          }
        }

      } catch(Exception ignore) {}
    }
    return receipts;
  }
}