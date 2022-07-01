package net.tnemc.core.account.holdings.modify;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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
