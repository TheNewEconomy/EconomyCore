package net.tnemc.core.api.callback.currency;
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

import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.Denomination;
import net.tnemc.plugincore.core.api.callback.Callback;

/**
 * DenominationLoadCallback
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class DenominationLoadCallback implements Callback {

  private Currency currency;
  private Denomination denomination;

  public DenominationLoadCallback(Currency currency, Denomination denomination) {
    this.currency = currency;
    this.denomination = denomination;
  }

  /**
   * The name of this callback.
   *
   * @return The name of this callback.
   */
  @Override
  public String name() {
    return "denomination_load";
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public Denomination getDenomination() {
    return denomination;
  }

  public void setDenomination(Denomination denomination) {
    this.denomination = denomination;
  }
}
