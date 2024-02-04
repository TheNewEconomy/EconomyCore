package net.tnemc.core.transaction.tax;

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

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Represents an entry for taxation that includes things such as tax amount.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public record TaxEntry(String type, Double amount) {

  public BigDecimal calculateTax(final BigDecimal amount) {

    final Optional<TaxType> type = TNECore.eco().transaction().findTax(this.type);

    if(type.isPresent()) {
      return type.get().handleTaxation(amount, new BigDecimal(this.amount));
    }

    return BigDecimal.ZERO;
  }

  public String asString() {

    final Optional<TaxType> type = TNECore.eco().transaction().findTax(this.type);
    if(type.isPresent()) {
      return type.get().asString(new BigDecimal(this.amount));
    }
    return "0.0";
  }
}