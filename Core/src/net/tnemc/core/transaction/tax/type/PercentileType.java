package net.tnemc.core.transaction.tax.type;

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

import net.tnemc.plugincore.core.io.maps.MapKey;
import net.tnemc.core.transaction.tax.TaxType;

import java.math.BigDecimal;

/**
 * Represents a percentile taxation type. This is taken as a percentage of the total amount.
 *
 * @see TaxType
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PercentileType implements TaxType {
  @Override
  @MapKey
  public String name() {
    return "percent";
  }

  @Override
  public String asString(final BigDecimal tax) {
    return tax.multiply(new BigDecimal(100)).toPlainString() + "%";
  }

  @Override
  public BigDecimal handleTaxation(final BigDecimal amount, final BigDecimal tax) {
    return amount.multiply(tax);
  }
}