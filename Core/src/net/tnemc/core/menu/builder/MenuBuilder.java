package net.tnemc.core.menu.builder;

import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.Page;
import net.tnemc.core.menu.callbacks.menu.MenuCloseCallback;
import net.tnemc.core.menu.callbacks.menu.MenuOpenCallback;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;

public class MenuBuilder {

  protected final ConcurrentSkipListMap<Integer, Page> pages = new ConcurrentSkipListMap<>();

  private String name;
  private String title;
  private int size;

  //Callbacks
  protected Consumer<MenuOpenCallback> open;
  protected Consumer<MenuCloseCallback> close;

  public MenuBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public MenuBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public MenuBuilder withSize(int size) {
    this.size = size;
    return this;
  }

  public MenuBuilder open(Consumer<MenuOpenCallback> callback) {
    this.open = callback;
    return this;
  }

  public MenuBuilder close(Consumer<MenuCloseCallback> callback) {
    this.close = callback;
    return this;
  }

  public MenuBuilder withPage(final Page page) {
    this.pages.put(page.getId(), page);
    return this;
  }

  public Menu create() {
    Menu menu = new Menu(name, title, size);
    menu.setOpen(open);
    menu.setClose(close);
    menu.getPages().putAll(pages);

    return menu;
  }
}