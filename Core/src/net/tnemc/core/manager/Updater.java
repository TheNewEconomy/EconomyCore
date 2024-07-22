package net.tnemc.core.manager;

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

import com.vdurmont.semver4j.Semver;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.utils.IOUtil;
import net.tnemc.plugincore.core.utils.UpdateChecker;

/**
 * Updater
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Updater extends UpdateChecker {

  final String ver;
  final int verStrip;
  final int pluginStrip;

  public Updater() {
    ver = IOUtil.readVersion().orElse("0.0.0.0");
    verStrip = Integer.parseInt(ver.replaceAll("\\.", ""));
    pluginStrip = Integer.parseInt(PluginCore.engine().version().replaceAll("\\.", ""));
  }

  @Override
  public boolean isEarlyBuild() {
    return verStrip < pluginStrip;
  }

  @Override
  public boolean needsUpdate() {
    return verStrip > pluginStrip;
  }

  @Override
  public String stable() {
    if(new Semver(PluginCore.engine().version() + "-" + PluginCore.engine().build(), Semver.SemverType.LOOSE).isStable()) {
      return "Stable";
    }
    return "Not Stable";
  }

  @Override
  public String getBuild() {
    return ver;
  }
}