package net.tnemc.core.menu.icons.myeco;
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
import net.tnemc.core.currency.item.ItemCurrency;
import net.tnemc.core.menu.icons.shared.SwitchPageIcon;
import net.tnemc.menu.core.icon.action.ActionType;
import net.tnemc.menu.core.icon.action.impl.DataAction;
import net.tnemc.plugincore.PluginCore;

import java.util.Collections;

import static net.tnemc.core.menu.MyEcoMenu.CURRENCY_EDIT_PAGE;

/**
 * CurrencyIcon
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class CurrencyIcon extends SwitchPageIcon {
  public CurrencyIcon(int slot, final Currency currency) {
    super(slot, PluginCore.server().stackBuilder().of(currency.getIconMaterial(), 1)
            .display(currency.getIdentifier()).lore(Collections.singletonList("Click to edit currency")),
            "my_eco", CURRENCY_EDIT_PAGE, ActionType.ANY, false);

    actions.add(new DataAction("CURRENCY_OBJECT", currency));

    //Basic Information
    actions.add(new DataAction("CURRENCY_UUID", currency.getUid().toString()));
    actions.add(new DataAction("CURRENCY_IDENTIFIER", currency.getIdentifier()));

    actions.add(new DataAction("CURRENCY_STARTING", currency.getStartingHoldings()));
    actions.add(new DataAction("CURRENCY_MAX", currency.getMaxBalance()));
    actions.add(new DataAction("CURRENCY_MIN", currency.getMinBalance()));
    actions.add(new DataAction("CURRENCY_ICON", currency.getIconMaterial()));

    if(currency instanceof ItemCurrency itemCurrency) {

      actions.add(new DataAction("CURRENCY_ENDER", (itemCurrency.canEnderChest())? "ENABLED" : "DISABLED"));
      actions.add(new DataAction("CURRENCY_ENDER_FILL", (itemCurrency.isEnderFill())? "ENABLED" : "DISABLED"));
    }

    //Type
    actions.add(new DataAction("CURRENCY_TYPE", currency.type().name()));

    //Format

    //Note

    addActions();
  }
}