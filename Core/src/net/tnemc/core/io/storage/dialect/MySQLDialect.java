package net.tnemc.core.io.storage.dialect;
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

import net.tnemc.plugincore.core.io.storage.Dialect;
import org.intellij.lang.annotations.Language;

/**
 * MySQLDialect
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MySQLDialect implements Dialect {

  private final String requirement = "8.0.0";

  //The load and save queries
  @Language("SQL")
  protected String saveName;

  @Language("SQL")
  protected String loadAccounts;

  @Language("SQL")
  protected String loadAccount;

  @Language("SQL")
  protected String saveAccount;

  @Language("SQL")
  protected String loadNonPlayer;

  @Language("SQL")
  protected String saveNonPlayer;

  @Language("SQL")
  protected String loadPlayer;

  @Language("SQL")
  protected String savePlayer;

  @Language("SQL")
  protected String loadMembers;

  @Language("SQL")
  protected String saveMember;

  @Language("SQL")
  protected String loadHoldings;

  @Language("SQL")
  protected String saveHolding;

  @Language("SQL")
  protected String loadReceipts;

  @Language("SQL")
  protected String saveReceipt;

  @Language("SQL")
  protected String loadReceiptHolding;

  @Language("SQL")
  protected String saveReceiptHolding;

  @Language("SQL")
  protected String loadParticipants;

  @Language("SQL")
  protected String saveParticipant;

  @Language("SQL")
  protected String loadModifiers;

  @Language("SQL")
  protected String saveModifier;

  protected String prefix;

  public MySQLDialect(final String prefix) {
    this.prefix = prefix;

    this.saveName = "INSERT INTO " + prefix + "player_names (uid, username) VALUES (UUID_TO_BIN(?), ?) ON DUPLICATE KEY UPDATE username = ?";

    this.loadAccounts = "SELECT BIN_TO_UUID(uid) AS uid, username, account_type, created, pin, status FROM " + prefix + "accounts";

    this.loadAccount = "SELECT username, account_type, created, pin, status FROM " + prefix + "accounts WHERE uid = UUID_TO_BIN(?)";

    this.saveAccount = "INSERT INTO " + prefix + "accounts (uid, username, account_type, created, pin, status)" +
                       "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE username = ?, pin = ?, status = ?";

    this.loadNonPlayer = "SELECT BIN_TO_UUID(owner) AS owner FROM " + prefix + "non_players_accounts WHERE uid = UUID_TO_BIN(?)";

    this.saveNonPlayer = "INSERT INTO " + prefix + "non_players_accounts (uid, owner) VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?)) " +
                         "ON DUPLICATE KEY UPDATE owner = UUID_TO_BIN(?)";

    this.loadPlayer = "SELECT last_online FROM " + prefix + "players_accounts WHERE uid = UUID_TO_BIN(?)";

    this.savePlayer = "INSERT INTO " + prefix + "players_accounts (uid, last_online) VALUES (UUID_TO_BIN(?), ?) " +
                      "ON DUPLICATE KEY UPDATE last_online = ?";

    this.loadMembers = "SELECT BIN_TO_UUID(uid) AS uid, perm, perm_value FROM " + prefix + "account_members WHERE account = UUID_TO_BIN(?)";

    this.saveMember = "INSERT INTO " + prefix + "account_members (uid, account, perm, perm_value) VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?) " +
                      "ON DUPLICATE KEY UPDATE perm_value = ?";

    this.loadHoldings = "SELECT region, BIN_TO_UUID(currency) AS currency, holdings_type, holdings FROM " + prefix +
                        "holdings WHERE uid = UUID_TO_BIN(?) AND server = ?";

    this.saveHolding = "INSERT INTO " + prefix + "holdings (uid, server, region, currency, holdings_type, holdings) " +
                       "VALUES (UUID_TO_BIN(?), ?, ?, UUID_TO_BIN(?), ?, ?) ON DUPLICATE KEY UPDATE holdings = ?";

    this.loadReceipts = "SELECT BIN_TO_UUID(uid) AS uid, performed, receipt_type, receipt_source, receipt_source_type, archive, voided FROM " +
                        prefix + "receipts";

    this.saveReceipt = "INSERT INTO " + prefix + "receipts (uid, performed, receipt_type, receipt_source, " +
                       "receipt_source_type, archive, voided) " +
                       "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE archive = ?, voided = ?";

    this.loadReceiptHolding = "SELECT BIN_TO_UUID(participant) AS participant, ending, server, region, " +
                              "BIN_TO_UUID(currency) AS currency, holdings_type, holdings FROM " +
                              prefix + "receipts_holdings WHERE uid = UUID_TO_BIN(?)";

    this.saveReceiptHolding = "INSERT INTO " + prefix + "receipts_holdings (uid, participant, ending, " +
                              "server, region, currency, holdings_type, holdings) " +
                              "VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?, ?, UUID_TO_BIN(?), ?, ?) ON DUPLICATE KEY UPDATE uid=uid";

    this.loadParticipants = "SELECT BIN_TO_UUID(participant) AS participant, participant_type, tax FROM " +
                            prefix + "receipts_participants WHERE uid = UUID_TO_BIN(?)";

    this.saveParticipant = "INSERT INTO " + prefix + "receipts_participants (uid, participant, participant_type, tax) " +
                           "VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?) ON DUPLICATE KEY UPDATE uid=uid";

    this.loadModifiers = "SELECT BIN_TO_UUID(participant) AS participant, participant_type, operation, region, " +
                          "BIN_TO_UUID(currency) AS currency, modifier FROM " + prefix + "receipts_modifiers WHERE uid = UUID_TO_BIN(?)";

    this.saveModifier = "INSERT INTO " + prefix + "receipts_modifiers (uid, participant, participant_type, operation, region, currency, modifier) " +
                        "VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?, ?, UUID_TO_BIN(?), ?) ON DUPLICATE KEY UPDATE uid = uid";

  }

  @Override
  public String accountsTable() {
    return "CREATE TABLE IF NOT EXISTS " + prefix + "accounts (\n" +
        "    uid BINARY(16) NOT NULL PRIMARY KEY,\n" +
        "    username VARCHAR(50) NOT NULL UNIQUE,\n" +
        "    account_type VARCHAR(30) NOT NULL,\n" +
        "    created DATETIME NOT NULL,\n" +
        "    pin VARCHAR(16),\n" +
        "    status VARCHAR(36)\n" +
        "    );";
  }

  @Override
  public String accountsNonPlayerTable() {
    return "CREATE TABLE IF NOT EXISTS " + prefix + "non_players_accounts (\n" +
        "    uid BINARY(16) NOT NULL UNIQUE,\n" +
        "    owner BINARY(16) NOT NULL,\n" +
        "    FOREIGN KEY(uid) REFERENCES " + prefix + "accounts(uid) ON DELETE CASCADE,\n" +
        "    FOREIGN KEY(owner) REFERENCES " + prefix + "accounts(uid) ON DELETE CASCADE\n" +
        "    );";
  }

  @Override
  public String accountsPlayerTable() {
    return "CREATE TABLE IF NOT EXISTS " + prefix + "players_accounts (\n" +
        "    uid BINARY(16) NOT NULL UNIQUE,\n" +
        "    last_online DATETIME NOT NULL,\n" +
        "    FOREIGN KEY(uid) REFERENCES " + prefix + "accounts(uid) ON DELETE CASCADE\n" +
        "    );";
  }

  @Override
  public String accountMembersTable() {
    return "CREATE TABLE IF NOT EXISTS " + prefix + "account_members (\n" +
        "    uid BINARY(16) NOT NULL,\n" +
        "    account BINARY(16) NOT NULL,\n" +
        "    perm VARCHAR(36) NOT NULL,\n" +
        "    perm_value TINYINT(1) NOT NULL,\n" +
        "    FOREIGN KEY(uid) REFERENCES " + prefix + "accounts(uid) ON DELETE CASCADE,\n" +
        "    FOREIGN KEY(account) REFERENCES " + prefix + "accounts(uid) ON DELETE CASCADE\n" +
        "    );";
  }

  @Override
  public String holdingsTable() {
    return "CREATE TABLE IF NOT EXISTS " + prefix + "holdings (\n" +
        "    uid BINARY(16) NOT NULL,\n" +
        "    server VARCHAR(40) NOT NULL,\n" +
        "    region VARCHAR(40) NOT NULL,\n" +
        "    currency BINARY(16) NOT NULL,\n" +
        "    holdings_type VARCHAR(30) NOT NULL,\n" +
        "    holdings DECIMAL(49, 4) NOT NULL,\n" +
        "    UNIQUE(`uid`, `server`, `region`, `currency`, `holdings_type`),\n" +
        "    FOREIGN KEY(uid) REFERENCES " + prefix + "accounts(uid) ON DELETE CASCADE\n" +
        "    );";
  }

  @Override
  public String receiptsTable() {
    return "CREATE TABLE IF NOT EXISTS " + prefix + "receipts (\n" +
        "    uid BINARY(16) NOT NULL UNIQUE,\n" +
        "    performed DATETIME NOT NULL,\n" +
        "    receipt_type VARCHAR(30) NOT NULL,\n" +
        "    receipt_source VARCHAR(60) NOT NULL,\n" +
        "    receipt_source_type VARCHAR(30) NOT NULL,\n" +
        "    archive TINYINT(1) NOT NULL,\n" +
        "    voided TINYINT(1) NOT NULL\n" +
        "    );";
  }

  @Override
  public String receiptsHoldingsTable() {
    return "CREATE TABLE IF NOT EXISTS " + prefix + "receipts_holdings (\n" +
        "    uid BINARY(16) NOT NULL UNIQUE,\n" +
        "    participant BINARY(16) NOT NULL,\n" +
        "    ending TINYINT(1) NOT NULL,\n" +
        "    server VARCHAR(40) NOT NULL,\n" +
        "    region VARCHAR(40) NOT NULL,\n" +
        "    currency BINARY(16) NOT NULL,\n" +
        "    holdings_type VARCHAR(30) NOT NULL,\n" +
        "    holdings DECIMAL(49, 4) NOT NULL,\n" +
        "    FOREIGN KEY(uid) REFERENCES " + prefix + "receipts(uid) ON DELETE CASCADE\n" +
        "    );";
  }

  @Override
  public String receiptsParticipantsTable() {
    return "CREATE TABLE IF NOT EXISTS " + prefix + "receipts_participants (\n" +
        "    uid BINARY(16) NOT NULL PRIMARY KEY,\n" +
        "    participant BINARY(16) NOT NULL,\n" +
        "    participant_type VARCHAR(10) NOT NULL,\n" +
        "    tax DECIMAL(49, 4) NOT NULL,\n" +
        "    FOREIGN KEY(uid) REFERENCES " + prefix + "receipts(uid) ON DELETE CASCADE\n" +
        "    );";
  }


  @Override
  public String receiptsModifiersTable() {
    return "CREATE TABLE IF NOT EXISTS " + prefix + "receipts_modifiers (\n" +
        "    uid BINARY(16) NOT NULL PRIMARY KEY,\n" +
        "    participant BINARY(16) NOT NULL,\n" +
        "    participant_type VARCHAR(10) NOT NULL,\n" +
        "    operation VARCHAR(10) NOT NULL,\n" +
        "    region VARCHAR(40) NOT NULL,\n" +
        "    currency BINARY(16) NOT NULL,\n" +
        "    modifier DECIMAL(49, 4) NOT NULL," +
        "    FOREIGN KEY(uid) REFERENCES " + prefix + "receipts(uid) ON DELETE CASCADE" +
        "    );";
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
  public String requirement() {
    return requirement;
  }
}