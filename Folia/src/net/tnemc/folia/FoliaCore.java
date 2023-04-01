package net.tnemc.folia;

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

import co.aikar.commands.PaperCommandManager;
import net.tnemc.bukkit.impl.BukkitLogProvider;
import net.tnemc.core.TNECore;
import net.tnemc.folia.command.AdminCommand;
import net.tnemc.folia.command.MoneyCommand;
import net.tnemc.folia.command.TransactionCommand;
import net.tnemc.folia.impl.FoliaServerProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * FoliaCore
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class FoliaCore extends TNECore {

  protected PaperCommandManager command;
  private JavaPlugin plugin;

  public FoliaCore(JavaPlugin plugin) {
    super(new FoliaServerProvider(), new BukkitLogProvider(plugin.getLogger()));
    setInstance(this);
    this.plugin = plugin;
  }

  @Override
  protected void onEnable() {
    this.directory = plugin.getDataFolder();

    super.onEnable();

    command = new PaperCommandManager(plugin);
    command.registerCommand(new AdminCommand());
    command.registerCommand(new MoneyCommand());
    command.registerCommand(new TransactionCommand());
  }

  public static FoliaCore instance() {
    return (FoliaCore)TNECore.instance();
  }
}