package net.tnemc.bukkit.depend.towny;


/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.id.UUIDPair;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.orphan.OrphanCommand;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * TownyCommand
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class TownyCommand implements OrphanCommand {

  @Subcommand({ "towny" })
  @Usage("")
  @Description("Used to realign old towny data with the new enhanced data for when you're using VaultUnlocked")
  @CommandPermission("tne.admin.towny")
  public void command(final BukkitCommandActor sender) {

    PluginCore.log().inform("Updating Towny accounts...");
    final Iterator<Map.Entry<UUID, UUIDPair>> idIterator = TNECore.eco().account().uuidProvider().pairs().entrySet().iterator();
    while(idIterator.hasNext()) {

      final Map.Entry<UUID, UUIDPair> entry = idIterator.next();
      final String name = entry.getValue().getUsername();

      PluginCore.log().inform("Updating account: " + name);

      if(name.contains(TownySettings.getTownAccountPrefix())) {

        final Town town = TownyAPI.getInstance().getTown(name.replace(TownySettings.getTownAccountPrefix(), ""));
        if(town == null) {
          //delete account
          TNECore.eco().account().deleteAccount(entry.getKey());
          continue;
        }

        final UUID townyUUID = town.getUUID();
        final Optional<Account> account = TNECore.eco().account().findAccount(entry.getKey().toString());
        if(!townyUUID.equals(entry.getKey()) && account.isPresent()) {

          final List<HoldingsEntry> holdings = account.get().getWallet().entryList();

          idIterator.remove();
          TNECore.eco().account().deleteAccount(entry.getKey());

          final Optional<SharedAccount> response = TNECore.eco().account().createNonPlayerAccount(townyUUID.toString(), name);
          if(response.isPresent()) {

            for(final HoldingsEntry entry1 : holdings) {

              response.get().setHoldings(entry1);
            }
          }
        }
      } else if(entry.getValue().getUsername().contains(TownySettings.getNationAccountPrefix())) {

        final Nation nation = TownyAPI.getInstance().getNation(name.replace(TownySettings.getNationAccountPrefix(), ""));
        if(nation == null) {
          //delete account
          TNECore.eco().account().deleteAccount(entry.getKey());
          continue;
        }

        final UUID townyUUID = nation.getUUID();
        final Optional<Account> account = TNECore.eco().account().findAccount(entry.getKey().toString());
        if(!townyUUID.equals(entry.getKey()) && account.isPresent()) {

          final List<HoldingsEntry> holdings = account.get().getWallet().entryList();

          idIterator.remove();
          TNECore.eco().account().deleteAccount(entry.getKey());

          final Optional<SharedAccount> response = TNECore.eco().account().createNonPlayerAccount(townyUUID.toString(), name);
          if(response.isPresent()) {

            for(final HoldingsEntry entry1 : holdings) {

              response.get().setHoldings(entry1);
            }
          }
        }
      }
    }

    PluginCore.log().inform("Finished updating Towny accounts!");
  }
}