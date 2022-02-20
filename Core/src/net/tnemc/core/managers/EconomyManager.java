package net.tnemc.core.managers;

import net.tnemc.core.account.NonPlayerAccount;
import net.tnemc.core.account.SharedAccount;
import net.tnemc.core.account.holdings.handlers.CoreHoldingsHandler;
import net.tnemc.core.account.holdings.handlers.EChestHandler;
import net.tnemc.core.account.holdings.handlers.HoldingsHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Represents the manager for everything in our economy system. This class is the core of everything
 * that happens in TNE.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class EconomyManager {

  //Constants
  public static final Pattern UUID_MATCHER_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

  private final TreeMap<Integer, List<HoldingsHandler>> holdingsHandlers = new TreeMap<>();

  /**
   * Instance of the Economy manager.
   */
  private static EconomyManager instance;

  private final AccountManager accountManager;
  private final CurrencyManager currencyManager;

  private boolean consolidate = false;
  private boolean cache = true;

  public EconomyManager() {
    instance = this;
    this.accountManager = new AccountManager();
    this.currencyManager = new CurrencyManager();

    //Initialize our built-in handlers.
    registerHandler(new CoreHoldingsHandler());
    registerHandler(new EChestHandler());
  }

  public void registerHandler(HoldingsHandler handler) {
    List<HoldingsHandler> handlers = holdingsHandlers.getOrDefault(handler.priority(), new ArrayList<>());
    handlers.add(handler);
    holdingsHandlers.put(handler.priority(), handlers);
  }

  public ConcurrentHashMap<String, NonPlayerAccount> findNonPlayers() {
    return null;
  }

  public ConcurrentHashMap<String, SharedAccount> findShared() {
    return null;
  }

  public TreeMap<Integer, List<HoldingsHandler>> getHoldingsHandlers() {
    return holdingsHandlers;
  }

  public static EconomyManager instance() {
    return instance;
  }

  public CurrencyManager currencyManager() {
    return currencyManager;
  }

  public AccountManager accountManager() {
    return accountManager;
  }

  public boolean canConsolidate() {
    return consolidate;
  }

  public void setConsolidate(boolean consolidate) {
    this.consolidate = consolidate;
  }

  public boolean canCache() {
    return cache;
  }

  public void setCache(boolean cache) {
    this.cache = cache;
  }
}