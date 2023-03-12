package net.tnemc.core.menu.impl.mycurrency.pages;

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
import net.tnemc.core.currency.Currency;
import net.tnemc.menu.core.Page;
import net.tnemc.menu.core.builder.IconBuilder;

import java.util.Arrays;

/**
 * CurrencyMainPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyMainPage extends Page {

  public CurrencyMainPage() {
    super(1);

    int i = 10;

    for(Currency currency : TNECore.eco().currency().currencies()) {
      icons.put(i, IconBuilder.of(TNECore.server()
                                       .stackBuilder()
                                       .of("PAPER", 1)
                                       .display(currency.getIdentifier())
                                       .lore(Arrays.asList("Left Click to View",
                                                           "Middle Click to Delete",
                                                           "Right Click to Edit"
                                       )))
          .create());

      i += 2;
    }
  }
}