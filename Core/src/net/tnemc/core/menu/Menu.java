package net.tnemc.core.menu;
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

import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.menu.callbacks.ClickCallback;
import net.tnemc.core.menu.callbacks.CloseCallback;
import net.tnemc.core.menu.callbacks.OpenCallback;
import net.tnemc.item.AbstractItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;

/**
 * Represents an inventory-based menu, which may be utilized for the player to perform actions or
 * view information in-game in a neat and organized manner.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Menu {

  protected final ConcurrentSkipListMap<Integer, Page> pages = new ConcurrentSkipListMap<>();

  protected String name;
  protected String title;
  protected int size;
  protected boolean readOnly = true;

  protected int page;

  //Callbacks
  protected Consumer<OpenCallback> open;
  protected Consumer<CloseCallback> close;
  protected Consumer<ClickCallback> click;

  public Menu(String name, String title, int size) {
    this.name = name;
    this.title = title;
    this.size = size;
    this.page = 1;
  }

  public void build(PlayerProvider player) {
    player.openMenu(this);
  }

  public void update(PlayerProvider player, int slot, AbstractItemStack<?> item) {
    player.updateMenu(slot, item);
  }

  public Map<Integer, Page> getPages() {
    return pages;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  public Consumer<OpenCallback> getOpen() {
    return open;
  }

  public void setOpen(Consumer<OpenCallback> open) {
    this.open = open;
  }

  public Consumer<CloseCallback> getClose() {
    return close;
  }

  public void setClose(Consumer<CloseCallback> close) {
    this.close = close;
  }

  public Consumer<ClickCallback> getClick() {
    return click;
  }

  public void setClick(Consumer<ClickCallback> click) {
    this.click = click;
  }
}