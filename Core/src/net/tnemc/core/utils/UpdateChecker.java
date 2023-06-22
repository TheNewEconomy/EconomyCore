package net.tnemc.core.utils;
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

import com.vdurmont.semver4j.Semver;
import net.tnemc.core.TNECore;

/**
 * UpdateChecker
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class UpdateChecker {

  private final Semver ver;

  public UpdateChecker() {
    ver = IOUtil.readVersion().map(Semver::new).orElseGet(()->new Semver("0.0.0.0"));
  }

  public boolean isEarlyBuild() {
    return ver.isLowerThan(TNECore.version + "-" + TNECore.build);
  }

  public boolean needsUpdate() {
    return ver.isGreaterThan(TNECore.version + "-" + TNECore.build);
  }

  public String stable() {
    if(new Semver(TNECore.version + "-" + TNECore.build).isStable()) {
      return "Stable";
    }
    return "Not Stable";
  }

  public String getBuild() {
    return ver.getValue();
  }
}