package net.tnemc.core.account.holdings.modify;

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

import net.tnemc.core.account.Account;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Represents a type of operation that should be performed during an {@link Account account's}
 * holdings modification.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public enum HoldingsOperation {

  ADD {
    @Override
    public BigDecimal perform(final BigDecimal value, final BigDecimal modifier) {
      return super.perform(value, modifier);
    }
  },
  SUBTRACT {
    @Override
    public BigDecimal perform(final BigDecimal value, final BigDecimal modifier) {
      return value.subtract(modifier);
    }
  },
  MULTIPLY {
    @Override
    public BigDecimal perform(final BigDecimal value, final BigDecimal modifier) {
      return value.multiply(modifier);
    }
  },
  SET {
    @Override
    public BigDecimal perform(final BigDecimal value, final BigDecimal modifier) {
      return modifier;
    }
  },
  DIVIDE {
    @Override
    public BigDecimal perform(final BigDecimal value, final BigDecimal modifier) {
      return value.divide(modifier, RoundingMode.valueOf(9));
    }
  };


  public BigDecimal perform(final BigDecimal value, final BigDecimal modifier) {
    return value.add(modifier);
  }

}
