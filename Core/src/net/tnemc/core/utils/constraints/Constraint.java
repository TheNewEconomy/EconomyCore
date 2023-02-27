package net.tnemc.core.utils.constraints;
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

/**
 * Represents a constraint applied to an object, which serves as a property. This may be any
 * type of object, but must be validated by the class utilizing the constraint.
 *
 * @param <TYPE> The type associated with this specific constraint for validation purposes.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface Constraint<TYPE> {

  /**
   * The identifier of the constraint.
   * @return The constraint identifier.
   */
  String identifier();

  /**
   * The default value for this constraint.
   * @return The default value for this constraint.
   */
  TYPE defaultValue();

  /**
   * Used to convert the provided input into the specified object.
   * @param value The string to convert into the object.
   * @return An optional containing the converted value, or an empty optional if the provided input
   * can't be converted into the specified object.
   */
  TYPE convert(final String value);

  /**
   * Used to validate that the provided input is of the valid type.
   *
   * @param value The input to validate.
   * @return True if the input is of the type associated with this constraint, otherwise false.
   */
  boolean validate(final String value);

  /**
   * Used to convert a specified value associated with this type to a string.
   * @param value The value to convert.
   * @return The String representation of the value provided.
   */
  default String asString(TYPE value) {
    return value.toString();
  }
}
