package net.tnemc.core.compatibility;
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

import java.util.Optional;
import java.util.UUID;

/**
 * CmdSource
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 * @see PlayerProvider
 */
public interface CmdSource {

  /**
   * The UUID of this command source.
   * @return The UUID of this command source.
   */
  UUID identifier();

  /**
   * Determines if this command source is a player, or not.
   * @return True if this source is a player, otherwise false.
   */
  boolean isPlayer();

  /**
   * Used to get the related {@link PlayerProvider} for this command source.
   * @return An optional containing the related {@link PlayerProvider} if this command source is a
   * player, otherwise an empty {@link Optional}.
   */
  Optional<PlayerProvider> player();
}