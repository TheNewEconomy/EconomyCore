package net.tnemc.core.account.shared;
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

import net.tnemc.core.account.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a player that has access to an account.
 *
 * @see Account
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class Member {

  public Map<String, Boolean> permissions = new HashMap<>();

  /**
   * The {@link UUID identifier} associated with the player that is
   * represented by this member object.
   */
  private final UUID id;

  /**
   * Construct a new member using an {@link UUID identifier}.
   * @param id The {@link UUID identifier} of this member.
   */
  public Member(final UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  /**
   * Sets the permission specified to the specified value for this member.
   * @param permission The permission to set.
   * @param value The value to set for the permission.
   */
  public void addPermission(Permission permission, boolean value) {
    permissions.put(permission.identifier(), value);
  }

  /**
   * Removes the permission specified from this member.
   * @param permission The permission to set.
   */
  public void removePermission(Permission permission) {
    permissions.remove(permission.identifier());
  }

  /**
   * Checks if this member has the specified permission.
   * @param permission The permission to we are checking for.
   * @return True if this member has the specified permission, otherwise false.
   */
  public boolean hasPermission(Permission permission) {
    return permissions.getOrDefault(permission.identifier(), permission.defaultValue());
  }
}