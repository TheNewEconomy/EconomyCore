package net.tnemc.core.io.maps;
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

import net.tnemc.core.manager.top.TopPage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * MultiTreeMap represents a tree map that supports multiple entries for each key.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MultiTreeMap<V> {

  private final TreeMap<BigDecimal, List<V>> map = new TreeMap<>(Collections.reverseOrder());
  private final Map<Integer, TopPage<V>> pageMap = new HashMap<>();

  private final int perPage;

  public MultiTreeMap(int perPage) {
    this.perPage = perPage;
  }

  public void put(final BigDecimal key, final V value) {
    map.computeIfAbsent(key, k->new ArrayList<>()).add(value);
  }

  public List<V> get(final BigDecimal key) {
    if(map.containsKey(key)) {
      return map.get(key);
    }
    return new ArrayList<>();
  }

  public void removeValues(final BigDecimal key) {
    map.remove(key);
  }

  public boolean containsKey(final BigDecimal key) {
    return map.containsKey(key);
  }

  public void sort() {

    int page = 1;
    TopPage<V> pageEntry = new TopPage<>(page);
    for(Map.Entry<BigDecimal, List<V>> entry : map.entrySet()) {
      for(V value : entry.getValue()) {
        pageEntry.getValues().put(value, entry.getKey());

        if(pageEntry.getValues().size() >= perPage) {

          pageMap.put(pageEntry.getPage(), pageEntry);

          page++;
          pageEntry = new TopPage<>(page);
        }
      }

      if(pageEntry.getValues().size() >= perPage) {

        pageMap.put(pageEntry.getPage(), pageEntry);

        page++;
        pageEntry = new TopPage<>(page);
      }
    }
    pageMap.put(pageEntry.getPage(), pageEntry);
    pageEntry = null;
    map.clear();
  }

  public int position(V search) {
    for(TopPage<V> page : pageMap.values()) {
      if(page.getValues().containsKey(search)) {
        return (((page.getPage() - 1) * perPage) + new LinkedList<>(page.getValues().keySet()).indexOf(search));
      }
    }
    return -1;
  }

  public TopPage<V> getValues(int page) {

    if(page > pageMap.size()) page = 1;

    return pageMap.get(page);
  }

  public int pages() {
    return pageMap.size();
  }
}