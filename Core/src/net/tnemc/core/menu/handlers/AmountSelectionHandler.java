package net.tnemc.core.menu.handlers;

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

import net.tnemc.menu.core.handlers.MenuClickHandler;

import java.math.BigDecimal;

/**
 * MenuCallback
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class AmountSelectionHandler {

  private final MenuClickHandler click;
  private final BigDecimal amount;

  public AmountSelectionHandler(MenuClickHandler click, BigDecimal amount) {
    this.click = click;
    this.amount = amount;
  }

  public MenuClickHandler getClick() {
    return click;
  }

  public BigDecimal getAmount() {
    return amount;
  }
}