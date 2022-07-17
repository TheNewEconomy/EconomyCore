package research;

/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.tnemc.core.io.message.TranslationProvider;
import org.bukkit.entity.Player;

/**
 * The core class for translating plugin messages
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MessageHandler {

  final MiniMessage mini;
  final BukkitAudiences audiences;
  final TranslationProvider translator;

  private static MessageHandler instance;


  public MessageHandler(final TranslationProvider translator, final BukkitAudiences audiences) {
    this.translator = translator;
    this.audiences = audiences;
    this.mini = MiniMessage.builder()
        .tags(TagResolver.standard()).build();

    instance = this;
  }

  public MessageHandler(final TranslationProvider translator, final BukkitAudiences audiences, final TagResolver... resolvers) {
    this.translator = translator;
    this.audiences = audiences;
    this.mini = MiniMessage.builder().tags(TagResolver.builder().resolvers(resolvers).build())
        .build();

    instance = this;
  }
  /**
   * Used to translate a message for a player and return the translated {@link Component}.
   *
   * @param message The message to translate. This could also be a node that points to a message in
   *                a configuration file.
   * @param player The player to translate the message for.
   *
   * @return The {@link Component} that is the result of the translation process of the message for
   * the given player.
   */
  public static Component grab(String message, Player player) {
    return instance.mini.deserialize(instance.translator.translate(player.getUniqueId(),
                                                                         message));
  }

  /**
   *
   * @param message
   * @param player
   */
  public static void translate(String message, Player player) {
    instance.audiences.player(player).sendMessage(instance.mini.deserialize(message));
  }

  /**
   * Used to translate a message for numerous players.
   *
   * @param message The message to translate. This could also be a node that points to a message in
   *                a configuration file.
   * @param players The players to send the translated message to.
   */
  public static void translate(String message, Player... players) {
    for(Player p : players) {
      instance.audiences.player(p).sendMessage(instance.mini.deserialize(message));
    }
  }
}