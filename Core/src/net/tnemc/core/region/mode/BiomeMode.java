package net.tnemc.core.region.mode;

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
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.region.RegionMode;
import net.tnemc.core.region.RegionType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * BiomeMode represents a {@link RegionMode} based on biomes.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 *
 * @see RegionMode
 */
public class BiomeMode implements RegionMode {
  @Override
  public String name() {
    return "biome";
  }

  @Override
  public List<RegionType> accepted() {
    return Collections.singletonList(RegionType.BIOME);
  }

  @Override
  public String region(@NotNull PlayerProvider player) {

    if(!MainConfig.yaml().getBoolean("Core.Region.MultiRegion")) {
      return TNECore.eco().region().resolve(TNECore.eco().region().defaultRegion());
    }
    return TNECore.eco().region().resolve(player.biome());
  }
}