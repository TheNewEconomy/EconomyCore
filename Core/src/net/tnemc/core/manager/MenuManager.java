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

import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icon.ActionType;
import net.tnemc.core.menu.impl.myeco.MyEcoMenu;
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

  public MenuManager() {
    menus.put("my_eco", new MyEcoMenu());
  }

  public boolean onClick(final String menu, ActionType type, PlayerProvider provider, int page, int slot) {
    if(!menus.containsKey(menu) || !inMenu(provider)) {
      return false;
    }

    return menus.get(menu).onClick(type, provider, page, slot);
  }

  public boolean inMenu(PlayerProvider provider) {
    return data.containsKey(provider.getUUID());
  }

  public boolean inMenu(UUID id) {
    return data.containsKey(id);
  }

  public void updateViewer(final UUID id, final String menu, final int page) {
    ViewerData viewer = data.getOrDefault(id, new ViewerData(id, menu));
    viewer.setMenu(menu);
    viewer.setPage(page);

    data.put(id, viewer);
  }

  public Optional<ViewerData> getData(final UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  public void removeData(UUID id) {
    data.remove(id);
  }

  public Optional<Object> getViewerData(UUID viewer, String identifier) {
    if(data.containsKey(viewer)) {
      return Optional.ofNullable(data.get(viewer).getValue(identifier));
    }
    return Optional.empty();
  }

  public void appendViewerData(UUID viewer, Map<String, Object> toAppend) {
    if(!data.containsKey(viewer)) {
      data.put(viewer, new ViewerData(viewer, toAppend));
      return;
    }
    data.get(viewer).getData().putAll(toAppend);
  }

  public void setViewerData(UUID viewer, final String menu, String identifier, Object value) {
    if(!data.containsKey(viewer)) {
      data.put(viewer, new ViewerData(viewer, menu));
    }
    data.get(viewer).setValue(identifier, value);
  }
}