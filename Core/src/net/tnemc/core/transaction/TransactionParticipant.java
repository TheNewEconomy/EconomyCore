package net.tnemc.core.transaction;

/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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

import java.util.Optional;

/**
 * Represents a participant in a {@link Transaction transaction}.
 *
 * @see Transaction
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TransactionParticipant {

  private final String id;
  private final HoldingsEntry startingBalance;
  private HoldingsEntry endingBalance;

  public TransactionParticipant(final String id, final HoldingsEntry startingBalance) {
    this.id = id;
    this.startingBalance = startingBalance;
  }

  public Optional<Account> asAccount() {
    return TNECore.eco().account().findAccount(id);
  }

  public String getId() {
    return id;
  }

  public HoldingsEntry getStartingBalance() {
    return startingBalance;
  }

  public HoldingsEntry getEndingBalance() {
    return endingBalance;
  }

  public void setEndingBalance(HoldingsEntry endingBalance) {
    this.endingBalance = endingBalance;
  }
}