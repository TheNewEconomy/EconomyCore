package net.tnemc.core.menu.icon.impl;
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

import net.tnemc.core.menu.icon.Icon;
import net.tnemc.item.AbstractItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * StateIcon
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class StateIcon extends Icon {

  protected Map<String, AbstractItemStack<?>> states = new HashMap<>();

  protected String state;

  public StateIcon(int slot, AbstractItemStack<?> item) {
    super(slot, item);
  }

  public StateIcon(int slot, Map<String, AbstractItemStack<?>> states, String state) {
    super(slot, states.get(state));
    this.states = states;
    this.state = state;
  }

  public void changeState(final String state) {
    if(states.containsKey(state)) {
      this.state = state;
      this.item = states.get(state);
    }
  }
}