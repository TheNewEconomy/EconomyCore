package net.tnemc.core.utils;
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
 * Identifier
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Identifier {

  private final String plugin;
  private final String id;

  public Identifier(String plugin, String id) {
    this.plugin = plugin;
    this.id = id;
  }

  public String getPlugin() {
    return plugin;
  }

  public String getId() {
    return id;
  }

  public String asID() {
    return plugin + ":" + id;
  }

  public static Identifier fromID(final String id) {
    final String[] split = id.split(":");

    if(split.length > 1) {

    } else {
    }
    //TODO: best way to do this? make holdings entry only hold string instead of object instance?
    return null;
  }

  /**
   * Checks to see if two identifiers are equal. This uses the asID to check.
   *
   * @param obj The object to check.
   * @return True if the ids match.
   */
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Identifier) {
      return ((Identifier)obj).asID().equalsIgnoreCase(asID());
    }
    return false;
  }
}