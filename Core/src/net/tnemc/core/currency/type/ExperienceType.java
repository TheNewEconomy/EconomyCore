package net.tnemc.core.currency.type;

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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.utils.Identifier;

/**
 * ExperienceType
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ExperienceType implements CurrencyType {
  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  @Override
  public String name() {
    return "experience";
  }

  @Override
  public boolean supportsVirtual() {
    return false;
  }

  @Override
  public Identifier defaultHandler() {
    return EconomyManager.EXPERIENCE;
  }
}
