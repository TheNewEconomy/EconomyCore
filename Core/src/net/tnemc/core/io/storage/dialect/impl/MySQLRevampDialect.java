package net.tnemc.core.io.storage.dialect.impl;
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

import net.tnemc.core.io.storage.dialect.TNEDialect;
import org.intellij.lang.annotations.Language;

/**
 * MySQLRevampDialect
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 *
 * CREATE TABLE IF NOT EXISTS prefix_accounts ( uid VARCHAR(36) NOT NULL PRIMARY KEY, username
 * VARCHAR(50) NOT NULL UNIQUE, account_type VARCHAR(30) NOT NULL, created DATETIME NOT NULL, pin
 * VARCHAR(16), status VARCHAR(36) );
 *
 * CREATE TABLE IF NOT EXISTS prefix_non_players_accounts ( uid VARCHAR(36) NOT NULL PRIMARY KEY,
 * owner VARCHAR(36) NOT NULL );
 *
 * CREATE TABLE IF NOT EXISTS prefix_players_accounts ( uid VARCHAR(36) NOT NULL PRIMARY KEY,
 * last_online DATETIME NOT NULL );
 *
 * CREATE TABLE IF NOT EXISTS prefix_account_members ( uid VARCHAR(36) NOT NULL PRIMARY KEY, account
 * VARCHAR(36) NOT NULL, perm VARCHAR(36) NOT NULL, perm_value TINYINT(1) NOT NULL );
 *
 * CREATE TABLE IF NOT EXISTS prefix_holdings ( uid VARCHAR(36) NOT NULL, server VARCHAR(40) NOT
 * NULL, region VARCHAR(40) NOT NULL, currency VARCHAR(36) NOT NULL, holdings_type VARCHAR(30) NOT
 * NULL, holdings DECIMAL(49, 4) NOT NULL, PRIMARY KEY(uid, server, region, currency, holdings_type)
 * );
 *
 * CREATE TABLE IF NOT EXISTS prefix_receipts ( uid VARCHAR(36) NOT NULL PRIMARY KEY, performed
 * DATETIME NOT NULL, receipt_type VARCHAR(30) NOT NULL, receipt_source VARCHAR(60) NOT NULL,
 * receipt_source_type VARCHAR(30) NOT NULL, archive TINYINT(1) NOT NULL, voided TINYINT(1) NOT NULL
 * );
 *
 * CREATE TABLE IF NOT EXISTS prefix_receipts_holdings ( uid VARCHAR(36) NOT NULL, participant
 * VARCHAR(36) NOT NULL, ending TINYINT(1) NOT NULL, server VARCHAR(40) NOT NULL, region VARCHAR(40)
 * NOT NULL, currency VARCHAR(36) NOT NULL, holdings_type VARCHAR(30) NOT NULL, holdings DECIMAL(49,
 * 4) NOT NULL, PRIMARY KEY(uid, participant) );
 *
 * CREATE TABLE IF NOT EXISTS prefix_receipts_participants ( uid VARCHAR(36) NOT NULL, participant
 * VARCHAR(36) NOT NULL, participant_type VARCHAR(10) NOT NULL, tax DECIMAL(49, 4) NOT NULL, PRIMARY
 * KEY(uid, participant) );
 *
 * CREATE TABLE IF NOT EXISTS prefix_receipts_modifiers ( uid VARCHAR(36) NOT NULL, participant
 * VARCHAR(36) NOT NULL, participant_type VARCHAR(10) NOT NULL, operation VARCHAR(10) NOT NULL,
 * region VARCHAR(40) NOT NULL, currency VARCHAR(36) NOT NULL, modifier DECIMAL(49, 4) NOT NULL,
 * PRIMARY KEY(uid, participant) );
 */
public class MySQLRevampDialect implements TNEDialect {

  public static final String requirement = "3.5.0";

  //The load and save queries
  @Language("SQL")
  protected final String saveName;

  @Language("SQL")
  protected final String loadAccounts;

  @Language("SQL")
  protected final String loadAccount;

  @Language("SQL")
  protected final String saveAccount;

  @Language("SQL")
  protected final String loadNonPlayer;

  @Language("SQL")
  protected final String saveNonPlayer;

  @Language("SQL")
  protected final String loadPlayer;

  @Language("SQL")
  protected final String savePlayer;

  @Language("SQL")
  protected final String loadMembers;

  @Language("SQL")
  protected final String saveMember;

  @Language("SQL")
  protected final String loadHoldings;

  @Language("SQL")
  protected final String saveHolding;

  @Language("SQL")
  protected final String loadReceipts;

  @Language("SQL")
  protected final String saveReceipt;

  @Language("SQL")
  protected final String loadReceiptHolding;

  @Language("SQL")
  protected final String saveReceiptHolding;

  @Language("SQL")
  protected final String loadParticipants;

  @Language("SQL")
  protected final String saveParticipant;

  @Language("SQL")
  protected final String loadModifiers;

  @Language("SQL")
  protected final String saveModifier;

  protected final String prefix;

  public MySQLRevampDialect(final String prefix) {

    this.prefix = prefix;

    this.saveName = "INSERT INTO " + prefix + "player_names (uid, username) VALUES (?, ?) ON DUPLICATE KEY UPDATE username = ?";

    this.loadAccounts = "SELECT uid AS uid, username, account_type, created, pin, status FROM " + prefix + "accounts";

    this.loadAccount = "SELECT username, account_type, created, pin, status FROM " + prefix + "accounts WHERE uid = ?";

    this.saveAccount = "INSERT INTO " + prefix + "accounts (uid, username, account_type, created, pin, status)" +
                       "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE username = ?, pin = ?, status = ?";

    this.loadNonPlayer = "SELECT owner AS owner FROM " + prefix + "non_players_accounts WHERE uid = ?";

    this.saveNonPlayer = "INSERT INTO " + prefix + "non_players_accounts (uid, owner) VALUES (?, ?) " +
                         "ON DUPLICATE KEY UPDATE owner = ?";

    this.loadPlayer = "SELECT last_online FROM " + prefix + "players_accounts WHERE uid = ?";

    this.savePlayer = "INSERT INTO " + prefix + "players_accounts (uid, last_online) VALUES (?, ?) " +
                      "ON DUPLICATE KEY UPDATE last_online = ?";

    this.loadMembers = "SELECT uid AS uid, perm, perm_value FROM " + prefix + "account_members WHERE account = ?";

    this.saveMember = "INSERT INTO " + prefix + "account_members (uid, account, perm, perm_value) VALUES (?, ?, ?, ?) " +
                      "ON DUPLICATE KEY UPDATE perm_value = ?";

    this.loadHoldings = "SELECT region, currency AS currency, holdings_type, holdings FROM " + prefix +
                        "holdings WHERE uid = ? AND server = ?";

    this.saveHolding = "INSERT INTO " + prefix + "holdings (uid, server, region, currency, holdings_type, holdings) " +
                       "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE holdings = ?";

    this.loadReceipts = "SELECT uid AS uid, performed, receipt_type, receipt_source, receipt_source_type, archive, voided FROM " +
                        prefix + "receipts";

    this.saveReceipt = "INSERT INTO " + prefix + "receipts (uid, performed, receipt_type, receipt_source, " +
                       "receipt_source_type, archive, voided) " +
                       "VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE archive = ?, voided = ?";

    this.loadReceiptHolding = "SELECT participant AS participant, ending, server, region, " +
                              "currency AS currency, holdings_type, holdings FROM " +
                              prefix + "receipts_holdings WHERE uid = ? AND participant = ? AND ending = ?";

    this.saveReceiptHolding = "INSERT INTO " + prefix + "receipts_holdings (uid, participant, ending, " +
                              "server, region, currency, holdings_type, holdings) " +
                              "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE uid=uid";

    this.loadParticipants = "SELECT participant AS participant, participant_type, tax FROM " +
                            prefix + "receipts_participants WHERE uid = ?";

    this.saveParticipant = "INSERT INTO " + prefix + "receipts_participants (uid, participant, participant_type, tax) " +
                           "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE uid=uid";

    this.loadModifiers = "SELECT participant AS participant, participant_type, operation, region, " +
                         "currency AS currency, modifier FROM " + prefix + "receipts_modifiers WHERE uid = ? AND participant = ? AND participant_type = ?";

    this.saveModifier = "INSERT INTO " + prefix + "receipts_modifiers (uid, participant, participant_type, operation, region, currency, modifier) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE uid = uid";

  }

  @Override
  public String accountsTable() {

    return "CREATE TABLE IF NOT EXISTS " + prefix + "accounts (\n" +
           "    uid VARCHAR(36) NOT NULL PRIMARY KEY,\n" +
           "    username VARCHAR(50) NOT NULL UNIQUE,\n" +
           "    account_type VARCHAR(30) NOT NULL,\n" +
           "    created DATETIME NOT NULL,\n" +
           "    pin VARCHAR(16),\n" +
           "    status VARCHAR(36)\n" +
           ");";
  }

  @Override
  public String accountsNonPlayerTable() {

    return "CREATE TABLE IF NOT EXISTS " + prefix + "non_players_accounts (\n" +
           "    uid VARCHAR(36) NOT NULL PRIMARY KEY,\n" +
           "    owner VARCHAR(36) NOT NULL\n" +
           ");";
  }

  @Override
  public String accountsPlayerTable() {

    return "CREATE TABLE IF NOT EXISTS " + prefix + "players_accounts (\n" +
           "    uid VARCHAR(36) NOT NULL PRIMARY KEY,\n" +
           "    last_online DATETIME NOT NULL\n" +
           ");";
  }

  @Override
  public String accountMembersTable() {

    return "CREATE TABLE IF NOT EXISTS " + prefix + "account_members (\n" +
           "    uid VARCHAR(36) NOT NULL,\n" +
           "    account VARCHAR(36) NOT NULL,\n" +
           "    perm VARCHAR(36) NOT NULL,\n" +
           "    perm_value TINYINT(1) NOT NULL,\n" +
           "    PRIMARY KEY(uid, account, perm)\n" +
           ");";
  }

  @Override
  public String holdingsTable() {

    return "CREATE TABLE IF NOT EXISTS " + prefix + "holdings (\n" +
           "    uid VARCHAR(36) NOT NULL,\n" +
           "    server VARCHAR(40) NOT NULL,\n" +
           "    region VARCHAR(40) NOT NULL,\n" +
           "    currency VARCHAR(36) NOT NULL,\n" +
           "    holdings_type VARCHAR(30) NOT NULL,\n" +
           "    holdings DECIMAL(49, 4) NOT NULL,\n" +
           "    PRIMARY KEY(uid, server, region, currency, holdings_type)\n" +
           ");";
  }

  @Override
  public String receiptsTable() {

    return "CREATE TABLE IF NOT EXISTS " + prefix + "receipts (\n" +
           "    uid VARCHAR(36) NOT NULL PRIMARY KEY,\n" +
           "    performed DATETIME NOT NULL,\n" +
           "    receipt_type VARCHAR(30) NOT NULL,\n" +
           "    receipt_source VARCHAR(60) NOT NULL,\n" +
           "    receipt_source_type VARCHAR(30) NOT NULL,\n" +
           "    archive TINYINT(1) NOT NULL,\n" +
           "    voided TINYINT(1) NOT NULL\n" +
           ");";
  }

  @Override
  public String receiptsHoldingsTable() {

    return "CREATE TABLE IF NOT EXISTS " + prefix + "receipts_holdings (\n" +
           "    uid VARCHAR(36) NOT NULL,\n" +
           "    participant VARCHAR(36) NOT NULL,\n" +
           "    ending TINYINT(1) NOT NULL,\n" +
           "    server VARCHAR(40) NOT NULL,\n" +
           "    region VARCHAR(40) NOT NULL,\n" +
           "    currency VARCHAR(36) NOT NULL,\n" +
           "    holdings_type VARCHAR(30) NOT NULL,\n" +
           "    holdings DECIMAL(49, 4) NOT NULL,\n" +
           "    PRIMARY KEY(uid, participant)\n" +
           ");";
  }

  @Override
  public String receiptsParticipantsTable() {

    return "CREATE TABLE IF NOT EXISTS " + prefix + "receipts_participants (\n" +
           "    uid VARCHAR(36) NOT NULL,\n" +
           "    participant VARCHAR(36) NOT NULL,\n" +
           "    participant_type VARCHAR(10) NOT NULL,\n" +
           "    tax DECIMAL(49, 4) NOT NULL,\n" +
           "    PRIMARY KEY(uid, participant)\n" +
           ");";
  }

  @Override
  public String receiptsModifiersTable() {

    return "CREATE TABLE IF NOT EXISTS " + prefix + "receipts_modifiers (\n" +
           "    uid VARCHAR(36) NOT NULL,\n" +
           "    participant VARCHAR(36) NOT NULL,\n" +
           "    participant_type VARCHAR(10) NOT NULL,\n" +
           "    operation VARCHAR(10) NOT NULL,\n" +
           "    region VARCHAR(40) NOT NULL,\n" +
           "    currency VARCHAR(36) NOT NULL,\n" +
           "    modifier DECIMAL(49, 4) NOT NULL,\n" +
           "    PRIMARY KEY(uid, participant)\n" +
           ");";
  }

  @Override
  public @Language("SQL") String accountPurge(final int days) {

    final String acc = prefix + "accounts";
    final String players = prefix + "players_accounts";

    return "DELETE FROM " + acc +
           "WHERE uid IN (" +
           "  SELECT uid FROM " + players +
           "  WHERE EXISTS (" +
           "    SELECT *" +
           "    FROM " + acc +
           "    WHERE " + acc + ".uid = " + players + ".uid" +
           "    AND DATEDIFF(DAY, " + players + ".last_online, NOW()) >= " + days +
           "  )" +
           ");";
  }

  @Override
  public @Language("SQL") String receiptPurge(final int days) {

    return "DELETE FROM " + prefix + "receipts WHERE archived = false" +
           "AND DATEDIFF(CURDATE(), performed) >= " + days;
  }

  @Override
  public @Language("SQL") String saveName() {

    return saveName;
  }

  @Override
  public @Language("SQL") String loadAccounts() {

    return loadAccounts;
  }

  @Override
  public @Language("SQL") String loadAccount() {

    return loadAccount;
  }

  @Override
  public @Language("SQL") String saveAccount() {

    return saveAccount;
  }

  @Override
  public @Language("SQL") String loadNonPlayer() {

    return loadNonPlayer;
  }

  @Override
  public @Language("SQL") String saveNonPlayer() {

    return saveNonPlayer;
  }

  @Override
  public @Language("SQL") String loadPlayer() {

    return loadPlayer;
  }

  @Override
  public @Language("SQL") String savePlayer() {

    return savePlayer;
  }

  @Override
  public @Language("SQL") String loadMembers() {

    return loadMembers;
  }

  @Override
  public @Language("SQL") String saveMembers() {

    return saveMember;
  }

  @Override
  public @Language("SQL") String loadHoldings() {

    return loadHoldings;
  }

  @Override
  public @Language("SQL") String saveHoldings() {

    return saveHolding;
  }

  @Override
  public @Language("SQL") String loadReceipts() {

    return loadReceipts;
  }

  @Override
  public @Language("SQL") String saveReceipt() {

    return saveReceipt;
  }

  @Override
  public @Language("SQL") String loadReceiptHolding() {

    return loadReceiptHolding;
  }

  @Override
  public @Language("SQL") String saveReceiptHolding() {

    return saveReceiptHolding;
  }

  @Override
  public @Language("SQL") String loadParticipants() {

    return loadParticipants;
  }

  @Override
  public @Language("SQL") String saveParticipant() {

    return saveParticipant;
  }

  @Override
  public @Language("SQL") String loadModifiers() {

    return loadModifiers;
  }

  @Override
  public @Language("SQL") String saveModifier() {

    return saveModifier;
  }

  @Override
  public String parseVersion(final String version) {

    return version.split("-")[1];
  }

  @Override
  public String requirement() {

    return requirement;
  }
}