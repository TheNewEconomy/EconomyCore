package net.tnemc.core.manager;
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
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.manager.top.TopCurrency;
import net.tnemc.core.manager.top.TopPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * TopManager handles all things baltop.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TopManager {

  private final List<Pattern> regexExclusions = new ArrayList<>();
  private final List<String> exclusions = new ArrayList<>();

  private static TopManager instance;

  private final Map<UUID, TopCurrency> topMap = new ConcurrentHashMap<>();

  public TopManager() {

    instance = this;

    for(String str : MainConfig.yaml().getStringList("Core.Commands.Top.Exclusions")) {
      try {
        regexExclusions.add(Pattern.compile(str));
      } catch(PatternSyntaxException ignore) {
        exclusions.add(str);
      }
    }
  }

  public void load() {

    topMap.clear();
    for(Currency currency : TNECore.eco().currency().currencies()) {
      topMap.put(currency.getUid(), new TopCurrency(TNECore.server().defaultRegion(), currency.getUid()));
    }
  }

  public Map<UUID, TopCurrency> getTopMap() {
    return topMap;
  }

  public TopPage<String> page(final int page, final UUID currency) {
    if(topMap.containsKey(currency)) {
      return topMap.get(currency).getBalances().getValues(page);
    }
    return null;
  }

  public int page(final UUID currency) {
    if(topMap.containsKey(currency)) {
      return topMap.get(currency).getBalances().pages();
    }
    return 0;
  }

  public static TopManager instance() {
    return instance;
  }

  public List<Pattern> getRegexExclusions() {
    return regexExclusions;
  }

  public List<String> getExclusions() {
    return exclusions;
  }
}