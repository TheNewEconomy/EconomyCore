package net.tnemc.core.manager.top;

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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.currency.format.CurrencyFormatter;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.io.message.MessageData;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TopPage
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TopPage<V> {

  private final LinkedHashMap<V, BigDecimal> values = new LinkedHashMap<>();

  private final int page;

  public TopPage(final int page) {

    this.page = page;
  }

  public LinkedHashMap<V, BigDecimal> getValues() {

    return values;
  }

  public V getAt(final int pos) {

    if(pos > values.size()) return (V)"no one";

    final Iterator<Map.Entry<V, BigDecimal>> it = values.entrySet().iterator();

    int i = 0;
    while(it.hasNext()) {
      if(pos - 1 == i) {
        return it.next().getKey();
      }

      it.next();
      i++;
    }
    return (V)"no one";
  }

  public MessageData getFor(final int pos, final UUID currency) {

    final MessageData data = new MessageData("Messages.Money.PlaceholderTopEntry");
    data.addReplacement("$toppos", String.valueOf(pos));
    data.addReplacement("$account", "no one");
    data.addReplacement("$balance", "0");

    if(pos > values.size()) {
      return data;
    }

    final Iterator<Map.Entry<V, BigDecimal>> it = values.entrySet().iterator();

    int i = 0;
    while(it.hasNext()) {
      if(pos - 1 == i) {
        data.addReplacement("$account", (String)it.next().getKey());
        data.addReplacement("$balance", CurrencyFormatter.format(null, new HoldingsEntry(PluginCore.server().defaultWorld(), currency, it.next().getValue(), EconomyManager.NORMAL)));
        return data;
      }

      it.next();
      i++;
    }
    return data;
  }

  public int getPage() {

    return page;
  }
}