package net.tnemc.bungee;

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

import net.md_5.bungee.api.plugin.Plugin;
import net.tnemc.bungee.event.MessageListener;
import net.tnemc.bungee.event.PlayerConnectListener;
import net.tnemc.bungee.message.MessageManager;

/**
 * BungeeCore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BungeeCore extends Plugin {

  private static BungeeCore instance;
  private MessageManager manager;

  @Override
  public void onEnable() {

    instance = this;

    this.manager = new MessageManager(new BungeeProxy());

    getProxy().registerChannel("tne:balance");
    getProxy().registerChannel("tne:sync");
    getProxy().registerChannel("tne:message");
    getProxy().getPluginManager().registerListener(this, new MessageListener());
    getProxy().getPluginManager().registerListener(this, new PlayerConnectListener());
  }

  public static BungeeCore instance() {

    return instance;
  }
}