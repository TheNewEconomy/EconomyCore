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
import net.tnemc.core.TNECore;
import net.tnemc.minestom.impl.MinestomLogProvider;
import net.tnemc.minestom.impl.MinestomServerProvider;

/**
 * MinestomCore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MinestomCore extends TNECore {

  private Extension extension;

  public MinestomCore(Extension extension) {
    super(new MinestomServerProvider(), new MinestomLogProvider(extension.getLogger()));

    this.extension = extension;
  }

  /**
   * Used to enable the core. This should contain things that can't be initialized until after the
   * server software is operational.
   */
  @Override
  protected void onEnable() {
    this.directory = extension.getDataDirectory().toFile();

    super.onEnable();
  }

  @Override
  public void registerCommandHandler() {

  }

  @Override
  public void registerCommands() {
    //TODO: Upgrade command set to lamp
  }
}