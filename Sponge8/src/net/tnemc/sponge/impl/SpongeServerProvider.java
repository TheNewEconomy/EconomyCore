package net.tnemc.sponge.impl;

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

import net.tnemc.core.compatibility.CmdSource;
import net.tnemc.core.compatibility.LogProvider;
import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.ProxyProvider;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.core.compatibility.helper.CraftingRecipe;
import net.tnemc.core.compatibility.scheduler.SchedulerProvider;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.region.RegionMode;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.sponge.SpongeCore;
import net.tnemc.sponge.SpongeItemCalculationsProvider;
import net.tnemc.sponge.SpongeItemStack;
import net.tnemc.sponge.impl.scheduler.SpongeScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.world.DefaultWorldKeys;
import org.spongepowered.api.world.server.ServerWorld;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.sponge.SpongeCommandActor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

/**
 * SpongeServerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeServerProvider implements ServerConnector {

  private final SpongeItemCalculationsProvider calc = new SpongeItemCalculationsProvider();
  private final SpongeProxyProvider proxy = new SpongeProxyProvider();

  private final SpongeScheduler scheduler;

  public SpongeServerProvider() {
    this.scheduler = new SpongeScheduler();
  }

  @Override
  public String name() {
    return "sponge";
  }

  /**
   * Used to replace placeholders from a string.
   *
   * @param player The player to use for the placeholder replacement.
   * @param message The message to replace placeholders in.
   *
   * @return The string after placeholders have been replaced.
   */
  @Override
  public String replacePlaceholder(UUID player, String message) {
    return message;
  }

  /**
   * The proxy provider to use for this implementation.
   *
   * @return The proxy provider to use for this implementation.
   */
  @Override
  public ProxyProvider proxy() {
    return proxy;
  }

  /**
   * Used to convert an {@link CommandActor} to a {@link CmdSource}.
   *
   * @param actor The command actor.
   *
   * @return The {@link CmdSource} for this actor.
   */
  @Override
  public CmdSource<?> source(@NotNull CommandActor actor) {
    return new SpongeCMDSource((SpongeCommandActor)actor);
  }

  /**
   * Used to get the amount of online players.
   *
   * @return The amount of online players.
   */
  @Override
  public int onlinePlayers() {
    return Sponge.server().onlinePlayers().size();
  }

  /**
   * Attempts to find a {@link PlayerProvider player} based on an {@link UUID identifier}.
   *
   * @param identifier The identifier
   *
   * @return An Optional containing the located {@link PlayerProvider player}, or an empty
   * Optional if no player is located.
   */
  @Override
  public Optional<PlayerProvider> findPlayer(@NotNull UUID identifier) {
    final Optional<ServerPlayer> player = Sponge.server().player(identifier);
    return player.map(value->new SpongePlayerProvider(value.user(), SpongeCore.instance().getContainer()));
  }

  /**
   * Used to determine if this player has played on this server before.
   *
   * @param identifier The {@link UUID} that is associated with the player.
   *
   * @return True if the player has played on the server before, otherwise false.
   */
  @Override
  public boolean playedBefore(UUID identifier) {
    final Optional<ServerPlayer> player = Sponge.server().player(identifier);
    return player.map(ServerPlayer::hasPlayedBefore).orElse(false);
  }

  /**
   * Used to determine if a player with the specified username has played
   * before.
   *
   * @param name The username to search for.
   *
   * @return True if someone with the specified username has played before,
   * otherwise false.
   */
  @Override
  public boolean playedBefore(String name) {
    final Optional<ServerPlayer> player = Sponge.server().player(name);
    return player.map(ServerPlayer::hasPlayedBefore).orElse(false);
  }

  /**
   * Used to determine if a player with the specified username is online.
   *
   * @param name The username to search for.
   *
   * @return True if someone with the specified username is online.
   */
  @Override
  public boolean online(String name) {
    try {

      final Optional<ServerPlayer> player = Sponge.server().player(UUID.fromString(name));
      return player.map(ServerPlayer::isOnline).orElse(false);
    } catch (Exception e) {

      final Optional<ServerPlayer> player = Sponge.server().player(name);
      return player.map(ServerPlayer::isOnline).orElse(false);
    }
  }

  @Override
  public Optional<UUID> fromName(String name) {
    return Optional.empty();
  }

  /**
   * Used to locate a username for a specific name. This could be called from either a primary or
   * secondary thread, and should not call back to the Mojang API ever.
   *
   * @param id The {@link UUID} to use for the search.
   *
   * @return An optional containing the name if exists, otherwise false.
   */
  @Override
  public Optional<String> fromID(UUID id) {
    return Optional.empty();
  }

  /**
   * Returns the name of the default region.
   *
   * @param mode The {@link RegionMode} to use for this.
   *
   * @return The name of the default region. This could be different based on the current
   * {@link RegionMode}.
   */
  @Override
  public String defaultRegion(final RegionMode mode) {

    final Optional<ServerWorld> world = Sponge.server().worldManager().world(DefaultWorldKeys.DEFAULT);
    if(mode.name().equalsIgnoreCase("biome")) {
      if(world.isPresent()) {
        return world.get().biome(world.get().properties().spawnPosition()).toString();
      }
    }
    return world.map(serverWorld -> serverWorld.key().value()).orElse("world");
  }

  /**
   * Used to replace colour codes in a string.
   *
   * @param string The string to format.
   *
   * @return The formatted string.
   */
  @Override
  public String replaceColours(String string) {
    return null;
  }

  @Override
  public AbstractItemStack<?> stackBuilder() {
    return new SpongeItemStack();
  }

  @Override
  public void saveResource(String resourcePath, boolean replace) {
    if (resourcePath != null && !resourcePath.equals("")) {
      resourcePath = resourcePath.replace('\\', '/');
      final LogProvider logger = SpongeCore.log();
      InputStream in = this.getResource(resourcePath);
      if (in == null) {
        throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in the jar.");
      } else {
        final File outFile = new File(SpongeCore.directory(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf(47);
        File outDir = new File(SpongeCore.directory(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
        if (!outDir.exists()) {
          outDir.mkdirs();
        }

        try {
          if (outFile.exists() && !replace) {
            final Level var10001 = Level.WARNING;
            final String var10002 = outFile.getName();
            //logger.log(var10001, "Could not save " + var10002 + " to " + outFile + " because " + outFile.getName() + " already exists.");
          } else {
            OutputStream out = new FileOutputStream(outFile);
            byte[] buf = new byte[1024];

            int len;
            while((len = in.read(buf)) > 0) {
              out.write(buf, 0, len);
            }

            out.close();
            in.close();
          }
        } catch (IOException var10) {
          logger.error("Could not save " + outFile.getName() + " to " + outFile);
        }

      }
    } else {
      throw new IllegalArgumentException("ResourcePath cannot be null or empty");
    }
  }

  /**
   * Provides this implementation's {@link SchedulerProvider scheduler}.
   *
   * @return The scheduler for this implementation.
   */
  @Override
  public SpongeScheduler scheduler() {
    return scheduler;
  }

  /**
   * Used to register a crafting recipe to the server.
   *
   * @param recipe The crafting recipe to register.
   *
   * @see CraftingRecipe
   */
  @Override
  public void registerCrafting(@NotNull CraftingRecipe recipe) {
    //TODO: Sponge Register crafting
  }

  @Override
  public SpongeItemCalculationsProvider calculations() {
    return calc;
  }

  @Override
  public SpongeItemStack denominationToStack(ItemDenomination denomination, int amount) {
    return new SpongeItemStack().of(denomination.getMaterial(), amount)
            .enchant(denomination.getEnchantments())
            .lore(denomination.getLore())
            .flags(denomination.getFlags())
            .damage(denomination.getDamage())
            .display(denomination.getName())
            .modelData(denomination.getCustomModel());
  }

  @Override
  public SpongeItemCalculations itemCalculations() {
    return new SpongeItemCalculations();
  }

  public @Nullable InputStream getResource(@NotNull String filename) {
    try {
      URL url = this.getClass().getClassLoader().getResource(filename);
      if (url == null) {
        return null;
      } else {
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
      }
    } catch (IOException var4) {
      return null;
    }
  }
}
