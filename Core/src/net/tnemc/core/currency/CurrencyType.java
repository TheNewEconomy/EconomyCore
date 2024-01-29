package net.tnemc.core.currency;

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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.HoldingsHandler;
import net.tnemc.core.currency.type.ItemType;
import net.tnemc.core.utils.Identifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static net.tnemc.core.EconomyManager.VIRTUAL;

/**
 * Represents a type of currency.
 *
 * @see Currency
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface CurrencyType {

  /**
   * @return The name of this currency type. Examples: Virtual, Item
   */
  String name();

  /**
   * @return True if this currency type supports offline players, otherwise false.
   */
  default boolean offline() {
    return true;
  }

  /**
   * @return True if this currency requires setting the player's balance when they log in. This
   * should be true for currency types like item currency.
   */
  default boolean loginCalculation() {
    return false;
  }

  /**
   * @return If balances of this currency type should be saved to the database. Return false to do
   * your own data handling.
   */
  default boolean database() {
    return true;
  }

  default boolean supportsVirtual() {
    return true;
  }

  /**
   * @return True if this currency type is based on physical items.
   */
  default boolean supportsItems() {
    return (this instanceof ItemType);
  }

  /**
   * @return The {@link Identifier} of the default handler.
   */
  default Identifier defaultHandler() {
    return VIRTUAL;
  }

  /**
   * @return True if this currency type supports the exchange command set, which is used to exchange
   * from items to/from virtual.
   */
  default boolean supportsExchange() {
    return false;
  }

  /**
   * Used to get the holdings for a specific account from this currency type.
   *
   * @param account The uuid of the account.
   * @param region The name of the region involved. This is usually a world, but could be something
   *               else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param type The {@link Identifier} of the holdings handler to use.
   * @return The holdings for the specific account.
   */
  default List<HoldingsEntry> getHoldings(Account account, String region, Currency currency, Identifier type) {

    final LinkedList<HoldingsEntry> holdings = new LinkedList<>();
    final List<HoldingsHandler> handlers = new ArrayList<>();

    if(type.equals(EconomyManager.NORMAL)) {

      handlers.addAll(EconomyManager.instance().getFor(account, this));

    } else if(type.equals(EconomyManager.ITEM_ONLY)) {

      final Optional<CurrencyType> curType = EconomyManager.instance().currency().findType("item");
      curType.ifPresent(currencyType -> handlers.addAll(EconomyManager.instance().getFor(account, currencyType)));

    } else {

      final Optional<HoldingsHandler> handler = EconomyManager.instance().findHandler(type);
      handler.ifPresent(handlers::add);
    }

    for(HoldingsHandler handler : handlers) {
      holdings.add(handler.getHoldings(account, region, currency, this));
    }
    return holdings;
  }

  /**
   * Used to set the holdings for a specific account.
   *
   * @param account The uuid of the account.
   * @param region The name of the region involved. This is usually a world, but could be something
   *               else such as a world guard region name/identifier.
   * @param currency The instance of the currency to use.
   * @param type The {@link Identifier} of the holdings handler to use.
   * @param amount The amount to set the player's holdings to.
   *
   * @return True if the holdings have been set, otherwise false.
   */
  default boolean setHoldings(Account account, String region, Currency currency, Identifier type, BigDecimal amount) {


    final Optional<HoldingsHandler> handler = EconomyManager.instance().findHandler(type);

    if(handler.isPresent()) {

      if(handler.get().database()) {
        addDatabase(account, region, currency, type, amount);
      }

      return handler.get().setHoldings(account, region, currency, this, amount);
    } else {

      final Optional<HoldingsHandler> handler1 = EconomyManager.instance().findHandler(defaultHandler());
      if(handler1.isPresent()) {
        if(handler1.get().database()) {
          addDatabase(account, region, currency, defaultHandler(), amount);
        }

        return handler1.get().setHoldings(account, region, currency, this, amount);
      }
    }
    return false;
  }

  default void addDatabase(Account account, String region, Currency currency, Identifier type, BigDecimal amount) {
    account.getWallet().setHoldings(new HoldingsEntry(region, currency.getUid(), amount, type));

    TNECore.storage().store(account, account.getIdentifier());
  }
}
