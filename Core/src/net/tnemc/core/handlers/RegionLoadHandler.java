package net.tnemc.core.handlers;
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
import net.tnemc.core.region.RegionType;
import net.tnemc.core.utils.HandlerResponse;

/**
 * This class is utilized to handle regions being loaded. In TNE, a region could be anything from a
 * world guard region to a world.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class RegionLoadHandler {

  public HandlerResponse handle(final String region, final RegionType type) {
    TNECore.eco().region().initializeRegion(region, type);

    return new HandlerResponse("", false);
  }
}