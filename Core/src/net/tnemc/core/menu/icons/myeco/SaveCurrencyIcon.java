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

import net.kyori.adventure.text.Component;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.menu.icons.shared.SwitchPageIcon;
import net.tnemc.menu.core.icon.action.ActionType;
import net.tnemc.menu.core.icon.action.impl.RunnableAction;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.MessageHandler;

import java.util.Collections;
import java.util.UUID;

import static net.tnemc.core.menu.MyEcoMenu.CURRENCIES_PAGE;

/**
 * SaveCurrencyIcon
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class SaveCurrencyIcon extends SwitchPageIcon {

  public SaveCurrencyIcon(final UUID id, final int slot, final Currency currency) {

    super(slot, PluginCore.server().stackBuilder().of("GREEN_WOOL", 1)
                  .display(Component.text(currency.getIdentifier())).lore(Collections.singletonList(MessageHandler.grab(new MessageData("Messages.Menu.MyEco.Currencies.Save"), id))),
          "my_eco", CURRENCIES_PAGE, ActionType.ANY, false);

    addAction(new RunnableAction((click->{


    })));

    addActions();
  }
}