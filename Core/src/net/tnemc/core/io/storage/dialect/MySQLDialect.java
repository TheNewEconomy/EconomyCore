package net.tnemc.core.io.storage.dialect;
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

import net.tnemc.core.io.storage.Dialect;
import org.intellij.lang.annotations.Language;

/**
 * MySQLDialect
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MySQLDialect implements Dialect {

  //The load and save queries
  @Language("SQL")
  private final String saveName;

  @Language("SQL")
  private final String loadAccounts;

  @Language("SQL")
  private final String loadAccount;

  @Language("SQL")
  private final String saveAccount;

  @Language("SQL")
  private final String loadNonPlayer;

  @Language("SQL")
  private final String saveNonPlayer;

  @Language("SQL")
  private final String loadPlayer;

  @Language("SQL")
  private final String savePlayer;

  @Language("SQL")
  private final String loadMembers;

  @Language("SQL")
  private final String saveMember;

  @Language("SQL")
  private final String loadHoldings;

  @Language("SQL")
  private final String saveHolding;

  @Language("SQL")
  private final String loadReceipts;

  @Language("SQL")
  private final String saveReceipt;

  @Language("SQL")
  private final String loadReceiptHolding;

  @Language("SQL")
  private final String saveReceiptHolding;

  @Language("SQL")
  private final String loadParticipants;

  @Language("SQL")
  private final String saveParticipant;

  @Language("SQL")
  private final String loadModifiers;

  @Language("SQL")
  private final String saveModifier;

  private final String prefix;

  public MySQLDialect(final String prefix) {
    this.prefix = prefix;

    this.saveName = "INSERT INTO " + prefix + "player_names (uid, username) VALUES (UUID_TO_BIN(?), ?) ON DUPLICATE KEY UPDATE username = ?";

    this.loadAccounts = "SELECT BIN_TO_UUID(uid) AS uid, username, account_type, created, pin, status FROM " + prefix + "accounts";

    this.loadAccount = "SELECT username, account_type, created, pin, status FROM " + prefix + "accounts WHERE uid = UUID_TO_BIN(?)";

    this.saveAccount = "INSERT INTO " + prefix + "accounts (uid, username, account_type, created, pin, status)" +
                       "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE username = ?, status = ?, pin = ?";

    this.loadNonPlayer = "SELECT BIN_TO_UUID(owner) AS owner FROM " + prefix + "non_players_accounts WHERE uid = UUID_TO_BIN(?)";

    this.saveNonPlayer = "INSERT INTO " + prefix + "non_players_accounts (uid, owner) VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?)) " +
                         "ON DUPLICATE KEY UPDATE owner = UUID_TO_BIN(?)";

    this.loadPlayer = "SELECT last_online FROM " + prefix + "players_accounts WHERE uid = UUID_TO_BIN(?)";

    this.savePlayer = "INSERT INTO " + prefix + "players_accounts (uid, last_online) VALUES (UUID_TO_BIN(?), ?) " +
                      "ON DUPLICATE KEY UPDATE last_online = ?";

    this.loadMembers = "SELECT BIN_TO_UUID(uid) AS uid, perm, perm_value FROM " + prefix + "account_members WHERE account = UUID_TO_BIN(?)";

    this.saveMember = "INSERT INTO " + prefix + "account_members (uid, account, perm, perm_value) VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?) " +
                      "ON DUPLICATE KEY UPDATE perm_value = ?";

    this.loadHoldings = "SELECT server, region, BIN_TO_UUID(currency) AS currency, holdings_type, holdings FROM " + prefix +
                        "holdings WHERE uid = UUID_TO_BIN(?)";

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
}