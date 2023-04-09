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

/**
 * MySQLDialect
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MySQLDialect implements Dialect {

  private final String loadNames;
  private final String loadName;
  private final String saveName;
  private final String loadAccounts;
  private final String loadAccount;
  private final String saveAccount;
  private final String loadNonPlayer;
  private final String saveNonPlayer;
  private final String loadPlayer;
  private final String savePlayer;
  private final String loadMembers;
  private final String saveMember;
  private final String loadHoldings;
  private final String saveHolding;
  private final String loadReceipts;
  private final String saveReceipt;
  private final String loadReceiptHolding;
  private final String saveReceiptHolding;
  private final String loadParticipants;
  private final String saveParticipant;
  private final String loadModifiers;
  private final String saveModifier;

  public MySQLDialect(final String prefix) {
    this.loadNames = "SELECT BIN_TO_UUID(uid) AS uid, username FROM " + prefix + "player_names";

    this.loadName = "SELECT username FROM " + prefix + "player_names WHERE uid = UUID_TO_BIN(?)";

    this.saveName = "INSERT INTO " + prefix + "player_names (uid, username) VALUES (UUID_TO_BIN(?), ?) ON DUPLICATE KEY UPDATE username = ?";

    this.loadAccounts = "SELECT BIN_TO_UUID(uid) AS uid, username, account_type, created, pin, status FROM " + prefix + "tne_accounts";

    this.loadAccount = "SELECT username, account_type, created, pin, status FROM " + prefix + "tne_accounts WHERE uid = UUID_TO_BIN(?)";

    this.saveAccount = "INSERT INTO " + prefix + "tne_accounts (uid, username, account_type, created, pin, status)" +
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

    this.loadHoldings = "SELECT server, region, currency, holdings_type, holdings FROM " + prefix +
                        "tne_holdings WHERE uid = UUID_TO_BIN(?)";

    this.saveHolding = "INSERT INTO " + prefix + "tne_holdings (uid, server, region, currency, holdings_type, holdings) " +
                       "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE holdings = ?";

    this.loadReceipts = "SELECT BIN_TO_UUID(uid) AS uid, performed, receipt_type, receipt_source, receipt_source_type, archive, voided FROM " +
                        prefix + "receipts";

    this.saveReceipt = "INSERT INTO " + prefix + "receipts (uid, performed, receipt_type, receipt_source, " +
                       "receipt_source_type, archive, voided) " +
                       "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE archive = ?, voided = ?";

    this.loadReceiptHolding = "SELECT BIN_TO_UUID(participant) AS participant, ending, server, region, currency, holdings_type, holdings FROM " +
                              prefix + "receipts_holdings WHERE uid = UUID_TO_BIN(?)";

    this.saveReceiptHolding = "INSERT INTO " + prefix + "receipts_holdings (uid, participant, ending, " +
                              "server, region, currency, holdings_type, holdings) " +
                              "VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE uid=uid";

    this.loadParticipants = "SELECT BIN_TO_UUID(participant) AS participant, participant_type, tax FROM " +
                            prefix + "receipts_participants WHERE uid = UUID_TO_BIN(?)";

    this.saveParticipant = "INSERT INTO " + prefix + "receipts_participants (uid, participant, participant_type, tax) " +
                           "VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?) ON DUPLICATE KEY UPDATE uid=uid";

    this.loadModifiers = "SELECT BIN_TO_UUID(participant) AS participant, participant_type, operation, region, currency, modifier FROM " +
                         prefix + "receipts_modifiers WHERE uid = UUID_TO_BIN(?)";

    this.saveModifier = "INSERT INTO " + prefix + "receipts_modifiers (uid, participant, participant_type, operation, region, currency, modifier) " +
                        "VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?), ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE uid = uid";

  }

  @Override
  public String loadNames() {
    return loadNames;
  }

  @Override
  public String loadName() {
    return loadName;
  }

  @Override
  public String saveName() {
    return saveName;
  }

  @Override
  public String loadAccounts() {
    return loadAccounts;
  }

  @Override
  public String loadAccount() {
    return loadAccount;
  }

  @Override
  public String saveAccount() {
    return saveAccount;
  }

  @Override
  public String loadNonPlayer() {
    return loadNonPlayer;
  }

  @Override
  public String saveNonPlayer() {
    return saveNonPlayer;
  }

  @Override
  public String loadPlayer() {
    return loadPlayer;
  }

  @Override
  public String savePlayer() {
    return savePlayer;
  }

  @Override
  public String loadMembers() {
    return loadMembers;
  }

  @Override
  public String saveMembers() {
    return saveMember;
  }

  @Override
  public String loadHoldings() {
    return loadHoldings;
  }

  @Override
  public String saveHoldings() {
    return saveHolding;
  }

  @Override
  public String loadReceipts() {
    return loadReceipts;
  }

  @Override
  public String saveReceipt() {
    return saveReceipt;
  }

  @Override
  public String loadReceiptHolding() {
    return loadReceiptHolding;
  }

  @Override
  public String saveReceiptHolding() {
    return saveReceiptHolding;
  }

  @Override
  public String loadParticipants() {
    return loadParticipants;
  }

  @Override
  public String saveParticipant() {
    return saveParticipant;
  }

  @Override
  public String loadModifiers() {
    return loadModifiers;
  }

  @Override
  public String saveModifier() {
    return saveModifier;
  }
}