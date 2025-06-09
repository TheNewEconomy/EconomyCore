package net.tnemc.core.menu.page.shared;
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
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.manager.TransactionManager;
import net.tnemc.core.menu.icons.actions.PageSwitchWithClose;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.item.providers.SkullProfile;
import net.tnemc.menu.core.builder.IconBuilder;
import net.tnemc.menu.core.callbacks.page.PageOpenCallback;
import net.tnemc.menu.core.viewer.MenuViewer;
import net.tnemc.plugincore.PluginCore;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * TransactionInfoPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TransactionInfoPage {

  private final String returnMenu;
  private final int returnPage;
  private final String transactionID;

  public TransactionInfoPage(final String returnMenu, final int returnPage, final String transactionID) {

    this.returnMenu = returnMenu;
    this.returnPage = returnPage;
    this.transactionID = transactionID;
  }

  public void handle(final PageOpenCallback callback) {

    final Optional<MenuViewer> viewer = callback.getPlayer().viewer();
    if(viewer.isPresent()) {

      callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("BARRIER", 1)
                                                         .customName(Component.text("Escape Menu"))
                                                         .lore(Collections.singletonList(Component.text("Click to exit this menu."))))
                                         .withActions(new PageSwitchWithClose(returnMenu, returnPage))
                                         .withSlot(0)
                                         .build());

      final Optional<Object> transactionObj = viewer.get().findData(transactionID);
      if(transactionObj.isEmpty()) {
        return;
      }

      try {

        final UUID id = UUID.fromString((String)transactionObj.get());
        final Optional<Receipt> receipt = TransactionManager.receipts().getReceiptByUUID(id);
        if(receipt.isPresent()) {

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("CLOCK", 1)
                                                             .customName(Component.text(TNECore.eco().transaction().getFormat().format(new Date(receipt.get().getTime()))))
                                                             .lore(Collections.singletonList(Component.text("Time of Transaction"))))
                                             .withSlot(2)
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("NAME_TAG", 1)
                                                             .customName(Component.text(id.toString()))
                                                             .lore(Collections.singletonList(Component.text("UUID of Transaction"))))
                                             .withSlot(4)
                                             .build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .customName(Component.text(receipt.get().getType()))
                                                             .lore(Collections.singletonList(Component.text("Type of Transaction"))))
                                             .withSlot(6)
                                             .build());

          final String source = (receipt.get().getSource() != null)? receipt.get().getSource().asString() : "No Source Logged";
          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PAPER", 1)
                                                             .customName(Component.text(source))
                                                             .lore(Collections.singletonList(Component.text("Source of Transaction"))))
                                             .withSlot(8)
                                             .build());

          if(receipt.get().getModifierTo() != null && receipt.get().getTo() != null) {

            final HoldingsModifier modifier = receipt.get().getModifierTo();
            final Optional<Account> to = receipt.get().getTo().asAccount();

            final String name = (to.isPresent())? to.get().getName() : "No Name";
            SkullProfile profile = null;
            try {

              if(to.isPresent() && to.get() instanceof final PlayerAccount playerAccount) {
                profile = new SkullProfile();

                if(PluginCore.server().playedBefore(playerAccount.getUUID())) {
                  profile.uuid(playerAccount.getUUID());
                }
              }

            } catch(final Exception ignore) { }

            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PLAYER_HEAD", 1)
                                                               .profile(profile)
                                                               .customName(Component.text(name))
                                                               .lore(Collections.singletonList(Component.text("To Account"))))
                                               .withSlot(24)
                                               .build());

            final Currency cur = TNECore.eco().currency().findOrDefault(modifier.getCurrency());
            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(cur.getIconMaterial(), 1)
                                                               .customName(Component.text(cur.getIdentifier()))
                                                               .lore(Collections.singletonList(Component.text("Modifier Currency"))))
                                               .withSlot(27)
                                               .build());

            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("MAP", 1)
                                                               .customName(Component.text(modifier.getRegion()))
                                                               .lore(Collections.singletonList(Component.text("Modifier Region"))))
                                               .withSlot(28)
                                               .build());

            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                                                               .customName(Component.text(modifier.getModifier().toPlainString()))
                                                               .lore(Collections.singletonList(Component.text("Modifier Amount"))))
                                               .withSlot(29)
                                               .build());

            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("REDSTONE_TORCH", 1)
                                                               .customName(Component.text(modifier.getOperation().name()))
                                                               .lore(Collections.singletonList(Component.text("Modifier Operation"))))
                                               .withSlot(30)
                                               .build());
          }

          if(receipt.get().getModifierFrom() != null && receipt.get().getFrom() != null) {

            final HoldingsModifier modifier = receipt.get().getModifierFrom();
            final Optional<Account> from = receipt.get().getFrom().asAccount();

            final String name = (from.isPresent())? from.get().getName() : "No Name";
            SkullProfile profile = null;
            try {

              if(from.isPresent() && from.get() instanceof final PlayerAccount playerAccount) {
                profile = new SkullProfile();

                if(PluginCore.server().playedBefore(playerAccount.getUUID())) {
                  profile.uuid(playerAccount.getUUID());
                }
              }

            } catch(final Exception ignore) { }

            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("PLAYER_HEAD", 1)
                                                               .profile(profile)
                                                               .customName(Component.text(name))
                                                               .lore(Collections.singletonList(Component.text("From Account"))))
                                               .withSlot(19)
                                               .build());

            final Currency cur = TNECore.eco().currency().findOrDefault(modifier.getCurrency());
            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of(cur.getIconMaterial(), 1)
                                                               .customName(Component.text(cur.getIdentifier()))
                                                               .lore(Collections.singletonList(Component.text("Modifier Currency"))))
                                               .withSlot(32)
                                               .build());

            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("MAP", 1)
                                                               .customName(Component.text(modifier.getRegion()))
                                                               .lore(Collections.singletonList(Component.text("Modifier Region"))))
                                               .withSlot(33)
                                               .build());

            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("GOLD_INGOT", 1)
                                                               .customName(Component.text(modifier.getModifier().toPlainString()))
                                                               .lore(Collections.singletonList(Component.text("Modifier Amount"))))
                                               .withSlot(34)
                                               .build());

            callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("REDSTONE_TORCH", 1)
                                                               .customName(Component.text(modifier.getOperation().name()))
                                                               .lore(Collections.singletonList(Component.text("Modifier Operation"))))
                                               .withSlot(35)
                                               .build());
          }

          //barrier icons
          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("WHITE_GLASS_PANE", 1)
                                                             .customName(Component.text("")))
                                             .withSlot(18).build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("WHITE_GLASS_PANE", 1)
                                                             .customName(Component.text("")))
                                             .withSlot(20).build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("WHITE_GLASS_PANE", 1)
                                                             .customName(Component.text("")))
                                             .withSlot(21).build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("WHITE_GLASS_PANE", 1)
                                                             .customName(Component.text("")))
                                             .withSlot(22).build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("WHITE_GLASS_PANE", 1)
                                                             .customName(Component.text("")))
                                             .withSlot(31).build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("WHITE_GLASS_PANE", 1)
                                                             .customName(Component.text("")))
                                             .withSlot(23).build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("WHITE_GLASS_PANE", 1)
                                                             .customName(Component.text("")))
                                             .withSlot(25).build());

          callback.getPage().addIcon(new IconBuilder(PluginCore.server().stackBuilder().of("WHITE_GLASS_PANE", 1)
                                                             .customName(Component.text("")))
                                             .withSlot(26).build());
        }

      } catch(final Exception ignore) {
      }
    }
  }
}