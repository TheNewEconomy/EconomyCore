package net.tnemc.core.revampmenu;
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

import net.tnemc.core.utils.constraints.ConstraintHolder;
import net.tnemc.core.utils.constraints.impl.BoolConstraint;
import net.tnemc.item.SerialItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Icon
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Icon implements ConstraintHolder {

  protected final Map<String, String> constraints = new HashMap<>();

  protected SerialItem<?> item;

  @Override
  public Map<String, String> constraints() {
    return constraints;
  }
}

enum IconBooleanConstraints implements BoolConstraint {

  READ_ONLY {
    @Override
    public String identifier() {
      return "READ_ONLY";
    }

    @Override
    public Boolean defaultValue() {
      return true;
    }
  }
}