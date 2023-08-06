package net.tnemc.core.manager.top;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.io.maps.MultiTreeMap;
import net.tnemc.core.manager.TopManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * TopCurrency
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TopCurrency {

  private final MultiTreeMap<String> balances = new MultiTreeMap<>(5);

  private final String region;
  private final UUID currency;

  public TopCurrency(String region, UUID currency) {
    this.region = region;
    this.currency = currency;

    load();
    balances.sort();
  }

  public UUID getCurrency() {
    return currency;
  }

  public void load() {

    for(Account account: TNECore.eco().account().getAccounts().values()) {
      if(excluded(account.getName())) continue;

      balances.put(account.getHoldingsTotal(region, currency), account.getName());
    }
  }

  public boolean excluded(final String name) {
    for(Pattern pattern : TopManager.instance().getRegexExclusions()) {
      System.out.println("pattern: " + pattern.pattern() + " name: " + name);
      if(pattern.matcher(name).matches()) return true;
    }

    for(String str : TopManager.instance().getExclusions()) {
      if(name.contains(str)) return true;
    }
    return false;
  }

  public MultiTreeMap<String> getBalances() {
    return balances;
  }
}