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

import net.tnemc.core.api.callback.TNECallbacks;
import net.tnemc.core.currency.Currency;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.plugincore.core.api.callback.Callback;

import java.util.Collection;
import java.util.UUID;

/**
 * CurrencyDropCallback
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyDropCallback implements Callback {

  private final UUID player;
  private final Collection<AbstractItemStack<Object>> drops;
  private Currency currency;

  public CurrencyDropCallback(final UUID player, final Currency currency, final Collection<AbstractItemStack<Object>> drops) {

    this.player = player;
    this.currency = currency;
    this.drops = drops;
  }

  /**
   * The name of this callback.
   *
   * @return The name of this callback.
   */
  @Override
  public String name() {

    return TNECallbacks.CURRENCY_DROP.id();
  }

  public Collection<AbstractItemStack<Object>> getDrops() {

    return drops;
  }

  public Currency getCurrency() {

    return currency;
  }

  public void setCurrency(final Currency currency) {

    this.currency = currency;
  }

  public UUID getPlayer() {

    return player;
  }
}
