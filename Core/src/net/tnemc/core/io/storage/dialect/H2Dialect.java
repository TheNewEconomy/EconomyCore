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
 * H2Dialect
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class H2Dialect extends MySQLDialect {

  public H2Dialect(final String prefix) {
    super(prefix);

    this.saveName = "INSERT INTO " + prefix + "player_names (uid, username) VALUES (UNHEX(REPLACE(?, '-', '')), ?) ON DUPLICATE KEY UPDATE username = ?";

    this.loadAccounts = "SELECT LOWER(CONCAT(" +
        "  LEFT(HEX(uid), 8), '-'," +
        "  MID(HEX(uid), 9, 4), '-'," +
        "  MID(HEX(uid), 13, 4), '-'," +
        "  MID(HEX(uid), 17, 4), '-'," +
        "  RIGHT(HEX(uid), 12)" +
        ")) AS uid, username, account_type, created, pin, status FROM " + prefix + "accounts";

    this.loadAccount = "SELECT username, account_type, created, pin, status FROM " + prefix + "accounts WHERE uid = UNHEX(REPLACE(?, '-', ''))";

    this.saveAccount = "INSERT INTO " + prefix + "accounts (uid, username, account_type, created, pin, status)" +
        "VALUES (UNHEX(REPLACE(?, '-', '')), ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE username = ?, pin = ?, status = ?";

    this.loadNonPlayer = "SELECT LOWER(CONCAT(" +
        "  LEFT(HEX(uid), 8), '-'," +
        "  MID(HEX(uid), 9, 4), '-'," +
        "  MID(HEX(uid), 13, 4), '-'," +
        "  MID(HEX(uid), 17, 4), '-'," +
        "  RIGHT(HEX(uid), 12)" +
        ")) AS owner FROM " + prefix + "non_players_accounts WHERE uid = UNHEX(REPLACE(?, '-', ''))";

    this.saveNonPlayer = "INSERT INTO " + prefix + "non_players_accounts (uid, owner) VALUES (UNHEX(REPLACE(?, '-', '')), UNHEX(REPLACE(?, '-', ''))) " +
        "ON DUPLICATE KEY UPDATE owner = UNHEX(REPLACE(?, '-', ''))";

    this.loadPlayer = "SELECT last_online FROM " + prefix + "players_accounts WHERE uid = UNHEX(REPLACE(?, '-', ''))";

    this.savePlayer = "INSERT INTO " + prefix + "players_accounts (uid, last_online) VALUES (UNHEX(REPLACE(?, '-', '')), ?) " +
        "ON DUPLICATE KEY UPDATE last_online = ?";

    this.loadMembers = "SELECT LOWER(CONCAT(" +
        "  LEFT(HEX(uid), 8), '-'," +
        "  MID(HEX(uid), 9, 4), '-'," +
        "  MID(HEX(uid), 13, 4), '-'," +
        "  MID(HEX(uid), 17, 4), '-'," +
        "  RIGHT(HEX(uid), 12)" +
        ")) AS uid, perm, perm_value FROM " + prefix + "account_members WHERE account = UNHEX(REPLACE(?, '-', ''))";

    this.saveMember = "INSERT INTO " + prefix + "account_members (uid, account, perm, perm_value) VALUES (UNHEX(REPLACE(?, '-', '')), UNHEX(REPLACE(?, '-', '')), ?, ?) " +
        "ON DUPLICATE KEY UPDATE perm_value = ?";

    this.loadHoldings = "SELECT server, region, LOWER(CONCAT(" +
        " LEFT(HEX(uid), 8), '-'," +
        " MID(HEX(currency), 9, 4), '-'," +
        " MID(HEX(currency), 13, 4), '-'," +
        " MID(HEX(currency), 17, 4), '-'," +
        " RIGHT(HEX(currency), 12)" +
        " )) AS AS currency, holdings_type, holdings FROM " + prefix +
        "holdings WHERE uid = UNHEX(REPLACE(?, '-', ''))";

    this.saveHolding = "INSERT INTO " + prefix + "holdings (uid, server, region, currency, holdings_type, holdings) " +
        "VALUES (UNHEX(REPLACE(?, '-', '')), ?, ?, UNHEX(REPLACE(?, '-', '')), ?, ?) ON DUPLICATE KEY UPDATE holdings = ?";

    this.loadReceipts = "SELECT LOWER(CONCAT(" +
        "  LEFT(HEX(uid), 8), '-'," +
        "  MID(HEX(uid), 9, 4), '-'," +
        "  MID(HEX(uid), 13, 4), '-'," +
        "  MID(HEX(uid), 17, 4), '-'," +
        "  RIGHT(HEX(uid), 12)" +
        ")) AS uid, performed, receipt_type, receipt_source, receipt_source_type, archive, voided FROM " +
        prefix + "receipts";

    this.saveReceipt = "INSERT INTO " + prefix + "receipts (uid, performed, receipt_type, receipt_source, " +
        "receipt_source_type, archive, voided) " +
        "VALUES (UNHEX(REPLACE(?, '-', '')), ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE archive = ?, voided = ?";

    this.loadReceiptHolding = "SELECT LOWER(CONCAT(" +
        "  LEFT(HEX(participant), 8), '-'," +
        "  MID(HEX(participant), 9, 4), '-'," +
        "  MID(HEX(participant), 13, 4), '-'," +
        "  MID(HEX(participant), 17, 4), '-'," +
        "  RIGHT(HEX(participant), 12)" +
        ")) AS participant, ending, server, region, " +
        "LOWER(CONCAT(" +
        "  LEFT(HEX(uid), 8), '-'," +
        "  MID(HEX(currency), 9, 4), '-'," +
        "  MID(HEX(currency), 13, 4), '-'," +
        "  MID(HEX(currency), 17, 4), '-'," +
        "  RIGHT(HEX(currency), 12)" +
        ")) AS currency, holdings_type, holdings FROM " +
        prefix + "receipts_holdings WHERE uid = UNHEX(REPLACE(?, '-', ''))";

    this.saveReceiptHolding = "INSERT INTO " + prefix + "receipts_holdings (uid, participant, ending, " +
        "server, region, currency, holdings_type, holdings) " +
        "VALUES (UNHEX(REPLACE(?, '-', '')), UNHEX(REPLACE(?, '-', '')), ?, ?, ?, UNHEX(REPLACE(?, '-', '')), ?, ?) ON DUPLICATE KEY UPDATE uid=uid";

    this.loadParticipants = "SELECT LOWER(CONCAT(" +
        "  LEFT(HEX(participant), 8), '-'," +
        "  MID(HEX(participant), 9, 4), '-'," +
        "  MID(HEX(participant), 13, 4), '-'," +
        "  MID(HEX(participant), 17, 4), '-'," +
        "  RIGHT(HEX(participant), 12)" +
        ")) AS participant, participant_type, tax FROM " +
        prefix + "receipts_participants WHERE uid = UNHEX(REPLACE(?, '-', ''))";

    this.saveParticipant = "INSERT INTO " + prefix + "receipts_participants (uid, participant, participant_type, tax) " +
        "VALUES (UNHEX(REPLACE(?, '-', '')), UNHEX(REPLACE(?, '-', '')), ?, ?) ON DUPLICATE KEY UPDATE uid=uid";

    this.loadModifiers = "SELECT LOWER(CONCAT(" +
        "  LEFT(HEX(participant), 8), '-'," +
        "  MID(HEX(participant), 9, 4), '-'," +
        "  MID(HEX(participant), 13, 4), '-'," +
        "  MID(HEX(participant), 17, 4), '-'," +
        "  RIGHT(HEX(participant), 12)" +
        ")) AS participant, participant_type, operation, region, " +
        "LOWER(CONCAT(" +
        "  LEFT(HEX(uid), 8), '-'," +
        "  MID(HEX(currency), 9, 4), '-'," +
        "  MID(HEX(currency), 13, 4), '-'," +
        "  MID(HEX(currency), 17, 4), '-'," +
        "  RIGHT(HEX(currency), 12)" +
        ")) AS currency, modifier FROM " + prefix + "receipts_modifiers WHERE uid = UNHEX(REPLACE(?, '-', ''))";

    this.saveModifier = "INSERT INTO " + prefix + "receipts_modifiers (uid, participant, participant_type, operation, region, currency, modifier) " +
        "VALUES (UNHEX(REPLACE(?, '-', '')), UNHEX(REPLACE(?, '-', '')), ?, ?, ?, UNHEX(REPLACE(?, '-', '')), ?) ON DUPLICATE KEY UPDATE uid = uid";

  }
}