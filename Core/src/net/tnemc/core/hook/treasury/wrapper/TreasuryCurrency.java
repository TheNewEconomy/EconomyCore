package net.tnemc.core.hook.treasury.wrapper;
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

import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import net.tnemc.core.currency.format.CurrencyFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * TreasuryCurrency
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TreasuryCurrency implements Currency {

  private final net.tnemc.core.currency.Currency currency;

  public TreasuryCurrency(net.tnemc.core.currency.Currency currency) {
    this.currency = currency;
  }

  @Override
  public @NotNull String getIdentifier() {
    return currency.getIdentifier();
  }

  @Override
  public @NotNull String getSymbol() {
    return currency.getSymbol();
  }

  @Override
  public char getDecimal(@Nullable Locale locale) {
    return currency.getDecimal().charAt(0);
  }

  @Override
  public @NotNull Map<Locale, Character> getLocaleDecimalMap() {
    return new HashMap<>();
  }

  @Override
  public @NotNull String getDisplayName(@NotNull BigDecimal value, @Nullable Locale locale) {
    if(value.compareTo(BigDecimal.ONE) != 0) {
      return currency.getDisplayPlural();
    }
    return currency.getDisplay();
  }

  @Override
  public int getPrecision() {
    return currency.getDecimalPlaces();
  }

  @Override
  public boolean isPrimary() {
    return currency.isGlobalDefault();
  }

  @Override
  public @NotNull BigDecimal getStartingBalance(@NotNull Account account) {
    return currency.getStartingHoldings();
  }

  @Override
  public @NotNull BigDecimal getConversionRate() {
    return BigDecimal.ONE;
  }

  @Override
  public @NotNull CompletableFuture<BigDecimal> parse(@NotNull String formattedAmount, @Nullable Locale locale) {
    //TODO: complete this.
    return null;
  }

  @Override
  public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale) {
    return CurrencyFormatter.format(null, amount);
  }

  @Override
  public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale, int precision) {
    return format(amount, locale);
  }
}
