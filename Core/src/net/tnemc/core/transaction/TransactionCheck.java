package net.tnemc.core.transaction;

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

import net.tnemc.core.actions.EconomyResponse;
import net.tnemc.core.io.maps.MapKey;

/**
 * Represents a check that happens during the processing of a {@link Transaction transaction}.
 * These could be anything from validating balances, to validating inventory space if required.
 *
 * @see Transaction
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface TransactionCheck {

  /**
   * The unique string-based identifier for this check in order to be able to allow control over
   * what checks are running, and which ones may not have to be utilized. For instance, we don't
   * want to waste resources with inventory-based checks when it may not involve item-based
   * currencies.
   *
   * @return The unique identifier for this check.
   */
  @MapKey
  String identifier();

  /**
   * This method is utilized to run the check on the specific transaction. This should return an
   * {@link EconomyResponse response}.
   *
   * @param transaction The {@link Transaction transaction} to perform the check on.
   * @return The {@link EconomyResponse response} for this check. This should include a success or
   * failure boolean along with a message for why it failed if it did. The messages for this response
   * are ignored if the check was successful.
   */
  EconomyResponse process(final Transaction transaction);
}
