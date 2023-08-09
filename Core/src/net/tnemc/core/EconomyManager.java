package net.tnemc.core;

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

import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsHandler;
import net.tnemc.core.account.holdings.handlers.EnderChestHandler;
import net.tnemc.core.account.holdings.handlers.ExperienceHandler;
import net.tnemc.core.account.holdings.handlers.InventoryHandler;
import net.tnemc.core.account.holdings.handlers.VirtualHandler;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.currency.CurrencyType;
import net.tnemc.core.manager.AccountManager;
import net.tnemc.core.manager.CurrencyManager;
import net.tnemc.core.manager.TopManager;
import net.tnemc.core.manager.TransactionManager;
import net.tnemc.core.region.RegionProvider;
import net.tnemc.core.utils.Identifier;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This class manages everything for the economy plugin, from language storage to holding instances
 * to other managers.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class EconomyManager {

  //Our core identifiers, leave these as static final, because they shouldn't be changed.

  //Normal and database are used internally only and don't have an associated holdings handler.
  public static final Identifier NORMAL = new Identifier("tne", "NORMAL_HOLDINGS");
  public static final Identifier DATABASE = new Identifier("tne", "DATABASE");


  public static final Identifier VIRTUAL = new Identifier("tne", "VIRTUAL_HOLDINGS");
  public static final Identifier EXPERIENCE = new Identifier("tne", "EXPERIENCE_HOLDINGS");
  public static final Identifier ITEM_ONLY = new Identifier("tne", "ITEM_ONLY");

  public static final Identifier INVENTORY_ONLY = new Identifier("tne", "INVENTORY_HOLDINGS");
  public static final Identifier E_CHEST = new Identifier("tne", "ENDER_HOLDINGS");

  private final LinkedHashMap<String, HoldingsHandler> handlers = new LinkedHashMap<>();

  private final Map<String, Identifier> ids = new ConcurrentHashMap<>();

  private final AccountManager accountManager;
  private final CurrencyManager currencyManager;
  private final TransactionManager transactionManager;
  private final RegionProvider regionProvider;

  private final TopManager topManager;

  private static EconomyManager instance;

  public EconomyManager() {
    instance = this;

    this.accountManager = new AccountManager();
    this.currencyManager = new CurrencyManager();
    this.transactionManager = new TransactionManager();
    this.regionProvider = new RegionProvider(MainConfig.yaml().getBoolean("Core.Region.GroupRealms"),
                                             MainConfig.yaml().getString("Core.Region.Mode"));
    this.topManager = new TopManager();
  }

  public void init() {

    //Init our default account manager stuff.
    this.accountManager.addDefaultStatuses();
    this.accountManager.addDefaultTypes();

    addIdentifier(NORMAL);
    addIdentifier(DATABASE);
    addIdentifier(VIRTUAL);
    addIdentifier(EXPERIENCE);
    addIdentifier(ITEM_ONLY);
    addIdentifier(INVENTORY_ONLY);
    addIdentifier(E_CHEST);

    //Add our core handlers
    addHandler(new VirtualHandler());
    addHandler(new ExperienceHandler());
    addHandler(new InventoryHandler());
    addHandler(new EnderChestHandler());
  }

  public Optional<Identifier> findID(String id) {
    return Optional.ofNullable(ids.get(id));
  }

  public Optional<HoldingsHandler> findHandler(final Identifier identifier) {
    return Optional.ofNullable(handlers.get(identifier.asID()));
  }

  public void addHandler(final HoldingsHandler handler) {
    handlers.put(handler.identifier().asID(), handler);
  }

  public void addIdentifier(final Identifier id) {
    ids.put(id.asID(), id);
  }

  public LinkedList<HoldingsHandler> getFor(final Account account) {
    return handlers.values().stream()
        .filter(handler->handler.appliesTo(account)).collect(Collectors.toCollection(LinkedList::new));
  }

  public LinkedList<HoldingsHandler> getFor(final CurrencyType type) {
    return handlers.values().stream()
        .filter(handler->handler.supports(type)).collect(Collectors.toCollection(LinkedList::new));
  }

  public LinkedList<HoldingsHandler> getFor(final Account account, final CurrencyType type) {
    return handlers.values().stream()
        .filter(handler->handler.appliesTo(account) && handler.supports(type))
        .collect(Collectors.toCollection(LinkedList::new));
  }

  public AccountManager account() {
    return accountManager;
  }

  public CurrencyManager currency() {
    return currencyManager;
  }

  public TransactionManager transaction() {
    return transactionManager;
  }

  public RegionProvider region() {
    return regionProvider;
  }

  public TopManager getTopManager() {
    return topManager;
  }

  public static EconomyManager instance() {
    return instance;
  }
}