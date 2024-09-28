package net.tnemc.core.transaction;

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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a participant in a {@link Transaction transaction}.
 *
 * @author creatorfromhell
 * @see Transaction
 * @since 0.1.2.0
 */
public class TransactionParticipant {

  private final List<HoldingsEntry> startingBalances = new ArrayList<>();
  private final List<HoldingsEntry> endingBalances = new ArrayList<>();

  private final UUID id;
  private BigDecimal tax;

  public TransactionParticipant(final UUID id, List<HoldingsEntry> startBalances) {

    this.id = id;
    this.startingBalances.addAll(startBalances);
    this.tax = BigDecimal.ZERO;
  }

  public Optional<Account> asAccount() {

    return TNECore.eco().account().findAccount(id);
  }

  public UUID getId() {

    return id;
  }

  public BigDecimal getTax() {

    return tax;
  }

  public void setTax(BigDecimal tax) {

    this.tax = tax;
  }

  public List<HoldingsEntry> getStartingBalances() {

    return startingBalances;
  }

  public List<HoldingsEntry> getEndingBalances() {

    return endingBalances;
  }

  public BigDecimal getCombinedEnding() {

    BigDecimal combined = BigDecimal.ZERO;

    for(HoldingsEntry entry : getEndingBalances()) {
      combined = combined.add(entry.getAmount());
    }
    return combined;
  }
}