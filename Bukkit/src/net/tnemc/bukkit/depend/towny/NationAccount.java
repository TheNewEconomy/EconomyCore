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
import net.tnemc.plugincore.PluginCore;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents an account linked to a Nation in the Towny Plugin.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class NationAccount extends NonPlayerAccount {

  public NationAccount(final UUID identifier, final String name) {

    super(identifier, name);

    final UUID nationID = generateIdentifier(name);

    this.identifier = nationID;

    this.receiptOwner = nationID;

    PluginCore.log().debug("Nation Account created with Name: " + name + " and UUID: " + nationID + ".");
    //this.owner = Objects.requireNonNull(TownyAPI.getInstance().getNation(name)).getKing().getUUID();
  }

  /**
   * Used to get the type of account that this is. This is for data-purposes only.
   *
   * @return The account type.
   */
  @Override
  public String type() {

    return "nation";
  }

  @Override
  public UUID generateIdentifier(final String name) {

    try {

      PluginCore.log().debug("Generating Nation UUID for: " + name + ".");
      final UUID nationID = Objects.requireNonNull(TownyAPI.getInstance().getNation(name.replace(TownySettings.getNationAccountPrefix(), ""))).getUUID();

      PluginCore.log().debug("Generated Nation UUID: " + nationID + ".");
      return nationID;
    } catch(final Exception ignore) {

      PluginCore.log().debug("Failed to generate Nation UUID for: " + name + ".");
      return super.generateIdentifier(name);
    }
  }
}