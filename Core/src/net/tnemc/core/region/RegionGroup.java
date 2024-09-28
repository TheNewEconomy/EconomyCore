package net.tnemc.core.region;

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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a group of regions that share information, and currencies.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class RegionGroup {

  private final Map<UUID, Boolean> currencies = new HashMap<>();

  private String name;

  public RegionGroup(final String name) {

    this.name = name;
  }

  public Map<UUID, Boolean> getCurrencies() {

    return currencies;
  }

  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return The unique name tied to this RegionGroup.
   */
  @NotNull
  public String name() {

    return name;
  }
}