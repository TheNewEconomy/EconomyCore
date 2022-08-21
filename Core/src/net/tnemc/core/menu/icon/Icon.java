package net.tnemc.core.menu.icon;
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
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.Page;
import net.tnemc.core.menu.callbacks.IconClickCallback;
import net.tnemc.item.AbstractItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents an Icon, which is a singular item in an inventory-based {@link Menu}.
 *
 * @see Menu
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Icon {

  protected List<IconAction> actions = new LinkedList<>();

  protected int slot;
  protected AbstractItemStack<?> item;

  //TODO: StateIcon? Changes item based on click?

  //Enhanced functionality
  protected String permission = "";
  protected String message = "";


  //TODO: Default actions? chat response, send message?, close, switch, add data?

  //Callbacks
  protected Consumer<IconClickCallback> click;

  public Icon(int slot, AbstractItemStack<?> item) {
    this.slot = slot;
    this.item = item;
  }

  public int getSlot() {
    return slot;
  }

  public void setSlot(int slot) {
    this.slot = slot;
  }

  public AbstractItemStack<?> getItem() {
    return item;
  }

  public void setItem(AbstractItemStack<?> item) {
    this.item = item;
  }

  public Consumer<IconClickCallback> getClick() {
    return click;
  }

  public void setClick(Consumer<IconClickCallback> click) {
    this.click = click;
  }


  public boolean onClick(ActionType type, Menu menu, Page page, PlayerProvider player) {

    if(!permission.equalsIgnoreCase("") && !player.hasPermission(permission)) {
      return true;
    }

    for(IconAction action : actions) {

      if(action.type().equals(ActionType.ANY) || action.type().name().equalsIgnoreCase(type.name())) {
        action.onPerform(menu, page, player, this);

        if(!action.continueOther()) {
          break;
        }
      }
    }

    if(click != null) {
      click.accept(new IconClickCallback(type, menu, page, player, this));
    }

    if(!message.equalsIgnoreCase("")) {
      //TODO: Send message translation
      //TNECore.eco().translation().
    }

    return true;
  }
}