package net.tnemc.core.io.message;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
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

  public MessageHandler(final TranslationProvider translator, final BukkitAudiences audiences, final TagResolver... resolvers) {
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
  public static void translate(String message, Audience audience) {
    audience.sendMessage(instance.mini.deserialize(message));
  }

  /**
   * Used to translate a message for numerous players.
   *
   * @param message The message to translate. This could also be a node that points to a message in
   *                a configuration file.
   * @param audiences The audiences that should receive the translated message.
   */
  public static void translate(String message, Audience... audiences) {
    for(Audience a : audiences) {
      a.sendMessage(instance.mini.deserialize(message));
    }
  }
}