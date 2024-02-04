package net.tnemc.sponge.impl;
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

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.spongeapi.SpongeAudiences;
import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.io.message.MessageHandler;
import net.tnemc.sponge.SpongeCore;
import org.spongepowered.api.Sponge;
import revxrsal.commands.sponge.SpongeCommandActor;

import java.util.Optional;

/**
 * SpongeCMDSource
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeCMDSource extends CmdSource<SpongeCommandActor> {

  private final SpongePlayerProvider provider;

  public SpongeCMDSource(SpongeCommandActor actor) {
    super(actor);

    if(actor.isPlayer()) {
      provider = new SpongePlayerProvider(actor.getAsPlayer(),
                                          SpongeCore.instance().getPlugin());
    } else {
      provider = null;
    }
  }

  /**
   * Determines if this {@link CmdSource} is an instance of a player.
   *
   * @return True if this represents a player, otherwise false if it's a non-player such as the console.
   */
  @Override
  public boolean isPlayer() {
    return actor.isPlayer();
  }

  /**
   * Used to get the related {@link PlayerProvider} for this command source.
   *
   * @return An optional containing the related {@link PlayerProvider} if this command source is a
   * player, otherwise an empty {@link Optional}.
   */
  @Override
  public Optional<PlayerProvider> player() {
    return Optional.ofNullable(provider);
  }

  /**
   * Used to send a message to this command source.
   *
   * @param messageData The message data to utilize for this translation.
   */
  @Override
  public void message(final MessageData messageData) {

    try(SpongeAudiences provider = SpongeAudiences.create(SpongeCore.instance().getContainer(), Sponge.getGame())) {
      final Audience audience = (actor.isPlayer())? provider.player(actor.getAsPlayer()) : provider.console();
      MessageHandler.translate(messageData, identifier(), audience);
    }
  }
}