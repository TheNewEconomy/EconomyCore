package net.tnemc.core.currency.parser;
/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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
import net.tnemc.core.currency.Currency;

import java.math.BigDecimal;

/**
 * ParseMoney
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class ParseMoney {

  private final String region;
  private BigDecimal amount;
  private Currency currency;

  public ParseMoney(final String region) {

    this.region = region;
    this.amount = BigDecimal.ZERO;
    this.currency = TNECore.eco().currency().getDefaultCurrency(region);
  }

  public String region() {

    return region;
  }

  public BigDecimal amount() {
    return amount;
  }

  public void amount(final BigDecimal amount) {
    this.amount = amount;
  }

  public Currency currency() {
    return currency;
  }

  public void currency(final Currency currency) {
    this.currency = currency;
  }

  public boolean hasCurrency() {
    return currency != null;
  }
}