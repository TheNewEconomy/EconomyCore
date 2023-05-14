package net.tnemc.core.region;
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
import net.tnemc.core.compatibility.PlayerProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * RegionMode
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface RegionMode {

  String name();

  List<RegionType> accepted();

  default String defaultRegion() {
    return TNECore.eco().region().defaultRegion();
  }

  default String region(@NotNull final UUID player) {
    final Optional<PlayerProvider> provider = TNECore.server().findPlayer(player);
    if(provider.isPresent()) {
      return region(provider.get());
    }
    return defaultRegion();
  }

  String region(@NotNull final PlayerProvider player);
}