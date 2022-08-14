package net.tnemc.core.utils.validator;
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

/**
 * Represents a validator that takes a string and determines if it is able to be converted into the
 * appropriate object.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface InputValidator<K> {

  /**
   * Used to convert the provided input into the specified object.
   * @param input The string to convert into the object.
   * @return An optional containing the converted value, or an empty optional if the provided input
   * is not able to be converted into the specified object.
   */
  Optional<K> convert(final String input);
}