package net.tnemc.paper.depend.faction;

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

import net.tnemc.core.account.NonPlayerAccount;


/**
 * Represents an account linked to a Faction in a Factions Plugin.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class FactionAccount extends NonPlayerAccount {

  public FactionAccount(String identifier, String name) {
    super(identifier, name);
  }

  /**
   * Used to get the type of account that this is. This is for data-purposes only.
   *
   * @return The account type.
   */
  @Override
  public String type() {
    return "faction";
  }
}