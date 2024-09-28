package net.tnemc.core.account;

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

import net.tnemc.plugincore.core.io.maps.MapKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the status of an {@link Account accout}. The  account status affects things such as
 * using the balance and receiving payments.
 *
 * @author creatorfromhell
 * @see Account
 * @since 0.1.2.0
 */
public interface AccountStatus {

  /**
   * @return The identifier of this account status.
   */
  @MapKey
  @NotNull
  String identifier();

  /**
   * Whether this status can be unlocked by entering the account's pin.
   *
   * @return True if this status can be unlocked by entering the account's pin, otherwise false.
   */
  boolean unlockable();

  /**
   * Whether the account may use money from their account.
   *
   * @return True if the account is able to use funds from its balance, otherwise false.
   */
  boolean use();

  /**
   * Whether the account may receive money into their account.
   *
   * @return True if the account is able to receive funds into its balance, otherwise false.
   */
  boolean receive();

}