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
import net.tnemc.core.menu.impl.shared.icons.PreviousPageIcon;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.page.Page;

import java.util.Collections;

/**
 * MainPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class AdminPage extends Page {

  public AdminPage() {
    super(4);

    icons.put(0, new PreviousPageIcon(0, 1));

    icons.put(19, IconBuilder.of(TNECore.server()
                                     .stackBuilder()
                                     .of("RED_WOOL", 1)
                                     .display("Reset Economy")
                                     .lore(Collections.singletonList("Click to reset the entire economy.")))
        .click(click -> {
          //TODO: Reset Economy, confirmation menu support in TNML?
        })
        .create());

    icons.put(21, IconBuilder.of(TNECore.server()
                                     .stackBuilder()
                                     .of("LAVA_BUCKET", 1)
                                     .display("Purge Economy")
                                     .lore(Collections.singletonList("Click to purge old accounts.")))
        .click(click -> {
          //TODO: Purge code.
        })
        .create());

    //TODO: Later on TNE will have admin tasks, such as balance-related alerts that could alert
    //admins to duping.
  }
}