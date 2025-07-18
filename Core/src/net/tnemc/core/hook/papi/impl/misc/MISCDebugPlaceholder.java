package net.tnemc.core.hook.papi.impl.misc;
/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.hook.papi.Placeholder;
import net.tnemc.plugincore.PluginCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * AccountCreatedPlaceholder
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class MISCDebugPlaceholder implements Placeholder {

  /**
   * Retrieves the identifier associated with this symbol.
   *
   * @return The identifier as a string
   */
  @Override
  public String identifier() {

    return "tne_debug";
  }

  /**
   * Checks if the specified parameters are valid for this method.
   *
   * @param params The parameters to be checked
   *
   * @return True if the parameters are valid, false otherwise
   */
  @Override
  public boolean applies(final String[] params) {

    return params[0].equalsIgnoreCase("debug");
  }

  /**
   * Processes a request with the given account and parameters.
   *
   * @param account Optional account associated with the request
   * @param params  Required parameters for the request
   *
   * @return Nullable string response from the request process
   */
  @Override
  public @Nullable String onRequest(@Nullable final String account, @NotNull final String[] params) {

    return "Debug Level: " + PluginCore.instance().getLevel().getIdentifier();
  }
}