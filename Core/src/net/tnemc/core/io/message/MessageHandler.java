package net.tnemc.core.io.message;


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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.UUID;

/**
 * The core class for translating plugin messages
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MessageHandler {

  final MiniMessage mini;
  final TranslationProvider translator;

  private static MessageHandler instance;


  public MessageHandler(final TranslationProvider translator) {
    this.translator = translator;
    this.mini = MiniMessage.builder()
        .tags(TagResolver.standard()).build();

    instance = this;
  }

  public MessageHandler(final TranslationProvider translator, final TagResolver... resolvers) {
    this.translator = translator;
    this.mini = MiniMessage.builder().tags(TagResolver.builder().resolvers(resolvers).build())
        .build();

    instance = this;
  }

  /**
   * Used to translate a message for a player and return the translated {@link Component}.
   *
   * @param message The message to translate. This could also be a node that points to a message in
   *                a configuration file.
   * @param id The {@link UUID unique identifier} of the player to translate this for.
   *
   * @return The {@link Component} that is the result of the translation process of the message for
   * the given player.
   */
  public static Component grab(String message, UUID id) {
    return instance.mini.deserialize(instance.translator.translate(id, message));
  }

  /**
   * Used to translate a message for an {@link Audience}.
   *
   * @param message The message to translate. This could also be a node that points to a message in
   *                a configuration file.
   * @param audience The audience that should receive the translated message.
   */
  public static void translate(String message, UUID identifier, Audience audience) {
    audience.sendMessage(instance.mini.deserialize(instance.translator.translateNode(message, "default")));
  }

  /**
   * Used to translate a message for numerous players.
   *
   * @param message The message to translate. This could also be a node that points to a message in
   *                a configuration file.
   * @param audiences The audiences that should receive the translated message.
   */
  public static void translate(String message, UUID identifier, Audience... audiences) {
    for(Audience a : audiences) {
      a.sendMessage(instance.mini.deserialize(instance.translator.translateNode(message, "default")));
    }
  }

  public MiniMessage getMini() {
    return mini;
  }

  public TranslationProvider getTranslator() {
    return translator;
  }

  public static MessageHandler getInstance() {
    return instance;
  }
}