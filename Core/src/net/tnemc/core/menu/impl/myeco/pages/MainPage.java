package net.tnemc.core.menu.impl.myeco.pages;

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
import net.tnemc.menu.core.Page;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.icon.ActionType;
import net.tnemc.menu.core.icon.action.SwitchMenuAction;
import net.tnemc.menu.core.icon.action.SwitchPageAction;

import java.util.Collections;

/**
 * MainPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MainPage extends Page {

  public MainPage() {
    super(1);

    icons.put(19, IconBuilder.of(TNECore.server()
                                     .stackBuilder()
                                     .of("GOLD_INGOT", 1)
                                     .display("Currency Editor")
                                     .lore(Collections.singletonList("Click to open currency editor.")))
        .withAction(new SwitchMenuAction("my_cur", ActionType.ANY))
        .create());

    icons.put(21, IconBuilder.of(TNECore.server()
                                     .stackBuilder()
                                     .of("COMPASS", 1)
                                     .display("World Editor")
                                     .lore(Collections.singletonList("Click to open world editor.")))
        //.withAction(new SwitchPageAction(2, ActionType.ANY))
        .create());

    icons.put(23, IconBuilder.of(TNECore.server()
                                     .stackBuilder()
                                     .of("REDSTONE", 1)
                                     .display("Configuration Editor")
                                     .lore(Collections.singletonList("Click to open config editor.")))
        .withAction(new SwitchPageAction(3, ActionType.ANY))
        .create());

    icons.put(25, IconBuilder.of(TNECore.server()
                                     .stackBuilder()
                                     .of("TORCH", 1)
                                     .display("Admin Tasks")
                                     .lore(Collections.singletonList("Click to open admin menu.")))
        .withAction(new SwitchPageAction(4, ActionType.ANY))
        .create());
  }
}