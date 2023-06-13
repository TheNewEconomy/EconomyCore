package net.tnemc.core.module;

import net.tnemc.core.TNECore;

import java.io.IOException;
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
public class ModuleWrapper {

  ModuleInfo info;
  Module module;
  URLClassLoader loader;

  public ModuleWrapper(Module module) {
    this.module = module;
  }

  public void unload() {
    try {
      loader.close();
      loader = null;
      System.gc();
    } catch (IOException ignore) {
      TNECore.log().inform("ModuleOld " + info.name() + " unloaded incorrectly.");
    }
    info = null;
  }

  public String name() {
    if(info == null) return "unknown";
    return info.name();
  }

  public String version() {
    if(info == null) return "unknown";
    return info.version();
  }

  public String author() {
    if(info == null) return "unknown";
    return info.author();
  }

  public ModuleInfo getInfo() {
    return info;
  }

  public void setInfo(ModuleInfo info) {
    this.info = info;
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public URLClassLoader getLoader() {
    return loader;
  }

  public void setLoader(URLClassLoader loader) {
    this.loader = loader;
  }
}