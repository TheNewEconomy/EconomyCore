package net.tnemc.minestom;

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

import net.minestom.server.extensions.Extension;

/**
 * TNE
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TNE extends Extension {

  private static TNE instance;
  private MinestomCore core;

  public void initialize() {
    this.instance = this;


    MinestomCore.eco().currency().load(getDataDirectory().toFile(), false);
    MinestomCore.eco().currency().saveCurrenciesUUID(getDataDirectory().toFile());

    getLogger().info("The New Economy has been enabled!");
  }

  public void terminate() {

  }

  public MinestomCore getCore() {
    return core;
  }

  public static TNE instance() {
    return instance;
  }
}