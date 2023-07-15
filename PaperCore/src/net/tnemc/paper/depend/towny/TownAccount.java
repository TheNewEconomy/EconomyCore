package net.tnemc.paper.depend.towny;

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

import com.palmergames.bukkit.towny.TownyAPI;
import net.tnemc.core.account.NonPlayerAccount;

import java.util.Objects;

/**
 * Represents an account linked to a Town in the Towny Plugin.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TownAccount extends NonPlayerAccount {

  public TownAccount(String identifier, String name) {
    super(identifier, name);
    //this.owner = Objects.requireNonNull(TownyAPI.getInstance().getTown(name)).getMayor().getUUID();
  }

  /**
   * Used to get the type of account that this is. This is for data-purposes only.
   *
   * @return The account type.
   */
  @Override
  public String type() {
    return "town";
  }

  @Override
  public String generateIdentifier(String name) {
    try {
      return Objects.requireNonNull(TownyAPI.getInstance().getTown(name)).getUUID().toString();
    } catch(Exception ignore) {
      return super.generateIdentifier(name);
    }
  }
}