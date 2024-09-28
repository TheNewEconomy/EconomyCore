package net.tnemc.core.command;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.manager.TransactionManager;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.history.AwayHistory;
import net.tnemc.core.transaction.history.SortedHistory;
import net.tnemc.plugincore.core.compatibility.CmdSource;
import net.tnemc.plugincore.core.io.message.MessageData;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * TransactionCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TransactionCommand {

  //[page #]
  public static void away(CmdSource<?> sender, int page) {

    final Optional<Account> account = BaseCommand.account(sender, "away");

    if(sender.player().isPresent() && MainConfig.yaml().getBoolean("Core.Commands.GUIAlternatives", true)) {
      sender.player().get().inventory().openMenu(sender.player().get(), "transaction_away");
      return;
    }

    if(account.isEmpty()) {
      sender.message(new MessageData("Messages.Transaction.AwayNone"));
      return;
    }

    final Optional<AwayHistory> away = account.get().away(((PlayerAccount)account.get()).getUUID());
    if(away.isEmpty()) {
      sender.message(new MessageData("Messages.Transaction.AwayNone"));
      return;
    }

    final MessageData heading = new MessageData("Messages.Transaction.Away");
    heading.addReplacement("$page", String.valueOf(page));
    heading.addReplacement("$page_top", String.valueOf(away.get().maxPages()));
    sender.message(heading);

    for(Map.Entry<Long, UUID> entry : away.get().getPage(page).entrySet()) {

      final Optional<Receipt> receipt = account.get().findReceipt(entry.getValue());
      if(receipt.isPresent()) {

        final MessageData awayEntry = new MessageData("Messages.Transaction.AwayEntry");
        awayEntry.addReplacement("$id", entry.getValue().toString());
        awayEntry.addReplacement("$type", receipt.get().getType());
        sender.message(awayEntry);
      }
    }
  }

  //[page:#] [world:name/all] [player:name]
  public static void history(CmdSource<?> sender, int page, String region, Account account) {

    if(sender.player().isPresent() && MainConfig.yaml().getBoolean("Core.Commands.GUIAlternatives", true)) {
      sender.player().get().inventory().openMenu(sender.player().get(), "transaction_menu");
      return;
    }

    region = TNECore.eco().region().resolve(region);

    final SortedHistory sorted = account.getSorted(account.getIdentifier());

    if(sorted == null || sorted.getReceipts().isEmpty()) {
      sender.message(new MessageData("Messages.Transaction.HistoryNone"));
      return;
    }

    final MessageData heading = new MessageData("Messages.Transaction.History");
    heading.addReplacement("$page", String.valueOf(page));
    heading.addReplacement("$page_top", String.valueOf(sorted.maxPages()));
    sender.message(heading);

    for(Map.Entry<Long, UUID> entry : sorted.getPage(page).entrySet()) {

      final Optional<Receipt> receipt = account.findReceipt(entry.getValue());
      if(receipt.isPresent()) {

        String from = "None";
        final Optional<Account> fromACC = receipt.get().getFrom().asAccount();
        if(fromACC.isPresent()) {
          from = fromACC.get().getName();
        }

        String to = "None";
        final Optional<Account> toACC = receipt.get().getTo().asAccount();
        if(toACC.isPresent()) {
          to = toACC.get().getName();
        }

        final MessageData awayEntry = new MessageData("Messages.Transaction.HistoryEntry");
        awayEntry.addReplacement("$id", entry.getValue().toString());
        awayEntry.addReplacement("$type", receipt.get().getType());
        awayEntry.addReplacement("$initiator", from);
        awayEntry.addReplacement("$recipient", to);
        sender.message(awayEntry);
      }
    }
  }

  //<uuid>
  public static void info(CmdSource<?> sender, UUID uuid) {

    final Optional<Receipt> receipt = TransactionManager.receipts().getReceiptByUUID(uuid);

    if(receipt.isEmpty()) {
      final MessageData message = new MessageData("Messages.Transaction.Invalid");
      message.addReplacement("$transaction", uuid.toString());
      sender.message(message);
      return;
    }

    final MessageData message = new MessageData("Messages.Transaction.Info");
    message.addReplacement("$id", uuid.toString());
    message.addReplacement("$type", receipt.get().getType());
    sender.message(message);
  }

  //<uuid>
  public static void voidT(CmdSource<?> sender, Account account, UUID uuid) {

    final Optional<Receipt> receipt = account.findReceipt(uuid);

    if(receipt.isEmpty()) {
      final MessageData message = new MessageData("Messages.Transaction.Invalid");
      message.addReplacement("$transaction", uuid.toString());
      sender.message(message);
      return;
    }

    if(receipt.get().isVoided()) {
      final MessageData message = new MessageData("Messages.Transaction.Already");
      message.addReplacement("$transaction", uuid.toString());
      sender.message(message);
      return;
    }

    final boolean voided = receipt.get().voidTransaction();
    if(!voided) {
      final MessageData message = new MessageData("Messages.Transaction.Unable");
      message.addReplacement("$transaction", uuid.toString());
      sender.message(message);
      return;
    }
    final MessageData message = new MessageData("Messages.Transaction.Voided");
    message.addReplacement("$transaction", uuid.toString());
    sender.message(message);
  }
}
