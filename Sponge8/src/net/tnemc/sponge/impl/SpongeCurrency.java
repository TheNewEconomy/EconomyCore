package net.tnemc.sponge.impl;
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

import net.kyori.adventure.text.Component;
import net.tnemc.core.currency.format.CurrencyFormatter;
import org.spongepowered.api.service.economy.Currency;

import java.math.BigDecimal;

/**
 * SpongeCurrency
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeCurrency implements Currency {

  private final net.tnemc.core.currency.Currency currency;

  public SpongeCurrency(net.tnemc.core.currency.Currency currency) {
    this.currency = currency;
  }

  @Override
  public Component displayName() {
    return Component.text(currency.getDisplay());
  }

  @Override
  public Component pluralDisplayName() {
    return Component.text(currency.getDisplayPlural());
  }

  @Override
  public Component symbol() {
    return Component.text(currency.getSymbol());
  }

  @Override
  public Component format(BigDecimal amount, int numFractionDigits) {
    return Component.text(CurrencyFormatter.format(null, amount));
  }

  @Override
  public int defaultFractionDigits() {
    return currency.getDecimalPlaces();
  }

  @Override
  public boolean isDefault() {
    return currency.isGlobalDefault();
  }
}
