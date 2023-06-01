package net.tnemc.core.module;

import net.tnemc.core.TNECore;

import java.net.URL;
import java.net.URLClassLoader;

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
public class ModuleClassLoader extends URLClassLoader {

  public ModuleClassLoader(URL url) {
    super(new URL[]{url}, TNECore.instance().getClass().getClassLoader());
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    try {
      return super.loadClass(name, resolve);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();

    TNECore.log().debug("Module Class Loader has been GC'd");
  }
}