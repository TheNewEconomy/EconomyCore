package net.tnemc.core.account.shared;

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

import net.tnemc.core.account.Account;

/**
 * Represents a permission for an {@link Member member} of an {@link Account account}.
 *
 * @author creatorfromhell
 * @see Member
 * @see Account
 * @since 0.1.1.17
 */
public interface Permission {

  /**
   * The identifier of the permission.
   *
   * @return The identifier of the permission.
   */
  String identifier();

  /**
   * The default value of the permission.
   *
   * @return The default value of the permission.
   */
  boolean defaultValue();
}