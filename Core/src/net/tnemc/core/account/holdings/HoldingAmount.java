package net.tnemc.core.account.holdings;

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

import net.tnemc.core.account.holdings.modify.HoldingsModifier;

import java.math.BigDecimal;

/**
 * This class acts like a wrapper over the {@link java.math.BigDecimal} class in order to interface
 * with {@link HoldingsModifier} a little more.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class HoldingAmount {

  private BigDecimal amount;

  public HoldingAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public void modify(HoldingsModifier modifier) {

  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}