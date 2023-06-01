package net.tnemc.core.module;
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

import co.aikar.commands.CommandManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.api.callback.TNECallback;
import net.tnemc.core.api.callback.TNECallbacks;
import net.tnemc.core.manager.DataManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Module represents an add-on module for TNE.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public interface Module {

  /**
   * Called when the {@link TNECore#enable()} method is called.
   */
  void enable(TNECore core);

  /**
   * Called when the {@link TNECore#onDisable()} method is called.
   */
  void disable(TNECore core);

  /**
   * Called when the configurations are initialized and loaded.
   */
  void initConfigurations();

  /**
   * Called when the datamanager runs its backup method.
   * @param manager The datamanager instance.
   */
  void backup(DataManager manager);

  /**
   * Called when the datamanager is enabled, and a connection is established.
   * @param manager The datamanager instance.
   */
  void enableSave(DataManager manager);

  /**
   * Called when the datamanager is enabled, and a connection is established.
   * @param manager The datamanager instance.
   */
  void disableSave(DataManager manager);

  /**
   * Called after the default TNE Commands are registered.
   * @param manager The {@link CommandManager} that the commands are registered to.
   */
  void registerCommands(CommandManager<?, ?, ?, ?, ?, ?> manager);

  /**
   * Called after the {@link net.tnemc.core.api.CallbackManager} is initialized. This method will
   * register the callbacks with the manager automatically.
   *
   * @return A map containing the callbacks to register where the key is the callback name and the
   * value is the callback function.
   */
  default Map<String, Function<TNECallback, Boolean>> registerCallbacks() {
    return new HashMap<>();
  }
}