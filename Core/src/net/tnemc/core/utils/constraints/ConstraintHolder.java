package net.tnemc.core.utils.constraints;
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

import java.util.Map;

/**
 * Represents an object that is able to hold {@link Constraint constraints}.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface ConstraintHolder {

  Map<String, String> constraints();

  default <TYPE> void addConstraint(Constraint<TYPE> constraint, TYPE value) {
    constraints().put(constraint.identifier(), constraint.asString(value));
  }

  default <TYPE> TYPE getConstraint(Constraint<TYPE> constraint) {
    return constraint.convert(constraints().get(constraint.identifier()));
  }

  default void removeConstraint(Constraint<?> constraint) {
    constraints().remove(constraint.identifier());
  }
}