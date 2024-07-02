package net.tnemc.core.transaction.check;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.EconomyResponse;
import net.tnemc.core.actions.response.GeneralResponse;
import net.tnemc.core.actions.response.HoldingsResponse;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionCheck;
import net.tnemc.core.transaction.TransactionParticipant;
import net.tnemc.plugincore.core.io.maps.MapKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * RegionCheck
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class RegionCheck implements TransactionCheck {
  /**
   * The unique string-based identifier for this check in order to be able to allow control over
   * what checks are running, and which ones may not have to be utilized. For instance, we don't
   * want to waste resources with inventory-based checks when it may not involve item-based
   * currencies.
   *
   * @return The unique identifier for this check.
   */
  @Override
  @MapKey
  public String identifier() {
    return "region";
  }

  /**
   * This method is utilized to run the check on specific {@link TransactionParticipant participants}.
   * This should return an {@link EconomyResponse response}.
   *
   * @param transaction The {@link Transaction transaction} to perform the check on.
   * @param participant The {@link TransactionParticipant participant} to perform the check on.
   * @param modifier    The {@link HoldingsModifier modifier} related to the specific participant.
   *
   * @return The {@link EconomyResponse response} for this check. This should include a success or
   * failure boolean along with a message for why it failed if it did. The messages for this response
   * are ignored if the check was successful.
   */
  @Override
  public EconomyResponse checkParticipant(Transaction transaction, @NotNull TransactionParticipant participant, HoldingsModifier modifier) {

    System.out.println("Region Check");

    final Optional<Account> account = participant.asAccount();
    if(account.isPresent()) {

      if(account.get() instanceof PlayerAccount player) {
        if(player.isOnline() && player.getPlayer().isPresent()) {
          System.out.println("Worlds: " + player.region());
          System.out.println("Worlds Contains: " + TNECore.eco().region().getDisabledRegions().contains(player.region()));
          if(TNECore.eco().region().getDisabledRegions().contains(player.region())) {
            return HoldingsResponse.DISABLED_REGION;
          }
        }
      }
    }

    if(modifier != null) {
      System.out.println("Worlds: " + modifier.getRegion());
      System.out.println("Worlds Contains: " + TNECore.eco().region().getDisabledRegions().contains(modifier.getRegion()));
      if(TNECore.eco().region().getDisabledRegions().contains(modifier.getRegion())) {
        return HoldingsResponse.DISABLED_REGION;
      }
    }
    return GeneralResponse.SUCCESS;
  }
}