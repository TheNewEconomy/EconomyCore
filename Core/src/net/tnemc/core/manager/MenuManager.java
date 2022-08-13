package net.tnemc.core.manager;
/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.viewer.ViewerData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MenuManager
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MenuManager {

  public final Map<String, Menu> menus = new HashMap<>();
  public final ConcurrentHashMap<UUID, ViewerData> data = new ConcurrentHashMap<>();



  public void removeData(UUID id) {
    data.remove(id);
  }

  public Optional<Object> getViewerData(UUID viewer, String identifier) {
    if(data.containsKey(viewer)) {
      return Optional.ofNullable(data.get(viewer).getValue(identifier));
    }
    return Optional.empty();
  }

  public void setViewerData(UUID viewer, String identifier, Object value) {
    if(!data.containsKey(viewer)) {
      data.put(viewer, new ViewerData(viewer));
    }
    data.get(viewer).setValue(identifier, value);
  }
}