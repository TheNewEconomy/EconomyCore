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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.utils.Monetary;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Represents an entry for holdings. This contains all the information including region, currency
 * and the actual BigDecimal Holdings.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class HoldingsEntry {

  /**
   * The name of the region involved. This is usually a world, but could be something else such as a
   * world guard region name/identifier.
   */
  private String region;

  /**
   * The identifier of the currency involved.
   */
  private String currency;

  /**
   * The {@link BigDecimal amount} that this charge is for.
   */
  private BigDecimal amount;

  /**
   * The {@link HoldingsType type} that this should be used for this.
   */
  private HoldingsType type = HoldingsType.VIRTUAL_HOLDINGS;

  /**
   * Constructs an object that represents a holding's entry.
   *
   * @param region The name of the region involved. This is usually a world, but could be something
   *               else such as a world guard region name/identifier.
   * @param currency The identifier of the currency involved.
   * @param amount The {@link BigDecimal amount} that this charge is for.
   */
  public HoldingsEntry(final @NotNull String region, final @NotNull String currency,
                       final @NotNull BigDecimal amount, final @NotNull HoldingsType type) {
    this.region = region;
    this.currency = currency;
    this.amount = amount;
    this.type = type;
  }

  /**
   * Constructs an object that represents a holdings entry from a {@link HoldingsModifier}.
   *
   * @param modifier The modifier to build this entry from.
   */
  public HoldingsEntry(final @NotNull HoldingsModifier modifier) {
    this.region = modifier.getRegion();
    this.currency = modifier.getCurrency();
    this.amount = modifier.getModifier();
  }

  public void modify(final HoldingsModifier modifier) {
    amount = modifier.modify(amount);
  }

  public HoldingsEntry modifyGrab(final BigDecimal modifier) {
    HoldingsEntry entry = new HoldingsEntry(region, currency, amount, type);
    entry.modify(new HoldingsModifier(region, currency, modifier));
    return entry;
  }

  public HoldingsEntry modifyGrab(final HoldingsModifier modifier) {
    HoldingsEntry entry = new HoldingsEntry(region, currency, amount, type);
    entry.modify(modifier);
    return entry;
  }

  public Optional<Currency> currency() {
    return TNECore.eco().currency().findCurrency(currency);
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Monetary asMonetary() {
    final Optional<Currency> cur = currency();
    return new Monetary(amount, cur.map(Currency::getDecimalPlaces).orElse(2));
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public HoldingsType getType() {
    return type;
  }

  public void setType(HoldingsType type) {
    this.type = type;
  }
}