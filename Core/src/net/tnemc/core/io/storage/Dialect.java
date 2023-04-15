package net.tnemc.core.io.storage;
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

/**
 * Dialect
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface Dialect {

  String accountPurge(final int days);

  String receiptPurge(final int days);

  //player name save
  String saveName();

  //load accounts
  String loadAccounts();

  //account load
  String loadAccount();

  //account save
  String saveAccount();

  //non player load
  String loadNonPlayer();

  //non player save
  String saveNonPlayer();

  //player load
  String loadPlayer();

  //player save
  String savePlayer();

  //members load
  String loadMembers();

  //members save
  String saveMembers();

  //holdings load
  String loadHoldings();

  //holdings save
  String saveHoldings();

  //receipts load
  String loadReceipts();

  //receipt save
  String saveReceipt();

  //receipts load
  String loadReceiptHolding();

  //receipt holding save
  String saveReceiptHolding();

  //receipt participants load
  String loadParticipants();

  //receipt participant save
  String saveParticipant();

  //receipt modifiers load
  String loadModifiers();

  //receipt modifier save
  String saveModifier();
}