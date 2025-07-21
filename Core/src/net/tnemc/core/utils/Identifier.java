package net.tnemc.core.utils;

/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
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

import java.util.Optional;

/**
 * Identifier
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public record Identifier(String plugin, String id) {

  public static Identifier fromID(final String id) {

    final Optional<Identifier> idObj = EconomyManager.instance().findID(id);
    if(idObj.isPresent()) {
      return idObj.get();
    }

    final String[] split = id.split(":");
    if(split.length > 1) {
      return new Identifier(split[0], split[1]);
    } else {
      return new Identifier("generic", split[0]);
    }
  }

  public String asID() {

    return plugin + ":" + id;
  }

  /**
   * Checks to see if two identifiers are equal. This uses the asID to check.
   *
   * @param obj The object to check.
   *
   * @return True if the ids match.
   */
  @Override
  public boolean equals(final Object obj) {

    if(obj instanceof Identifier) {
      return ((Identifier)obj).asID().equalsIgnoreCase(asID());
    }
    return false;
  }
}