package net.tnemc.core.account;

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

import net.tnemc.core.account.shared.Member;
import net.tnemc.core.account.shared.Permission;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents an Account object that is shared by multiple players.
 *
 * @see Account
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SharedAccount extends Account {

  protected ConcurrentHashMap<UUID, Member> members = new ConcurrentHashMap<>();

  /**
   * The {@link UUID identifier} of the owner of this {@link Account account}.
   */
  protected UUID owner;

  public SharedAccount(String identifier, String name, UUID owner) {
    super(identifier, name);

    this.owner = owner;
  }

  /**
   * Checks if the specified identifier is a member of this account.
   * @param identifier The identifier to check.
   * @return True if the specified identifier is a member, otherwise false.
   */
  public boolean isMember(UUID identifier) {
    return owner.equals(identifier) || members.containsKey(identifier);
  }

  /**
   * Used to find a specific {@link Member member} of this account if it
   * exists.
   *
   * @param identifier The identifier to use for the search.
   * @return An Optional containing the {@link Member member} if exists, otherwise an
   * empty Optional.
   */
  public Optional<Member> findMember(UUID identifier) {
    return Optional.ofNullable(members.get(identifier));
  }

  /**
   * Sets the permission specified to the specified value for the specified member.
   *
   * @param identifier The identifier of the member to use.
   * @param permission The permission to set.
   * @param value The value to set for the permission.
   */
  public void addPermission(UUID identifier, Permission permission, boolean value) {
    findMember(identifier).ifPresent(mem->mem.addPermission(permission, value));
  }

  /**
   * Removes the permission specified from the specified member.
   *
   * @param identifier The identifier of the member to use.
   * @param permission The permission to set.
   */
  public void removePermission(UUID identifier, Permission permission) {
    findMember(identifier).ifPresent(mem->mem.removePermission(permission));
  }

  /**
   * Checks if the specified member has the specified permission.
   *
   * @param identifier The identifier of the member to use.
   * @param permission The permission to we are checking for.
   * @return True if the specified member has the specified permission, otherwise false.
   */
  public boolean hasPermission(UUID identifier, Permission permission) {
    return findMember(identifier).map(value->value.hasPermission(permission))
        .orElseGet(permission::defaultValue);
  }
}