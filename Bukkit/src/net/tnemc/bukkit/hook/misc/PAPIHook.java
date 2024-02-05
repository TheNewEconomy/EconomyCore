package net.tnemc.bukkit.hook.misc;
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

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.core.utils.Identifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PAPIHook
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class PAPIHook extends PlaceholderExpansion {

  @Override
  public String getAuthor() {
    return "creatorfromhell";
  }

  @Override
  public String getIdentifier() {
    return "TNE";
  }

  @Override
  public String getVersion() {
    return "0.1.2.8";
  }

  @Override
  public boolean persist() {
    return true;
  }

  @Override
  public String onPlaceholderRequest(Player player, @NotNull String identifier) {

    if(player == null) {
      return null;
    }

    final String[] args = identifier.split("_");

    Optional<Account> account = TNECore.eco().account().findAccount(player.getUniqueId());
    if(identifier.contains("player:")) {

      final String[] playerTest = identifier.split("player:");
      if(playerTest.length >= 2) {
        account = TNECore.api().getAccount(playerTest[1]);
      }
    }

    if(account.isEmpty()) {
      return null;
    }

    //%tne_balance:inventory/balance:e_chest/balance:experience/balance:virtualargs[1]%
    if(identifier.contains("balance:")) {

      TNECore.log().debug("Balance ID: " + args[0].split(":")[1]);

      final Identifier id = switch(args[0].split(":")[1]) {
        case "inventory" -> EconomyManager.INVENTORY_ONLY;
        case "virtual" -> EconomyManager.VIRTUAL;
        case "ender" -> EconomyManager.E_CHEST;
        default -> EconomyManager.NORMAL;
      };

      final UUID curID = TNECore.eco().currency().getDefaultCurrency().getUid();
      final List<HoldingsEntry> entries = account.get().getHoldings(TNECore.eco().region().defaultRegion(), curID, id);
      final BigDecimal amount = (entries.size() > 0)? entries.get(0).getAmount() : BigDecimal.ZERO;

      if(args.length >= 2 && args[1].equalsIgnoreCase("formatted")) {
        if(entries.size() > 0) {
          return CurrencyFormatter.format(account.get(), entries.get(0));
        }

        final HoldingsEntry entry = new HoldingsEntry(TNECore.eco().region().defaultRegion(),
                curID,
                amount,
                EconomyManager.NORMAL);
        return CurrencyFormatter.format(account.get(), entry);
      } else {
        return amount.toPlainString();
      }
    }

    //%tne_balance%
    if(identifier.contains("balance")) {

      final UUID curID = TNECore.eco().currency().getDefaultCurrency().getUid();
      final BigDecimal amount = account.get().getHoldingsTotal(TNECore.eco().region().defaultRegion(),
                                                               curID);

      if(args.length >= 2 && args[1].equalsIgnoreCase("formatted")) {
        final HoldingsEntry entry = new HoldingsEntry(TNECore.eco().region().defaultRegion(),
                                                      curID,
                                                      amount,
                                                      EconomyManager.NORMAL);
        return CurrencyFormatter.format(account.get(), entry);
      } else {
        return amount.toPlainString();
      }
    }

    //%tne_wcurarg[0]_<world name>arg[1]_<currency name>arg[2]
    if(identifier.toLowerCase().contains("wcur_")) {
      if(args.length >= 3) {

        final String region = TNECore.eco().region().resolve(args[1]);
        final Optional<Currency> currency = TNECore.eco().currency().findCurrency(args[2]);

        final UUID curID = (currency.isPresent())? currency.get().getUid() : TNECore.eco().currency().getDefaultCurrency().getUid();


        final BigDecimal amount = account.get().getHoldingsTotal(region, curID);

        if(args.length >= 4 && args[3].equalsIgnoreCase("formatted")) {
          final HoldingsEntry entry = new HoldingsEntry(region,
                                                        curID,
                                                        amount,
                                                        EconomyManager.NORMAL);
          return CurrencyFormatter.format(account.get(), entry);
        } else {
          return amount.toPlainString();
        }
      }
    }

    //%tne_world_<world name>args[1]%
    if(identifier.toLowerCase().contains("world_")) {
      if(args.length >= 2) {

        final String region = TNECore.eco().region().resolve(args[1]);
        final UUID curID = TNECore.eco().currency().getDefaultCurrency(region).getUid();
        final BigDecimal amount = account.get().getHoldingsTotal(region, curID);

        if(args.length >= 3 && args[2].equalsIgnoreCase("formatted")) {

          final HoldingsEntry entry = new HoldingsEntry(region,
                                                        curID,
                                                        amount,
                                                        EconomyManager.NORMAL);
          return CurrencyFormatter.format(account.get(), entry);
        } else {
          return amount.toPlainString();
        }
      }
    }

    //%tne_currency_<currency name>args[1]%
    if(identifier.toLowerCase().contains("currency_")) {
      if(args.length >= 2) {

        final String region = TNECore.eco().region().defaultRegion();
        final Optional<Currency> currency = TNECore.eco().currency().findCurrency(args[1]);

        final UUID curID = (currency.isPresent())? currency.get().getUid() : TNECore.eco().currency().getDefaultCurrency().getUid();


        final BigDecimal amount = account.get().getHoldingsTotal(region, curID);
        if(args.length >= 3 && args[2].equalsIgnoreCase("formatted")) {

          final HoldingsEntry entry = new HoldingsEntry(region,
                                                        curID,
                                                        amount,
                                                        EconomyManager.NORMAL);
          return CurrencyFormatter.format(account.get(), entry);
        } else {
          return amount.toPlainString();
        }
      }
    }

    //%tne_toppos[0]_<currency name>[1]_position[2]_<pos>[3]%
    //%tne_toppos[0]_<currency name>[1]%
    if(identifier.toLowerCase().contains("toppos")) {
      int pos = 0;

      if(args.length >= 2) {

        final Optional<Currency> currency = TNECore.eco().currency().findCurrency(args[1]);
        final UUID curID = (currency.isPresent())? currency.get().getUid() : TNECore.eco().currency().getDefaultCurrency().getUid();

        if(args.length >= 4 && args[2].equalsIgnoreCase("position")) {

          //%tne_toppos[0]_<currency name>[1]_position[2]_<pos>[3]%
          pos = Integer.parseInt(args[3]);

          return TNECore.eco().getTopManager().getAt(pos, curID);

        } else {

          //%tne_toppos[0]_<currency name>[1]%
          pos = TNECore.eco().getTopManager().position(curID, account.get().getName());
        }
      }
      return String.valueOf(pos);
    }
    return null;
  }

  public boolean formatted(final String parameter) {
    return parameter.contains("formatted");
  }

  public Optional<Account> findFromArgs(final String parameter) {
    return TNECore.eco().account().findAccount(parameter);
  }
}