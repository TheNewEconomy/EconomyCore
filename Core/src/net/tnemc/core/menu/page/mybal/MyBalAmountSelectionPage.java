package net.tnemc.core.menu.page.mybal;

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
import net.tnemc.core.menu.MyBalMenu;
import net.tnemc.core.menu.handlers.AmountSelectionHandler;
import net.tnemc.core.menu.page.shared.AmountSelectionPage;
import net.tnemc.item.providers.SkullProfile;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.icon.action.impl.SwitchPageAction;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * MyBalAmountSelectionPage
 *
 * @author creatorfromhell
 * @since 0.1.3.0
 */
public class MyBalAmountSelectionPage extends AmountSelectionPage {

  public MyBalAmountSelectionPage(String amtID, String returnMenu, String menuName, int menuPage,
                                  int returnPage, final Consumer<AmountSelectionHandler> selectionListener) {
    super(amtID, returnMenu, menuName, menuPage, returnPage, selectionListener);
  }

  @Override
  public void handle(PageOpenCallback callback) {
    super.handle(callback);

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("STONE_BUTTON", 1)
              .display(Component.text("Add Max"))
              .lore(Collections.singletonList(Component.text("Adds your entire balance."))))
              .withActions(new SwitchPageAction(menuName, menuPage))
              .withClick((click)->balAddClick(click, ((BigDecimal)viewer.get().dataOrDefault(MyBalMenu.ACTION_MAX_HOLDINGS, BigDecimal.ZERO))))
              .withSlot(31)
              .build());

      final Optional<Object> name = viewer.get().findData(MyBalMenu.ACTION_ACCOUNT_ID + "_NAME");
      final Optional<Object> id = viewer.get().findData(MyBalMenu.ACTION_ACCOUNT_ID + "_ID");
      if(name.isPresent() && id.isPresent()) {

        SkullProfile profile = null;
        try {

          final UUID account = UUID.fromString((String)id.get());
          profile = new SkullProfile();

          if(PluginCore.server().playedBefore(account)) {
            profile.setUuid(account);
          }

        } catch(Exception ignore) {}

        callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PLAYER_HEAD", 1)
                .display(Component.text((String)name.get()))
                .lore(Collections.singletonList(Component.text("Player action will be performed on.")))
                .profile(profile))
                .withActions(new SwitchPageAction(menuName, menuPage))
                .withSlot(13)
                .build());
      }
    }
  }
}
