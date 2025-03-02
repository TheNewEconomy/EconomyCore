package net.tnemc.paper.listener;
/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.tnemc.core.TNECore;
import net.tnemc.core.currency.parser.MoneyParser;
import net.tnemc.core.currency.parser.ParseMoney;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * PlayerChatListener
 *
 * @author creatorfromhell
 * @since 0.1.3.6
 */
public class PlayerChatListener implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInteract(final AsyncChatEvent event) {

    final Player player = event.getPlayer();
    final MoneyParser parser = new MoneyParser();
    final String region = TNECore.eco().region().resolve(event.getPlayer().getWorld().getName());

    final String message = PlainTextComponentSerializer.plainText().serialize(event.message());;
    System.out.println("Message: " + message);
    if(message.contains("parse:")) {

      System.out.println("Parsing");
      final ParseMoney parsed = parser.parse(region, message.replace("parse:", ""));

      System.out.println("Currency: " + parsed.currency().getIdentifier() + " Amount: " + parsed.amount().toPlainString());
      player.sendMessage("Currency: " + parsed.currency().getIdentifier() + " Amount: " + parsed.amount().toPlainString());
    }
  }
}