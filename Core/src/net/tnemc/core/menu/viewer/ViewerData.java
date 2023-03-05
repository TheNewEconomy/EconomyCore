package net.tnemc.core.menu.viewer;

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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents data that belongs to a user that is interacting with a menu.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ViewerData {

  private final Map<String, Object> data = new HashMap<>();
  private UUID viewer;
  private String menu;

  private int page = 1;

  public ViewerData(UUID viewer, final String menu) {
    this.viewer = viewer;
    this.menu = menu;
  }

  public ViewerData(UUID viewer, Map<String, Object> data) {
    this.viewer = viewer;
    this.data.putAll(data);
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data.clear();
    this.data.putAll(data);
  }

  public UUID getViewer() {
    return viewer;
  }

  public void setViewer(UUID viewer) {
    this.viewer = viewer;
  }

  public String getMenu() {
    return menu;
  }

  public void setMenu(String menu) {
    this.menu = menu;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public Object getValue(String identifier) {
    return data.get(identifier);
  }

  public void setValue(String identifier, Object value) {
    data.put(identifier, value);
  }
}