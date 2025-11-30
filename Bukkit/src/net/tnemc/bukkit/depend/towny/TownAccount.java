package net.tnemc.bukkit.depend.towny;

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

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownySettings;
import net.tnemc.core.account.NonPlayerAccount;
import net.tnemc.core.utils.MISCUtils;
import net.tnemc.plugincore.PluginCore;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an account linked to a Town in the Towny Plugin.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TownAccount extends NonPlayerAccount {

  public TownAccount(final UUID identifier, final String name) {

    super(identifier, name);

    final UUID townID = generateIdentifier(name);

    this.identifier = townID;

    this.receiptOwner = townID;

    PluginCore.log().debug("Town Account created with Name: " + name + " and UUID: " + townID + ".");
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
  public UUID generateIdentifier(final String name) {

    MISCUtils.printStack();

    try {

      PluginCore.log().debug("Generating Town UUID for: " + name + ".");
      final UUID townID = Objects.requireNonNull(TownyAPI.getInstance().getTown(name.replace(TownySettings.getTownAccountPrefix(), ""))).getUUID();

      PluginCore.log().debug("Generated Town UUID: " + townID + ".");
      return townID;
    } catch(final Exception ignore) {

      PluginCore.log().debug("Failed to generate Town UUID for: " + name + ".");
      return super.generateIdentifier(name);
    }
  }
}